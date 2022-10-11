 package com.woody.woodytwit.modules.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

  @NotBlank
  @Length(min = 3, max = 20)
  @Pattern(regexp = "^[A-Za-z0-9_-]{3,20}$", message = "영문자,숫자,-,_만 사용가능합니다.")
  private String url;

  @NotBlank
  @Length(min = 3, max = 20)
  @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$", message = "특수문자는 사용불가합니다.")
  private String nickname;

  @Email
  @NotBlank
  private String email;

  @NotBlank
  @Length(min = 8, max = 50)
  private String password;
}