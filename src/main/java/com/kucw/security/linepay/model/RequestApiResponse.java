package com.kucw.security.linepay.model;

import com.kucw.security.linepay.http.ResponseBody;

public class RequestApiResponse extends ResponseBody {
    private Info info;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
