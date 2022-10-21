package com.woody.woodytwit.modules.user;

import com.woody.woodytwit.modules.user.dto.ProfileUpdateDto;
import com.woody.woodytwit.modules.user.dto.SignUpDto;
import com.woody.woodytwit.modules.user.dto.UserDto;
import java.io.IOException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SettingsController {
  private final UserService userService;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  @GetMapping("/settings/profile")
  public String profileForm(@CurrentUser User user, Model model) {
    ProfileUpdateDto profileUpdateDto = modelMapper.map(user, ProfileUpdateDto.class);
    profileUpdateDto.setProfileImage(null);
    profileUpdateDto.setBackgroundImage(null);
    model.addAttribute(profileUpdateDto);

    return "settings/profile";
  }

  @PostMapping("/settings/profile")
  public String profile(@CurrentUser User user, @Valid ProfileUpdateDto profileUpdateDto, Errors errors, Model model)
      throws IOException {

    if (errors.hasErrors()) {
      return "settings/profile";
    }

    userService.updateProfile(user, profileUpdateDto);

    return "redirect:/user/"+user.getUsername();
  }
}
