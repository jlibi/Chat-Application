package group.chatting.application;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.net.*;
import java.io.*;

import static java.lang.System.out;

public class UserTwo implements ActionListener, Runnable {
    private String clientId;  // Define clientId
    JTextField text;
    JPanel a1;
    static Box vertical = Box.createVerticalBox();
    static JFrame f = new JFrame();
    /*static DataOutputStream dout;*/

    BufferedReader reader;
    BufferedWriter writer;
    private Socket socket;  // Socket as an instance variable if needed elsewhere
    String name = "Guddu Bhaiya";

    UserTwo() {
        initializeUI();
        establishConnection();
    }

    private void initializeUI() {

        // UI setup code here
        f.setLayout(null);

        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7, 94, 84));
        p1.setBounds(0, 0, 450, 70);
        p1.setLayout(null);
        f.add(p1);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2 = i1.getImage().getScaledInstance(25, 25, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel back = new JLabel(i3);
        back.setBounds(5, 20, 25, 25);
        p1.add(back);

        back.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ae) {
                System.exit(0);
            }
        });

        ImageIcon i4 = new ImageIcon(ClassLoader.getSystemResource("icons/mirzapur.png"));
        Image i5 = i4.getImage().getScaledInstance(60,60, Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel profile = new JLabel(i6);
        profile.setBounds(40, 5, 60, 60);
        p1.add(profile);

        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image i8 = i7.getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        ImageIcon i9 = new ImageIcon(i8);
        JLabel video = new JLabel(i9);
        video.setBounds(300, 20, 30, 30);
        p1.add(video);

        ImageIcon i10 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image i11 = i10.getImage().getScaledInstance(35, 30, Image.SCALE_DEFAULT);
        ImageIcon i12 = new ImageIcon(i11);
        JLabel phone = new JLabel(i12);
        phone.setBounds(360, 20, 35, 30);
        p1.add(phone);

        ImageIcon i13 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image i14 = i13.getImage().getScaledInstance(10, 25, Image.SCALE_DEFAULT);
        ImageIcon i15 = new ImageIcon(i14);
        JLabel morevert = new JLabel(i15);
        morevert.setBounds(420, 20, 10, 25);
        p1.add(morevert);

        JLabel name = new JLabel("Mirzapur");
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
        p1.add(name);

        JLabel status = new JLabel("Guddu, Kaleen, Bablu, Sweety, IG Dubey, Shukla");
        status.setBounds(110, 35, 160, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SERIF", Font.BOLD, 14));
        p1.add(status);

        a1 = new JPanel();
        a1.setBounds(5, 75, 440, 570);
        a1.setBackground(Color.WHITE);
        f.add(a1);

        text = new JTextField();
        text.setBounds(5, 655, 310, 40);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(text);

        JButton send = new JButton("Send");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(0, 0, 255));
        send.setForeground(Color.BLUE);
        send.addActionListener(this);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
        f.add(send);

        f.setSize(450, 700);
        f.setLocation(490, 50);
        f.setUndecorated(true);
        f.getContentPane().setBackground(Color.WHITE);

        f.setVisible(true);
    }

        /*Socket socket = null;*/
    private void establishConnection() {
        try {
            socket = new Socket("localhost", 2003);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Attempt to read the clientId sent by the server
            this.clientId = reader.readLine(); // Ensure server sends this immediately after connection
            if (this.clientId == null) {
                throw new IOException("Server did not send a client ID.");
            }

            // Connection established successfully
            JOptionPane.showMessageDialog(f, "Connected to the server as " + clientId, "Connection Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(f, "Failed to connect to server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            closeResources();
            e.printStackTrace();
            // Clean up resources
            if (this.writer != null) {
                try { this.writer.close(); } catch (IOException ex) { ex.printStackTrace(); }
            }
            if (this.reader != null) {
                try { this.reader.close(); } catch (IOException ex) { ex.printStackTrace(); }
            }
            if (socket != null) {
                try { socket.close(); } catch (IOException ex) { ex.printStackTrace(); }
            }
        }
    }

    private void closeResources() {
        try {
            if (writer != null) writer.close();
            if (reader != null) reader.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class MessageUtils {
        private static int messageIdCounter = 1;

        public static synchronized String generateMessageId() {
            return "msg-" + messageIdCounter++;
        }
    }

    public void actionPerformed(ActionEvent ae) {
        if (this.writer == null) {
            JOptionPane.showMessageDialog(f, "Not connected to server", "Error", JOptionPane.ERROR_MESSAGE);
            return;  // Skip further processing
        }
        try {

            // Extract the message directly, apply HTML only for local display if needed
            String messageContent = text.getText().trim();
            String messageId = UserTwo.MessageUtils.generateMessageId(); // Generate unique IDs
            String formattedMessage = messageId + "|" + messageContent; // Sending format
            /*String out = "<html><p>" + name + "</p><p>" + text.getText() + "</p></html>";*/

            // Create the display panel for the message, applying HTML formatting here if necessary for display
            String displayContent = "<html><p>" + name + "</p><p>" + messageContent + "</p></html>";
            JPanel p2 = formatLabel(displayContent);

            a1.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.setBackground(Color.WHITE);
            right.add(p2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            a1.add(vertical, BorderLayout.PAGE_START);

            // Send the unformatted message with the ID
            try {
                this.writer.write(formattedMessage);
                this.writer.newLine();
                this.writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }

            text.setText(""); // Clear the text field after sending

            f.repaint();
            f.invalidate();
            f.validate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(f, "Error sending message: " + e.getMessage(), "Network Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(new Color(37, 211, 102)); // Example color
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(0, 15, 0, 50));

        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));

        panel.add(time);

        return panel;
    }

    public void run() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                System.out.println("Received raw message: " + msg);  // Improved logging for debugging

                // Handle potential system or control messages
                if (msg.startsWith("ClientID:")) {
                    this.clientId = msg.split(":")[1].trim();
                    continue;  // Skip further processing for this loop iteration
                }

                // Process regular chat messages
                String[] parts = msg.split("\\|", 2);
                if (parts.length == 2) {
                    String senderId = parts[0].trim();
                    String messageContent = parts[1].trim();

                    if (!senderId.equals(this.clientId)) {  // Check sender ID to ensure message is not from self
                        SwingUtilities.invokeLater(() -> {
                            JPanel panel = formatLabel(messageContent);
                            JPanel left = new JPanel(new BorderLayout());
                            left.setBackground(Color.WHITE);
                            left.add(panel, BorderLayout.LINE_START);
                            vertical.add(left);
                            a1.add(vertical, BorderLayout.PAGE_START);

                            a1.revalidate();
                            a1.repaint();

                            JOptionPane.showMessageDialog(f, "New message received");
                            sendReadReceipt(senderId);  // Sending only the sender ID for the read receipt
                        });
                    }
                } else {
                    System.err.println("Incorrect message format received: " + msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(f, "Error: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendReadReceipt(String senderId) {
        try {
            writer.write("Read:" + senderId + "," + this.clientId);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String extractMessageId(String message) {
        // Split the message into two parts: the ID and the content
        String[] parts = message.split("\\|", 2); // Use regex escape for '|' and limit split to 2 parts
        if (parts.length > 1) {
            return parts[0].trim(); // The first part is the ID
        } else {
            return null; // Return null if the message is not correctly formatted
        }
    }

    public static void main(String[] args) {
        UserTwo two = new UserTwo();
        Thread t2 = new Thread(two);
        t2.start();
    }
}
