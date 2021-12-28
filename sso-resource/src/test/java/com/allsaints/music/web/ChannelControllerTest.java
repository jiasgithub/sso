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
import com.allsaints.music.entity.SysChannel;
import com.allsaints.music.entity.repository.SysChannelRepository;

import lombok.SneakyThrows;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
class ChannelControllerTest {
	
	private static HttpHeaders httpHeaders = new HttpHeaders();

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		httpHeaders = TokenUtils.obtainAccessToken();
	}
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SysChannelRepository channelRepository;
	
	FieldDescriptor[] CHANNEL = new FieldDescriptor[] {
			fieldWithPath("id").description("ID"),
			fieldWithPath("code").description("渠道代码"),
			fieldWithPath("name").description("渠道名称"), 
			fieldWithPath("description").description("渠道描述"),
			fieldWithPath("sequenceNum").description("序号").type("Number").optional(),
			fieldWithPath("supportRegions").description("渠道支持的区域").type("Array").optional(), 
			fieldWithPath("status").description("状态")
		};
	
	private SysChannel createChannel() {
		SysChannel channel = new SysChannel();
		channel.setCode("1001");
		channel.setName("OPPO");
		channel.setDescription("OPPO");
		channel.setStatus(1);
		channel.setSequenceNum(1);
		return channelRepository.save(channel);
	}

	@Test
	@SneakyThrows
	void testQuery() {
		
		createChannel();
		
		mockMvc.perform(get("/channel/query")
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"【渠道】查询", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("code").description("渠道代码").optional(),
								parameterWithName("name").description("渠道名称").optional(),
								parameterWithName("regionId").description("区域ID").optional(),
								parameterWithName("status").description("数据状态").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("返回数据"),
								fieldWithPath("data.content[]").description("渠道列表")
								).andWithPrefix("data.content[].", CHANNEL)
							)
						);

	}

	@Test
	@SneakyThrows
	void testGet() {
		SysChannel channel = createChannel();
		
		mockMvc.perform(get("/channel/get").param("channelId", channel.getId() + "")
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"【渠道】获取信息", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("channelId").description("渠道ID")
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("渠道信息")
								).andWithPrefix("data.", CHANNEL)
							)
						);
	}

	@Test
	@SneakyThrows
	void testPost() {
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("code", "1001");
		params.add("name", "OPPO");
		params.add("description", "OPPO");
		params.add("sequenceNum", "1");
		params.add("status", "1");
		params.add("regions", "1,2");
		
		mockMvc.perform(post("/channel/post").params(params)
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"【渠道】新增", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("code").description("渠道代码"),
								parameterWithName("name").description("渠道名称"),
								parameterWithName("description").description("渠道描述").optional(),
								parameterWithName("sequenceNum").description("序号").optional(),
								parameterWithName("status").description("渠道状态，1 有效，0 无效").optional(),
								parameterWithName("regions").description("区域ID集合（逗号分隔）").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("渠道信息")
								).andWithPrefix("data.", CHANNEL)
							)
						);

	}

	@Test
	@SneakyThrows
	void testUpdate() {
		SysChannel channel = createChannel();
		
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("channelId", channel.getId() + "");
		params.add("code", "1001");
		params.add("name", "OPPO");
		params.add("description", "OPPO");
		params.add("sequenceNum", "1");
		params.add("status", "1");
		params.add("regions", "1,2");
		
		mockMvc.perform(post("/channel/update").params(params)
				.headers(httpHeaders))
				.andExpect(status().isOk())
				.andDo(print())
				.andDo(document(
						"【渠道】更新", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()), 
						requestParameters(
								parameterWithName("channelId").description("渠道ID，必填").optional(),
								parameterWithName("code").description("渠道代码"),
								parameterWithName("name").description("渠道名称"),
								parameterWithName("description").description("渠道描述").optional(),
								parameterWithName("sequenceNum").description("序号").optional(),
								parameterWithName("status").description("渠道状态，1 有效，0 无效").optional(),
								parameterWithName("regions").description("区域ID集合（逗号分隔）").optional()
						),
						relaxedResponseFields(
								fieldWithPath("code").description("返回码"),
								fieldWithPath("message").description("返回描述"),
								fieldWithPath("data").description("返回数据"),
								fieldWithPath("data").description("渠道信息")
								).andWithPrefix("data.", CHANNEL)
							)
						);
	}

}
