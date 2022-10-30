package com.woody.woodytwit.modules.user;

import com.woody.woodytwit.infra.mail.EmailMessage;
import com.woody.woodytwit.infra.mail.EmailService;
import com.woody.woodytwit.modules.api.dto.ImageUploadRequestDto;
import com.woody.woodytwit.modules.common.ImageUtils;
import com.woody.woodytwit.modules.user.dto.ProfileUpdateDto;
import com.woody.woodytwit.modules.user.dto.SignUpDto;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  private final PasswordEncoder passwordEncoder;

  private final EmailService emailService;

  @Value("${storage.host}")
  private String storageHost;

  public User processNewUser(SignUpDto signUpDto) {
    User user = modelMapper.map(signUpDto, User.class);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.generateEmailCheckToken();
    User newUser = userRepository.save(user);
    sendSignupConfirmEmail(newUser);

    return newUser;
  }

  private void sendSignupConfirmEmail(User user) {

    EmailMessage emailMessage = EmailMessage.builder()
        .to(Arrays.asList(user.getEmail()))
        .subject("회원 가입 인증")
        .message("회원 가입 인증 토큰: " + user.getEmailCheckToken())
        .build();

    emailService.sendEmail(emailMessage);
  }

  @Override
  public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {

    User user = userRepository.findByEmail(emailOrUsername);
    if(user == null){
      user = userRepository.findByUsername(emailOrUsername);
    }

    if(user == null){
      throw new UsernameNotFoundException(emailOrUsername);
    }

    return new PrincipalDetails(user);
  }

  public void login(User user) {
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
        new PrincipalDetails(user), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));

    SecurityContextHolder.getContext().setAuthentication(token);
  }

  public void completeSignUp(User user) {
    user.completeSignUp();
    userRepository.save(user);
  }

  public void generateEmailCheckToken(User user) {
    user.generateEmailCheckToken();
    userRepository.save(user);
    sendSignupConfirmEmail(user);
  }

  public void updateProfile(User user, ProfileUpdateDto profileUpdateDto) throws IOException {
    modelMapper.typeMap(ProfileUpdateDto.class, User.class)
        .addMappings(mapper -> mapper.skip(User::setProfileImage))
        .addMappings(mapper -> mapper.skip(User::setBackgroundImage));
    modelMapper.map(profileUpdateDto, user);

    if(StringUtils.hasText(profileUpdateDto.getBackgroundImage())){
      String backgroundImage = ImageUtils.resizeDataURI(profileUpdateDto.getBackgroundImage(), 650);
      String fileName = UUID.randomUUID().toString();
      saveImage(backgroundImage, fileName);
      user.setBackgroundImage(fileName);
    }

    if(StringUtils.hasLength(profileUpdateDto.getProfileImage())){
      String profileImage = ImageUtils.resizeDataURI(profileUpdateDto.getProfileImage(), 200);
      String profileThumbnail = ImageUtils.resizeDataURI(profileUpdateDto.getProfileImage(), 50);
      String fileName = UUID.randomUUID().toString();
      String thumbnailFileName = fileName + "_thumbnail";


      saveImage(profileImage, fileName);
      saveImage(profileThumbnail, thumbnailFileName);

      user.setProfileImage(fileName);
      user.setProfileThumbnail(thumbnailFileName);
    }

    userRepository.save(user);
  }

  private void saveImage(String dataUri, String fileName) {
    ImageUploadRequestDto imageUploadRequestDto = ImageUploadRequestDto.builder().dataUri(
        dataUri).fileName(
        fileName).build();
    RestTemplate restTemplate = new RestTemplate();
    String url = storageHost;
    HttpEntity<ImageUploadRequestDto> requestEntity = new HttpEntity<>(imageUploadRequestDto);
    String post = restTemplate.postForObject(url, requestEntity, String.class);
  }
}
