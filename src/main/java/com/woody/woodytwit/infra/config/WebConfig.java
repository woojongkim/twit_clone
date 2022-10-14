package com.woody.woodytwit.infra.config;

import com.woody.woodytwit.modules.user.CurrentUserInterceptor;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final CurrentUserInterceptor currentUserInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    List<String> staticResourcesPath = Arrays.stream(StaticResourceLocation.values())
        .flatMap(StaticResourceLocation::getPatterns)
        .collect(Collectors.toList());
    staticResourcesPath.add("/node_modules/**");

    registry.addInterceptor(currentUserInterceptor)
        .excludePathPatterns(staticResourcesPath);
  }
}
