package security;

import java.security.MessageDigest;
import java.util.Formatter;

public class Hash {
	
  /*
   * Hash a String into an integer.
   */
  public static int hash(String id){
	return id.hashCode();
  }
	
  public static byte[] encrypt(String x) throws Exception {
	java.security.MessageDigest d = MessageDigest.getInstance("SHA-1");
	d.reset();
	d.update(x.getBytes());
	return d.digest();
  }
  
  private static String byteArray2Hex(byte[] hash) {
      Formatter formatter = new Formatter();
      for (byte b : hash) {
          formatter.format("%02x", b);
      }
      return formatter.toString();
  }
  
  /*
   * Hash a string to SHA1 and return it in its hexadecimal form.
   */
  public static String encrypt2Hex(String x) throws Exception{
	  return byteArray2Hex(encrypt(x));
  }
  
  /*
   * SHA1("The quick brown fox jumps over the lazy dog")
   * = 2fd4e1c6 7a2d28fc ed849ee1 bb76e739 1b93eb12
   * cf http://en.wikipedia.org/wiki/SHA-1
   */
  public static void main(String[] args) throws Exception {
	  	String hash = "The quick brown fox jumps over the lazy dog";
	    System.out.println(encrypt2Hex(hash));    
  }
  
}