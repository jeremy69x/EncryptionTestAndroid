package com.example.encryptiontest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


import android.R.string;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener {
	
	
	
	Button SaveKeyBtn; 
	Button GetKeyBtn;
	Button EncryptBtn;
	Button DecryptBtn;
	EditText privatekey;
	EditText publickey;
	EditText data;
	EditText datatoEncrypt;
	 byte[] iv;
	
	  private ViewGroup mRootView;
		private View mBlackScreen;
	 
	 
	   static {
		      System.loadLibrary("encryptor"); // "myjni.dll" in Windows, "libmyjni.so" in Unixes
		   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
        //getWindow().setFlags(LayoutParams.FLAG_SECURE,
                //LayoutParams.FLAG_SECURE);
        
        
		// Prevent screenshots
		// (But not on Gingerbread)
		//if (android.os.Build.VERSION.SDK_INT != android.os.Build.VERSION_CODES.GINGERBREAD && android.os.Build.VERSION.SDK_INT != android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {
			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		//}
 
		// The root view must have the flag android:filterTouchesWhenObscured set to true.
		setContentView(R.layout.activity_main);
 
		// Init the black screen
		mBlackScreen = new View(this);
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		mBlackScreen.setLayoutParams(layoutParams);
		mBlackScreen.setBackgroundColor(Color.BLACK);
		mBlackScreen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Catch click and do nothing
			}
		});
 
		// Get the root view
		mRootView = (ViewGroup) getWindow().findViewById(android.R.id.content);
        
        
        
        //setContentView(R.layout.activity_main);
        setupUI();

    }

    private void setupUI(){
    	//map the views on screen to local variables
    			SaveKeyBtn = (Button) findViewById(R.id.saveprivate);
    			SaveKeyBtn.setOnClickListener(this);
    			GetKeyBtn = (Button)findViewById(R.id.getkeybtn);
    			GetKeyBtn.setOnClickListener(this);
    			GetKeyBtn = (Button)findViewById(R.id.encryptDatabtn);
    			GetKeyBtn.setOnClickListener(this);
    			GetKeyBtn = (Button)findViewById(R.id.decryptDatabtn);
    			GetKeyBtn.setOnClickListener(this);
    			
    			privatekey= (EditText) findViewById(R.id.privatekey);
    			
    			publickey= (EditText) findViewById(R.id.publickey);
    			data= (EditText) findViewById(R.id.datatxtunencrypted);
    			datatoEncrypt= (EditText) findViewById(R.id.dateEditTxt);
    			
    			privatekey.setHint("privatekey");
    			privatekey.setText(CreateBase64EncodedEncryptionKey());
    			
    			
    			String android_id = Secure.getString(getContentResolver(),
                        Secure.ANDROID_ID); 
    			
    			
    			publickey.setHint(android_id);
    			data.setHint(GetApplicationRelativePath());
    			
    			datatoEncrypt.setText("Enter Data to Encrypt Here");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
 
    }
    
    
    // please, let me live even though I used this dark programming technique
    public  String CreateBase64EncodedEncryptionKey() {
  
    	KeyGenerator keyGen;
		try {
			keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(256); // for example
			SecretKey secretKey = keyGen.generateKey();
	    	
	    	this.privatekey.setText(Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT));
	    	
	    	
			return Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);
			
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return "";

    }  

    
    
    // please, let me live even though I used this dark programming technique
   /* public  String GeneratePublicKey() {
  
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
    public  String Encrypt(String data, String key) {
  

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
    public  String Decrypt(String data, String key) {


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
    
  */  

    
    
    public String GetApplicationRelativePath(){
    	File dir = this.getFilesDir();
    	return dir.getAbsolutePath();
    	
    }
    
    @Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		return super.onTouchEvent(event);

	}



	@Override
	public void onClick(View v) {
		String data;
		
		
		switch(v.getId()){	
		case R.id.saveprivate:
			this.data.setText("");
	        encryptakey(privatekey.getText().toString());
			//final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
			//try {
			  //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
			//} catch (android.content.ActivityNotFoundException anfe) {
			  //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
			//}
			break;		
		case R.id.getkeybtn:
			this.data.setText(getakey());
			break;	
		case R.id.encryptDatabtn:
			
			data = this.Encrypt(datatoEncrypt.getText().toString(), getakey());
			
			datatoEncrypt.setText(data);
			break;
		case R.id.decryptDatabtn:
			data = this.Decrypt(datatoEncrypt.getText().toString(), getakey());
			
			datatoEncrypt.setText(data);
			break;
		}	
		
	}
	
	
	
	
	
	
	

	 

		@Override
		protected void onPause() {
			//addBlackScreen();
			super.onPause();
		}
	 
		@Override
		protected void onResume() {
			//removeBlackScreen();
			super.onResume();
		}
	 
		private void addBlackScreen() {
			mRootView.addView(mBlackScreen);
		}
	 
		private void removeBlackScreen() {
			mRootView.removeView(mBlackScreen);
		}
		
	
	
	
	
	
	
	
	
	
	
	
	@Override
		protected void onStart() {
			// TODO Auto-generated method stub
		removeBlackScreen();
			super.onStart();
		}

		@Override
		protected void onStop() {
			// TODO Auto-generated method stub
			addBlackScreen();
			super.onStop();
		}

	public native void encryptakey( String privatekey);
	public native String getakey();

}
