package com.itheima.handbyhand.bean;

import com.itheima.handbyhand.utils.RSAUtils;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Wallet {

    //非对称加密中公钥就是钱包的地址,相当于账号,密钥就是钱包的密码,所以需要公钥和私钥两个字段,钱包具有转账的功能(方法);
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public Wallet(String name){
        File pubfile = new File(name+".pub");
        File prifile = new File(name+".pri");
        if (!pubfile.exists()||pubfile.length()==0||!prifile.exists()||prifile.length()==0){

            RSAUtils.generateKeysJS("RSA",name+".pri",name+".pub");

        }
        publicKey= RSAUtils.getPublicKeyFromFile("RSA",name+".pub");
        privateKey= RSAUtils.getPrivateKey("RSA",name+".pri");
    }
    //转账的功能
    public Transaction transfer(String receiverPublicKey,String content){
        //将公钥的类型转换成字符串类型
        String publicKeyEncode = Base64.encode(publicKey.getEncoded());
        //生成签名,字符创类型的签名可以在网络中传输
        String signature = RSAUtils.getSignature("SHA256withRSA", privateKey, content);
        //创建交易对象
        Transaction transaction = new Transaction(publicKeyEncode,receiverPublicKey,signature,content);
        return transaction;

    }
    public static void main(String[] args) {
        Wallet a = new Wallet("lili");
        Wallet b = new Wallet("hehe");

    }


}
