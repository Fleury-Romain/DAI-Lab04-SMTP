package ch.heig.dai.lab.SMTP;

import java.io.BufferedReader;
import java.util.List;

public class MailAddress {
    /*
    Cette classe récupère des groupes de 3-4 addreses e-mails et définit le premier comme l'envoyeur
    les adresses suivantes comme les cibles
     */
    int groupeSize;
    private List<String> from;
    private List<List<String>> to;

    public MailAddress(MailAddressFileReader mafr){
        from = mafr.getFrom();
        to = mafr.getTo();
    }

    public int getNbrGroupe(){
        return from.size();
    }
    public String getFrom(int id){
        return from.get(id);
    }

    public List<String> getTo(int id){
        return to.get(id);
    }
}
