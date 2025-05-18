package com.finance.gateway.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.gateway.model.AuthRequest;
import com.finance.gateway.model.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RestAPIClient {

    @Value("${user.getuser.url}")
    private String GET_USER_URL;

    @Autowired(required = true)
    private final RestTemplate restTemplate;

    @Autowired
    public RestAPIClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Gateway-Auth", "valid");
        return headers;
    }

    public AuthRequest fetchUserDetails(String username) {
        log.info("Fetching user details for username: {}", username);
        String url = GET_USER_URL + username;

        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        log.info("Fetching user details for url: {}", url);

        ResponseEntity<RestResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, RestResponse.class);
        log.info("Fetching user details for response: {}", response);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("User details fetched successfully for data: {}", response.getBody());
                AuthRequest authRequest = new ObjectMapper().convertValue(response.getBody().getData(), AuthRequest.class);
                return authRequest;
            }
            throw new UsernameNotFoundException("User not found");
        }


}
