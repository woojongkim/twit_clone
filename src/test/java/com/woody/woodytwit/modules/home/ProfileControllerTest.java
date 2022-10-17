package com.woody.woodytwit.modules.home;

import static com.woody.woodytwit.modules.user.UserConstants.SIGN_UP_DTO;
import static com.woody.woodytwit.modules.user.UserConstants.SIGN_UP_DTO2;
import static org.junit.jupiter.api.Assertions.*;

import com.woody.woodytwit.infra.MockMvcTest;
import com.woody.woodytwit.modules.follow.FollowRepository;
import com.woody.woodytwit.modules.follow.FollowService;
import com.woody.woodytwit.modules.user.User;
import com.woody.woodytwit.modules.user.UserRepository;
import com.woody.woodytwit.modules.user.UserService;
import com.woody.woodytwit.modules.user.WithUser;
import com.woody.woodytwit.modules.user.dto.UserDto;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class ProfileControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  UserService userService;

  @Autowired
  FollowRepository followRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  FollowService followService;

  @Test
  @DisplayName("프로필 페이지")
  @WithUser("dalniim12@email.com")
  void profile() throws Exception {
    mockMvc.perform(get("/user/inking"))
        .andExpect(status().isOk())
        .andExpect(authenticated())
        .andExpect(view().name("user/profile"))
        .andExpect(model().attributeExists("user"))
        .andExpect(model().attributeExists("isOwner"))
        .andExpect(model().attributeExists("countFollowed"))
        .andExpect(model().attributeExists("countFollowing"));
  }

  @Test
  @DisplayName("팔로잉 리스트")
  @WithUser("dalniim12@email.com")
  void following() throws Exception {
    User fromUser = userRepository.findByUsername(
        userService.processNewUser(SIGN_UP_DTO).getUsername());
    User toUser = userRepository.findByUsername(
        userService.processNewUser(SIGN_UP_DTO2).getUsername());

    followService.follow(fromUser, toUser);

    mockMvc.perform(get("/user/"+fromUser.getUsername()+"/following"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("following"))
        .andExpect(authenticated())
        .andExpect(view().name("user/following"));
  }

  @Test
  @DisplayName("팔로워 리스트")
  @WithUser("dalniim12@email.com")
  void followers() throws Exception {
    User fromUser = userRepository.findByUsername(
        userService.processNewUser(SIGN_UP_DTO).getUsername());
    User toUser = userRepository.findByUsername(
        userService.processNewUser(SIGN_UP_DTO2).getUsername());

    followService.follow(fromUser, toUser);

    mockMvc.perform(get("/user/"+fromUser.getUsername()+"/followers"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("following"))
        .andExpect(authenticated())
        .andExpect(view().name("user/following"));
  }
}