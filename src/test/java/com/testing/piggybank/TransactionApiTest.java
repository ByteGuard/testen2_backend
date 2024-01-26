package com.testing.piggybank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.testing.piggybank.model.Transaction;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Objects;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;




@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class TransactionApiTest
{
@Autowired
    private TestRestTemplate restTemplate;
    ObjectMapper objectMapper = new ObjectMapper();
    @Test
    public void testGetTransactionById() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-User-Id", "1");

        // Make an API request for a specific transaction ID (assuming ID 1 in this case)
        ResponseEntity<Transaction> response = restTemplate.getForEntity("/api/v1/transactions/1", Transaction.class);

        // Verify the response status code
        assertEquals(200, response.getStatusCode().value());

        // Add additional assertions based on the expected response
        assertNotNull(response.getBody());
        // You can further assert the content of the Transaction object returned.
    }

    @Test
    public void testGetAllAccounts() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-User-Id", "1");

        // Make an API request to get all accounts
        ResponseEntity<Object> response = restTemplate.getForEntity("/api/v1/accounts", Object.class);

        // Verify the response status code
        assertEquals(200, response.getStatusCode().value());

        // Assert that the response body is not null
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateAccount() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-User-Id", "1");

        // Prepare the update payload
        String accountName = "Rekening van Melviny";
        long accountId = 1L;

        // Create an HttpEntity with headers and payload
        HttpEntity<String> requestEntity = new HttpEntity<>(getUpdatePayload(accountId, accountName), headers);

        // Make a PUT request to update the account
        ResponseEntity<Object> response = restTemplate.exchange("/api/v1/accounts", HttpMethod.PUT, requestEntity, Object.class);

        // Log the response body for debugging
        System.out.println("Response Body: " + Objects.toString(response.getBody()));

        // Verify the response status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert that the response body is not null
        assertNotNull(response.getBody());
    }


    private String getUpdatePayload(long accountId, String accountName) {
        try {
            // Create a JSON string representing the update payload
            return objectMapper.writeValueAsString(Map.of("accountId", accountId, "accountName", accountName));
        } catch (Exception e) {
            throw new RuntimeException("Error converting update payload to JSON", e);
        }
    }
}
