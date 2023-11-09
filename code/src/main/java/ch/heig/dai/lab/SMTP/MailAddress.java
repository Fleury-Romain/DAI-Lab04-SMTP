package ch.heig.dai.lab.SMTP;

import java.io.BufferedReader;

public class MailAddress {
    /*
    Cette classe récupère des groupes de 3-4 addreses e-mails et définit le premier comme l'envoyeur
    les adresses suivantes comme les cibles
     */
    int groupeSize;
    String from;
    String[] to;

    public MailAddress(int groupSize, String from, String[] to) {
        this.groupeSize = groupSize;
        this.from = from;
        this.to = to;
    }

    public String getFrom(){
        return from;
    }

    public String getTo(int id){
        return to[id];
    }
}
