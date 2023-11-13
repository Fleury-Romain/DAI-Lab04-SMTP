package ch.heig.dai.lab.SMTP;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MailAddressFileReader implements AutoCloseable{
    private final BufferedReader file;
    private List<String> from;
    private List<List<String>> to;
    public MailAddressFileReader(String filePath) throws IOException {
        file = new BufferedReader(new FileReader(filePath));
        this.setAddress();
    }

    private void setAddress() throws IOException {
        /*
         read line per line file, detect # as the beginning of a new group and what follows the # sign as the name of the group, sender: and what
         follows as the e-mail address of the sender, and the each line starting with receiver: and what
         follows as the e-mail address of the receiver (one receiver per line).
         finish when a line with only \n is found
         */
        List<String> from = new ArrayList<>();
        List<List<String>> to = new ArrayList<>();

        String line;
        int groupe = -1;
        while((line = file.readLine()) != null){
            if(line.equals("\n")) continue; // si tu break tu arrètes de lire.
            if(line.contains("# Groupe")){
                // # = commentaire
                // L'ordre des groupes est donné par
                // l'ordre des elements de la liste

                to.add(new ArrayList<>());
                groupe++;
            }
            if(line.contains("sender:")){
                from.add(line.split("sender:")[1]);
            }
            if(line.contains("receiver:")){
                to.get(groupe).add(line.split("receiver:")[1]);
            }
        }

        this.from = from;
        this.to = to;
    }

    protected List<String> getFrom(){
        return from;
    }
    protected List<List<String>> getTo(){
        return to;
    }

    @Override
    public void close() throws Exception {
        file.close();
    }

}
