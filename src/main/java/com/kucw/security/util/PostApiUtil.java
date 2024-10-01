package com.kucw.security.util;

import com.kucw.security.linepay.model.RequestApiResponse;
import com.kucw.security.util.model.LinePayHeaderData;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class PostApiUtil {

    /**
     * @param linePayHeaderData
     * @param httpsUrl    : 請求網址
     * @param requestBody
     */
    public static RequestApiResponse sendLinePost(LinePayHeaderData linePayHeaderData, String httpsUrl, String requestBody) {
        RestTemplate restTemplate = new RestTemplate();

        // Post.Headers 設定
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-LINE-ChannelId", linePayHeaderData.getChannelId());
        headers.add("X-LINE-Authorization-Nonce", linePayHeaderData.getNonce());
        headers.add("X-LINE-Authorization", linePayHeaderData.getSignature());

        HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);

        RequestApiResponse responseBody = restTemplate.postForObject(httpsUrl, request, RequestApiResponse.class);

        return responseBody;
    }

}
