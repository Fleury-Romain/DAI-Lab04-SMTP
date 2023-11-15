package ch.heig.dai.lab.SMTP;

import java.util.List;

public class MailContent {
    private int id;
    private List<String> subjects;
    private List<String> contents;

    public MailContent(String subject, String content){
        this.subjects = List.of(subject);
        this.contents = List.of(content);
    }
    public MailContent(List<String> subjects, List<String> contents){
        // FIXME vérifier le tableau de string transmis !
        this.subjects = subjects;
        this.contents = contents;
    }
    public MailContent(MailContentFileReader mcfr, int id){
        this.subjects = mcfr.getSubjects();
        this.contents = mcfr.getContents();
        if(id <= subjects.size()){
            this.id = id;
        }else{
            this.id = 1;
        }
    }

    public int getNbr(){
        return subjects.size();
    }
    public int getId(){
        return id;
    }
    public String getSubject(int id){
        return subjects.get(id);
    }
    public String getContent(int id){
        return contents.get(id);
    }
}
