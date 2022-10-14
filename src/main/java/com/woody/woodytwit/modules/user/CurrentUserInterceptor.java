package com.woody.woodytwit.modules.user;

import com.woody.woodytwit.modules.user.dto.UserDto;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
public class CurrentUserInterceptor implements HandlerInterceptor {

  private final ModelMapper modelMapper;

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (modelAndView != null && authentication != null
        && !"anonymousUser".equals(authentication.getName())
        && authentication.getPrincipal() instanceof PrincipalDetails) {
      User user = ((PrincipalDetails) authentication.getPrincipal()).getUser();
      UserDto map = modelMapper.map(user, UserDto.class);
      modelAndView.addObject("currentUser", map);
    }
  }
}
