package com.woody.woodytwit.modules.user;

import com.woody.woodytwit.modules.user.dto.SignUpDto;
import com.woody.woodytwit.modules.user.dto.UserDto;
import com.woody.woodytwit.modules.user.validation.SIgnUpDtoValidator;
import javax.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
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

  private final ModelMapper modelMapper;

  @InitBinder("signUpDto")
  private void initBinder(WebDataBinder binder) {
    binder.addValidators(sIgnUpDtoValidator);
  }


  @GetMapping("/signup")
  public String signup(Model model){
    model.addAttribute(new SignUpDto());
    return "user/signup";
  }

  @PostMapping("/signup")
  public String signupForm(@Valid SignUpDto signUpDto, BindingResult bindingResult){
    log.info("signUpDto : {}", signUpDto);
    if(bindingResult.hasErrors()){
      return "user/signup";
    }

    UserDto userDto = userService.processNewUser(signUpDto);
    return "redirect:/";
  }

  @GetMapping("/login")
  public String loginForm(){
    return "user/login";
  }

  @GetMapping("/email-confirm")
  public String emailConfirm(String token, String email, Model model){
    User user = userRepository.findByEmail(email);

    String view = "user/email-confirm";
    if(user == null){
      model.addAttribute("error","wrong.email");
      return view;
    }

    if(!user.isValidToken(token)){
      model.addAttribute("error","wrong.token");
      return view;
    }

    user.completeSignUp();

//    userService.login(user);

    return view;
  }
}
