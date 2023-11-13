package ch.heig.dai.lab.SMTP;

import javax.management.Query;
import javax.management.relation.RoleInfoNotFoundException;
import java.io.IOException;
import java.security.spec.ECField;
import java.util.List;
import java.util.Scanner;

public class CmdHandler {
    private String[] args;
    private String ip;
    private int port;
    private MailAddress mailAddress;
    private MailContent mailContent;
    private int groupe;
    private int mail;

    private enum Data{IP, PORT, MAILADDRESS, MAILCONTENT, GROUPE, MAIL};

    public CmdHandler(String[] args){
        this.args = args;
        argsParser();
        consoleTotale();
    }

    /**
     * Gère l'ensemble du programme en fonction des arguments présents.
     * @return int Status code
     */
    public void run()  {
        if(args.length == 0){ // pas d'argument, entrée dans l'application en ligne de commande
            consoleTotale();
        }else { // avec certain argument, parse + envoyé direct par défaut
            // Récupération des arguments
            argsParser();
        }

        // on vérifie que tous les paramètres sont valides avant de
        // lancer la connection smtp
        if(argsChecker()) {
            // Lancement de la connection SMTP
            smtpConnect();
        }

    }

    private int consoleTotale(){

        // process les commandes
        Scanner sc = new Scanner(System.in);
        String line = null;
        do { // FIXME effacer la console entre chaque affichage de l'app (cls/clear)
            // Affichage ddu menu principal de l'application
            displayApp();

            System.out.print("Cmd > ");
            line = sc.nextLine();
            appCommand(line);
        }while(!line.contains("exit"));

        return 1;
    }

    private void displayApp(){
        displayHeader();
        displayArgs();
    }

    private void appCommand(String cmd){
        // FIXME vérifier la validité de la commande passé (nbr params, etc ...)
        String[] cmdargs = cmd.split(" ");

        if(cmdargs[0].equals("set")){ // Commande d'initialisation
            if(cmdargs[1].equals("ip")){
                setIP(cmdargs[2]);
            }
            // TODO Ajouter toutes les autres comandes de paramètres

        } else if (cmdargs[0].equals("get")) {
            if(mailAddress == null || mailContent == null){return;}

            // FIXME vérifier que l'argument est bien dans la plage du nombre d'élements (groupe/mail)
            if(cmdargs[1].equals("groupe")){
                if(cmdargs.length > 2){
                    System.out.println("From : " + mailAddress.getFrom(Integer.parseInt(cmdargs[2])));
                    for(String s: mailAddress.getTo(Integer.parseInt(cmdargs[2]))){
                        System.out.println("To : " + s);
                    }
                }else{
                    for(int i = 0; i < mailAddress.getNbrGroupe(); i++){
                        System.out.println("From : " + mailAddress.getFrom(i));
                        for(String s: mailAddress.getTo(i)){
                            System.out.println("To : " + s);
                        }
                    }
                }
            } else if (cmdargs[1].equals("mail")) {
                if(cmdargs.length > 2){
                    System.out.println("Mail #" + cmdargs[2]);
                    System.out.println("Subject : " + mailContent.getSubject(Integer.parseInt(cmdargs[2])));
                    System.out.println("Content : " + mailContent.getContent(Integer.parseInt(cmdargs[2])));
                }else{
                    for(int i = 0; i < mailContent.getNbr(); i++){
                        System.out.println("Mail #" + i);
                        System.out.println("Subject : " + mailContent.getSubject(i));
                        System.out.println("Content : " + mailContent.getContent(i));
                    }
                }
            }
        } else if (cmdargs[0].equals("send")) {
            if(argsChecker()) {
                smtpConnect();
            }
        }
    }

    private void setIP(String ip){
        this.ip = ip;
    }
    private void setPort(int port){
        if(port > 0 && port < 65536) {
            this.port = port;
        }else{
            System.out.println("Port invalide !");
        }
    }
    private void setMailAddress(String filePath){
        try {
            mailAddress = new MailAddress(new MailAddressFileReader(filePath));
        }catch (Exception e){
            throw new RuntimeException("Impossible de créer l'objet mailAddress : " + e);
        }
    }
    private void setMailContent(String filePath){
        try{
            mailContent = new MailContent(new MailContentFileReader(filePath));
        }catch(Exception e){
            throw new RuntimeException("Impossible de créer l'objet mailContent : " + e);
        }
    }
    private void setGroupe(int groupe){
        if(mailAddress == null){
            System.out.println("Veuillez d'abord renseigner un fichier d'adresse !");
        }else{
            if(groupe > 0 && groupe < mailAddress.getNbrGroupe()){
                this.groupe = groupe;
            }else{
                System.out.println("La valeur spécifié ne correspond pas à un groupe valide");
            }
        }
    }
    private void setMail(int mail){
        if(mailContent == null){
            System.out.println("Veuillez d'abord renseigner un fichier de mail !");
        }else{
            if(mail > 0 && mail < mailContent.getNbr()){
                this.mail = mail;
            }else{
                System.out.println("La valeur spécifié ne correspond pas à un mail valide");
            }
        }
    }
    private void displayHeader(){
        System.out.print(
            "   _____   __  __   _______   _____    _____               _   _   _  __\n" +
            "  / ____| |  \\/  | |__   __| |  __ \\  |  __ \\      /\\     | \\ | | | |/ /\n" +
            " | (___   | \\  / |    | |    | |__) | | |__) |    /  \\    |  \\| | | ' / \n" +
            "  \\___ \\  | |\\/| |    | |    |  ___/  |  _  /    / /\\ \\   | . ` | | <  \n" +
            "  ____) | | |  | |    | |    | |      | | \\ \\   / ____ \\  | |\\  | | . \\\n" +
            " |_____/  |_|  |_|    |_|    |_|      |_|  \\_\\ /_/    \\_\\ |_| \\_| |_|\\_\n\n"
        );
    }

    private void displayArgs(){
        for(Data d : Data.values()){
            System.out.print(String.format("%-25s : %s\n", d, getArgsStatus(d)));
        }
        System.out.println();
    }

    private String getArgsStatus(Data d){
        switch (d){
            case IP:
                if(ip != null){ return String.format("%-7s : %s" ,"OK", ip); }
                break;
            case PORT:
                if(port != 0){ return String.format("%-7s : %d" ,"OK", port); }
                break;
            case MAILADDRESS:
                if(mailAddress != null){ return String.format("%-7s : %s" ,"OK", "#groupe(" + mailAddress.getNbrGroupe() + ")"); }
                break;
            case MAILCONTENT:
                if(mailContent != null){ return String.format("%-7s : %s" ,"OK", "#mail(" + mailContent.getNbr() + ")"); }
                break;
            case GROUPE:
                if(groupe != 0){ return String.format("%-7s : %d" ,"OK", groupe); }
                else if (groupe == -1) { return String.format("%-7s : %s" ,"OK", "Aléatoire"); }
                break;
            case MAIL:
                if(mail != 0){ return String.format("%-7s : %d" ,"OK", mail); }
                else if (mail == -1) { return  String.format("%-7s : %s" ,"OK", "Aléatoire"); }
                break;
        }

        return "missing";
    }

    private void argsParser(){
        for(int i = 0; i < args.length; i++){
            switch (args[i]){
                case "-i":
                case "--ip":
                    ip = args[i+1];
                    break;
                case "-p":
                case "--port":
                    port = Integer.parseInt(args[i+1]);
                    break;
                case "-ma":
                case "--mailaddress":
                    try {
                        mailAddress = new MailAddress(new MailAddressFileReader(args[i + 1]));
                    }catch (Exception e){
                        throw new RuntimeException("Impossible de créer l'objet mailAddress : " + e);
                    }
                case "-mc":
                case "--mailcontent":
                    try{
                        mailContent = new MailContent(new MailContentFileReader(args[i+1]));
                    }catch (Exception e){
                        throw new RuntimeException("Impossible de créer l'objet mailContent");
                    }
                    break;
                case "-g":
                case "--groupe":
                    groupe = Integer.parseInt(args[i+1]);
                    break;
                case "-m" :
                case "--mail":
                    mail = Integer.parseInt(args[i+1]);
                    break;

            }
        }
    }

    private Boolean argsChecker(){
        Scanner sc = new Scanner(System.in);
        if(ip == null){
            // Localhost par défaut
            ip = "locahost";
        }
        if(port == 0){
            // 1025 (maildev) par défaut
            port = 1025;
        }
        if(mailAddress == null){
            try {
                mailAddress = new MailAddress(new MailAddressFileReader(getData(sc, Data.MAILADDRESS)));
            }catch (Exception e){
                throw new RuntimeException("Impossible de créer l'objte mailAddress : " + e);
            }
        }
        if(mailContent == null){
            try{
                mailContent = new MailContent(new MailContentFileReader(getData(sc, Data.MAILCONTENT)));
            }catch(Exception e){
                throw new RuntimeException("Impossible de créer l'objet malContent : " + e);
            }
        }
        if(groupe == 0){
            // Aléatoire par défaut
            groupe = -1;
        }
        if(mail == 0){
            // Aléatoire par défaut
            mail = -1;
        }

        return Boolean.TRUE;
    }
    private String getData(Scanner sc, Data d){
        String data = null;
        switch (d) {
            case IP:
                System.out.print("Input a valid SMTP port : ");
                data = sc.nextLine();
                break;
            case PORT:
                do {
                    System.out.print("Input a valid SMTP port : ");
                    data = sc.nextLine();
                } while (Integer.parseInt(data) < 0 || Integer.parseInt(data) > 65536);
                break;
            case MAILADDRESS:
                do {
                    System.out.print("Input a valid mailaddress file : ");
                    data = sc.nextLine();
                } while (!data.contains(".txt"));
                break;
            case MAILCONTENT:
                do {
                    System.out.print("Input a valid mailcontent file  : ");
                    data = sc.nextLine();
                } while (!data.contains(".txt"));
                break;
            case GROUPE:
                do {
                    System.out.print("Input a valid group (max :" + mailAddress.getNbrGroupe() + ") : ");
                    data = sc.nextLine();
                } while (Integer.parseInt(data) < 0 || Integer.parseInt(data) > mailAddress.getNbrGroupe());
                break;
            case MAIL:
                do {
                    System.out.print("Input a valid mail number (max :" + mailContent.getNbr() + ") : ");
                    data = sc.nextLine();
                } while (Integer.parseInt(data) < 0 || Integer.parseInt(data) > mailContent.getNbr());
                break;

        }
        return data;
    }

    private int smtpConnect(){
        ConnectionHandler ch = new ConnectionHandler(ip, port, mailAddress, mailContent);
        ch.run();

        return 1;
    }
}
