package com.example.TodolistLesson.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Test
@WithSecurityContext(factory = WithMockCustomUserSecrityContextFactory.class)
public @interface WithMockCustomUser {
	String userId() default "testuser@xxx.co.jp";
	String password() default "password";
	String appUserName() default "test user";
	int gender() default 1;
	String role() default "ROLE_GENERAL";
}
