package com.kucw.security.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kucw.security.linepay.http.ResponseBody;
import com.kucw.security.linepay.model.RequestApiResponse;
import com.kucw.security.util.model.LinePayData;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class PostApiUtil {

    /**
     * @param linePayData
     * @param httpsUrl    : 請求網址
     * @param requestBody
     */
    public static RequestApiResponse sendLinePost(LinePayData linePayData, String httpsUrl, String requestBody) {
        RestTemplate restTemplate = new RestTemplate();

        // Post.Headers 設定
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-LINE-ChannelId", linePayData.getChannelId());
        headers.add("X-LINE-Authorization-Nonce", linePayData.getNonce());
        headers.add("X-LINE-Authorization", linePayData.getSignature());

        HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);

        RequestApiResponse responseBody = restTemplate.postForObject(httpsUrl, request, RequestApiResponse.class);

        return responseBody;
    }

}
