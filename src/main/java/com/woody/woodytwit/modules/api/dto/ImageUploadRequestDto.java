package com.woody.woodytwit.modules.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageUploadRequestDto {
  private String dataUri;

  private String fileName;
}
