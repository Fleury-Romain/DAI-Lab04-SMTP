package ch.heig.dai.lab.SMTP;

import java.util.List;

public class MailAddress {
    /*
    Cette classe récupère des groupes de 3-4 addreses e-mails et définit le premier comme l'envoyeur
    les adresses suivantes comme les cibles
     */
    private final List<String> from;
    private final List<List<String>> to;

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
