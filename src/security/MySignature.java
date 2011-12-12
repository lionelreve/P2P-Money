package security;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;

/*
 * A class that permit to sign an array of bytes.
 */
public class MySignature {

  public static byte[] signData(byte[] data, PrivateKey key) throws Exception {
    Signature signer = Signature.getInstance("SHA1withRSA");
    signer.initSign(key);
    signer.update(data);
    return (signer.sign());
  }

  /*
   * Return if the array of data is well signed.
   */
  public static boolean verifySig(byte[] data, PublicKey key, byte[] sig) throws Exception {
    Signature signer = Signature.getInstance("SHA1withRSA");
    signer.initVerify(key);
    signer.update(data);
    return (signer.verify(sig));

  }

  /*
   * Generates a pair of keys.
   */
  public static KeyPair generateKeyPair(long seed) throws Exception {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
    SecureRandom rng = SecureRandom.getInstance("SHA1PRNG", "SUN");
    rng.setSeed(seed);
    keyGenerator.initialize(1024, rng);

    return (keyGenerator.generateKeyPair());
  }
	
  public static void main(String args[]) throws Exception {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

    KeyPair keyPair = generateKeyPair(999);

    byte[] data = { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74 };
    byte[] digitalSignature = signData(data, keyPair.getPrivate());

    boolean verified;

    verified = verifySig(data, keyPair.getPublic(), digitalSignature);
    System.out.println(verified) ;

    keyPair = generateKeyPair(888);
    verified = verifySig(data, keyPair.getPublic(), digitalSignature);
    System.out.println(verified);

  }

}
           
