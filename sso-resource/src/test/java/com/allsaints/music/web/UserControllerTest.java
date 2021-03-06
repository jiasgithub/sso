package com.allsaints.music.web;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.allsaints.music.TokenUtils;
import com.allsaints.music.entity.SysUser;
import com.allsaints.music.entity.repository.SysUserRepository;

import lombok.SneakyThrows;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class UserControllerTest {

	private static HttpHeaders httpHeaders = new HttpHeaders();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		httpHeaders = TokenUtils.obtainAccessToken();
	}
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SysUserRepository userRepository;
	
	FieldDescriptor[] USER = new FieldDescriptor[] {
			fieldWithPath("id").description("ID"),
			fieldWithPath("username").description("????????????"),
			fieldWithPath("phone").description("??????").optional().type("String"), 
			fieldWithPath("email").description("??????").optional().type("String"), 
			fieldWithPath("description").description("????????????").optional().type("String"),
			fieldWithPath("status").description("??????")
		};
	
	private SysUser createUser() {
		SysUser user = new SysUser();
		user.setUsername("username");
		user.setPassword("password");
		user.setPhone("phone");
		user.setDeptId(1L);
		user.setEmail("email");
		user.setAdminFlag(1);
		user.setSuperFlag(1);
		user.setLockStatus(0);
		user.setStatus(1);
		return userRepository.save(user);
	}

	@Test
	@SneakyThrows
	void testInfo() {
		mockMvc.perform(get("/user/info")
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"???INFO???????????????", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("name").description("????????????").optional(),
								parameterWithName("status").description("????????????").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("?????????"),
								fieldWithPath("message").description("????????????"),
								fieldWithPath("data").description("????????????"),
								fieldWithPath("data.name").description("????????????"),
								fieldWithPath("data.roles[]").description("????????????")
								)
							)
						);
	}
	
	@Test
	@SneakyThrows
	void testMenus() {
		mockMvc.perform(get("/user/menus").param("serviceId", "pms")
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"????????????", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("serviceId").description("pms").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("?????????"),
								fieldWithPath("message").description("????????????"),
								fieldWithPath("data").description("????????????")
								)
							)
						);
	}

	@Test
	@SneakyThrows
	void testProfile() {
		SysUser user = createUser();
		
		mockMvc.perform(get("/user/profile").param("userId", user.getId() + "")
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"???PROFILE???????????????", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("userId").description("??????ID?????????").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("?????????"),
								fieldWithPath("message").description("????????????"),
								fieldWithPath("data").description("????????????")
								).andWithPrefix("data.", USER)
							)
						);
	}

	@Test
	@SneakyThrows
	void testQuery() {
		
		createUser();
		
		mockMvc.perform(get("/user/query")
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"??????????????????", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("name").description("????????????").optional(),
								parameterWithName("status").description("????????????").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("?????????"),
								fieldWithPath("message").description("????????????"),
								fieldWithPath("data").description("????????????"),
								fieldWithPath("data.content[]").description("????????????")
								).andWithPrefix("data.content[].", USER)
							)
						);

	}

	@Test
	@SneakyThrows
	void testGet() {
		SysUser user = createUser();
		
		mockMvc.perform(get("/user/get").param("userId", user.getId() + "")
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"????????????????????????", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("userId").description("??????ID?????????").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("?????????"),
								fieldWithPath("message").description("????????????"),
								fieldWithPath("data").description("????????????")
								).andWithPrefix("data.", USER)
							)
						);
	}

	@Test
	@SneakyThrows
	void testPost() {
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("username", "username");
		params.add("password", "password");
		params.add("phone", "13000000000");
		params.add("deptId", "1");
		params.add("email", "10000@qq.com");
		params.add("lockStatus", "0");
		params.add("status", "1");
		
		mockMvc.perform(post("/user/post").params(params)
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"??????????????????", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("username").description("????????????"),
								parameterWithName("password").description("????????????"),
								parameterWithName("phone").description("??????").optional(),
								parameterWithName("email").description("??????").optional(),
								parameterWithName("deptId").description("??????ID").optional(),
								parameterWithName("lockStatus").description("?????????????????????0 ????????????1 ?????????").optional(),
								parameterWithName("status").description("???????????????1 ?????????0 ??????").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("?????????"),
								fieldWithPath("message").description("????????????"),
								fieldWithPath("data").description("????????????")
								).andWithPrefix("data.", USER)
							)
						);

	}

	@Test
	@SneakyThrows
	void testUpdate() {
		
		SysUser user = createUser();
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("userId", user.getId() + "");
		params.add("username", "username");
		params.add("password", "password");
		params.add("phone", "13000000000");
		params.add("deptId", "1");
		params.add("email", "10000@qq.com");
		params.add("lockStatus", "0");
		params.add("status", "1");
		
		mockMvc.perform(post("/user/update").params(params)
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"??????????????????", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("userId").description("??????ID?????????").optional(),
								parameterWithName("username").description("????????????"),
								parameterWithName("password").description("????????????"),
								parameterWithName("phone").description("??????").optional(),
								parameterWithName("email").description("??????").optional(),
								parameterWithName("deptId").description("??????ID").optional(),
								parameterWithName("lockStatus").description("?????????????????????0 ????????????1 ?????????").optional(),
								parameterWithName("status").description("???????????????1 ?????????0 ??????").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("?????????"),
								fieldWithPath("message").description("????????????"),
								fieldWithPath("data").description("????????????")
								).andWithPrefix("data.", USER)
							)
						);

	}

}
