package com.fxmlspringtest.services;

import com.fxmlspringtest.Controller.AdminController;
import com.fxmlspringtest.model.Account;
import com.fxmlspringtest.model.SelfSignedSSL;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by jonatan on 2016-05-26.
 */
public class ServerServiceTest {
    //private String url = "https://projektessence.se/api/users";
    private String url = "http://192.168.1.46:44344/api/users";
    //private String loginUrl = "https://projektessence.se/api/account";
    private String loginUrl = "http://192.168.1.46:44344/api/account";
    //private String logoutUrl = "https://projektessence.se/logout";
    private String logoutUrl = "http://192.168.1.46:44344/logout";

    private static final Logger log = LoggerFactory.getLogger(ServerService.class);
    private RestTemplate restTemplate;
    private AdminController controller;
    private HttpHeaders headers;



    @Test
    public void testGetAllUsers() throws Exception {

        log.info("All users");
        try {
            SelfSignedSSL.trustSelfSignedSSL();

            headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, "Basic " + encryptAuthentication("admin","pass"));
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(2000);
            requestFactory.setReadTimeout(2000);
            restTemplate.setRequestFactory(requestFactory);

            ResponseEntity<Account[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET,requestEntity,Account[].class);
            log.info(Arrays.toString(responseEntity.getBody()));
        } catch (HttpClientErrorException e) {
            log.error(e.getStatusCode().toString());
            controller.showError(e.getStatusCode().toString());
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    private String encryptAuthentication(String username, String password) {
        String plainCreds = username + ":" + password;
        byte[] plainCredsByte = plainCreds.getBytes();
        byte[] base64 = Base64Utils.encode(plainCredsByte);
        return new String(base64);
    }

}