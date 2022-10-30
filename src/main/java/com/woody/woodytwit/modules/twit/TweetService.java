package com.woody.woodytwit.modules.twit;

import com.woody.woodytwit.modules.twit.dto.TweetCreateRequestDto;
import com.woody.woodytwit.modules.user.User;
import java.util.ArrayList;
import java.util.List;
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

  public Tweet reply(User user, Long tweetId, TweetCreateRequestDto tweetCreateRequestDto) {
    Tweet tweet = modelMapper.map(tweetCreateRequestDto, Tweet.class);
    tweet.setUser(user);

    Tweet parentTweet = tweetRepository.findById(tweetId).orElseThrow(() -> new TweetNotFoundException("트윗이 존재하지 않습니다."));
    parentTweet.addReply(tweet);

    tweet.setReplyTo(parentTweet);


    return tweetRepository.save(tweet);
  }

  public void test(){
    // generate arrays of object
    List<Tweet> tweets = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      Tweet tweet = new Tweet();
      tweet.setId((long) i);
      tweet.setReplyTo(null);
      tweets.add(tweet);
    }



  }
}
