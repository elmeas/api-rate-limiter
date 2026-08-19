package com.elmeas.interview;

import com.elmeas.interview.service.HelloService;
import com.elmeas.interview.service.InMemoryRateLimiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HelloControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private HelloService helloService;

  @Autowired
  private InMemoryRateLimiter rateLimiter;

  @BeforeEach
  void resetRateLimiter() {
    rateLimiter.clear();
  }

  @Test
  void shouldReturnHelloMessage() throws Exception {
    mockMvc.perform(get("/hello").header("X-User-Id", "user-1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Hello, There!"));
  }

  @Test
  void shouldRejectRequestsOverTheLimit() throws Exception {
    for (int request = 0; request < 100; request++) {
      mockMvc.perform(get("/hello").header("X-User-Id", "user-1"))
          .andExpect(status().isOk());
    }

    mockMvc.perform(get("/hello").header("X-User-Id", "user-1"))
        .andExpect(status().isTooManyRequests());
  }

  @Test
  void shouldTrackUsersIndependently() throws Exception {
    for (int request = 0; request < 100; request++) {
      mockMvc.perform(get("/hello").header("X-User-Id", "user-1"))
          .andExpect(status().isOk());
    }

    mockMvc.perform(get("/hello").header("X-User-Id", "user-2"))
        .andExpect(status().isOk());
  }
}
