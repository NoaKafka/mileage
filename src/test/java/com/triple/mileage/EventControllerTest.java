package com.triple.mileage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.triple.mileage.data.Event;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
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
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Post_EventControllerTest")
    public void eventTest() throws Exception{
        // body에 json 형식으로 회원의 데이터를 넣기 위해서 Map을 이용한다.
        List<String> array = new ArrayList<>(Arrays.asList("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8", "afb0cef2- 851d-4a50-bb07-9cc15cbdc332"));

        Event event = Event.builder()
                .type("REVIEW")
                .action("ADD")
                .reviewId("240a0658-dc5f-4878-9381-ebb7b2667772")
                .content("좋아요!")
                .attachedPhotoIds(array)
                .userId("noakafka")
                .placeId("충정로")
                .build();

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk())
                .andExpect(content().string(event.toString()))
                .andDo(print());


    }
}
