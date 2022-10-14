package com.woody.woodytwit.modules.home;

import static org.junit.jupiter.api.Assertions.*;

import com.woody.woodytwit.infra.MockMvcTest;
import com.woody.woodytwit.modules.user.WithUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class ProfileControllerTest {

  @Autowired
  MockMvc mockMvc;

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
}