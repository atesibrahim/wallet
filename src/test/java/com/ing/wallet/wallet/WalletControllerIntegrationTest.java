package com.ing.wallet.wallet;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.wallet.authentication.application.dto.request.LoginRequest;
import com.ing.wallet.wallet.application.dto.CreateWalletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:wallet_test_3;DB_CLOSE_DELAY=-1"
})
class WalletControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;

    @BeforeEach
    void setUpUserAndAuthenticate() throws Exception {
        LoginRequest loginRequest = new LoginRequest("b@test.com", "test");

        MvcResult result = mockMvc.perform(post("/wallet/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        JsonNode root = objectMapper.readTree(json);
        this.jwtToken = root.path("data").path("token").asText();
    }

    @Test
    void testCreateWalletWithValidToken() throws Exception {
        CreateWalletRequest request = new CreateWalletRequest(
                "My Authenticated Wallet",
                "EUR",
                true,
                true,
                1L
        );

        mockMvc.perform(post("/wallet/v1/wallets")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletName").value("My Authenticated Wallet"))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void testRejectUnauthorizedAccess() throws Exception {
        jwtToken = "invalidvalid-j";
        CreateWalletRequest request = new CreateWalletRequest(
                "Invalid Token Wallet",
                "USD",
                true,
                true,
                1L
        );

        mockMvc.perform(post("/wallet/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testListWalletsForCustomerWhenAuthorized() throws Exception {
        Long customerId = 1L;

        mockMvc.perform(get("/wallet/v1/wallets/{customerId}", customerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());
    }
}