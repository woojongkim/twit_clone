package com.woody.woodytwit.modules.follow;

import com.woody.woodytwit.modules.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {

  private final FollowRepository followRepository;
  public Follow follow(User fromUser, User toUser) {
    Follow follow = Follow.builder().fromUser(fromUser).toUser(toUser).build();
    Follow newFollow = followRepository.save(follow);
    return newFollow;
  }

  public void unfollow(User user, User toUser) {
    Follow byFromUserAndToUser = followRepository.findByFromUserAndToUser(user, toUser);
    followRepository.delete(byFromUserAndToUser);
  }
}
