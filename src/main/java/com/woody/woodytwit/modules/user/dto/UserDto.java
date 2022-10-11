package com.woody.woodytwit.modules.user.dto;

import java.time.LocalDateTime;
import javax.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class UserDto {
  private String email;

  private String username;

  private String nickname;

  private LocalDateTime joinedDate;
}
