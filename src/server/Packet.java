//Packet.java
//Formatted packet for client-server communication


package server;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Kane
 */
public class Packet {
    
    public static final String TEXT = "TEXT";
    public static final String CONNECT = "CONNECT";
    public static final String DISCONNECT = "DCONNECT";
    public static final String POLL = "POLL";
    public static final String LOGIN = "LOGIN";
    public static final String LOGIN_ACCEPT = "WELCOME";
    public static final String LOGIN_REJECT = "REJECT";
    
    private String packetType,packetData;
    private int packetID;
    
    
    
    public Packet(String type, String data) {
        packetType = type;
        packetData = data;
        packetID = -1;

    }
    
    //use this with the packets()[] method
    public Packet(String packetText) {
        String[] words = packetText.split(" ");
        packetType = words[3];
        packetData = getPacketData(packetText);
        packetID = -1;
    }
    
    public int getPacketID() {
        String[] words = packetData.split(" ");
        packetID = Integer.parseInt(words[0]);
        return packetID;
    }
    
    public String getType() {
        return packetType;
    }
    public String getData() {
        return packetData;
    }
    public void setType(String s) {
        packetType = s;
    }
    public void setData(String s) {
        packetData = s;
    }
    public int length() {
        return toString().length();
    }
    public int dataLength() {
        return packetData.length();
    }
    
    public String toString() {
        int dL = packetData.length();
        int tL = packetType.length();
        
        String dataLength = "";
        if (dL < 10)
            dataLength = "000"+dL;
        else if (dL < 100)
            dataLength = "00"+dL;
        else if (dL < 1000)
            dataLength = "0"+dL;
        else
            dataLength = ""+dL;
        
        return "[%[ "+dataLength+" "+tL+" "+packetType+" "+packetData+" ]%]";
    }
    
    public static String getPacketData(String data) {
        for (int i=0; i<data.length(); i++) {
            if (data.charAt(i)=='[') {
                data = data.substring(i);
                break;
            }
        }
        String[] words = data.split(" ");
        if (!words[0].equals("[%["))
            return "Invalid packet format.1";
        if (!words[words.length-1].endsWith("]%]")) {
            System.out.println(words[words.length-1]);
            return "Invalid packet format.2";
        }
       String packet = data.substring(4, data.length()-3);
       int dataLength = Integer.parseInt(words[1]);
       int typeLength = Integer.parseInt(words[2]);
       String pType = words[3];
       String pData = packet.substring(8+typeLength);
       return pData;
    }
    
    
    public static String[] packets(String stream) {
        ArrayList<String> packets = new ArrayList<>();
        String build = "";
        int stage = 0;
        for (int i=0; i<stream.length(); i++) {
            if (stream.charAt(i) == '[') {
                if (stage==0)
                    stage=1;
                else if (stage==2)
                    stage=3;
                else if (stage!=3)
                    stage=0;
                continue;
            }
            else if (stream.charAt(i)== '%') {
                if (stage==1)
                    stage=2;
                else if (stage==4)
                    stage=5;
                else if (stage!=3)
                    stage=0;
            }
            else if (stream.charAt(i)== ']') {
                if (stage==3)
                    stage=4;
                else if (stage==5)
                    stage=6;
                else if (stage!=3)
                    stage=0;
            }
            if (stage==3)
                build+=stream.charAt(i);
            if (stage==6) {
                packets.add("[%["+build+"]%]");
                build="";
                stage=0;
            }
                
        }
        Iterator it = packets.iterator();
        int size = packets.size();
        String[] output = new String[size];
        int i=0;
        while (it.hasNext())
            output[i++]=(String)it.next();
        return output;
    }
    
    public static void test() {
        String cone = "[%[ 0034 4 TEXT Hello. Are you getting my message?]%][%[ 0032 4 TEXT Hey. Is this a separate message?]%]";
        String[] p = packets(cone);
        int i=0;
        for (String s: p)
            System.out.println((i++)+": "+s);
    }
    
    public String getText() {
        String id = getData().split(" ")[0];
        return getData().substring(id.length()+1,getData().length()-1);
    }

}
