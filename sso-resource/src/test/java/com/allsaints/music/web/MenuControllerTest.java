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
import com.allsaints.music.entity.SysPermission;
import com.allsaints.music.entity.repository.SysPermissionRepository;

import lombok.SneakyThrows;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class MenuControllerTest {

	private static HttpHeaders httpHeaders = new HttpHeaders();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		httpHeaders = TokenUtils.obtainAccessToken();
	}
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SysPermissionRepository permissionRepository;
	
	FieldDescriptor[] MENU = new FieldDescriptor[] {
			fieldWithPath("id").description("ID"),
			fieldWithPath("name").description("菜单名称"),
			fieldWithPath("title").description("菜单标题"),
			fieldWithPath("icon").description("菜单图标"),
			fieldWithPath("resource").description("菜单路径"),
			fieldWithPath("orderNum").description("菜单排序码"), 
			fieldWithPath("description").description("菜单描述").optional().type("String"),
			fieldWithPath("permissions").description("子菜单").optional().type("Array"),
			fieldWithPath("status").description("状态")
		};
	
	private SysPermission createMenu() {
		SysPermission permission = new SysPermission();
		permission.setName("Name");
		permission.setTitle("Title");
		permission.setIcon("icon");
		permission.setResource("resource");
		permission.setOrderNum(1);
		permission.setParentId(1L);
		permission.setDescription("测试数据");
		permission.setStatus(1);
		return permissionRepository.save(permission);
	}

	@Test
	@SneakyThrows
	void testQuery() {
		
		createMenu();
		
		mockMvc.perform(get("/menu/query")
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"【菜单】查询", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("name").description("菜单名称").optional(),
								parameterWithName("status").description("数据状态").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("返回数据"),
								fieldWithPath("data.content[]").description("菜单列表")
								).andWithPrefix("data.content[].", MENU)
							)
						);

	}

	@Test
	@SneakyThrows
	void testGet() {
		SysPermission menu = createMenu();
		
		mockMvc.perform(get("/menu/get").param("permissionId", menu.getId() + "")
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"【菜单】获取信息", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("permissionId").description("菜单ID")
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("菜单信息")
								).andWithPrefix("data.", MENU)
							)
						);
	}

	@Test
	@SneakyThrows
	void testPost() {
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("name", "OPPO");
		params.add("title", "OPPO");
		params.add("parentId", "1");
		params.add("resource", "1");
		params.add("icon", "icon");
		params.add("orderNum", "1");
		params.add("description", "OPPO");
		params.add("status", "1");
		
		mockMvc.perform(post("/menu/post").params(params)
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"【菜单】新增", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("name").description("菜单名称"),
								parameterWithName("title").description("菜单title").optional(),
								parameterWithName("parentId").description("父菜单").optional(),
								parameterWithName("resource").description("菜单资源路径").optional(),
								parameterWithName("icon").description("菜单图标").optional(),
								parameterWithName("orderNum").description("排序码").optional(),
								parameterWithName("description").description("菜单描述").optional(),
								parameterWithName("status").description("数据状态，1 有效，0 无效").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("菜单信息")
								).andWithPrefix("data.", MENU)
							)
						);

	}

	@Test
	@SneakyThrows
	void testUpdate() {
		
		SysPermission menu = createMenu();
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("permissionId", menu.getId() + "");
		params.add("name", "OPPO");
		params.add("title", "OPPO");
		params.add("parentId", "1");
		params.add("resource", "1");
		params.add("icon", "icon");
		params.add("orderNum", "1");
		params.add("description", "OPPO");
		params.add("status", "1");
		
		mockMvc.perform(post("/menu/update").params(params)
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"【菜单】更新", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("permissionId").description("菜单ID"),
								parameterWithName("name").description("菜单名称").optional(),
								parameterWithName("title").description("菜单title").optional(),
								parameterWithName("parentId").description("父菜单").optional(),
								parameterWithName("resource").description("菜单资源路径").optional(),
								parameterWithName("icon").description("菜单图标").optional(),
								parameterWithName("orderNum").description("排序码").optional(),
								parameterWithName("description").description("菜单描述").optional(),
								parameterWithName("status").description("数据状态，1 有效，0 无效").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("菜单信息")
								).andWithPrefix("data.", MENU)
							)
						);

	}

}
