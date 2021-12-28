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
			fieldWithPath("name").description("部门名称"), 
			fieldWithPath("description").description("部门描述").type("String").optional(),
			fieldWithPath("sequenceNum").description("序号").type("Number").optional(),
			fieldWithPath("supportRoles").description("部门角色").type("Array").optional(),
			fieldWithPath("status").description("状态")
		};

	private SysDept createDept() {
		SysDept dept = new SysDept();
		dept.setName("OPPO");
		dept.setDescription("测试数据");
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
						"【部门】查询", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("name").description("部门名称").optional(),
								parameterWithName("roleId").description("角色ID").optional(),
								parameterWithName("status").description("数据状态").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("返回数据"),
								fieldWithPath("data.content[]").description("部门列表")
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
						"【部门】获取信息", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("deptId").description("部门ID")
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("部门信息")
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
						"【部门】新增", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("name").description("部门名称"),
								parameterWithName("description").description("部门描述").optional(),
								parameterWithName("sequenceNum").description("序号").optional(),
								parameterWithName("roles").description("角色ID集合（逗号分隔）").optional(),
								parameterWithName("status").description("数据状态，1 有效，0 无效").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("部门信息")
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
						"【部门】更新", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("deptId").description("部门ID"),
								parameterWithName("name").description("部门名称"),
								parameterWithName("description").description("部门描述").optional(),
								parameterWithName("sequenceNum").description("序号").optional(),
								parameterWithName("roles").description("角色ID集合（逗号分隔）").optional(),
								parameterWithName("status").description("数据状态，1 有效，0 无效").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("部门信息")
								).andWithPrefix("data.", DEPT)
							)
						);

	}

}
