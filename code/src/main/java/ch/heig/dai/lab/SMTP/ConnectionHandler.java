package ch.heig.dai.lab.SMTP;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Base64;

public class ConnectionHandler {
    private final String ip;
    private final int port;
    private final MailAddress mailAddress;
    private final MailContent mailContent;
    private final int addressID;
    private final int nbrTo;
    private int contentID;
    private final int isRandom;
    Random rand = new Random();

    public ConnectionHandler(String ip, int port, MailAddress mailAddress, MailContent mailContent, int addressID, int nbrTo, int contentID){
        this.ip = ip;
        this.port = port;
        this.mailAddress = mailAddress;
        this.mailContent = mailContent;

        if(addressID > -1) {
            this.addressID = addressID - 1;
        }else{
            this.addressID = rand.nextInt(mailAddress.getNbrGroupe());
        }
        this.nbrTo = nbrTo;
        if(contentID > -1){
            this.contentID = contentID - 1;
            isRandom = 0;
        }else{
            isRandom = 1;
        }

    }

    public void run(){
        if(isRandom == 1){ // set random
            contentID = rand.nextInt(mailContent.getNbr());
        }

        try(
            Socket s = new Socket(ip, port);
            var in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
            var out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8))
        ) {
            String line;
            int flag = 0;

            while (!(s.isClosed())) {
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                    if(!line.contains("-")){
                        flag++;
                        break;
                    }
                }
                // Warning, mais a cette étape nous sommes sûrs
                // qu'il y a encore une ligne (!=null)
                if(line.contains("221")){ break; }
                SMTPhandler(out, flag);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void send(BufferedWriter out, String msg) throws IOException {
        if(!msg.contains("\n")){msg += "\n";}
        out.write(msg);
        out.flush();
    }

    private void SMTPhandler(BufferedWriter out, int flag) throws IOException {
        switch (flag) {
            case 1:
                send(out, "EHLO rfz.root.sx\n");
                break;
            case 2:
                send(out, "MAIL FROM:<" + mailAddress.getFrom(addressID) + ">\n");
                break;
            case 3:
                for(int i = 0; i < nbrTo; i++) { // ajouter de nouveaux destinataires
                    send(out, "RCPT TO: <" + mailAddress.getTo(addressID).get(i) + ">\n");
                }
                break;
            case 4:
                send(out, "DATA\n");
                break;
            case 5:
                send(out, "SUBJECT: " + "=?UTF-8?B?"+ Base64.getEncoder().encodeToString(mailContent.getSubject(contentID).getBytes()) + "?=" + "\n");
                send(out, "FROM: <" + mailAddress.getFrom(addressID) + ">\n");
                for(int i = 0; i < nbrTo; i++) {
                    send(out, "TO: <" + mailAddress.getTo(addressID).get(i) + ">\n"); // ajouter de nouveaux destinataire
                }
                send(out, "DATE: April 1st, 2021\n");
                send(out, "Content-Type: text/plain; charset=utf-8\n\n");
                send(out, mailContent.getContent(contentID) + "\n");
                send(out, "\r\n.\r\n");
                break;
            default:
                send(out, "QUIT\n");
                break;
        }
    }
}