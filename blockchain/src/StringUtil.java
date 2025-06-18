
import java.security.MessageDigest;

public class StringUtil {
    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256"); //creates an object for sha-256 hashing

            byte[] hash = digest.digest(input.getBytes("UTF-8")); // converts input string to bytes 
            //and stores in byte array
            
            StringBuffer hexString = new StringBuffer(); // resizable string buffer
            for(int i=0; i<hash.length; i++){ // loop through each byte in hash array
                String hex = Integer.toHexString(0xff & hash[i]); // masks the byte to treat as unsigned , 
                // since byute could be engative annd Integer.toHexString expects positive only
                if(hex.length()==1) hexString.append('0'); // ensures single digit hex values are paddded with leading 0
                hexString.append(hex);
            }
            return  hexString.toString();

        } catch (Exception e) { // error handling
            throw new RuntimeException(e);
        }
    }
}
