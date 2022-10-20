package com.woody.woodytwit.modules.common.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
  private String path;
  private String method;
  private String message;
  private Object data;
  private HttpStatus status;
  private LocalDateTime timestamp;
}
