package com.allsaints.music;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

//@SpringBootTest
class SsoAuthApplicationTests {

	@Test
	void contextLoads() {
		// 1ef130b4-e4f8-4d30-8743-7cc09ce249d6
		System.out.println(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("123456"));
	}

}
