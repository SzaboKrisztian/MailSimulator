package dk.kea.stud.chris;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class MailServer {
  private final Queue<Mail> mailQueue = new LinkedList<>();
  private ServerSocket serverSocket;

  public MailServer(int port) throws IOException {
    serverSocket = new ServerSocket(port);
  }

  public Socket getConnection() {
    try {
      return serverSocket.accept();
    } catch (IOException e) {
      return null;
    }
  }

  public Queue<Mail> getMailQueue() {
    return this.mailQueue;
  }

  public static void main(String[] args) {
    Scanner scn = new Scanner(System.in);
    System.out.print("Enter port [def 12345]: ");
    int port;
    String input = scn.nextLine();
    if (input.equals("")) {
      port = 12345;
    } else {
      try {
        port = Integer.parseInt(scn.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("Invalid input; using port 12345");
        port = 12345;
      }
    }

    MailServer mailServer = null;
    try {
      mailServer = new MailServer(port);
    } catch (IOException e) {
      System.out.println("Failed binding to port " + port + ". Aborting.");
      System.exit(1);
    }

    new Thread(new MailSender(mailServer.getMailQueue())).start();

    Socket newConnection;
    System.out.println("Waiting for connections.");
    while (true) {
      newConnection = mailServer.getConnection();
      if (newConnection != null) {
        ClientHandler client = new ClientHandler(newConnection, mailServer.getMailQueue());
        new Thread(client).start();
      }
    }
  }
}