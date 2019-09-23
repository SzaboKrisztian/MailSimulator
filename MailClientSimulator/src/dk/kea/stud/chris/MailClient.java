package dk.kea.stud.chris;

import java.io.*;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MailClient {
  private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm:ss");
  private Socket clientSocket;
  private PrintWriter out;
  private BufferedReader in;

  public boolean connect(String address, int port) {
    try {
      clientSocket = new Socket(address, port);
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  public boolean disconnect() {
    try {
      in.close();
      out.close();
      clientSocket.close();
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  public void sendMessage(String message) {
    if (clientSocket == null || out == null || in == null) {
      System.out.println("Client not connected.");
    } else {
      out.println(message);
    }
  }

  public static void main(String[] args) {
    Scanner scn = new Scanner(System.in);
    System.out.print("Enter address [def 127.0.0.1]: ");

    String address = scn.nextLine();
    if (address.equals("")) {
      address = "127.0.0.1";
    }

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

    List<String> addresses = readFromFile("addresses.txt");
    List<String> subjects = readFromFile("subjects.txt");
    List<String> content = readFromFile("content.txt");
    System.out.println("Loaded " + addresses.size() + " addresses.");
    System.out.println("Loaded " + subjects.size() + " subject lines.");
    System.out.println("Loaded " + content.size() + " lines of content.");

    while (true) {
      MailClient client = new MailClient();
      if (client.connect(address, port)) {
        client.sendMessage(getRandomListItem(addresses));
        client.sendMessage(getRandomListItem(subjects));
        client.sendMessage(getRandomListItem(content));
        client.sendMessage(".");
        client.disconnect();
        System.out.println("[" + LocalTime.now().format(format) + "] Mail successfully sent.");
      } else {
        System.out.println("Error connecting.");
      }
      try {
        Thread.sleep(new Random().nextInt(10000));
      } catch (InterruptedException e) {
        System.out.println("Thread, interrupted");
      }
    }
  }

  private static List<String> readFromFile(String file) {
    List<String> result = new ArrayList<>();
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(file));

      String line;
      try {
        while ((line = reader.readLine()) != null) {
          result.add(line);
        }
      } catch (IOException e) {
        System.out.println("Cannot read from file.");
        return null;
      }
    } catch (FileNotFoundException e) {
      System.out.println(file + " file not found.");
      return null;
    }
    return result;
  }

  private static String getRandomListItem(List<String> list) {
    Random randomGenerator = new Random();
    int index = randomGenerator.nextInt(list.size());
    return list.get(index);
  }
}