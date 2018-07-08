package com.itheima.handbyhand.bean;

import com.itheima.handbyhand.utils.RSAUtils;

import java.security.PublicKey;

//交易需要付款方的公钥,和收款放的公钥,就相当于寄快递需要双方的地址,还需要转账的信息,和付款方的签名才能完成转账操作
public class Transaction {

        private String senderPublicKey;
        private String receiverPublicKey;
        private String signature;
        private String content;

//验证交易的正确性
    public boolean verify(){
        //拿到付款人的公钥
        PublicKey sendPublicKey = RSAUtils.getPublicKeyFromString("RSA", senderPublicKey);
        //使用RSA里面的验证方式验证
        return RSAUtils.verifyDataJS("SHA256withRSA",sendPublicKey,content,signature);
    }


    public Transaction(String senderPublicKey, String receiverPublicKey, String signature, String content) {
        this.senderPublicKey = senderPublicKey;
        this.receiverPublicKey = receiverPublicKey;
        this.signature = signature;
        this.content = content;
    }

    public Transaction() {
    }

    public String getSenderPublicKey() {
        return senderPublicKey;
    }

    public void setSenderPublicKey(String senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
    }

    public String getReceiverPublicKey() {
        return receiverPublicKey;
    }

    public void setReceiverPublicKey(String receiverPublicKey) {
        this.receiverPublicKey = receiverPublicKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
