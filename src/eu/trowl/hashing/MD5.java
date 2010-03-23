package eu.trowl.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author sttaylor
 */
public class MD5 {
    

    private MD5() { }
    
    /**
     *
     * @param s
     * @return
     */
    public static String hash(String s) {
	try {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    byte[] digest = md.digest(s.getBytes());
	    
	    StringBuffer hash = new StringBuffer();
	    for (int i=0; i < digest.length; i++)
		hash.append(Integer.toHexString(0xFF & digest[i]));

	    return hash.toString();
	} catch (NoSuchAlgorithmException ex) {
	     ex.printStackTrace();
        }
	
	return null;
    }
}
