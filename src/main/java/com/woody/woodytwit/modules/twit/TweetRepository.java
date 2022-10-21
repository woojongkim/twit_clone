package com.woody.woodytwit.modules.twit;

import com.woody.woodytwit.modules.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TweetRepository extends JpaRepository<Tweet, Long> {

  @EntityGraph(attributePaths = {"user"})
  Tweet findTweetWithUserById(Long id);
}
