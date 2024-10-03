package com.kucw.security.util.model;

public class LinePayHeaderData extends BaseHeader{

    // 金流整合資訊 - Channel ID
    private String channelId;

    // UUID or timestamp(時間戳)
    private String nonce;

    // HMAC Base64 簽章
    private String signature;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public LinePayHeaderData(String channelId, String nonce, String signature) {
        this.channelId = channelId;
        this.nonce = nonce;
        this.signature = signature;
    }
}
