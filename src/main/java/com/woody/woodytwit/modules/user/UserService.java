package com.woody.woodytwit.modules.user;

import com.woody.woodytwit.infra.mail.EmailMessage;
import com.woody.woodytwit.infra.mail.EmailService;
import com.woody.woodytwit.modules.user.dto.SignUpDto;
import com.woody.woodytwit.modules.user.dto.UserDto;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

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

  private void sendSignupConfirmEmail(User newUser) {

    String link = String.format("/email-confirm?token=%s&email=%s", newUser.getEmailCheckToken(),
        newUser.getEmail());

    EmailMessage emailMessage = EmailMessage.builder()
        .to(Arrays.asList(newUser.getEmail()))
        .subject("회원 가입 인증")
        .message("회원 가입 인증 토큰: " + link)
        .build();

    emailService.sendEmail(emailMessage);
  }
}
