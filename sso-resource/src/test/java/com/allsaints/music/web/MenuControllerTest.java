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
			fieldWithPath("name").description("????????????"),
			fieldWithPath("title").description("????????????"),
			fieldWithPath("icon").description("????????????"),
			fieldWithPath("resource").description("????????????"),
			fieldWithPath("orderNum").description("???????????????"), 
			fieldWithPath("description").description("????????????").optional().type("String"),
			fieldWithPath("permissions").description("?????????").optional().type("Array"),
			fieldWithPath("status").description("??????")
		};
	
	private SysPermission createMenu() {
		SysPermission permission = new SysPermission();
		permission.setName("Name");
		permission.setTitle("Title");
		permission.setIcon("icon");
		permission.setResource("resource");
		permission.setOrderNum(1);
		permission.setParentId(1L);
		permission.setDescription("????????????");
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
						"????????????????????????", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("permissionId").description("??????ID")
						),
						relaxedResponseFields(
								fieldWithPath("code").description("?????????"),
								fieldWithPath("message").description("????????????"),
								fieldWithPath("data").description("????????????")
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
						"??????????????????", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("name").description("????????????"),
								parameterWithName("title").description("??????title").optional(),
								parameterWithName("parentId").description("?????????").optional(),
								parameterWithName("resource").description("??????????????????").optional(),
								parameterWithName("icon").description("????????????").optional(),
								parameterWithName("orderNum").description("?????????").optional(),
								parameterWithName("description").description("????????????").optional(),
								parameterWithName("status").description("???????????????1 ?????????0 ??????").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("?????????"),
								fieldWithPath("message").description("????????????"),
								fieldWithPath("data").description("????????????")
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
						"??????????????????", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("permissionId").description("??????ID"),
								parameterWithName("name").description("????????????").optional(),
								parameterWithName("title").description("??????title").optional(),
								parameterWithName("parentId").description("?????????").optional(),
								parameterWithName("resource").description("??????????????????").optional(),
								parameterWithName("icon").description("????????????").optional(),
								parameterWithName("orderNum").description("?????????").optional(),
								parameterWithName("description").description("????????????").optional(),
								parameterWithName("status").description("???????????????1 ?????????0 ??????").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("?????????"),
								fieldWithPath("message").description("????????????"),
								fieldWithPath("data").description("????????????")
								).andWithPrefix("data.", MENU)
							)
						);

	}

}
