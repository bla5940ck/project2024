package com.kucw.security.linepay.model;

import com.kucw.security.http.ResponseBody;

public class ConfirmApiResponse extends ResponseBody {

    private Info info;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
