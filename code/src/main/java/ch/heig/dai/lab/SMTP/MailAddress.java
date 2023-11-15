package ch.heig.dai.lab.SMTP;

import java.io.BufferedReader;
import java.util.List;

public class MailAddress {
    /*
    Cette classe récupère des groupes de 3-4 addreses e-mails et définit le premier comme l'envoyeur
    les adresses suivantes comme les cibles
     */
    int groupeSize;
    int groupeID;
    private List<String> from;
    private List<List<String>> to;

    public MailAddress(MailAddressFileReader mafr, int groupeID, int groupeSize){
        from = mafr.getFrom();
        to = mafr.getTo();

        if(groupeID <= from.size()){
            this.groupeID = groupeID;
        }else{
            this.groupeID = 1;
        }

        if(groupeSize <= from.size()) {
            this.groupeSize = groupeSize;
        }else {
            this.groupeSize = this.getTo(groupeID).size();
        }
    }

    public int getNbrGroupe(){
        return from.size();
    }
    public String getFrom(int id){
        return from.get(id);
    }
    public int getGroupeSize(){
        return groupeSize;
    }

    public int getGroupeID(){
        return groupeID;
    }

    public List<String> getTo(int id){
        return to.get(id);
    }
}
