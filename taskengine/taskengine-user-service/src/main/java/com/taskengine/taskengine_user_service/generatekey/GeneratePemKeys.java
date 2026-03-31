package com.taskengine.taskengine_user_service.generatekey;
/*
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

public class GeneratePemKeys {
    public static void main(String[] args) throws Exception {


        KeyPairGenerator generator=KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);

        KeyPair keyPair=generator.generateKeyPair();

        WritePem("private.pem","PRIVATE KEY",keyPair.getPrivate().getEncoded());
        WritePem("public.pem","PUBLIC KEY",keyPair.getPublic().getEncoded());
        System.out.println("RSA keys generated successfully");
    }
    private static void WritePem(String fileName,String type,byte[] keyBytes) throws Exception {
        try(FileOutputStream fos=new FileOutputStream(fileName)) {
            fos.write(("-----BEGIN "+type+"-----\n").getBytes());
            fos.write(Base64.getMimeEncoder(64,"\n".getBytes()).encode(keyBytes));
            fos.write(("\n-----END "+type+"-----\n").getBytes());
        }

    }
}*/



