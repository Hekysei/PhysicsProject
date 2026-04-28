package com.example.isib.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class KirchhoffWebFlowTest {

  private static final Path USERS_FILE = Path.of(
      System.getProperty("java.io.tmpdir"),
      "kirchhoff-users-" + UUID.randomUUID() + ".txt");

  @jakarta.annotation.Resource
  private WebApplicationContext webApplicationContext;

  private MockMvc mockMvc;

  @DynamicPropertySource
  static void registerProperties(DynamicPropertyRegistry registry) {
    registry.add("app.security.users-file", USERS_FILE::toString);
  }

  @BeforeEach
  void setUp() throws IOException {
    Files.deleteIfExists(USERS_FILE);
    Files.createDirectories(USERS_FILE.getParent());
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .apply(springSecurity())
        .build();
  }

  @Test
  void registerAndLoginFlowWorks() throws Exception {
    mockMvc.perform(post("/register")
            .with(csrf())
            .param("username", "student_user")
            .param("password", "secret123")
            .param("confirmPassword", "secret123"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/login?registered"));

    mockMvc.perform(formLogin("/login").user("student_user").password("secret123"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));
  }

  @Test
  void simulationFormShowsValidationErrors() throws Exception {
    mockMvc.perform(post("/")
            .with(user("demo").roles("USER"))
            .with(csrf())
            .param("v1", "12")
            .param("r1", "100")
            .param("r2", "200")
            .param("r3", "0")
            .param("r4", "100")
            .param("r5", "500")
            .param("r6", "500")
            .param("errorsEnabled", "true")
            .param("voltageErrorPercent", "1")
            .param("resistorErrorPercent", "2"))
        .andExpect(status().isOk())
        .andExpect(view().name("kirchhoff-main"))
        .andExpect(model().attributeHasFieldErrors("circuitData", "r3"))
        .andExpect(content().string(containsString("Сопротивление R3 должно быть больше 0.")));
  }

  @Test
  void restApiReturnsValidationErrorsForInvalidRequest() throws Exception {
    mockMvc.perform(post("/api/circuits/kirchhoff")
            .with(user("demo").roles("USER"))
            .with(csrf())
            .contentType("application/json")
            .content("""
                {
                  "v1": 12,
                  "r1": 100,
                  "r2": 200,
                  "r3": 0,
                  "r4": 100,
                  "r5": 500,
                  "r6": 500,
                  "errorsEnabled": true,
                  "voltageErrorPercent": 1,
                  "resistorErrorPercent": 2
                }
                """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.title").value("Validation failed"))
        .andExpect(jsonPath("$.errors.r3").value("Сопротивление R3 должно быть больше 0."));
  }

  @Test
  void restApiReturnsCalculatedPayload() throws Exception {
    mockMvc.perform(post("/api/circuits/kirchhoff")
            .with(user("demo").roles("USER"))
            .with(csrf())
            .contentType("application/json")
            .content("""
                {
                  "v1": 12,
                  "r1": 100,
                  "r2": 200,
                  "r3": 200,
                  "r4": 100,
                  "r5": 500,
                  "r6": 500,
                  "errorsEnabled": false,
                  "voltageErrorPercent": 1,
                  "resistorErrorPercent": 2
                }
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.requestedData.r1").value(100.0))
        .andExpect(jsonPath("$.effectiveData.r1").value(100.0))
        .andExpect(jsonPath("$.results.totalResistance").exists());
  }

  @Test
  void loginPageRemainsPublic() throws Exception {
    mockMvc.perform(get("/login"))
        .andExpect(status().isOk());
  }

  @Test
  void registerPageRemainsPublic() throws Exception {
    mockMvc.perform(get("/register"))
        .andExpect(status().isOk())
        .andExpect(view().name("kirchhoff-register"));
  }
}
