package dk.kea.stud.chris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;

public class ClientHandler implements Runnable {
  private final Queue<Mail> mailQueue;
  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;

  public ClientHandler(Socket socket, Queue<Mail> mailQueue) {
    this.socket = socket;
    this.mailQueue = mailQueue;
    try {
      this.out = new PrintWriter(socket.getOutputStream(), true);
      this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    } catch (IOException e) {
      System.out.println("IO Error on client handler creation");
    }
  }

  private void close() {
    try {
      out.close();
      in.close();
      socket.close();
    } catch (IOException e) {
      System.out.println("IOException on terminating ClientHandler.");
    }
  }

  @Override
  public void run() {
    String destination;
    String subject;
    String input;
    StringBuilder content = new StringBuilder();

    try {
      destination = in.readLine();
      subject = in.readLine();
      while(true) {
        input = in.readLine();
        if (input.equals(".")) {
          break;
        }
        content.append(input);
        content.append(System.lineSeparator());
      }
      synchronized (mailQueue) {
        mailQueue.add(new Mail(destination, subject, content.toString()));
      }
    } catch (IOException e) {
      System.out.println("Error receiving message");
    } finally {
      close();
    }
  }
}
