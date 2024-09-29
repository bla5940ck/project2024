package com.kucw.security.linepay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kucw.security.linepay.model.CheckoutPaymentRequestForm;
import com.kucw.security.linepay.model.ProductForm;
import com.kucw.security.linepay.model.ProductPackageForm;
import com.kucw.security.linepay.model.RedirectUrls;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

public final class HmacSignature {
    private HmacSignature(){}

    public static String encrypt(final String keys, final String data) {
        return toBase64String(HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, keys.getBytes()).doFinal(data.getBytes()));
    }

    public static String toBase64String(byte[] bytes) {
        byte[] byteArray = Base64.encodeBase64(bytes, true);
        return new String(byteArray);
    }

    public static void main(String[] args) {
        CheckoutPaymentRequestForm form = new CheckoutPaymentRequestForm();


        form.setAmount(new BigDecimal("100"));
        form.setCurrency("TWD");
        form.setOrderId("merchant_order_id1234");

        ProductPackageForm productPackageForm = new ProductPackageForm();
        productPackageForm.setId("package_id");
        productPackageForm.setName("shop_name");
        productPackageForm.setAmount(new BigDecimal("100"));

        ProductForm productForm = new ProductForm();
        productForm.setId("product_id");
        productForm.setName("product_name");
        productForm.setImageUrl("https://cdn.pixabay.com/photo/2021/07/30/04/17/orange-6508617_1280.jpg");
        productForm.setQuantity(new BigDecimal("10"));
        productForm.setPrice(new BigDecimal("10"));
        productPackageForm.setProducts(Arrays.asList(productForm));

        form.setPackages(Arrays.asList(productPackageForm));

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setConfirmUrl("https://www.google.com");
        redirectUrls.setCancelUrl("");
        form.setRedirectUrls(redirectUrls);

        String ChannelSecret = "c0205a0b92af3fa0ed220b5f3d0af349";
        String requestUri = "/v3/payments/request";
        String nonce = UUID.randomUUID().toString();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String signature = encrypt(ChannelSecret,ChannelSecret + requestUri + objectMapper.writeValueAsString(form) + nonce);
            System.out.println("signature => " + signature);
            System.out.println("body => " + objectMapper.writeValueAsString(form));
            System.out.println("nonce => " + nonce);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
