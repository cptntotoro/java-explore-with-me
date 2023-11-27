package ru.practicum.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@SpringBootTest
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getSignUp_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/sign-up")).andDo(print())
                .andExpect(view().name("sign-up"));
    }

    @Test
    public void getLogin_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/sign-in")).andDo(print())
                .andExpect(view().name("sign-in"));
    }

}

