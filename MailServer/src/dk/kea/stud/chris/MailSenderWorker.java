package dk.kea.stud.chris;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MailSenderWorker implements Runnable {
  private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("[HH:mm:ss]");
  private Mail mail;

  public MailSenderWorker(Mail mail) {
    this.mail = mail;
  }

  @Override
  public void run() {
    System.out.println("[" + LocalTime.now().format(format) + "] Received following mail: ");
    System.out.println(mail.getDestination());
    System.out.println(mail.getSubject());
    System.out.println(mail.getContent());
  }
}
