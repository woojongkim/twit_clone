package com.woody.woodytwit.modules.user.dto;

import java.time.LocalDateTime;
import javax.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
  private String email;

  private String username;

  private String nickname;

  private LocalDateTime joinedDate;

  private String description;

  private String backgroundImage;

  private String profileImage;

  private String profileThumbnail;
}
