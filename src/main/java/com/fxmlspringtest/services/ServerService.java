package com.fxmlspringtest.services;

import com.fxmlspringtest.Controller.AdminController;
import com.fxmlspringtest.model.Account;
import com.fxmlspringtest.model.SelfSignedSSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Jonatan Fridsten on 2016-05-08.
 *
 * @author Jonatan Fridsten,Gustaf Bohlin, Sebastian Börebäck
 *         <p>
 *         This class handles the connection between the program and server.
 */
public class ServerService {

    private String url = "https://projektessence.se/api/users";
    //private String url = "http://192.168.1.46:44344/api/users";
    private String loginUrl = "https://projektessence.se/api/account";
    //private String loginUrl = "http://192.168.1.46:44344/api/account";
    private String logoutUrl = "https://projektessence.se/logout";
    //private String logoutUrl = "http://192.168.1.46:44344/logout";

    private static final Logger log = LoggerFactory.getLogger(ServerService.class);
    private RestTemplate restTemplate;
    private AdminController controller;
    private HttpHeaders headers;

    /**
     * Method will start the connection to the server and try to login, if it succeed i check will be done,
     * because the account need to have admin authorities to use the application.
     *
     * @param username Username for the account object
     * @param password Password for the account object
     * @return an Account with the correct username and password
     */
    public boolean login(String username, String password) {
        restTemplate = new RestTemplate();
        String userCredentials = encryptAuthentication(username, password);
        username = null;
        password = null;
        ResponseEntity<Map> response = null;
        try {
            SelfSignedSSL.trustSelfSignedSSL();

            headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, "Basic " + userCredentials);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(2000);
            requestFactory.setReadTimeout(2000);
            restTemplate.setRequestFactory(requestFactory);

            response = restTemplate.exchange(loginUrl, HttpMethod.GET, requestEntity, Map.class);

            Account adminAcc = new Account();
            LinkedHashMap<String, Object> responseMap = (LinkedHashMap<String, Object>) response.getBody();
            ArrayList<LinkedHashMap<String, Object>> auth = (ArrayList<LinkedHashMap<String, Object>>) responseMap.get("authorities");
            for (int i = 0; i < auth.size(); i++) {
                log.info(String.valueOf(auth.get(i).get("authority")));
                String res = String.valueOf(auth.get(i).get("authority"));
                if (res.equals(Account.ROLE_ADMIN)) {
                    log.info("admin == true");
                    LinkedHashMap<String, Object> accountMap = (LinkedHashMap<String, Object>) responseMap.get("principal");
                    adminAcc.createAccountFromMap(accountMap, userCredentials, new ArrayList<>());
                    log.info("Account: " + adminAcc.toString());
                    controller.setCurrentUser(adminAcc);
                    return true;
                } else {
                    controller.showLoginError("You don't have permission to use the application");
                }
            }
        } catch (HttpClientErrorException http) {
            log.error(http.getStatusCode().toString());
            controller.showLoginError("Wrong username or password");
        } catch (Exception e) {
            controller.showError("Can´t connect to the server");
            log.error(e.toString());
            log.error("är servern uppe??");
        }
        return false;
    }

    /**
     * Will get all the accounts that exists on the server
     *
     * @return array of the accounts in the server
     */
    public Account[] getAllUsers() {
        log.info("All users");
        try {
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<Account[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Account[].class);

            ResponseEntity<String> responseEntity2 = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
            log.info(Arrays.toString(responseEntity.getBody()));
            log.info(responseEntity2.getBody());
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            log.error(e.getStatusCode().toString());
            controller.showError(e.getStatusCode().toString());
        } catch (Exception e) {
            log.error(e.toString());
        }
        return null;
    }

    /**
     * Will send an post request to the server with the account object
     * so it could be added in the server
     *
     * @param account the new account object
     */
    public void addUser(Account account) {

        try {
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("Account", account);
            HttpEntity<MultiValueMap> entity = new HttpEntity<>(body, headers);
            log.info("Entitybody " + entity.getBody());
            log.info("Entity " + entity.toString());

            ResponseEntity<Account> responseEntity = restTemplate.exchange(url + "/a", HttpMethod.POST, entity, Account.class);
            log.info("AddBody: " + responseEntity.toString());
            log.info("Add user: " + responseEntity.getStatusCode().toString());
        } catch (HttpClientErrorException http) {
            log.error("!!Add user!!" + http.getStatusCode().toString());
            HttpStatus httpStatus = http.getStatusCode();
            switch (httpStatus) {
                case CONFLICT:
                    controller.showError("Rfid already in use");
                    break;
                case NOT_ACCEPTABLE:
                    controller.showError("Missing Firstname,Lastname and Password");
                    break;
                case METHOD_FAILURE:
                    controller.showError("Missing Password");
                    break;
                case IM_USED:
                    controller.showError("Username already exists");
                    break;
                default:
                    controller.showError(httpStatus.toString());
            }
        } catch (Exception e) {
            controller.showError(e.toString());
        }
    }

    /**
     * Metod will send an account id to the server with an
     * request for deleting the account
     *
     * @param account we want to delete
     */
    public void deleteUser(Account account) {
        log.info(account.toString());
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("Account", account);
        HttpEntity<MultiValueMap> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Account> responseEntity = restTemplate.exchange(url + "/" + account.getId(), HttpMethod.DELETE,
                    entity, Account.class);
            log.info("Deleted" + responseEntity.getBody());
        } catch (HttpClientErrorException e) {
            log.error(e.toString());
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    /**
     * This metod will send an updated version of an account to
     * pdate the original one on the server
     *
     * @param account updated account
     */
    public void updateUser(Account account) {
        log.info("Update user");
        try {
            MultiValueMap<String, Account> body = new LinkedMultiValueMap<>();
            body.add("Account", account);
            HttpEntity<MultiValueMap> entity = new HttpEntity<>(body, headers);
            log.info("Entitybody " + entity.getBody());
            log.info("Entity " + entity.toString());

            ResponseEntity<Account> responseEntity = restTemplate.exchange(url + "/a", HttpMethod.PUT, entity, Account.class);
            log.info(responseEntity.getBody().toString());
            log.info(responseEntity.getStatusCode().toString());
        } catch (HttpClientErrorException http) {
            log.error("!!!Errormessage!!!!");
            controller.showError(http.getStatusCode().toString());
            //switch ()
        } catch (Exception e) {
            log.error("kaka " + e.toString());
        }
    }

    /**
     * This metod will encrypt the username and password
     * so that its not avible to se easy
     *
     * @param username username
     * @param password pasword
     * @return encrypted password and username
     */
    private String encryptAuthentication(String username, String password) {
        String plainCreds = username + ":" + password;
        byte[] plainCredsByte = plainCreds.getBytes();
        byte[] base64 = Base64Utils.encode(plainCredsByte);
        return new String(base64);
    }


    /**
     * This metod connects this service with the controller
     *
     * @param controller
     */
    public void setController(AdminController controller) {
        this.controller = controller;
    }


    /**
     * This will tell the server that the user wants to logout from the server
     */
    public void logout() {
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(logoutUrl, HttpMethod.POST, httpEntity, String.class);
            log.info(responseEntity.getStatusCode().toString());
        } catch (HttpClientErrorException e) {
            log.info("HttpLogout " + e.toString());
        } catch (Exception e) {
            log.info("Logout " + e.toString());
        }
    }
}
