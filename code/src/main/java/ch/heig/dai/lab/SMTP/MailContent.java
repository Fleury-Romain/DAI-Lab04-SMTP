package ch.heig.dai.lab.SMTP;

import java.util.List;

public class MailContent {
    private final List<String> subjects;
    private final List<String> contents;

    public MailContent(MailContentFileReader mcfr){
        this.subjects = mcfr.getSubjects();
        this.contents = mcfr.getContents();
    }

    public int getNbr(){
        return subjects.size();
    }
    public String getSubject(int id){
        return subjects.get(id);
    }
    public String getContent(int id){
        return contents.get(id);
    }
}
