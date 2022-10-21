package com.woody.woodytwit.modules.twit.dto;

import java.util.List;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TweetDto {

      private long id;

      private String content;

      private List<TweetDto> replies;

      private List<TweetDto> retweets;
}
