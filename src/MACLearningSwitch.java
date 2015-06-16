import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class MACLearningSwitch {
    static HashMap<String,Integer> macTable = new HashMap<String,Integer>();   //mactable = macaddress : port number
    
    static int search(String mac) {
        if(macTable!=null && macTable.containsKey(mac)){
            int port = macTable.get(mac);
            return port;
        }else{
            return -1;
        }
    }
    
    static boolean updateMapping(String mac, int port) {
        Integer prevPort = macTable.put(mac,port);
        if(prevPort != null){ 
            return false;   
        }else{
            return true;
        }
    }
    
    static void addMapping(String mac, int port){
        macTable.put(mac,port);
    }
    
    static String getHexString(String macAddr){
       StringBuilder sb = new StringBuilder();
       for (String twoDigits:macAddr.split(":")){
         sb.append(twoDigits);
       }
       return sb.toString();
    }
    
    static boolean isMulticast(String hexString){
        long i = Long.parseLong(hexString, 16);
	    String bin = Long.toBinaryString(i);
        int len = bin.length();
	    if(len != 48){
	       String zero_pad = "0";
	       for(int j=1;j<48-len;j++) zero_pad = zero_pad + "0"; 
	       bin = zero_pad + bin;
        }    
        if(bin.charAt(7)=='1'){
            return true;
        }else{
            return false;
        }
    }
    
    public static String zero_pad_bin_char(String bin_char){
	    int len = bin_char.length();
	    if(len == 48) return bin_char;
	    String zero_pad = "";
	    for(int i=0;i<48-len;i++) zero_pad = zero_pad + "0"; 
	    return zero_pad + bin_char;
	}
    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        
        Scanner in = new Scanner(System.in);
        int packets = Integer.parseInt(in.nextLine());
        
        for(int i = 0 ; i<packets; i++)
        {   String currPacket = in.nextLine();
           
            String[] splittedPacket = currPacket.split(" ");
            int pIn = Integer.parseInt(splittedPacket[0]);
            String dest = getHexString(splittedPacket[1]);
            String source = getHexString(splittedPacket[2]);
            int pOut = 0;
            if(isMulticast(source)){
                  System.out.println("drop");
                  continue;
            }else{
                  int port = search(source);
                  if(port==-1){
                      addMapping(source,pIn);
                  }else if(port!=pIn){
                      updateMapping(source,pIn);
                  }else{
                      // do nothing;
                  }}
            
            if(isMulticast(dest)){
                System.out.println("flood");
            }else{ //unicast dest
                int port = search(dest);
                if(port==-1){ // no mapping found for dest
                   System.out.println("flood");
                }else{
                   if(pIn==port){  // if pIn == pOut --> drop packet
                       System.out.println("drop");
                   }else{  //print pOut
                       System.out.println(port);
                   }
                }}
            
        }
      }

}