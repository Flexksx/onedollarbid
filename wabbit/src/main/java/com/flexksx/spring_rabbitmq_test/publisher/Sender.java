package com.flexksx.spring_rabbitmq_test.publisher;

import com.flexksx.spring_rabbitmq_test.config.RaftLeaderConfig;
import com.flexksx.spring_rabbitmq_test.dto.AuctionItem;
import com.flexksx.spring_rabbitmq_test.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Service
@RequiredArgsConstructor
public class Sender {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);
    private final RestTemplate restTemplate;
    private final RaftLeaderConfig raftLeaderConfig;

    public void sendAuctionItemPostRequest(AuctionItem item) {
        String url = raftLeaderConfig.getBaseUrl() + "/api/items";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<AuctionItem> requestEntity = new HttpEntity<>(item, headers);

            ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                    url,
                    requestEntity,
                    ApiResponse.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                LOGGER.info("Response from leader '{}': {}", raftLeaderConfig.getPort(), response.getBody());
            } else {
                LOGGER.error("Failed to send POST request: {}", response.getBody());
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("HTTP error sending item to endpoint: {}", e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            LOGGER.error("Error sending item to endpoint: {}", url);
        }
    }

    public void sendMultipartFileRequest(File file) {
        String url = raftLeaderConfig.getBaseUrl() + "/data/json";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("files", new FileSystemResource(file));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<ApiResponse> response = restTemplate.postForEntity(
                    url,
                    requestEntity,
                    ApiResponse.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                LOGGER.info("Response from leader '{}': Status Code = {}, Body = {}", raftLeaderConfig.getPort(),
                        response.getStatusCode(), response.getBody());
            } else {
                LOGGER.error("Failed to send Multipart File request: Status Code = {}, Body = {}",
                        response.getStatusCode(), response.getBody());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            LOGGER.error("HTTP error sending file to endpoint: {}", e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            LOGGER.error("Error sending multipart file to endpoint {}", url);
        }
    }
}
