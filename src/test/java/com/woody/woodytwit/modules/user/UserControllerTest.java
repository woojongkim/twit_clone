package com.woody.woodytwit.modules.user;

import static org.junit.jupiter.api.Assertions.*;

import com.woody.woodytwit.infra.MockMvcTest;
import com.woody.woodytwit.infra.mail.EmailMessage;
import com.woody.woodytwit.infra.mail.EmailService;
import com.woody.woodytwit.modules.user.dto.SignUpDto;
import com.woody.woodytwit.modules.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class UserControllerTest {

  private final String email = "5385kim@gmail.com";
  private final String url = "woody";
  private final String nickname = "woody";
  private final String password = "qwerqwer";

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
            .param("nickname", nickname)
            .param("url", url)
            .param("email", email)
            .param("password", password))
        .andExpect(status().isOk())
        .andExpect(view().name("user/signup"))
        .andExpect(model().attributeExists("signUpDto"));
  }

  @Test
  @DisplayName("회원가입 - 성공")
  void signup() throws Exception {
    mockMvc.perform(post("/signup")
            .param("nickname", nickname)
            .param("url", url)
            .param("email", email)
            .param("password", password)
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/"));

    User newUser = userRepository.findByNickname(nickname);

    assertNotNull(newUser);
    assertNotEquals(newUser.getPassword(), password);

    then(emailService).should().sendEmail(any(EmailMessage.class));
  }


  @Test
  @DisplayName("회원가입 - 실패 : password 길이 짧음, url없음")
  void signupfailInvalidArguements2() throws Exception {
    mockMvc.perform(post("/signup")
            .param("nickname", nickname)
            .param("email", email)
            .param("password", "1234")
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(view().name("user/signup"))
        .andExpect(model().hasErrors())
        .andExpect(model().attributeHasFieldErrors("signUpDto", "password","url"));
  }


  @Test
  @DisplayName("회원가입 - 실패 : 중복된 닉네임")
  void signupDuplicated() throws Exception {
    mockMvc.perform(post("/signup")
            .param("nickname", nickname)
            .param("url", url)
            .param("email", email)
            .param("password", password)
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/"));

    mockMvc.perform(post("/signup")
            .param("nickname", nickname)
            .param("url", url)
            .param("email", email)
            .param("password", password)
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(view().name("user/signup"))
        .andExpect(model().hasErrors())
        .andExpect(model().attributeHasFieldErrors("signUpDto", "nickname","email","url"));
  }

  @Test
  @DisplayName("회원가입 - 이메일 인증 성공")
  void confirmEmail() throws Exception {
    SignUpDto signUpDto = SignUpDto.builder().url(url).nickname(nickname).email(email).password(password).build();

    userService.processNewUser(signUpDto);
    String emailCheckToken = userRepository.findByEmail(email).getEmailCheckToken();
    mockMvc.perform(get("/email-confirm")
            .param("token", emailCheckToken)
            .param("email", email))
        .andExpect(status().isOk())
        .andExpect(view().name("user/email-confirm"))
        .andExpect(model().attributeDoesNotExist("error"));
  }

  @Test
  @DisplayName("회원가입 - 이메일 인증 실패 : 토큰 불일치")
  void confirmEmailFail() throws Exception {
    SignUpDto signUpDto = SignUpDto.builder().url(url).nickname(nickname).email(email).password(password).build();

    userService.processNewUser(signUpDto);
    String emailCheckToken = userRepository.findByEmail(email).getEmailCheckToken();
    mockMvc.perform(get("/email-confirm")
            .param("token", emailCheckToken+"ABCD")
            .param("email", email))
        .andExpect(status().isOk())
        .andExpect(view().name("user/email-confirm"))
        .andExpect(model().attributeExists("error"));
  }

}