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
import com.allsaints.music.entity.SysService;
import com.allsaints.music.entity.repository.SysServiceRepository;

import lombok.SneakyThrows;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ServiceControllerTest {

	private static HttpHeaders httpHeaders = new HttpHeaders();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		httpHeaders = TokenUtils.obtainAccessToken();
	}
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SysServiceRepository serviceRepository;
	
	FieldDescriptor[] SERVICE = new FieldDescriptor[] {
			fieldWithPath("id").description("ID"),
			fieldWithPath("code").description("服务代码"), 
			fieldWithPath("name").description("服务名称"), 
			fieldWithPath("description").description("服务描述").optional().type("String"),
			fieldWithPath("status").description("状态")
		};
	
	private SysService createCreate() {
		SysService service = new SysService();
		service.setCode("code");
		service.setName("name");
		service.setParentId(1L);
		service.setDescription("description");
		service.setSequenceNum(1);
		service.setStatus(1);
		return serviceRepository.save(service);
	}

	@Test
	@SneakyThrows
	void testQuery() {
		
		createCreate();
		
		mockMvc.perform(get("/service/query")
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"【服务】查询", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("name").description("服务名称").optional(),
								parameterWithName("status").description("数据状态").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("返回数据"),
								fieldWithPath("data.content[]").description("服务列表")
								).andWithPrefix("data.content[].", SERVICE)
							)
						);

	}

	@Test
	@SneakyThrows
	void testGet() {
		SysService service = createCreate();
		
		mockMvc.perform(get("/service/get").param("serviceId", service.getId() + "")
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"【服务】获取信息", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("serviceId").description("服务ID，必填").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("服务信息")
								).andWithPrefix("data.", SERVICE)
							)
						);
	}

	@Test
	@SneakyThrows
	void testPost() {
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("name", "OPPO");
		params.add("code", "OPPO");
		params.add("description", "OPPO");
		params.add("status", "1");
		
		mockMvc.perform(post("/service/post").params(params)
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"【服务】新增", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("name").description("服务名称，必填").optional(),
								parameterWithName("code").description("服务代码").optional(),
								parameterWithName("description").description("服务描述").optional(),
								parameterWithName("status").description("数据状态，1 有效，0 无效").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("服务信息")
								).andWithPrefix("data.", SERVICE)
							)
						);

	}

	@Test
	@SneakyThrows
	void testUpdate() {
		
		SysService service = createCreate();
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("serviceId", service.getId() + "");
		params.add("name", "OPPO");
		params.add("code", "OPPO");
		params.add("description", "OPPO");
		params.add("status", "1");
		
		mockMvc.perform(post("/service/update").params(params)
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"【服务】更新", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("serviceId").description("服务ID"),
								parameterWithName("name").description("服务名称"),
								parameterWithName("code").description("服务代码").optional(),
								parameterWithName("description").description("服务描述").optional(),
								parameterWithName("status").description("数据状态，1 有效，0 无效").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("服务信息")
								).andWithPrefix("data.", SERVICE)
							)
						);

	}

}
