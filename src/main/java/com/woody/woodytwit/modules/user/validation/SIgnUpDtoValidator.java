package com.woody.woodytwit.modules.user.validation;


import com.woody.woodytwit.modules.user.UserRepository;
import com.woody.woodytwit.modules.user.UserService;
import com.woody.woodytwit.modules.user.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SIgnUpDtoValidator implements Validator {

  private final UserRepository userRepository;

  @Override
  public boolean supports(Class<?> clazz) {
    return SignUpDto.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    SignUpDto signUpDto = (SignUpDto) target;
    if (userRepository.existsByEmail(signUpDto.getEmail())) {
      errors.rejectValue("email", "invalid.email", new Object[]{signUpDto.getEmail()},
          "이미 사용중인 이메일입니다.");
    }

    if (userRepository.existsByNickname(signUpDto.getNickname())) {
      errors.rejectValue("nickname", "invalid.nickname", new Object[]{signUpDto.getEmail()},
          "이미 사용중인 닉네임입니다.");
    }

    if (userRepository.existsByUrl(signUpDto.getUrl())) {
      errors.rejectValue("url", "invalid.url", new Object[]{signUpDto.getUrl()},
          "이미 사용중인 태그입니다.");
    }
  }
}
