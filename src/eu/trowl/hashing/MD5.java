/*
 * This file is part of TrOWL.
 *
 * TrOWL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TrOWL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with TrOWL.  If not, see <http://www.gnu.org/licenses/>. 
 *
 * Copyright 2010 University of Aberdeen
 */

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
