package ch.heig.dai.lab.SMTP;

public class MailContent {
    private String subject;
    private String content;

    public MailContent(String subject, String content){
        this.subject = subject;
        this.content = content;
    }

    public MailContent(String[] mailContent){
        // FIXME vérifier le tableau de string transmis !
        this.subject = mailContent[0];
        this.content = mailContent[1];
    }

    public String getSubject(){
        return subject;
    }

    public String getContent(){
        return content;
    }
}
