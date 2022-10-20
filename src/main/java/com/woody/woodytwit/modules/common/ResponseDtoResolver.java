package com.woody.woodytwit.modules.common;

import com.woody.woodytwit.modules.common.dto.ResponseDto;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class ResponseDtoResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(ResponseDto.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

    ResponseDto responseDto = ResponseDto.builder().build();
    responseDto.setMethod(webRequest.getNativeRequest(HttpServletRequest.class).getMethod());
    responseDto.setPath(webRequest.getNativeRequest(HttpServletRequest.class).getRequestURI());
    responseDto.setTimestamp(LocalDateTime.now());

    return responseDto;
  }
}
