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
import com.allsaints.music.entity.SysDept;
import com.allsaints.music.entity.repository.SysDeptRepository;

import lombok.SneakyThrows;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class DeptControllerTest {

	private static HttpHeaders httpHeaders = new HttpHeaders();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		httpHeaders = TokenUtils.obtainAccessToken();
	}
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SysDeptRepository deptRepository;
	
	FieldDescriptor[] DEPT = new FieldDescriptor[] {
			fieldWithPath("id").description("ID"),
			fieldWithPath("name").description("????????????"), 
			fieldWithPath("description").description("????????????").type("String").optional(),
			fieldWithPath("sequenceNum").description("??????").type("Number").optional(),
			fieldWithPath("supportRoles").description("????????????").type("Array").optional(),
			fieldWithPath("status").description("??????")
		};

	private SysDept createDept() {
		SysDept dept = new SysDept();
		dept.setName("OPPO");
		dept.setDescription("????????????");
		dept.setSequenceNum(1);
		dept.setStatus(1);
		return deptRepository.save(dept);
	}
	
	@Test
	@SneakyThrows
	void testQuery() {
		
		createDept();
		
		mockMvc.perform(get("/dept/query")
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"??????????????????", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("name").description("????????????").optional(),
								parameterWithName("roleId").description("??????ID").optional(),
								parameterWithName("status").description("????????????").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("?????????"),
								fieldWithPath("message").description("????????????"),
								fieldWithPath("data").description("????????????"),
								fieldWithPath("data.content[]").description("????????????")
								).andWithPrefix("data.content[].", DEPT)
							)
						);

	}

	@Test
	@SneakyThrows
	void testGet() {
		SysDept dept = createDept();
		
		mockMvc.perform(get("/dept/get").param("deptId", dept.getId() + "")
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"????????????????????????", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("deptId").description("??????ID")
						),
						relaxedResponseFields(
								fieldWithPath("code").description("?????????"),
								fieldWithPath("message").description("????????????"),
								fieldWithPath("data").description("????????????")
								).andWithPrefix("data.", DEPT)
							)
						);
	}

	@Test
	@SneakyThrows
	void testPost() {
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("name", "OPPO");
		params.add("description", "OPPO");
		params.add("sequenceNum", "1");
		params.add("roles", "1,2");
		params.add("status", "1");
		
		mockMvc.perform(post("/dept/post").params(params)
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"??????????????????", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("name").description("????????????"),
								parameterWithName("description").description("????????????").optional(),
								parameterWithName("sequenceNum").description("??????").optional(),
								parameterWithName("roles").description("??????ID????????????????????????").optional(),
								parameterWithName("status").description("???????????????1 ?????????0 ??????").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("?????????"),
								fieldWithPath("message").description("????????????"),
								fieldWithPath("data").description("????????????")
								).andWithPrefix("data.", DEPT)
							)
						);

	}

	@Test
	@SneakyThrows
	void testUpdate() {
		
		SysDept dept = createDept();
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("deptId", dept.getId() + "");
		params.add("name", "OPPO");
		params.add("description", "OPPO");
		params.add("sequenceNum", "1");
		params.add("roles", "1,2");
		params.add("status", "1");
		
		mockMvc.perform(post("/dept/update").params(params)
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"??????????????????", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("deptId").description("??????ID"),
								parameterWithName("name").description("????????????"),
								parameterWithName("description").description("????????????").optional(),
								parameterWithName("sequenceNum").description("??????").optional(),
								parameterWithName("roles").description("??????ID????????????????????????").optional(),
								parameterWithName("status").description("???????????????1 ?????????0 ??????").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("?????????"),
								fieldWithPath("message").description("????????????"),
								fieldWithPath("data").description("????????????")
								).andWithPrefix("data.", DEPT)
							)
						);

	}

}
