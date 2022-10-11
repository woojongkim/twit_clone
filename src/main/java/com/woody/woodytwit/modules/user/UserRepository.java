package com.woody.woodytwit.modules.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByEmail(String email);

  boolean existsByEmail(String email);
  boolean existsByUsername(String username);

  User findByUsername(String username);
}
