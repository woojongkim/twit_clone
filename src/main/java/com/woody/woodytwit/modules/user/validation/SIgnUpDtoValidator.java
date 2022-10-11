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

    if (userRepository.existsByUsername(signUpDto.getUsername())) {
      errors.rejectValue("username", "invalid.username", new Object[]{signUpDto.getUsername()},
          "이미 사용중인 사용자 아이디입니다.");
    }
  }
}
