package dk.kea.stud.chris;

import java.util.Queue;

public class MailSender implements Runnable {
  private final Queue<Mail> mailQueue;

  public MailSender(Queue<Mail> mailQueue) {
    this.mailQueue = mailQueue;
  }

  @Override
  public void run() {
    System.out.println("MailSender started.");
    while (true) {
      synchronized (mailQueue) {
        if (!mailQueue.isEmpty()) {
          new Thread(new MailSenderWorker(mailQueue.remove())).start();
        }
      }
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        System.out.println("MailSender was interrupted.");
      }
    }
  }
}
