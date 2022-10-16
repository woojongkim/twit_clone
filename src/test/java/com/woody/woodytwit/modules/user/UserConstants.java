package com.woody.woodytwit.modules.user;

import com.woody.woodytwit.modules.user.dto.SignUpDto;
import com.woody.woodytwit.modules.user.dto.UserDto;

public interface UserConstants {

  public final SignUpDto SIGN_UP_DTO = SignUpDto.builder()
      .email("5385kim@gmail.com")
      .username("woody")
      .nickname("우디")
      .password("qwerqwer")
      .build();

  public final SignUpDto SIGN_UP_DTO2 = SignUpDto.builder()
      .email("5385kim@naver.com")
      .username("5385kim")
      .nickname("우종")
      .password("qwerqwer")
      .build();
}
