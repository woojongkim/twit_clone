package com.woody.woodytwit.modules.twit;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woody.woodytwit.infra.MockMvcTest;
import com.woody.woodytwit.modules.twit.dto.TweetCreateRequestDto;
import com.woody.woodytwit.modules.user.WithUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockMvcTest
class TweetControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ModelMapper modelMapper;

  @Test
  @DisplayName("트윗 POST : 실패 - 유효성 검사")
  @WithUser("dalniim12@gmail.com")
  void tweetPostFail() throws Exception {

    TweetCreateRequestDto tweetCreateRequestDto = new TweetCreateRequestDto();
    tweetCreateRequestDto.setContent("");

    ObjectMapper objectMapper = new ObjectMapper();

    mockMvc.perform(post("/api/tweets")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(tweetCreateRequestDto))
        .with(csrf()))
        .andExpect(status().isBadRequest())
        .andExpect(authenticated())
        .andDo(print());
  }

  @Test
  @DisplayName("트윗 POST : 성공")
  @WithUser("dalniim12@gmail.com")
  void tweetPost() throws Exception {

    TweetCreateRequestDto tweetCreateRequestDto = new TweetCreateRequestDto();
    tweetCreateRequestDto.setContent("test");

    ObjectMapper objectMapper = new ObjectMapper();

    mockMvc.perform(post("/api/tweets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(tweetCreateRequestDto))
            .with(csrf()))
        .andExpect(status().isCreated())
        .andExpect(authenticated())
        .andDo(print());
  }
}