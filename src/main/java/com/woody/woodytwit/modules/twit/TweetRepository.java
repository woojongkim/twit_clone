package com.woody.woodytwit.modules.twit;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface TweetRepository extends JpaRepository<Tweet, Long> {

  @EntityGraph(attributePaths = {"user"})
  Tweet findWithUserById(Long id);

  @EntityGraph(attributePaths = {"user"})
  List<Tweet> findWithUserByReplyTo(Tweet tweet);
}
