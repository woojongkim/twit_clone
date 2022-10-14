package com.woody.woodytwit.modules.user;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithUserSecurityContextFactory.class)
public @interface WithUser {

    String value();

}
