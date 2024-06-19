import java.net.*;
import java.io.*;
import java.util.ArrayList;

// ./ds-server -p 52584 -n -v all -c ds-sample-config01.xml

public class Client {
    public static void main(String[] args) {
        try {
            // Socket Setup -----------
            int port = 52584;
            Socket clientSocket = new Socket("127.0.0.1", port);
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // -------------------------

            String jobCall = ""; // String that holds whole JOBN calls
            String currReadLine = ""; // String that holds the current read line from reader
            int dataCallServerNum = 0; // Integer that holds the current number of servers from DATA call for loop interation
            ArrayList<String> serverList = new ArrayList<>(); // Holds array list of server data
            
            // Handshake Begin ---------------
            sendMessage("HELO", writer); 

            if (recieveMessage(reader).equals("OK")) {
                sendMessage("AUTH xxx", writer);
            } else {
                System.out.println("unrecognised greeting");
            }

            if (recieveMessage(reader).equals("OK")) { // Handshake End (technically) -----------------
                sendMessage("REDY", writer);
            } else {
                System.out.println("unrecognised greeting");
            }
          
            // While loop holding most code for algorithm
            while (!(currReadLine = recieveMessage(reader)).equals("NONE")){ // Base case check
                if (currReadLine.contains("JOBN")){
                    jobCall = currReadLine; // JOBN call
                    String[] values = currReadLine.split(" "); // values is used as an arbitrary string splitter array
                    sendMessage(String.format("GETS Capable %s %s %s",  values[3], values[4], values[5]), writer); // core, memory, disk
                    currReadLine = recieveMessage(reader); // DATA
                    values = currReadLine.split(" ");
                    dataCallServerNum = Integer.parseInt(values[1]); // num of servers for iterations
                    sendMessage("OK", writer);

                    // loop recording all server input, num of iterations from DATA call
                    for (int i = 0; i < dataCallServerNum; i++){
                        serverList.add(recieveMessage(reader)); // adds whole lines to ID servers
                    }

                    sendMessage("OK", writer); // starts scheduling
                    recieveMessage(reader); // recieves .
                    values = serverList.get(0).split(" "); // can hardcode to grab first item of arrayList because "first capable" algorithm
                    String[] jobID = jobCall.split(" "); // need to use a seperate string array because server info is being pulled from `values`
                    sendMessage(String.format("SCHD %s %s %s", jobID[1], values[0], values[1]), writer);  //SCHD jobID serverType serverID
                    recieveMessage(reader); // OK
                    sendMessage("REDY", writer);
                    serverList.clear(); // clears the arrayList otherwise server info keeps being appended to the end of it
                } else if (currReadLine.contains("JCPL")) {
                    // JCPL endTime jobID serverType serverID
                    sendMessage("REDY", writer);
                } else {
                    break;
                }
            }

            // Closing of all sockets
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

    // Standaridised send message functionality method
    public static void sendMessage(String msg, PrintWriter writer) {
        System.out.println(String.format("Sending: %s", msg));
        writer.println(msg);
    }

    // Standaridised recieve message functionality method that returns the message as a string
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