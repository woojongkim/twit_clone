package com.woody.woodytwit.modules.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByNickname(String nickname);

  User findByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByNickname(String nickname);

  boolean existsByUrl(String url);

  User findByEmailCheckToken(String token);
}
