package com.woody.woodytwit.modules.twit;

import com.woody.woodytwit.modules.common.dto.ResponseDto;
import com.woody.woodytwit.modules.twit.dto.TweetCreateRequestDto;
import com.woody.woodytwit.modules.twit.dto.TweetDto;
import com.woody.woodytwit.modules.user.CurrentUser;
import com.woody.woodytwit.modules.user.User;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TweetController {

  private final TweetRepository tweetRepository;

  private final TweetService tweetService;

  private final ModelMapper modelMapper;

  @PostMapping("/api/tweets")
  public @ResponseBody ResponseEntity<ResponseDto> tweet(@CurrentUser User user, @RequestBody @Validated TweetCreateRequestDto tweetCreateRequestDto, BindingResult bindingResult, ResponseDto responseDto) {
    if(bindingResult.hasErrors()) {
      responseDto.setMessage("유효성 검사 실패");
      responseDto.setStatus(HttpStatus.BAD_REQUEST);
      responseDto.setData(bindingResult.getAllErrors());
      return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }


    Tweet tweet = tweetService.tweet(user, tweetCreateRequestDto);

    responseDto.setData(modelMapper.map(tweet, TweetDto.class));
    responseDto.setStatus(HttpStatus.OK);
    responseDto.setMessage("트윗 성공");

    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
  }

  @GetMapping("/tweets/{id}")
  public String tweetDetail(@CurrentUser User user, @PathVariable Long id, Model model) {
    Tweet tweet = tweetRepository.findTweetWithUserById(id);
    model.addAttribute("tweet", tweet);

    return "tweet/tweetDetail";
  }
}
