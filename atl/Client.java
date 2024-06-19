import java.net.*;
import java.io.*;
import java.util.ArrayList;

// ./ds-server -p 52584 -n -v all -c ds-sample-config01.xml

public class Client {
    public static void main(String[] args) {
        try {
            int port = 52584;
            Socket clientSocket = new Socket("127.0.0.1", port);
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
            // Handshake Begin ---------------
            sendMessage("HELO", writer); 

            if (recieveMessage(reader).equals("OK")) {
                sendMessage("AUTH xxx", writer);
            } else {
                System.out.println("unrecognised greeting");
            }

            if (recieveMessage(reader).equals("OK")) { // Handshake End -----------------
                sendMessage("REDY", writer);
            } else {
                System.out.println("unrecognised greeting");
            }
            
            String jobCall = "";
            String currReadLine = "";
            int dataCallServerNum = 0;
            ArrayList<Integer> coreCounts = new ArrayList<>();
            ArrayList<String> serverList = new ArrayList<>();
            while (!(currReadLine = recieveMessage(reader)).equals("NONE")){
                if (currReadLine.contains("JOBN")){
                    jobCall = currReadLine; // 
                    String[] values = currReadLine.split(" "); 
                    sendMessage(String.format("GETS Capable %s %s %s",  values[3], values[4], values[5]), writer);
                    currReadLine = recieveMessage(reader);

                    values = currReadLine.split(" ");
                    dataCallServerNum = Integer.parseInt(values[1]);
                    sendMessage("OK", writer);

                    // loop taking in all server input
                    for (int i = 0; i < dataCallServerNum; i++){
                        currReadLine = recieveMessage(reader);
                        serverList.add(currReadLine); // adds whole lines to ID servers
                        values = currReadLine.split(" ");
                        coreCounts.add(Integer.parseInt(values[4]));
                    }

                    sendMessage("OK", writer); // starts scheduling
                    recieveMessage(reader); // recieves .
                    values = serverList.get(returnIndexOfLargestCores(coreCounts)).split(" "); 
                    String[] jobID = jobCall.split(" ");
                    sendMessage(String.format("SCHD %s %s %s", jobID[1], values[0], values[1]), writer);  //SCHD jobID serverType serverID
                    recieveMessage(reader);
                    sendMessage("REDY", writer);
                    serverList.clear();
                } else if (currReadLine.contains("JCPL")) {
                    // JCPL endTime jobID serverType serverID
                    sendMessage("REDY", writer);
                } else {
                    break;
                }
            }

            sendMessage("QUIT", writer);
            if (recieveMessage(reader).equals("QUIT")){
                reader.close();
                writer.close();
                clientSocket.close();
            } else {
                System.out.println("unrecognised greeting");
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int returnIndexOfLargestCores(ArrayList<Integer> list) {
        int maxIndex = 0;
        int maxValue = list.get(0);

        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) > maxValue) {
                maxValue = list.get(i); // New largest value
                maxIndex = i; // Index of the largest value
            }
        }
        return maxIndex;
    }

    public static void sendMessage(String msg, PrintWriter writer) {
        System.out.println(String.format("Sending: %s", msg));
        writer.println(msg);
    }

    public static String recieveMessage(BufferedReader reader){
        String msg = "";
        try {
            msg = reader.readLine().trim();
            System.out.println(String.format("Recieved: %s", msg));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return msg;
    }

}