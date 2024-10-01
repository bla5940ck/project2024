package com.kucw.security.linepay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kucw.security.linepay.model.*;
import com.kucw.security.util.PostApiUtil;
import com.kucw.security.util.model.LinePayHeaderData;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import java.util.Base64;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

public class CustomerCheck {

    public static void main(String[] args) {

        // Request API
        CheckoutPaymentRequestFormData form = new CheckoutPaymentRequestFormData();
        form.setAmount(new BigDecimal("100"));
        form.setCurrency("TWD");
        form.setOrderId("merchant_order_id1235");

        ProductPackageForm productPackageForm = new ProductPackageForm();
        productPackageForm.setId("package_id");
        productPackageForm.setName("shop_name");
        productPackageForm.setAmount(new BigDecimal("100"));


        ProductForm productForm = new ProductForm();
        productForm.setId("product_id");
        productForm.setName("烏薩奇玩偶");
        productForm.setImageUrl("https://media.karousell.com/media/photos/products/2024/4/24/chiikawa___1713962766_66a785ab_progressive.jpg");
        productForm.setQuantity(new BigDecimal("10"));
        productForm.setPrice(new BigDecimal("10"));
        productPackageForm.setProducts(Arrays.asList(productForm));

        form.setPackages(Arrays.asList(productPackageForm));

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setConfirmUrl("https://www.google.com");
        redirectUrls.setCancelUrl("");
        form.setRedirectUrls(redirectUrls);
        String requestUri = "/v3/payments/request";
        String requestHttpUri = "https://sandbox-api-pay.line.me/v3/payments/request";
        String nonce = UUID.randomUUID().toString();

        // 共用轉換
        ObjectMapper objectMapper = new ObjectMapper();

        // confirm API
        ConfirmFormData confirmFormData = new ConfirmFormData();
        // amount金額的部分對應的就是Request API中這筆交易的總金額。
        confirmFormData.setAmount(new BigDecimal("100"));
        confirmFormData.setCurrency("TWD");
        String confirmNonce = UUID.randomUUID().toString();
        String confirmUri = "/v3/payments/{transactionId}/confirm";

        String ChannelSecret = "c0205a0b92af3fa0ed220b5f3d0af349";


        try {
            // request
            String signature = encrypt(ChannelSecret,ChannelSecret + requestUri + objectMapper.writeValueAsString(form) + nonce);
            LinePayHeaderData linePayHeaderData = new LinePayHeaderData("2006397378", nonce, signature);
            System.out.println("signature => " + signature);
            System.out.println("body => " + objectMapper.writeValueAsString(form));
            System.out.println("nonce => " + nonce);

            // 發送post請求
            RequestApiResponse requestApiResponseBody = PostApiUtil.sendLinePost(linePayHeaderData, requestHttpUri, objectMapper.writeValueAsString(form));

            if (requestApiResponseBody != null) {
                long transactionId = requestApiResponseBody.getInfo().getTransactionId();
                confirmUri = confirmUri.replace("{transactionId}", String.valueOf(transactionId));

                System.out.println(requestApiResponseBody.getInfo().getPaymentUrl().getWeb());
            }

            // confirm
            String signatureConfirm = encrypt(ChannelSecret,ChannelSecret + confirmUri + objectMapper.writeValueAsString(confirmFormData) + confirmNonce);
            System.out.println("signatureConfirm => " + signatureConfirm);
            System.out.println("bodyConfirm => " + objectMapper.writeValueAsString(confirmFormData));
            System.out.println("confirmNonce => " + confirmNonce);


//            JsonNode confirmApiResponseBody = PostApiUtil.sendLinePost(linePayData, requestHttpUri, objectMapper.writeValueAsString(form));



        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }

    public static String encrypt(final String keys, final String data) {
        return toBase64String(HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, keys.getBytes()).doFinal(data.getBytes()));
    }

    public static String toBase64String(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

}
