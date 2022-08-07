package com.example.TodolistLesson.demo.annotation;

import static org.mockito.Mockito.when;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.example.TodolistLesson.domain.user.model.MUser;

public class WithMockCustomUserSecrityContextFactory
	implements WithSecurityContextFactory<WithMockCustomUser>{
	@Override
	public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		MUser principal = new MUser();
	    principal.setUserId(customUser.userId());
	    principal.setAppUserName(customUser.appUserName());
	    principal.setPassword(customUser.password());
	    principal.setGender(customUser.gender());
	    principal.setRole(customUser.role());
		Authentication auth =
			new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());		
		context.setAuthentication(auth);
		return context;
	}
}
