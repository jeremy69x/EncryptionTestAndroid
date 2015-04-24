/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// OpenGL ES 2.0 code

#include <jni.h>
#include <android/log.h>


#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string>
#include <sstream>
#include <algorithm>
#include <iostream>
#include <fstream>

#define  LOG_TAG    "encryptor"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)





/*
 * This Method is a lite mothod to do a simple encryption of the public key
 * This type of method should be changed to do something more like real encryption
 * The effect is still the same however in that the public key will not appear in plan text here
 * This should be made to do something more legitamate towards encryption
 * call back into the java or something
 *
 *
 */
std::string encryptDecryptpubkey(std::string toEncrypt) {
    char key[62] = {'0','1', '2', '3','4', '5', '6','7', '8', '9','a', 'b', 'c','d', 'e', 'f','g', 'h', 'i','j', 'k', 'l'
    		,'m', 'n', 'o','p', 'q', 'r','s', 't', 'u','v', 'w', 'x','y', 'z'
    		, 'A','B', 'C', 'D','E', 'F', 'G','H', 'I', 'J','K', 'L', 'M','N', 'O'
    		, 'P','Q', 'R', 'S','T', 'U', 'V','W', 'X', 'Y','Z'};
    std::string output = toEncrypt;

    for (int i = 0; i < toEncrypt.size(); i++)
        output[i] = toEncrypt[i] ^ key[i % (sizeof(key) / sizeof(char))];

    return output;
}



/*
 * This method is an example of how to callback into the java program and pass a paramter along with recieveing
 *  data back
 *  This method calls a method in the calling java class to generate a GUID as a public key
 */
std::string GetApplicationDirectory( JNIEnv* env, jobject obj){


   jclass clazz = env->FindClass("com/example/encryptiontest/MainActivity");




   jmethodID javaMethod = env->GetMethodID( clazz, "GetApplicationRelativePath", "()Ljava/lang/String;");
   jobject result = env->CallObjectMethod( obj, javaMethod);

   const char* str = env->GetStringUTFChars((jstring) result, NULL); // should be released but what a heck, it's a tutorial :)
   std::string s = str;


   return s;
}




std::string ReadFromBinaryFile(JNIEnv* env, jobject obj, std::string filename) {

	std::string filepath = GetApplicationDirectory(env, obj) + "/" + filename;

     std::ostringstream ostrm;
     std::ifstream fin(filepath.c_str(), std::ios::in | std::ios::binary);

     ostrm << fin.rdbuf();

     std::string data( ostrm.str() );
     return data;
 }

 /*
  * This method is an example of how to callback into the java program and pass a paramter along with recieveing
  *  data back
  *  This method calls a method in the calling java class to generate a GUID as a public key
  */
 std::string getPubKey( JNIEnv* env, jobject obj){


    jclass clazz = env->FindClass("com/example/encryptiontest/MainActivity");
    jmethodID javaMethod = env->GetMethodID( clazz, "GeneratePublicKey", "()Ljava/lang/String;");
    jobject result = env->CallObjectMethod( obj, javaMethod);

    const char* str = env->GetStringUTFChars((jstring) result, NULL); // should be released but what a heck, it's a tutorial :)
    std::string s = str;

    return s;
}

 /*
  * This method is an example of how to callback into the java program and pass a paramter along with recieveing
  *  data back
  *  This method calls a method in the calling java class to generate a GUID as a public key
  */
 std::string encryptPrivKey( JNIEnv* env, jobject obj,std::string privkey,std::string pubkey){


    jclass clazz = env->FindClass("com/example/encryptiontest/MainActivity");
    jmethodID javaMethod = env->GetMethodID( clazz, "Encrypt", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    jobject result = env->CallObjectMethod( obj, javaMethod, env->NewStringUTF(privkey.c_str()),env->NewStringUTF(pubkey.c_str()));

    const char* str = env->GetStringUTFChars((jstring) result, NULL); // should be released but what a heck, it's a tutorial :)
    std::string s = str;

    return s;
}


 /*
  * This method is an example of how to callback into the java program and pass a paramter along with recieveing
  *  data back
  *  This method calls a method in the calling java class to generate a GUID as a public key
  */
 std::string decryptPrivKey( JNIEnv* env, jobject obj,std::string encprivkey,std::string pubkey){


    jclass clazz = env->FindClass("com/example/encryptiontest/MainActivity");



    jmethodID javaMethod = env->GetMethodID( clazz, "Decrypt", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    jobject result = env->CallObjectMethod( obj, javaMethod, env->NewStringUTF(encprivkey.c_str()),env->NewStringUTF(pubkey.c_str()));

    const char* str = env->GetStringUTFChars((jstring) result, NULL); // should be released but what a heck, it's a tutorial :)
    std::string s = str;

    return s;
}






 void WriteToABinaryFile(JNIEnv* env, jobject obj, std::string filename, std::string toWrite){

	 std::string filepath = GetApplicationDirectory(env, obj) + "/" + filename;

     FILE* file = fopen(filepath.c_str(),"w+");
     fflush(file);
     fclose(file);
     std::ofstream myfile;
     myfile.open (filepath.c_str(), std::ios::out | std::ios::binary);

     if (myfile.is_open())
     {

       myfile << toWrite;
       myfile.close();
     }

 }







/*
 * This method orchestrates taking a public and private key and obfuscating them
 * then stores them to a text file called hello.txt
 * I would recommend changing the name of the file to something more general and possibly also filling it
 * with dummy data
 */

 void saveEncryptionKeys(JNIEnv* env, jobject obj, std::string privkey){

	    //LOGI("Saving Keys");

	 	std::string pubkey = getPubKey(env,  obj);
	 	std::string encryptedprivkey = encryptPrivKey(env, obj, privkey, pubkey);

	    std::string encryptedpubkey = encryptDecryptpubkey( pubkey);




	    //LOGI("Public Key---%s",pubkey.c_str());
	    //LOGI("Public Key Encrypted---%s",encryptedpubkey.c_str());
	    //LOGI("Private Key----%s",privkey.c_str());
	    //LOGI("Private Key Encrypted----%s",encryptedprivkey.c_str());

	    //writeToAFile( env,  obj, encryptedpubkey.c_str(), encryptedprivkey.c_str() );
	    WriteToABinaryFile(env,  obj,"hello.txt",encryptedpubkey);
	    WriteToABinaryFile(env,  obj,"hello1.txt",encryptedprivkey);

}


 /*
  * This method will orchestrate reading an obfuscated public and private key from
  * the hello.txt file where the keys are stored and decrypt them to return to the java code
  *
  *
  *
  */
 std::string getPrivateKey(JNIEnv * env, jobject obj){



	  std::string encryptedpublickey;
	  std::string encryptedprivkey;
	  std::string publickey;
	  std::string privkey;


	  encryptedprivkey = ReadFromBinaryFile(env, obj, "hello1.txt");
	  encryptedpublickey = ReadFromBinaryFile(env, obj, "hello.txt");

	  publickey = encryptDecryptpubkey( encryptedpublickey).c_str();

	  privkey = decryptPrivKey(env, obj, encryptedprivkey, publickey).c_str();



	  return privkey;

}


/*
 * Here are the JNI declarations for calling from the Java code into the C++ code
 *
 *
 */
extern "C" {
    JNIEXPORT void JNICALL Java_com_example_encryptiontest_MainActivity_encryptakey(JNIEnv * env, jobject obj,  jstring privatekey);
    JNIEXPORT jstring JNICALL Java_com_example_encryptiontest_MainActivity_getakey(JNIEnv * env, jobject obj);
};



JNIEXPORT void JNICALL Java_com_example_encryptiontest_MainActivity_encryptakey(JNIEnv * env, jobject obj,  jstring privatekey)
{

	saveEncryptionKeys(env, obj, env->GetStringUTFChars(privatekey,0));

};

JNIEXPORT jstring JNICALL Java_com_example_encryptiontest_MainActivity_getakey(JNIEnv * env, jobject obj)
{
	return env->NewStringUTF(getPrivateKey(env, obj).c_str());
};




