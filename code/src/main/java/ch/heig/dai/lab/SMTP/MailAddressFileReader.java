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
        lire le fichier ligne par ligne,
        détecter # comme le début d'un nouveau groupe et ce qui suit le signe # comme le nom du groupe,
        sender: et ce qui suit comme l'adresse e-mail de l'expéditeur,
        et chaque ligne commençant par receiver: et ce qui suit comme l'adresse e-mail d'un destinataire (un destinataire par line).
        Se termine quand une ligne ne contenant que \n est trouvée
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
