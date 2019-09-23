package dk.kea.stud.chris;

public class Mail {
  private String destination;
  private String subject;
  private String content;

  public Mail() {}

  public Mail(String destination, String subject, String content) {
    this.destination = destination;
    this.subject = subject;
    this.content = content;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
