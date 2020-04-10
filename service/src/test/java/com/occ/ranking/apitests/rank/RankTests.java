package com.occ.ranking.apitests.rank;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RankTests {

    String service = "com.occ.ranking.service.Ranking";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSmallFile() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", getFileFromResources("testfiles/small.txt"));
        body.add("service", service);
        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);
        ResponseEntity response = restTemplate.
                postForEntity(url(), requestEntity, String.class);
        assert(response.getStatusCode().is2xxSuccessful() == true);
        assert(response.getBody().toString().equals("3194"));
    }

    @Test
    public void testLargeFile() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", getFileFromResources("testfiles/large.txt"));
        body.add("service", service);
        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);
        ResponseEntity response = restTemplate.
                postForEntity(url(), requestEntity, String.class);
        assert(response.getStatusCode().is2xxSuccessful() == true);
        assert(response.getBody().toString().equals("871198282"));
    }

    private FileSystemResource getFileFromResources(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
            File file = new File(resource.getFile());
            return new FileSystemResource(file);
        }
    }

    private String url() {
        return "http://localhost:" + port + "/rank";
    }
}
