package org.mlgnu.learnova.support.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtUserFactory.class)
public @interface WithMockJwtUser {

    long id() default 1L;

    String username() default "user";

    String[] authorities() default {"ROLE_USER"};
}
