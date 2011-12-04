package security;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {

	public RSA(boolean sign) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException{
		
	    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");

	    keyGen.initialize(512, new SecureRandom());

	    KeyPair keyPair = keyGen.generateKeyPair();
	    Signature signature = Signature.getInstance("SHA1withRSA", "BC");

	    signature.initSign(keyPair.getPrivate(), new SecureRandom());

	    byte[] message = "abc".getBytes();
	    signature.update(message);

	    byte[] sigBytes = signature.sign();
	    signature.initVerify(keyPair.getPublic());
	    signature.update(message);
	    System.out.println(signature.verify(sigBytes));
	}
	
	public RSA(){
		try {
			// Creation of what is needed for the security
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		    Cipher cipher;
			cipher = Cipher.getInstance("RSA/None/NoPadding", "BC");
		    SecureRandom random = new SecureRandom();
		    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
		    generator.initialize(512, random);
		    KeyPair keyPair = generator.generateKeyPair();
		    Signature signature = Signature.getInstance("SHA1withRSA", "BC");
		    signature.initSign(keyPair.getPrivate(), new SecureRandom());
		    
		    // Initialisation of the cipher
		    Key pubKey = keyPair.getPublic();
		    Key privKey = keyPair.getPrivate();
			cipher.init(Cipher.ENCRYPT_MODE, pubKey, random);
		    
			// Utilisation example
		    
			// Signature
			byte[] input = "test".getBytes();
			signature.update(input);
		    byte[] sigBytes = signature.sign();
		    signature.initVerify(keyPair.getPublic());
		    // signature.update(input);
		    // System.out.println(signature.verify(sigBytes));
			
			
			// Encryption/Decryption
			byte[] cipherText = cipher.doFinal(input);
		    System.out.println("cipher: " + new String(cipherText));
		    cipher.init(Cipher.DECRYPT_MODE, privKey);
		    byte[] plainText = cipher.doFinal(cipherText);
		    System.out.println("plain : " + new String(plainText));
		}catch (SignatureException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}	
	}
	
	
}
