package dk.kea.stud.chris;

import java.util.Queue;
import java.util.concurrent.ThreadPoolExecutor;

public class MailSender implements Runnable {
  private final ThreadPoolExecutor threadPool;
  private final Queue<Mail> mailQueue;

  public MailSender(Queue<Mail> mailQueue, ThreadPoolExecutor threadPool) {
    this.threadPool = threadPool;
    this.mailQueue = mailQueue;
  }

  @Override
  public void run() {
    System.out.println("MailSender started.");
    while (true) {
      synchronized (mailQueue) {
        if (!mailQueue.isEmpty()) {
          threadPool.execute(new MailSenderWorker(mailQueue.remove()));
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
