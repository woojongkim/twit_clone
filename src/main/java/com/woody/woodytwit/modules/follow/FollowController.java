package com.woody.woodytwit.modules.follow;

import com.woody.woodytwit.modules.user.CurrentUser;
import com.woody.woodytwit.modules.user.User;
import com.woody.woodytwit.modules.user.UserRepository;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class FollowController {

  private final FollowService followService;

  private final FollowRepository followRepository;

  private final UserRepository userRepository;

  @PostMapping("/user/{username}/follow")
  public String follow(@CurrentUser User user, @PathVariable("username") String username, HttpServletRequest request, Model model){

    User toUser = userRepository.findByUsername(username);

      String view = getPreviousPageByRequest(request).orElse("redirect:/user/"+username);
    if(toUser == null){
      model.addAttribute("error", "존재하지 않는 사용자입니다.");
      return view;
    }

    boolean isFollow = followRepository.findByFromUserAndToUser(user, toUser) != null;
    if(isFollow == true){
      model.addAttribute("error", "이미 팔로우한 사용자입니다.");
      return view;
    }

    if(user.equals(toUser)){
      model.addAttribute("error", "자기 자신을 팔로우할 수 없습니다.");
      return view;
    }

    Follow follow = followService.follow(user, toUser);

    if(follow != null){
      model.addAttribute("followed", true);
      model.addAttribute("countFollowed", followRepository.countByToUser(toUser));
      model.addAttribute("countFollowing", followRepository.countByFromUser(toUser));
    }

    return view;
  };

  @PostMapping("/user/{username}/unfollow")
  public String unfollow(@CurrentUser User user, @PathVariable("username") String username, HttpServletRequest request, Model model){

    User toUser = userRepository.findByUsername(username);

    String view = getPreviousPageByRequest(request).orElse("redirect:/user/"+username);
    if(toUser == null){
      model.addAttribute("error", "존재하지 않는 사용자입니다.");
      return view;
    }

    boolean isFollow = followRepository.findByFromUserAndToUser(user, toUser) != null;
    if(isFollow == false){
      model.addAttribute("error", "이미 언팔로우 사용자입니다.");
      return view;
    }

    if(user.equals(toUser)){
      model.addAttribute("error", "자기 자신을 팔로우/언팔로우 할 수 없습니다.");
      return view;
    }

    followService.unfollow(user, toUser);

    model.addAttribute("followed", false);
    model.addAttribute("countFollowed", followRepository.countByToUser(toUser));
    model.addAttribute("countFollowing", followRepository.countByFromUser(toUser));

    return view;
  };

  /**
   * Returns the viewName to return for coming back to the sender url
   *
   * @param request Instance of {@link HttpServletRequest} or use an injected instance
   * @return Optional with the view name. Recomended to use an alternativa url with
   * {@link Optional#orElse(java.lang.Object)}
   */
  protected Optional<String> getPreviousPageByRequest(HttpServletRequest request)
  {
    return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:" + requestUrl);
  }

}
