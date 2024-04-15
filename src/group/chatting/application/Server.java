package group.chatting.application;

/** Server-side:
        Registers clients and handles sending messages.
        Processes and notifies when messages are read.
 */

import java.net.*;
import java.io.*;
import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Server implements Runnable {
    
    private Socket socket;
    private BufferedWriter writer;
    private String clientId;

    public static Map<String, BufferedWriter> clientWriters = new ConcurrentHashMap<>();
    
    /*public static Vector client = new Vector();*/
    
    public Server (Socket socket, String clientIdCounter) {
        this.socket = socket;
        /*this.clientId = "Client" + clientIdCounter;*/
        this.clientId = clientIdCounter;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // Send client ID back to client
            /*writer.write("ClientID:" + clientId);*/
            writer.write(clientId);
            writer.newLine();
            writer.flush();

            clientWriters.put(clientId, writer);
        } catch (IOException e) {
            System.out.println("Error setting up client: " + clientId);
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String data;
            while ((data = reader.readLine()) != null) {
                data = data.trim();
                if (data.startsWith("Read")) {
                    handleReadReceipt(data);
                } else {
                    System.out.println("Received message: " + clientId + ": " + data);
                    broadcastMessage(data, clientId);
                }
            }
        } catch (Exception e) {
            System.out.println(clientId + " has disconnected");
        } finally {
            cleanup();
        }
    }
            /*try {
                clientWriters.remove(clientId);
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
    private void cleanup() {
        try {
            clientWriters.remove(clientId);
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch(IOException e) {
            System.out.println("Error cleaning up client: " + clientId);
            e.printStackTrace();
        }
    }

    private void handleReadReceipt(String data) {
        String[] parts = data.substring(5).split(","); // Substring to remove "Read:"
        String messageId = parts[0];
        String fromClientId = parts[1];

        //Log receipt or notify the original sender here
        System.out.println("Message " + messageId + "read by " + fromClientId);
    }


    private void broadcastMessage(String message, String senderId) throws  IOException {
        for (Map.Entry<String, BufferedWriter> entry : clientWriters.entrySet()) {
            if (!entry.getKey().equals(senderId)) { // Skip the sender
                BufferedWriter bw = entry.getValue();
                bw.write(message);
                bw.write("\r\n");
                bw.flush();
            }
        }
    }


/*                for (int i = 0; i < client.size(); i++) {
                    try {
                        BufferedWriter bw = (BufferedWriter) client.get(i);
                        bw.write(data);
                        bw.write("\r\n");
                        bw.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/



    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(2003)) {
            int clientIdCounter = 1;

            System.out.println("Server started on port 2003");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                String clientId = "Client " + clientIdCounter++;
                System.out.println("Client connected: " + clientId);
                Server server = new Server(clientSocket, clientId);
                new Thread(server).start();
            }
        } catch (IOException e) {
            System.out.println("Server failed to start: " + e.getMessage());
        }
    }
}
