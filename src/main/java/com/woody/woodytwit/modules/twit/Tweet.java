package com.woody.woodytwit.modules.twit;

import com.woody.woodytwit.modules.common.entity.DeletableEntity;
import com.woody.woodytwit.modules.user.User;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id", callSuper = false)
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tweet extends DeletableEntity {

  @Id @GeneratedValue
  private long id;

  private String content;

  @ManyToOne
  private User user;

  @OneToMany
  private List<Tweet> replies;

  @OneToMany
  private List<User> likes;

  @OneToMany
  private List<User> retweets;

  @OneToMany
  private List<Tweet> retweetsWithComment;

  @ColumnDefault("0")
  private long replyCount;

  @ColumnDefault("0")
  private long likeCount;

  @ColumnDefault("0")
  private long retweetCount;

  @ColumnDefault("0")
  private long retweetWithCommentCount;

  @ManyToOne
  private Tweet replyTo;

  @ManyToOne
  private Tweet retweetTo;

  public void addReply(Tweet tweet) {
    this.replies.add(tweet);
    this.replyCount++;
  }
}
