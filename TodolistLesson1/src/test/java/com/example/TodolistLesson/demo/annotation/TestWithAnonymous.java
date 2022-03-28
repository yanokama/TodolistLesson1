package com.example.TodolistLesson.demo.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Test
@WithUserDetails(userDetailsServiceBeanName = "UserDerviceImpl",
		value = "user@example.com")
public @interface TestWithAnonymous {}
