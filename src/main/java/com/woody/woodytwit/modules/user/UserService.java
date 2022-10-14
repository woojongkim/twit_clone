package com.woody.woodytwit.modules.user;

import com.woody.woodytwit.infra.mail.EmailMessage;
import com.woody.woodytwit.infra.mail.EmailService;
import com.woody.woodytwit.modules.user.dto.ProfileUpdateDto;
import com.woody.woodytwit.modules.user.dto.SignUpDto;
import com.woody.woodytwit.modules.user.dto.UserDto;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  private final PasswordEncoder passwordEncoder;

  private final EmailService emailService;

  public UserDto processNewUser(SignUpDto signUpDto) {
    User user = modelMapper.map(signUpDto, User.class);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.generateEmailCheckToken();
    User newUser = userRepository.save(user);
    sendSignupConfirmEmail(newUser);

    return modelMapper.map(newUser, UserDto.class);
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

  public void updateProfile(User user, ProfileUpdateDto profileUpdateDto) {
    modelMapper.map(profileUpdateDto, user);
    userRepository.save(user);
  }
}
