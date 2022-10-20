package com.woody.woodytwit.modules.user;

import com.woody.woodytwit.modules.user.dto.SignUpDto;
import com.woody.woodytwit.modules.user.dto.UserDto;
import com.woody.woodytwit.modules.user.validation.SIgnUpDtoValidator;
import java.time.LocalDateTime;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final SIgnUpDtoValidator sIgnUpDtoValidator;
  private final UserRepository userRepository;

  @InitBinder("signUpDto")
  private void initBinder(WebDataBinder binder) {
    binder.addValidators(sIgnUpDtoValidator);
  }


  @GetMapping("/signup")
  public String signup(Model model) {
    model.addAttribute(new SignUpDto());
    return "user/signup";
  }

  @PostMapping("/signup")
  public String signupForm(@Valid SignUpDto signUpDto, BindingResult bindingResult) {
    log.info("signUpDto : {}", signUpDto);
    if (bindingResult.hasErrors()) {
      return "user/signup";
    }

    User newUser = userService.processNewUser(signUpDto);
    userService.login(newUser);

    return "redirect:/";
  }

  @GetMapping("/verify-email")
  public String verifyEmailForm(@CurrentUser User user) {
    if (user.isEmailVerified()) {
      return "redirect:/";
    }

    if (LocalDateTime.now().isAfter(user.getEmailCheckTokenGeneratedDate().plusHours(1))) {
      userService.generateEmailCheckToken(user);
      return "user/verify-email";
    }

    return "user/verify-email";
  }

  @PostMapping("/verify-email")
  public String verifyEmailForm(@CurrentUser User user, String verifyToken, Model model) {
    if (user.isEmailVerified()) {
      return "redirect:/";
    }

    User byIdUser = userRepository.findById(user.getId()).get();

    // 토큰만든지 1시간 지나면 만료
    if (LocalDateTime.now().isAfter(byIdUser.getEmailCheckTokenGeneratedDate().plusHours(1))) {
      model.addAttribute("error", "expired.token");
      return "user/verify-email";
    }

    // 토큰이 불일치하면 에러
    if (!byIdUser.getEmailCheckToken().equals(verifyToken)) {
      model.addAttribute("error", "wrong.value");
      return "user/verify-email";
    }

    userService.completeSignUp(byIdUser);

    return "redirect:/";
  }


  @GetMapping("/verify-email/resend")
  public String resendVerifyEmail(@CurrentUser User user) {
    if (user.isEmailVerified()) {
      return "redirect:/";
    }
    userService.generateEmailCheckToken(user);

    return "user/verify-email";
  }

  @GetMapping("/login")
  public String loginForm(@CurrentUser User user) {
    if(user != null) {
      return "redirect:/";
    }

    log.info("loginForm called");
    return "user/login";
  }
}
