import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) {
        try {
            int port = 52584;
            Socket clientSocket = new Socket("127.0.0.1", port);
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String sendMsg = "HELO";
            System.out.println(String.format("Sending: %s", sendMsg));
            writer.println(sendMsg);
            String recievedMsg = reader.readLine();
            System.out.println(String.format("Recieved: %s", recievedMsg));

            if (recievedMsg.equals("G\'DAY")) {
                sendMsg = "BYE";
                System.out.println(String.format("Sending: %s", sendMsg));
                writer.println(sendMsg);
            } else {
                System.out.println("unrecognised greeting");
            }

            reader.close();
            writer.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}