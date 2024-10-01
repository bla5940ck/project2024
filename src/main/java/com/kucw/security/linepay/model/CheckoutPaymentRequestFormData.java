package com.kucw.security.linepay.model;

import java.util.List;

public class CheckoutPaymentRequestFormData extends BaseFormData {

    private String orderId;

    private List<ProductPackageForm> packages;

    private RedirectUrls redirectUrls;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<ProductPackageForm> getPackages() {
        return packages;
    }

    public void setPackages(List<ProductPackageForm> packages) {
        this.packages = packages;
    }

    public RedirectUrls getRedirectUrls() {
        return redirectUrls;
    }

    public void setRedirectUrls(RedirectUrls redirectUrls) {
        this.redirectUrls = redirectUrls;
    }
}
