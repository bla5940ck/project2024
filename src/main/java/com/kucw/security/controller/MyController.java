package com.kucw.security.controller;

import com.kucw.security.linepay.LinePayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Autowired
    private LinePayService linePayService;

    @GetMapping("/index")
    public String index(Authentication authentication) {

        // 取得使用者的帳號（若使用 OAuth2 登入，會使用 providerId 的值當作帳號）
        String username = authentication.getName();

        return "Hello " + username + " 您的權限為: " + authentication.getAuthorities();
    }

    @PostMapping("/welcome")
    public String welcome() {
        return "Welcome!";
    }

    @PostMapping("/vipUrl")
    public String vip() {
        return "vip";
    }

    @PostMapping("/linePay/confirm")
    public String confirmPayment() {
        return linePayService.confirmPayment();
    }
}
