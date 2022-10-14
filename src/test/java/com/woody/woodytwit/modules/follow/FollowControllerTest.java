package com.woody.woodytwit.modules.follow;

import static org.junit.jupiter.api.Assertions.*;

import com.woody.woodytwit.infra.MockMvcTest;
import com.woody.woodytwit.modules.user.User;
import com.woody.woodytwit.modules.user.UserRepository;
import com.woody.woodytwit.modules.user.UserService;
import com.woody.woodytwit.modules.user.WithUser;
import com.woody.woodytwit.modules.user.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;


import static com.woody.woodytwit.modules.user.UserConstants.SIGN_UP_DTO;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class FollowControllerTest {

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
  @DisplayName("팔로우 성공")
  @WithUser("dalniim12@email.com")
  void follow() throws Exception {
    UserDto userDto = userService.processNewUser(SIGN_UP_DTO);

    mockMvc.perform(post("/user/" + userDto.getUsername() + "/follow")
        .with(csrf()))
        .andExpect(model().attributeDoesNotExist("error"))
        .andExpect(status().is3xxRedirection());

    User fromUser = userRepository.findByUsername("inking");
    User toUser = userRepository.findByUsername(SIGN_UP_DTO.getUsername());

    Follow follow = followRepository.findByFromUserAndToUser(fromUser, toUser);

    assertNotNull(follow);
  }

  @Test
  @DisplayName("언팔로우 성공")
  @WithUser("dalniim12@email.com")
  void unfollow() throws Exception {
    userService.processNewUser(SIGN_UP_DTO);

    User fromUser = userRepository.findByUsername("inking");
    User toUser = userRepository.findByUsername(SIGN_UP_DTO.getUsername());

    followService.follow(fromUser, toUser);

    mockMvc.perform(post("/user/" + toUser.getUsername() + "/unfollow")
        .with(csrf()))
        .andExpect(model().attributeDoesNotExist("error"))
        .andExpect(status().is3xxRedirection());

    Follow follow = followRepository.findByFromUserAndToUser(fromUser, toUser);

    assertNull(follow);
  }
}