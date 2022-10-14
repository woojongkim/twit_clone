package com.woody.woodytwit.modules.user;

import com.woody.woodytwit.modules.user.dto.ProfileUpdateDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tuser")
public class User {

  @Id
  @GeneratedValue
  private long id;

  @Column(unique = true)
  private String email;

  @Column(unique = true)
  private String username;

  private String nickname;

  private String password;

  private boolean emailVerified;

  private String emailCheckToken;

  private LocalDateTime emailCheckTokenGeneratedDate;

  private LocalDateTime joinedDate;

  private String description;

  @Lob
  @Basic(fetch = FetchType.EAGER)
  private String backgroundImage;

  @Lob @Basic(fetch = FetchType.EAGER)
  private String profileImage;

  public void generateEmailCheckToken() {
    this.emailCheckToken = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    this.emailCheckTokenGeneratedDate = LocalDateTime.now();
  }

  public void completeSignUp() {
    this.emailVerified = true;
    this.joinedDate = LocalDateTime.now();
  }

  public boolean isValidToken(String token) {
    return this.emailCheckToken.equals(token);
  }

  public boolean canSendConfirmEmail() {
    return this.emailCheckTokenGeneratedDate.isBefore(LocalDateTime.now().minusHours(1));
  }

}
