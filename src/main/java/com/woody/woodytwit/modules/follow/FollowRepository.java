package com.woody.woodytwit.modules.follow;

import com.woody.woodytwit.modules.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface FollowRepository extends JpaRepository<Follow, Long> {

  long countByToUser(User user);

  long countByFromUser(User user);

  Follow findByFromUserAndToUser(User user, User byUsername);

  @EntityGraph(attributePaths = {"toUser"})
  List<Follow> findByFromUser(User fromUser);
  @EntityGraph(attributePaths = {"fromUser"})
  List<Follow> findByToUser(User toUser);
}
