package com.woody.woodytwit.modules.home;

import com.woody.woodytwit.modules.follow.Follow;
import com.woody.woodytwit.modules.follow.FollowRepository;
import com.woody.woodytwit.modules.user.CurrentUser;
import com.woody.woodytwit.modules.user.User;
import com.woody.woodytwit.modules.user.UserRepository;
import com.woody.woodytwit.modules.user.dto.UserDto;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
@Validated
public class ProfileController {

  private final UserRepository userRepository;

  private final ModelMapper modelMapper;

  private final FollowRepository followRepository;

  @GetMapping("/")
  public String home(@CurrentUser User user, Model model) {
    if(user.isEmailVerified() == false){
      return "redirect:/verify-email";
    }

    return "index";
  }

  @GetMapping("/user/{username}")
  public String profile(@PathVariable("username") String username, Model model, @CurrentUser User user) {
    User byUsername = userRepository.findByUsername(username);
    if(byUsername == null){
      throw new UsernameNotFoundException(username);
    }

    UserDto map = modelMapper.map(byUsername, UserDto.class);
    model.addAttribute("user", map);
    boolean isOwner = byUsername.equals(user);
    model.addAttribute("isOwner",isOwner);
    model.addAttribute("countFollowed", followRepository.countByToUser(byUsername));
    model.addAttribute("countFollowing", followRepository.countByFromUser(byUsername));

    if(isOwner){
      model.addAttribute("isFollow", false);
    }else{
      model.addAttribute("isFollow", followRepository.findByFromUserAndToUser(user, byUsername) != null);
    }

    return "user/profile";
  }

  @GetMapping("/user/{username}/following")
  public String following(@CurrentUser User user, @Valid @PathVariable("username") String username, Model model) {
    User byUsername = userRepository.findByUsername(username);
    if(byUsername == null){
      throw new UsernameNotFoundException(username);
    }

    List<UserDto> collect = followRepository.findFollowWithToUserByFromUser(byUsername).stream()
        .map(Follow::getToUser).map(u -> modelMapper.map(u, UserDto.class)).collect(
            Collectors.toList());

    model.addAttribute("mode", "following");
    model.addAttribute("userDto", modelMapper.map(byUsername, UserDto.class));
    model.addAttribute("following", collect);

    return "user/following";
  }

  @GetMapping("/user/{username}/followers")
  public String followers(@CurrentUser User user, @Valid @PathVariable("username") String username, Model model) {
    User byUsername = userRepository.findByUsername(username);
    if(byUsername == null){
      throw new UsernameNotFoundException(username);
    }

    List<UserDto> collect = followRepository.findFollowWithFromUserByToUser(byUsername).stream()
        .map(Follow::getFromUser).map(u -> modelMapper.map(u, UserDto.class)).collect(
            Collectors.toList());

    model.addAttribute("mode", "followers");
    model.addAttribute("userDto", modelMapper.map(byUsername, UserDto.class));
    model.addAttribute("following", collect);

    return "user/following";
  }
}
