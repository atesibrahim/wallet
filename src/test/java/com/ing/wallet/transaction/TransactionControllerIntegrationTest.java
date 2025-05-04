package com.ing.wallet.transaction;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.wallet.authentication.application.dto.request.LoginRequest;
import com.ing.wallet.transaction.application.dto.ApproveTransactionRequest;
import com.ing.wallet.transaction.application.dto.DepositRequest;
import com.ing.wallet.transaction.application.dto.WithdrawRequest;
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
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:wallet_test_2;DB_CLOSE_DELAY=-1"
})
class TransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String jwtToken;
    private String walletId;

    @BeforeEach
    void setup() throws Exception {
        authenticate("b@test.com", "test");

        Long customerId = 1L;

        ResultActions walletResult = mockMvc.perform(get("/wallet/v1/wallets/{customerId}", customerId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());

        JsonNode walletJson = objectMapper.readTree(walletResult.andReturn().getResponse().getContentAsString());
        this.walletId = walletJson.get(0).path("id").asText();
    }

    @Test
    void shouldDepositSuccessfully() throws Exception {
        DepositRequest depositRequest = new DepositRequest(
                100.0,
                walletId,
                "BANK_TRANSFER"
        );

        mockMvc.perform(post("/wallet/v1/transactions/deposit")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(walletId))
                .andExpect(jsonPath("$.amount").value(100.0));
    }

    @Test
    void shouldWithdrawSuccessfully() throws Exception {
        WithdrawRequest withdrawRequest = new WithdrawRequest(
                50.0,
                walletId,
                "BANK_ACCOUNT"
        );

        mockMvc.perform(post("/wallet/v1/transactions/withdraw")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(withdrawRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(walletId))
                .andExpect(jsonPath("$.amount").value(50.0));
    }

    @Test
    void shouldListTransactions() throws Exception {
        mockMvc.perform(get("/wallet/v1/transactions/{walletId}", walletId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldApproveTransaction() throws Exception {

        DepositRequest depositRequest = new DepositRequest(
                1200.0,
                walletId,
                "PAYONEER"
        );

        MvcResult result = mockMvc.perform(post("/wallet/v1/transactions/deposit")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        String transactionId = objectMapper.readTree(responseJson).path("id").asText();

        authenticate("a@test.com", "abcde");

        ApproveTransactionRequest approveRequest = new ApproveTransactionRequest(Long.valueOf(transactionId), "APPROVED");

        mockMvc.perform(post("/wallet/v1/transactions/approve")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(approveRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    private void authenticate(String mail, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest(mail, password);

        MvcResult result1 = mockMvc.perform(post("/wallet/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String json = result1.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(json);
        this.jwtToken = root.path("data").path("token").asText();
    }
}