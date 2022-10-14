package com.woody.woodytwit.modules.user;

import static com.woody.woodytwit.modules.user.UserConstants.SIGN_UP_DTO;
import static org.junit.jupiter.api.Assertions.*;

import com.woody.woodytwit.infra.MockMvcTest;
import com.woody.woodytwit.infra.mail.EmailMessage;
import com.woody.woodytwit.infra.mail.EmailService;
import com.woody.woodytwit.modules.user.dto.SignUpDto;
import lombok.With;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class UserControllerTest {

  private final String email = "5385kim2@gmail.com";
  private final String username = "woody2";
  private final String nickname = "woody2";
  private final String password = "qwerqwer";
  private final String nicknameParam = "nickname";
  private final String emailParam = "email";
  private final String usernameParam = "username";
  private final String passwordParam = "password";

  @Autowired
  MockMvc mockMvc;

  @Autowired
  UserRepository userRepository;

  @MockBean
  EmailService emailService;

  @Autowired
  private UserService userService;

  @Test
  @DisplayName("회원가입 - 폼")
  void signupForm() throws Exception {
    mockMvc.perform(get("/signup")
            .param(nicknameParam, nickname)
            .param(this.usernameParam, username)
            .param(emailParam, email)
            .param(passwordParam, password))
        .andExpect(status().isOk())
        .andExpect(view().name("user/signup"))
        .andExpect(model().attributeExists("signUpDto"));
  }

  @Test
  @DisplayName("회원가입 - 성공")
  void signup() throws Exception {
    mockMvc.perform(post("/signup")
            .param(nicknameParam, nickname)
            .param(usernameParam, username)
            .param(emailParam, email)
            .param(passwordParam, password)
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/"));

    User newUser = userRepository.findByUsername(username);

    assertNotNull(newUser);
    assertNotEquals(newUser.getPassword(), password);

    then(emailService).should().sendEmail(any(EmailMessage.class));
  }


  @Test
  @DisplayName("회원가입 - 실패 : password 길이 짧음, username없음")
  void signupfailInvalidArguements2() throws Exception {
    mockMvc.perform(post("/signup")
            .param(nicknameParam, nickname)
            .param(emailParam, email)
            .param(passwordParam, "1234")
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(view().name("user/signup"))
        .andExpect(model().hasErrors())
        .andExpect(model().attributeHasFieldErrors("signUpDto", passwordParam, usernameParam));
  }


  @Test
  @DisplayName("회원가입 - 실패 : 중복된 닉네임")
  void signupDuplicated() throws Exception {
    mockMvc.perform(post("/signup")
            .param(nicknameParam, nickname)
            .param(usernameParam, username)
            .param(emailParam, email)
            .param(passwordParam, password)
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/"));

    mockMvc.perform(post("/signup")
            .param(nicknameParam, nickname)
            .param(usernameParam, username)
            .param(emailParam, email)
            .param(passwordParam, password)
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(view().name("user/signup"))
        .andExpect(model().hasErrors())
        .andExpect(model().attributeHasFieldErrors("signUpDto", emailParam,usernameParam));
  }

  @Test
  @DisplayName("로그인 - 폼")
  void loginForm() throws Exception {
    mockMvc.perform(get("/login"))
        .andExpect(status().isOk())
        .andExpect(view().name("user/login"));
  }

  @Test
  @DisplayName("로그인 - 성공")
  void login() throws Exception {
    SignUpDto signUpDto = SignUpDto.builder().username(username).nickname(nickname).email(email).password(password).build();

    userService.processNewUser(signUpDto);

    mockMvc.perform(post("/login")
            .param(usernameParam, username)
            .param(passwordParam, password)
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
        .andExpect(authenticated().withUsername(email));
  }

  @Test
  @DisplayName("로그인 - 실패 : 없는 username")
  void loginFail() throws Exception {
    SignUpDto signUpDto = SignUpDto.builder().username(username).nickname(nickname).email(email).password(password).build();

    userService.processNewUser(signUpDto);

    mockMvc.perform(post("/login")
            .param(usernameParam, "더미"+username)
            .param(passwordParam, password)
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login?error"));
  }

  @Test
  @DisplayName("로그인 - 실패 : password 틀림")
  void loginFail2() throws Exception {
    SignUpDto signUpDto = SignUpDto.builder().username(username).nickname(nickname).email(email).password(password).build();

    userService.processNewUser(signUpDto);

    mockMvc.perform(post("/login")
            .param(usernameParam, username)
            .param(passwordParam, password+"1234")
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login?error"));
  }

  @Test
  @DisplayName("이메일 인증 : 성공")
  void verifyEmail() throws Exception {
    userService.processNewUser(SIGN_UP_DTO);
    User user = userRepository.findByUsername(SIGN_UP_DTO.getUsername());
    userService.login(user);

    mockMvc.perform(post("/verify-email")
            .param("verifyToken", user.getEmailCheckToken())
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));
  }

  @Test
  @DisplayName("이메일 인증 : 실패 - 토큰 불일치")
  void verifyEmailTokenInvalid() throws Exception {
    userService.processNewUser(SIGN_UP_DTO);
    User user = userRepository.findByUsername(SIGN_UP_DTO.getUsername());
    userService.login(user);

    mockMvc.perform(post("/verify-email")
            .param("verifyToken", user.getEmailCheckToken()+"!")
            .with(csrf()))
        .andExpect(model().attributeExists("error"))
        .andExpect(view().name("user/verify-email"));
  }
}