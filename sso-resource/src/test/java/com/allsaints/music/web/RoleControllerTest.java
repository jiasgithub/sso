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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.allsaints.music.TokenUtils;
import com.allsaints.music.entity.SysRole;
import com.allsaints.music.entity.repository.SysRoleRepository;

import lombok.SneakyThrows;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class RoleControllerTest {

	private static HttpHeaders httpHeaders = new HttpHeaders();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		httpHeaders = TokenUtils.obtainAccessToken();
	}
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SysRoleRepository roleRepository;
	
	FieldDescriptor[] ROLE = new FieldDescriptor[] {
			fieldWithPath("id").description("ID"),
			fieldWithPath("name").description("角色名称"), 
			fieldWithPath("description").description("角色描述").optional().type("String"),
			fieldWithPath("status").description("状态")
		};
	
	private SysRole createRole() {
		SysRole role = new SysRole();
		role.setParentId(1L);
		role.setName("name");
		role.setDescription("description");
		role.setStatus(1);
		return roleRepository.save(role);
	}

	@Test
	@SneakyThrows
	void testQuery() {
		
		createRole();
		
		mockMvc.perform(get("/role/query")
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"【角色】查询", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("name").description("角色名称").optional(),
								parameterWithName("status").description("数据状态").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("返回数据"),
								fieldWithPath("data.content[]").description("角色列表")
								).andWithPrefix("data.content[].", ROLE)
							)
						);

	}

	@Test
	@SneakyThrows
	void testGet() {
		SysRole role = createRole();
		
		mockMvc.perform(get("/role/get").param("roleId", role.getId() + "")
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"【角色】获取信息", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("roleId").description("角色ID")
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("角色信息")
								).andWithPrefix("data.", ROLE)
							)
						);
	}

	@Test
	@SneakyThrows
	void testPost() {
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("name", "OPPO");
		params.add("description", "OPPO");
		params.add("status", "1");
		
		mockMvc.perform(post("/role/post").params(params)
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"【角色】新增", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("name").description("角色名称"),
								parameterWithName("description").description("角色描述").optional(),
								parameterWithName("status").description("数据状态，1 有效，0 无效").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("角色信息")
								).andWithPrefix("data.", ROLE)
							)
						);

	}

	@Test
	@SneakyThrows
	void testUpdate() {
		
		SysRole role = createRole();
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("roleId", role.getId() + "");
		params.add("name", "OPPO");
		params.add("description", "OPPO");
		params.add("status", "1");
		
		mockMvc.perform(post("/role/update").params(params)
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"【角色】更新", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("roleId").description("角色ID"),
								parameterWithName("name").description("角色名称").optional(),
								parameterWithName("description").description("角色描述").optional(),
								parameterWithName("status").description("数据状态，1 有效，0 无效").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("角色信息")
								).andWithPrefix("data.", ROLE)
							)
						);

	}

}
