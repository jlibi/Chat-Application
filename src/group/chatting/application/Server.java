/*
package group.chatting.application;

*/
/** Server-side:
        Registers clients and handles sending messages.
        Processes and notifies when messages are read.
 *//*


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private static final AtomicInteger clientIdCounter = new AtomicInteger(1);

    private final Socket socket;
    private BufferedWriter writer;
    private final String clientId;

    public static Map<String, BufferedWriter> clientWriters = new ConcurrentHashMap<>();
    public static Map<String, String> messageOwners = new ConcurrentHashMap<>();

    */
/*public static Vector client = new Vector();*//*


    */
/*public Server (Socket socket, String clientIdCounter) {
        this.socket = socket;
        *//*
*/
/*this.clientId = "Client" + clientIdCounter;*//*
*/
/*
        this.clientId = clientIdCounter;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            // Send client ID back to client
            *//*
*/
/*writer.write("ClientID:" + clientId);*//*
*/
/*
            writer.write(clientId);
            writer.newLine();
            writer.flush();

            clientWriters.put(clientId, writer);
        } catch (IOException e) {
            System.out.println("Error setting up client: " + clientId);
            e.printStackTrace();
        }
    }*//*

    public Server(Socket socket) {
        this.socket = socket;
        this.clientId = "Client " + clientIdCounter.getAndIncrement();
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write("ClientID:" + clientId);
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
            */
/*try {
                clientWriters.remove(clientId);
                writer.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*//*

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

    private void handleReadReceipt(String data) throws IOException {
        String[] parts = data.substring(5).split(","); // Substring to remove "Read:"
        String messageId = parts[0];
        String fromClientId = parts[1];

        */
/*//*
/Log receipt or notify the original sender here
        System.out.println("Message " + messageId + "read by " + fromClientId);*//*


        notifySender(messageId, fromClientId);
    }

    private void notifySender(String messageId, String fromClientId) throws IOException {
        // Here you would find the original sender of the messageId and send them a notification
        // This is an example and might need adjustment based on your application's logic
        for (Map.Entry<String, BufferedWriter> entry : clientWriters.entrySet()) {
            if (entry.getKey().equals(findSenderId(messageId))) { // You need a method to find the sender by messageId
                BufferedWriter bw = entry.getValue();
                String notification = "Read:" + messageId + " by " + fromClientId;
                bw.write(notification);
                bw.newLine();
                bw.flush();
                break;
            }
        }
    }

    private String findSenderId(String messageId) {
        return messageOwners.get(messageId); // Directly return the sender ID from the map
    }



    private void broadcastMessage(String message, String senderId) throws  IOException {
        String messageId = extractMessageId(message); // Implement this method to extract message ID from message
        messageOwners.put(messageId, senderId); // Store message ID and sender ID mapping
        for (Map.Entry<String, BufferedWriter> entry : clientWriters.entrySet()) {
            if (!entry.getKey().equals(senderId)) { // Skip the sender
                BufferedWriter bw = entry.getValue();
                bw.write(message);
                bw.write("\r\n");
                bw.flush();
            }
        }
    }

    private String extractMessageId(String message) {
        // Assuming message format is "messageId|restOfMessage"
        String[] parts = message.split("\\|");
        return parts.length > 0 ? parts[0] : null; // Return the first part as the message ID
    }


*/
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
    }*//*




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
*/



package group.chatting.application;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private static final AtomicInteger clientIdCounter = new AtomicInteger(1);
    private final Socket socket;
    private BufferedWriter writer;
    private final String clientId;
    public static Map<String, BufferedWriter> clientWriters = new ConcurrentHashMap<>();
    public static Map<String, String> messageOwners = new ConcurrentHashMap<>();

    public Server(Socket socket) {
        this.socket = socket;
        this.clientId = "Client " + clientIdCounter.getAndIncrement();
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.write("ClientID:" + clientId);
            writer.newLine();
            writer.flush();
            clientWriters.put(clientId, writer);
        } catch (IOException e) {
            System.out.println("Error setting up client: " + clientId);
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String data;
            while ((data = reader.readLine()) != null) {
                if (data.startsWith("Read")) {
                    handleReadReceipt(data);
                } else {
                    broadcastMessage(data, clientId);
                }
            }
        } catch (Exception e) {
            System.out.println(clientId + " has disconnected");
        } finally {
            cleanup();
        }
    }

    private void cleanup() {
        clientWriters.remove(clientId);
        try {
            if (writer != null) writer.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Error cleaning up client: " + clientId);
        }
    }

    private void handleReadReceipt(String data) throws IOException {
        String[] parts = data.substring(5).split(",");
        if (parts.length == 2) {
            String messageId = parts[0];
            String fromClientId = parts[1];
            notifySender(messageId, fromClientId);
        }
    }

    private void notifySender(String messageId, String fromClientId) throws IOException {
        String senderId = messageOwners.get(messageId);
        BufferedWriter bw = clientWriters.get(senderId);
        if (bw != null) {
            bw.write("Read:" + messageId + " by " + fromClientId);
            bw.newLine();
            bw.flush();
        }
    }

    private void broadcastMessage(String message, String senderId) throws IOException {
        String messageId = extractMessageId(message);
        messageOwners.put(messageId, senderId);
        for (Map.Entry<String, BufferedWriter> entry : clientWriters.entrySet()) {
            if (!entry.getKey().equals(senderId)) {
                BufferedWriter bw = entry.getValue();
                bw.write(message);
                bw.newLine();
                bw.flush();
            }
        }
    }

    private String extractMessageId(String message) {
        return message.split("\\|")[0];
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(2003)) {
            System.out.println("Server started on port 2003");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Server server = new Server(clientSocket);
                new Thread(server).start();
            }
        } catch (IOException e) {
            System.out.println("Server failed to start: " + e.getMessage());
        }
    }
}
