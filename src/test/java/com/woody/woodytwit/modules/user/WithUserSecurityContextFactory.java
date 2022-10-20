package com.woody.woodytwit.modules.user;

import com.woody.woodytwit.modules.user.dto.SignUpDto;
import com.woody.woodytwit.modules.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithUserSecurityContextFactory implements WithSecurityContextFactory<WithUser> {

    private final UserService userService;

    private final UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(WithUser withAccount) {
        String nickname = withAccount.value();

        SignUpDto signUpDto = SignUpDto.builder()
                .nickname("잉깅")
                .username("inking")
                .email("dalniim12@email.com")
                .password("qwerqwer")
                .build();
        User user = userService.processNewUser(signUpDto);
        user.completeSignUp();

        UserDetails principal = userService.loadUserByUsername(signUpDto.getEmail());

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
