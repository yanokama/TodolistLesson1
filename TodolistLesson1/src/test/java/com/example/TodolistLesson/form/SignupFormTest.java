package com.example.TodolistLesson.form;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.SmartValidator;

@SpringBootTest
public class SignupFormTest {	
	@Autowired
	SmartValidator smartValidator;
    
	private SignupForm signupForm =  new SignupForm();
	private Map<String, String> errors ;
	private BindingResult bindingResult = new BindException(signupForm, "signupForm");
	Class<?> validationGroup1 = ValidGroup1.class;
	Class<?> validationGroup2 = ValidGroup2.class;
	
	@BeforeEach
	void init() {
		signupForm = new SignupForm();
		errors = new HashMap<>();	
	}
    
	@ParameterizedTest
	@CsvSource({
		"testuser1@xxx.co.jp, 12345678, hogehoge, 1",
		"testuser1@xxx.co.jp, abcdefgh, hogehoge, 2",
		"testuser1@xxx.co.jp, @@@@@@@@, hogehoge, 1",
		"testuser1@xxx.co.jp, 123def@@, hogehoge, 1"
	})
	public void 正常系のテスト(String userId, String password, String name, Integer gendar ) {
		//setup
		signupForm.setUserId(userId);
		signupForm.setPassword(password);
		signupForm.setAppUserName(name);
		signupForm.setGender(gendar);
		List<FieldError> expected = new ArrayList<>();
		//exercise
		smartValidator.validate(signupForm, bindingResult, validationGroup1);
		//verify
		assertIterableEquals( expected, bindingResult.getFieldErrors());
		//exercise
		smartValidator.validate(signupForm, bindingResult, validationGroup2);
		//verify
		assertIterableEquals( expected, bindingResult.getFieldErrors());		
	}
	
	/**どういうわけか、バリデーション結果が安定せずNotNull、NotBlank、nullとなっていた
	 * notNullをとなるであろうgenderのみ値を入れたところ、安定したので、これらは分けてテストする*/
	@Test
	public void ブランクの場合はエラー_gendar以外() {
		//setup
		signupForm.setUserId(null);
		signupForm.setPassword(null);
		signupForm.setAppUserName(null);
		signupForm.setGender(1);
		String notBlank = "NotBlank";
		//exercise
		smartValidator.validate(signupForm, bindingResult, validationGroup1);
		for(FieldError error:bindingResult.getFieldErrors()) {
			errors.put(error.getField(), bindingResult.getFieldError().getCode());
		}
		//verify
		assertEquals(3, bindingResult.getFieldErrorCount());
		assertEquals( notBlank , errors.get("userId"), "userId" );
		assertEquals( notBlank , errors.get("password"), "password");
		assertEquals( notBlank , errors.get("appUserName"), "appUserName");	
	}
	@Test
	public void gendarがブランクの場合はエラー() {
		//setup
		signupForm.setUserId("testuser1@xxx.co.jp");
		signupForm.setPassword("password");
		signupForm.setAppUserName("hogehoge");
		signupForm.setGender(null);
		String notNull = null;
		//exercise
		smartValidator.validate(signupForm, bindingResult, validationGroup1);
		for(FieldError error:bindingResult.getFieldErrors()) {
			errors.put(error.getField(), bindingResult.getFieldError().getCode());
		}	
		//verify
		assertEquals(1, bindingResult.getFieldErrorCount());
		assertEquals( notNull, errors.get("gendar"), "gendar");		
	}
	@Test
	public void userIdがeメール形式じゃない場合はエラー() {
		//setup
		signupForm.setUserId("testuser1");//email形式じゃない
		signupForm.setPassword("password");
		signupForm.setAppUserName("hogehoge");
		signupForm.setGender(1);
		String expectedError = "Email";	
		smartValidator.validate(signupForm, bindingResult, validationGroup2);
		for(FieldError error:bindingResult.getFieldErrors()) {
			errors.put(error.getField(), bindingResult.getFieldError().getCode());
		}
		//verify
		assertEquals(1, bindingResult.getFieldErrorCount());
		assertEquals( expectedError, errors.get("userId"), "Field:userId");		
	}
	@Test
	public void passwordが7文字以下の場合はエラー() {
		//setup
		signupForm.setUserId("testuser1@xxx.co.jp");
		signupForm.setPassword("passwor");//7文字
		signupForm.setAppUserName("hogehoge");
		signupForm.setGender(1);
		String expectedError = "Length";
		//exercise
		smartValidator.validate(signupForm, bindingResult, validationGroup2);
		for(FieldError error:bindingResult.getFieldErrors()) {
			errors.put(error.getField(), bindingResult.getFieldError().getCode());
		}
		//verify
		assertEquals(1, bindingResult.getFieldErrorCount());
		assertEquals( expectedError, errors.get("password"), "Field:password");				
	}
	@Test
	public void passwordに使用できない文字を含む場合はエラー() {
		//setup
		signupForm.setUserId("testuser1@xxx.co.jp");
		signupForm.setPassword("passwordあ");
		signupForm.setAppUserName("hogehoge");
		signupForm.setGender(1);
		String expectedError = "Pattern";
		//exercise
		smartValidator.validate(signupForm, bindingResult, validationGroup2);
		for(FieldError error:bindingResult.getFieldErrors()) {
			errors.put(error.getField(), bindingResult.getFieldError().getCode());
		}
		//verify
		assertEquals(1, bindingResult.getFieldErrorCount());
		assertEquals( expectedError, errors.get("password"), "Field:password");		
	}
}
