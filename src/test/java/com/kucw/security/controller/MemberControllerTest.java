package com.kucw.security.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kucw.security.dao.MemberDao;
import com.kucw.security.dto.MemberRegisterRequest;
import com.kucw.security.model.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientRegistrationRepository clientRegistrationRepository;

    @MockBean
    private JwtDecoder jwtDecoder;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MemberDao memberDao;

    // 註冊新帳號
    @Test
    public void register_success() throws Exception {
        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest();
        memberRegisterRequest.setEmail("test1@gmail.com");
        memberRegisterRequest.setPassword("123");
        memberRegisterRequest.setName("test1");
        memberRegisterRequest.setAge("18");

        String json = objectMapper.writeValueAsString(memberRegisterRequest);

        RequestBuilder requestBuilder = post("/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.memberId", notNullValue()))
                .andExpect(jsonPath("$.email", equalTo("test1@gmail.com")))
                .andExpect(jsonPath("$.createdDate", notNullValue()))
                .andExpect(jsonPath("$.lastModifiedDate", notNullValue()));

        // 檢查資料庫中的密碼不為明碼
        Member member1 = memberDao.getMemberByEmail(memberRegisterRequest.getEmail());
        assertNotEquals(member1.getPassword(), memberRegisterRequest.getPassword());
    }

    @Test
    public void register_emailAlreadyExist() throws Exception {
        // 先註冊一個帳號
        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest();
        memberRegisterRequest.setEmail("test2@gmail.com");
        memberRegisterRequest.setPassword("123");
        memberRegisterRequest.setName("test2");
        memberRegisterRequest.setAge("30");

        String json = objectMapper.writeValueAsString(memberRegisterRequest);

        RequestBuilder requestBuilder = post("/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201));

        // 再次使用同個 email 註冊
        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    @WithMockUser(username = "test33@gmail.com", roles = {"NORMAL_MEMBER"})
    public void testLogin_success() throws Exception {

        mockMvc.perform(post("/welcome")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome!"));

    }

    @Test
    @WithMockUser(username = "test33@gmail.com", roles = {"NORMAL_MEMBER"})
    public void testLogin_Forbidden() throws Exception {

        mockMvc.perform(post("/vipUrl")
        .with(csrf()))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenInvalidPassword_whenLogin_thenRedirection() throws Exception {
        // 先註冊一個帳號
        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest();
        memberRegisterRequest.setEmail("test6@gmail.com");
        memberRegisterRequest.setPassword("1234");
        memberRegisterRequest.setName("test6");
        memberRegisterRequest.setAge("30");

        String json = objectMapper.writeValueAsString(memberRegisterRequest);
        RequestBuilder requestBuilder = post("/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(201));

        // 模擬用戶密碼錯誤 會重新導向
        mockMvc.perform(post("/login")
                .param("username", "test6@gmail.com")  // 正確帳號
                .param("password", "123").with(csrf()))  // 錯誤密碼
                .andExpect(status().isFound());
    }
}