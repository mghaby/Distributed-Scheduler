import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Client {
    public static void main(String[] args) {
        try {
            int port = 52584;
            Socket clientSocket = new Socket("127.0.0.1", port);
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
           
            ArrayList<Integer> coreCounts = new ArrayList<>();
            
            ArrayList<String> serverList = new ArrayList<>();
            
            int dataCallServerNum = 0;


            String sendMsg = "HELO";
            System.out.println(String.format("Sending: %s", sendMsg));
            writer.println(sendMsg);

            String recievedMsg = reader.readLine();
            System.out.println(String.format("Recieved: %s", recievedMsg));
            if (recievedMsg.equals("OK")) {
                sendMsg = "AUTH xxx";
                System.out.println(String.format("Sending: %s", sendMsg));
                writer.println(sendMsg);
            } else {
                System.out.println("unrecognised greeting");
            }

            // turn this whole reading and sending messages stuff into separate functions
            recievedMsg = reader.readLine();
            System.out.println(String.format("Recieved: %s", recievedMsg));
            if (recievedMsg.contains("OK")) {
                sendMsg = "REDY";
                System.out.println(String.format("Sending: %s", sendMsg));
                writer.println(sendMsg);
            } else {
                System.out.println("unrecognised greeting");
            }

            recievedMsg = reader.readLine();
            System.out.println(String.format("Recieved: %s", recievedMsg));
            // JOBN jobID submitTime core memory disk estRuntim
            if (recievedMsg.equals("JOBN 0 37 3 700 3800 653")) {
                sendMsg = "GETS Capable 3 700 3800";
                //sendMsg = "GETS All";
                System.out.println(String.format("Sending: %s", sendMsg));
                writer.println(sendMsg);
            } else {
                System.out.println("unrecognised greeting");
            }

            recievedMsg = reader.readLine();
            System.out.println(String.format("Recieved: %s", recievedMsg));
            // if (recievedMsg.equals("DATA 5 124")) { // this is for GETS All 
            if (recievedMsg.equals("DATA 3 124")) { // first parameter in DATA gives how many servers there are
                String[] values = recievedMsg.split(" ");
                dataCallServerNum = Integer.parseInt(values[1]);
                sendMsg = "OK";
                System.out.println(String.format("Sending: %s", sendMsg));
                writer.println(sendMsg);
            } else {
                System.out.println("unrecognised greeting");
            }

            // server data print = serverType serverID state curStartTime core memory
         
            // ALWAYS USE A FOR LOOP ON TO READ SERVER DATA BASED ON A DATA CALL

            // determines core count & stores them in arrayList coreCounts
            for (int i = 0; i < dataCallServerNum; i++){
                recievedMsg = reader.readLine().trim();
                System.out.println(String.format("Recieved: %s", recievedMsg));
    
                serverList.add(recievedMsg); // adds whole lines to ID servers
        
                String[] values = recievedMsg.split(" "); // splits each line
                coreCounts.add(Integer.parseInt(values[4])); // adds core count to ArrayList
            }

            //SCHD jobID serverType serverID
            sendMsg = "OK";
            System.out.println(String.format("Sending: %s", sendMsg));
            writer.println(sendMsg);
            recievedMsg = reader.readLine();
            String popped = serverList.get(returnIndexOfLargestCores(coreCounts));
            String[] tempArr = popped.split(" "); 
            String serverType = tempArr[0]; 
            String serverID = tempArr[1]; 
            sendMsg = String.format("SCHD 0 %s %s", serverType, serverID);
            System.out.println(String.format("Sending: %s", sendMsg));
            writer.println(sendMsg);

            // ./ds-server -p 52584 -n -v all -c ds-sample-config01.xml

            recievedMsg = reader.readLine();
            if (recievedMsg.equals("OK")) {
                sendMsg = "QUIT";
                System.out.println(String.format("Sending: %s", sendMsg));
                writer.println(sendMsg);
            } else {
                System.out.println("unrecognised greeting");
            }

            // exit loop
            recievedMsg = reader.readLine();
            if (recievedMsg.equals("QUIT")) {
                System.out.println(String.format("Recieved: %s", recievedMsg));
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

}