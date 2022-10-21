package com.woody.woodytwit.modules.twit.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TweetCreateRequestDto {
      @NotBlank(message = "내용을 입력해주세요.")
      @Size(min = 1, max = 140, message = "내용은 1자 이상 140자 이하로 입력해주세요.")
      private String content;
}
