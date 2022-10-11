package com.woody.woodytwit.modules.user;

import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class PrincipalDetails extends User {

  private com.woody.woodytwit.modules.user.User user;

  public PrincipalDetails(com.woody.woodytwit.modules.user.User user){
    super(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
    this.user = user;
  }
}
