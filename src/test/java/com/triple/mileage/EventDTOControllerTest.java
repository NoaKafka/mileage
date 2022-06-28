package com.triple.mileage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.triple.mileage.repository.query.EventDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@WebMvcTest // MVC와 관련된 클래스들만 로딩, MVC (단위)테스트 하기위해
@AutoConfigureMockMvc   // 자동으로 MockMvc 설정
public class EventDTOControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Post_EventControllerTest")
    public void eventTest() throws Exception{
        // body에 json 형식으로 회원의 데이터를 넣기 위해서 Map을 이용한다.
        List<String> array = new ArrayList<>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2- 851d-4a50-bb07-9cc15cbdc332"));

        EventDTO eventDTO = new EventDTO("REVIEW",
                "ADD",
                "review2",
                "좋아요!",
                array,
                "noakafka",
                "충정로");

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(eventDTO.toString()))
                .andDo(print());


    }
}
