package com.woody.woodytwit.modules.follow;

import com.woody.woodytwit.modules.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class Follow {
  @Id
  @GeneratedValue
  private long id;

  @ManyToOne
  @JoinColumn(name = "fromUser")
  private User fromUser;

  @ManyToOne
  @JoinColumn(name = "toUser")
  private User toUser;
}
