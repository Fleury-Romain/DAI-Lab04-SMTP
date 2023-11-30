package ch.heig.dai.lab.SMTP;

import java.util.Arrays;
import java.util.Scanner;

public class CmdHandler {
    private final String[] args;
    private String ip;
    private int port;
    private MailAddress mailAddress = null;
    private MailContent mailContent = null;
    private int groupe;
    private int groupeSize;
    private int mail;
    private int nbmail;

    private enum Data{IP, PORT, MAILADDRESS, MAILCONTENT, GROUPE, MAIL, GROUPESIZE, NBMAIL}

    public CmdHandler(String[] args){
        this.args = args;
    }

    public void run()  {
        if(args.length == 0){ // pas d'argument, entrée dans l'application en ligne de commande
            consoleTotale();
        }else { // avec certain argument, parse + envoyé direct par défaut
            // Récupération des arguments
            argsParser();
            // on vérifie que tous les paramètres sont valides avant de
            // lancer la connection smtp
            if(argsChecker()) {
                // Lancement de la connection SMTP
                smtpConnect();
            }
        }
    }

    private void consoleTotale(){
        // process les commandes
        Scanner sc = new Scanner(System.in);
        String line = null;
        do {
            // Affichage ddu menu principal de l'application
            clearScreen();
            displayHeader();
            appCommand(line);System.out.println();
            displayArgs();

            System.out.print("Cmd > ");
            line = sc.nextLine();
        }while(!line.contains("exit"));
    }

    /**
     * Vérifie que les arguments de la commande sont valides
     * @param args arguments de la commande
     * @return true si les arguments sont valides, false sinon
     */
    private boolean cmdArgsHandler(String[] args){
        switch(args[0]) {
            case "set" :
                String[] setArgs = {"ip", "port", "mailaddress", "mailcontent", "groupe", "mail", "size", "nbmail"};
                if (!Arrays.asList(setArgs).contains(args[1])) {
                    System.out.println("Commande invalide !");
                    return false;
                }
                if (args.length != 3) {
                    System.out.println("Nombre d'arguments incorrect !");
                    return false;
                }
                switch (args[1]) {
                    case "port" : if(Integer.parseInt(args[2]) <= 0) {
                        System.out.println("Le numéro port doit être un entier positif !");
                        return false;
                    }
                    case "groupe" : if ((mailAddress == null) || (Integer.parseInt(args[2]) < 1 || Integer.parseInt(args[2]) > mailAddress.getNbrGroupe())) {
                        System.out.println("Numéro du groupe invalide ou commande set mailaddress pas encore exécutéee !");
                        return false;
                    }
                    case "mail" : if ((mailContent == null) || (Integer.parseInt(args[2]) < 1 || Integer.parseInt(args[2]) > mailContent.getNbr())) {
                        System.out.println("Numéro du mail invalide ou commande set mailcontent pas encore exécutéee !");
                        return false;
                    }
                    case "size" : if ((mailContent == null) || (Integer.parseInt(args[2]) < 1 || Integer.parseInt(args[2]) > mailContent.getNbr())) {
                        System.out.println("Numéro du mail invalide ou commande set mailcontent pas encore exécutéee !");
                        return false;
                    }
                    case "nbmail" : if (mailAddress == null || mailContent == null || Integer.parseInt(args[2]) < 1) {
                        System.out.println("Le nombre de mails doit être un entier positif ou les commandes set mailaddress ou set groupe n'ont pas encore été exécutées !");
                        return false;
                    }
                }
                /*
                if (args[1].equals("port") && Integer.parseInt(args[2]) <= 0) {
                    System.out.println("Le numéro port doit être un entier positif !");
                    return false;
                }
                if (args[1].equals("groupe")) {
                    if ((mailAddress == null) || (Integer.parseInt(args[2]) < 1 || Integer.parseInt(args[2]) > mailAddress.getNbrGroupe())) {
                        System.out.println("Numéro du groupe invalide ou commande set mailaddress pas encore exécutéee !");
                        return false;
                    }
                }
                if (args[1].equals("mail")) {
                    if ((mailContent == null) || (Integer.parseInt(args[2]) < 1 || Integer.parseInt(args[2]) > mailContent.getNbr())) {
                        System.out.println("Numéro du mail invalide ou commande set mailcontent pas encore exécutéee !");
                        return false;
                    }
                }
                if (args[1].equals("size")) {
                    if ((mailAddress == null || groupe == 0) || Integer.parseInt(args[2]) < 1 || Integer.parseInt(args[2]) > mailAddress.getTo(groupe-1).size()) {
                        System.out.println("Taille du groupe invalide ou commandes set mailaddress ou set groupe pas encore exécutées !");
                        return false;
                    }
                }
                */
            case "get" :
                if (args.length < 2 || args.length > 3) {
                    System.out.println("Nombre d'arguments incorrect !");
                    return false;
                }
                switch (args[1]) {
                    case "groupe" :
                        if (mailAddress == null) {
                            System.out.println("Champ 'groupe' non rempli !");
                            return false;
                        }
                        if (args.length == 3) {
                            try {
                                Integer.parseInt(args[2]);
                            } catch (NumberFormatException e) {
                                System.out.println("L'argument du groupe doit être un entier positif !");
                                return false;
                            }
                            if (Integer.parseInt(args[2]) >= mailAddress.getNbrGroupe()) {
                                System.out.println("Numéro du groupe invalide !");
                            }
                            return false;
                        }
                        break;
                    case "mail" :
                        if (mailContent == null) {
                            System.out.println("Champs 'mail' non rempli !");
                            return false;
                        }
                        if (args.length == 3 && Integer.parseInt(args[2]) >= mailContent.getNbr()) {
                            System.out.println("Numéro de mail invalide !");
                            return false;
                        }
                        break;
                    default :
                        System.out.println("Commande invalide !");
                        return false;
                }
                break;
            case "send" :
                if (args.length > 1) {
                    System.out.println("La commande 'send' ne requiert aucun argument !");
                    return false;
                }
                break;
            default :
                System.out.println("Commande non supportée !");
                return false;
        }
        return true;
    }

    private void appCommand(String cmd){
        if(cmd == null){
            return;
        }
        String[] cmdargs = cmd.split(" ");

        // Parsing du filepath dans les arguments
        if (cmdargs[2].contains("\"")) {
            for (int i = 3; i < cmdargs.length; i++) {
                cmdargs[2] = cmdargs[2] + " " + cmdargs[i];
            }
            cmdargs[2] = cmdargs[2].replace("\"", "");
        }

        if (!cmdArgsHandler(cmdargs)){
            return;
        }

        switch (cmdargs[0]) {
            case "set" -> {  // Commande d'initialisation
                // Traitement de la commande
                switch (cmdargs[1]) {
                    case "ip" -> setIP(cmdargs[2]);
                    case "port" -> setPort(Integer.parseInt(cmdargs[2]));
                    case "mailaddress" -> setMailAddress(cmdargs[2]);
                    case "mailcontent" -> setMailContent(cmdargs[2]);
                    case "groupe" -> setGroupe(Integer.parseInt(cmdargs[2]));
                    case "mail" -> setMail(Integer.parseInt(cmdargs[2]));
                    case "size" -> this.groupeSize = Integer.parseInt(cmdargs[2]); // TODO changer pour une fonction propre avec vérification !
                    case "nbmail" -> setNbMail(Integer.parseInt(cmdargs[2]));
                }
            }
            case "get" -> {
                if (mailAddress == null || mailContent == null) {
                    return;
                }

                if (cmdargs[1].equals("groupe")) {
                    if (cmdargs.length == 3) {
                        System.out.println("From : " + mailAddress.getFrom(Integer.parseInt(cmdargs[2])));
                        for (String s : mailAddress.getTo(Integer.parseInt(cmdargs[2]))) {
                            System.out.println("To : " + s);
                        }
                    } else {
                        for (int i = 0; i < mailAddress.getNbrGroupe(); i++) {
                            System.out.println("From : " + mailAddress.getFrom(i));
                            for (String s : mailAddress.getTo(i)) {
                                System.out.println("To : " + s);
                            }
                        }
                    }
                } else if (cmdargs[1].equals("mail")) {
                    if (cmdargs.length == 3) {
                        System.out.println("Mail #" + cmdargs[2]);
                        System.out.println("Subject : " + mailContent.getSubject(Integer.parseInt(cmdargs[2])));
                        System.out.println("Content : " + mailContent.getContent(Integer.parseInt(cmdargs[2])));
                    } else {
                        for (int i = 0; i < mailContent.getNbr(); i++) {
                            System.out.println("Mail #" + i);
                            System.out.println("Subject : " + mailContent.getSubject(i));
                            System.out.println("Content : " + mailContent.getContent(i));
                        }
                    }
                }
            }
            case "send" -> {
                if (argsChecker()) {
                    smtpConnect();
                }
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
            if(groupe > 0 && groupe <= mailAddress.getNbrGroupe()){
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

    private void setNbMail(int nb){
        if(nb > 0){
            nbmail = nb;
        }
    }
    private void displayHeader(){
        System.out.print(
                """
                           _____   __  __   _______   _____    _____               _   _   _  __
                          / ____| |  \\/  | |__   __| |  __ \\  |  __ \\      /\\     | \\ | | | |/ /
                         | (___   | \\  / |    | |    | |__) | | |__) |    /  \\    |  \\| | | ' /\s
                          \\___ \\  | |\\/| |    | |    |  ___/  |  _  /    / /\\ \\   | . ` | | < \s
                          ____) | | |  | |    | |    | |      | | \\ \\   / ____ \\  | |\\  | | . \\
                         |_____/  |_|  |_|    |_|    |_|      |_|  \\_\\ /_/    \\_\\ |_| \\_| |_|\\_

                        """
        );
    }

    private void displayArgs(){
        for(Data d : Data.values()){
            System.out.printf("%-25s : %s\n", d, getArgsStatus(d));
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
                if(groupe > 0){ return String.format("%-7s : %d" ,"OK", groupe); }
                else if (groupe == -1) { return String.format("%-7s : %s" ,"OK", "Aléatoire"); }
                break;
            case MAIL:
                if(mail > 0){ return String.format("%-7s : %d" ,"OK", mail); }
                else if (mail == -1) { return  String.format("%-7s : %s" ,"OK", "Aléatoire"); }
                break;
            case GROUPESIZE:
                if(groupeSize != 0){ return String.format("%-7s : %d", "OK", groupeSize); }
                break;
            case NBMAIL:
                if(nbmail != 0){ return String.format("%-7s : : %d", "OK", nbmail); }
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
                    break;
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
                case "-gs":
                case "--groupesize":
                    groupeSize = Integer.parseInt(args[i+1]);
                    break;
                case "-nm":
                case "--nombremail":
                    nbmail = Integer.parseInt(args[i+1]);
                    break;
            }
        }
    }

    private Boolean argsChecker(){
        Scanner sc = new Scanner(System.in);
        if(ip == null){
            // Localhost par défaut
            ip = "localhost";
        }
        if(port == 0){
            // 1025 (maildev) par défaut
            port = 1025;
        }
        if(mailAddress == null){
            try {
                mailAddress = new MailAddress(new MailAddressFileReader(getData(sc, Data.MAILADDRESS)));
            }catch (Exception e){
                throw new RuntimeException("Impossible de créer l'objet mailAddress : " + e);
            }
        }
        if(mailContent == null){
            try{
                mailContent = new MailContent(new MailContentFileReader(getData(sc, Data.MAILCONTENT)));
            }catch(Exception e){
                throw new RuntimeException("Impossible de créer l'objet mailContent : " + e);
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
        if(groupeSize == 0){
            groupeSize = 1;
        }

        if(nbmail == 0){
            nbmail = 1;
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
                    data = data.replace("\"", "");
                } while (!data.contains(".txt"));
                break;
            case MAILCONTENT:
                do {
                    System.out.print("Input a valid mailcontent file  : ");
                    data = sc.nextLine();
                    data = data.replace("\"", "");
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
            case GROUPESIZE:
                do{
                    System.out.println("Input a valid groupe size (max :" + mailAddress.getTo(groupe).size());
                    data = sc.nextLine();
                }while(Integer.parseInt(data) <= 0 || Integer.parseInt(data) > mailAddress.getTo(groupe).size());

        }
        return data;
    }

    private void smtpConnect(){
        ConnectionHandler ch = new ConnectionHandler(ip, port, mailAddress, mailContent, groupe, groupeSize, mail);

        for(int i = 0; i < nbmail; i++){
            ch.run();
        }
    }
    // Clear the terminal screen
    public static void clearScreen() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                // For Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // For Unix, Linux, Mac OS
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("Error clearing the terminal: " + e.getMessage());
        }
    }
}
