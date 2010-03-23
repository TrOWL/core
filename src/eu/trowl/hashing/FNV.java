/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.trowl.hashing;

/**
 *
 * @author ed
 */
public class FNV {
    private static org.getopt.util.hash.FNV1 hasher = new org.getopt.util.hash.FNV164();
    /**
     *
     * @param str
     * @return
     */

    public static Long hash(Object str) { return hash(str.toString()); }
    public static Long hash(String str) {
//        int hash = kFNVOffset;
        
        hasher.init(str);
        return hasher.getHash();

//        for (int i = 0; i < str.length(); i++) {
//            hash ^= (0x0000ffff & (int) str.charAt(i));
//            hash *= kFNVPrime;
//        }

///        Long out = 0x00000000ffffffffl & (long) hash;
 //       return out.toString();
    }


        public static String hash32(Object str) {return hash32(str.toString()); }
    /**
     *
     * @param str
     * @return
     */
    public static String hash32(String str) {
        // now hash48 :/
        hasher.init(str);
        long out = hasher.getHash();
        byte[] hashb = new byte[]{
            (byte) (out >>> 32),
            (byte) (out >>> 24),
            (byte) (out >>> 16),
            (byte) (out >>> 8),
            (byte) (out >>> 0)
        };
        return (new sun.misc.BASE64Encoder().encode(hashb).substring(0, 8));
    }
}