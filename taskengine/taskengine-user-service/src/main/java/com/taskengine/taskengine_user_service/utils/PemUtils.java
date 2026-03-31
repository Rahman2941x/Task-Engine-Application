package com.taskengine.taskengine_user_service.utils;

import org.springframework.core.io.Resource;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PemUtils {
    public static PrivateKey readPrivateKey(Resource resource) throws Exception {

        String key= new String(resource.getInputStream().readAllBytes());

        key=key
                .replace("-----BEGIN PRIVATE KEY-----","")
                .replace("-----END PRIVATE KEY-----","")
                .replaceAll("\\s+","");

        byte[] decoded= Base64.getDecoder().decode(key);

        PKCS8EncodedKeySpec spec=new PKCS8EncodedKeySpec(decoded);
        KeyFactory kf=KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public static PublicKey readPublicKey(Resource resource) throws Exception {
        String key=new String(resource.getInputStream().readAllBytes());

        key=key
                .replace("-----BEGIN PUBLIC KEY-----","")
                .replace("-----END PUBLIC KEY-----","")
                .replaceAll("\\s+","");

        byte[] decoded=Base64.getDecoder().decode(key);

        X509EncodedKeySpec spec= new X509EncodedKeySpec(decoded);
        KeyFactory kf=KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}
