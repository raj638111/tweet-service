package com.occ.rankingservice;

import com.occ.rankingservice.impl.Ranking;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RankingController.class)
public class RankingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void firstTest() throws Exception {
        this.mockMvc.perform(get("/ls"))
            .andDo(print())
            .andExpect(status().isOk());
    }
}
