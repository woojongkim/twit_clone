 package com.woody.woodytwit.modules.user.dto;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

 @Data
 @Builder
 @NoArgsConstructor
 @AllArgsConstructor
 public class ProfileUpdateDto {
   @NotBlank
   @Length(min = 1, max = 20)
   private String nickname;

   @Length(max = 256)
   private String description;

   private String backgroundImage;

   private String profileImage;
 }