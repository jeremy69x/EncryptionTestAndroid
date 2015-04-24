package com.example.encryptiontest;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import android.app.Activity;

import android.util.Base64;

public aspect Encryptor {
    // please, let me live even though I used this dark programming technique
    public  String MainActivity.GeneratePublicKey() {
  
    	KeyGenerator keyGen;
		try {

			keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(256); // for example
			SecretKey secretKey = keyGen.generateKey();
			
			
	    	this.publickey.setText(Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT));
	    	
	    	
			return Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);
			
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return "";

    }
    
    // please, let me live even though I used this dark programming technique
    public  String MainActivity.Encrypt(String data, String key) {
  

		try {
			

		    byte[] encodedPubKey     = Base64.decode(key, Base64.DEFAULT);
		    

		    
		    
		    SecretKeySpec skeySpec = new SecretKeySpec(encodedPubKey, "AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
	        byte[] encrypted = cipher.doFinal(data.getBytes());
	        
	        
	        
	        
	        return new String(Base64.encode(encrypted, Base64.DEFAULT));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

    }
    
    // please, let me live even though I used this dark programming technique
    public  String MainActivity.Decrypt(String data, String key) {


		try {
		    byte[] encodedPubKey     = Base64.decode(key, Base64.DEFAULT);
		    byte[] encodedPrivKey     = Base64.decode(data, Base64.DEFAULT);
		    
		    
	        SecretKeySpec skeySpec = new SecretKeySpec(encodedPubKey, "AES");
	        Cipher cipher = Cipher.getInstance("AES");
	        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
	        byte[] decrypted = cipher.doFinal(encodedPrivKey);


	        
	        
	        
	        
	        return new String(decrypted, "UTF-8");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

    }
}
