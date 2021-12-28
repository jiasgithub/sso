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
			fieldWithPath("username").description("用户名称"),
			fieldWithPath("phone").description("电话").optional().type("String"), 
			fieldWithPath("email").description("邮箱").optional().type("String"), 
			fieldWithPath("description").description("用户描述").optional().type("String"),
			fieldWithPath("status").description("状态")
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
						"【INFO】用户信息", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("name").description("用户名称").optional(),
								parameterWithName("status").description("数据状态").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("返回数据"),
								fieldWithPath("data.name").description("用户名称"),
								fieldWithPath("data.roles[]").description("用户权限")
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
						"获取菜单", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("serviceId").description("pms").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("返回数据")
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
						"【PROFILE】用户配置", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("userId").description("用户ID，必填").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("返回数据")
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
						"【用户】查询", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("name").description("用户名称").optional(),
								parameterWithName("status").description("数据状态").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("返回数据"),
								fieldWithPath("data.content[]").description("用户列表")
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
						"【用户】获取信息", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("userId").description("用户ID，必填").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("用户信息")
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
						"【用户】新增", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("username").description("用户名称"),
								parameterWithName("password").description("登录密码"),
								parameterWithName("phone").description("电话").optional(),
								parameterWithName("email").description("邮箱").optional(),
								parameterWithName("deptId").description("部门ID").optional(),
								parameterWithName("lockStatus").description("账号锁定状态，0 未锁定，1 已锁定").optional(),
								parameterWithName("status").description("数据状态，1 有效，0 无效").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("用户信息")
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
						"【用户】更新", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("userId").description("用户ID，必填").optional(),
								parameterWithName("username").description("用户名称"),
								parameterWithName("password").description("登录密码"),
								parameterWithName("phone").description("电话").optional(),
								parameterWithName("email").description("邮箱").optional(),
								parameterWithName("deptId").description("部门ID").optional(),
								parameterWithName("lockStatus").description("账号锁定状态，0 未锁定，1 已锁定").optional(),
								parameterWithName("status").description("数据状态，1 有效，0 无效").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("用户信息")
								).andWithPrefix("data.", USER)
							)
						);

	}

}
