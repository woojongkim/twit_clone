package com.woody.woodytwit.modules.twit;

import com.woody.woodytwit.modules.twit.dto.TweetCreateRequestDto;
import com.woody.woodytwit.modules.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TweetService{

  private final TweetRepository tweetRepository;
  private final ModelMapper modelMapper;

  public Tweet tweet(User user, TweetCreateRequestDto tweetCreateRequestDto) {
    Tweet tweet = modelMapper.map(tweetCreateRequestDto, Tweet.class);
    tweet.setUser(user);

    return tweetRepository.save(tweet);
  }
}
