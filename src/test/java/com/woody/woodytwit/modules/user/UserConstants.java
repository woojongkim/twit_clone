package com.woody.woodytwit.modules.user;

import com.woody.woodytwit.modules.user.dto.SignUpDto;
import com.woody.woodytwit.modules.user.dto.UserDto;

public interface UserConstants {

  public final SignUpDto SIGN_UP_DTO = SignUpDto.builder()
      .email("5385kim@gmail.com")
      .username("woody")
      .nickname("woody")
      .password("qwerqwer")
      .build();
}
