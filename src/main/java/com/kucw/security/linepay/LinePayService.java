package com.kucw.security.linepay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kucw.security.linepay.model.*;
import com.kucw.security.model.order.OrderItem;
import com.kucw.security.util.PostApiUtil;
import com.kucw.security.util.model.LinePayHeaderData;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

@Component
public class LinePayService {
    public String requestPayment(Integer orderId, BigDecimal totalAmount, Integer memberId, List<OrderItem> orderItemList) {
        // Request API
        CheckoutPaymentRequestFormData form = new CheckoutPaymentRequestFormData();
        form.setAmount(totalAmount.setScale(0));
        form.setCurrency("TWD");
        form.setOrderId(String.valueOf(orderId));

        ProductPackageForm productPackageForm = new ProductPackageForm();
        productPackageForm.setId("package_id");
        productPackageForm.setName("陳小小之家");
        productPackageForm.setAmount(totalAmount.setScale(0));

        List<ProductForm> productFormList = new ArrayList<>();
        for (OrderItem orderItem : orderItemList) {
            ProductForm productForm = new ProductForm();
            productForm.setId(String.valueOf(orderItem.getProductId()));
            productForm.setName(orderItem.getProductName());
            productForm.setImageUrl(orderItem.getImageUrl());
            productForm.setQuantity(new BigDecimal(orderItem.getQuantity()));
            productForm.setPrice(orderItem.getPrice().setScale(0));
            productFormList.add(productForm);
        }

        productPackageForm.setProducts(productFormList);

        form.setPackages(Arrays.asList(productPackageForm));

        RedirectUrls redirectUrls = new RedirectUrls();
        // 先暫填google 可導向付款成功頁面
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


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "success";

    }

    public static String encrypt(final String keys, final String data) {
        return toBase64String(HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, keys.getBytes()).doFinal(data.getBytes()));
    }

    public static String toBase64String(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }



}
