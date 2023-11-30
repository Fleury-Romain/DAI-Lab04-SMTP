# SMTPrank

Ce programme est une application en ligne de commande permettant d'envoyer des courriels SMTP à plusieurs destinataires à partir d'une liste d'adresses électroniques et de contenus prédéfinis.

## Description

Ce projet vise à simplifier l'envoi de courriels en masse en utilisant le protocole SMTP. Il permet de spécifier des adresses électroniques de destinataires ainsi que le contenu des courriels à envoyer, le tout géré via des paramètres en ligne de commande ou une interface console.

## Comment installer le programme

### Compilation standard

Commencez par cloner ce repository : 
```sh 
git clone https://github.com/Fleury-Romain/DAI-Lab04-SMTP 
```

Ensuite, il faut compiler le code présent dans ```DAI-Lab04-SMTP``` Dans un dossier de destination de compilation, ici ```bin/```

```shell
javac -d bin/ code/src/main/java/ch/heig/dai/lab/SMTP/*.java
```

Puis, vous pouvez executer la classe principale ```MainTest``` pour lancer le programme (sans argument en console)

```shell
java bin/ch.heig.dai.lab.SMTP.SMTPrank
```

#### Script complet de déploiement standard

```shell
git clone https://github.com/Fleury-Romain/DAI-Lab04-SMTP 

cd DAI-Lab04-SMTP
mkdir bin

javac -d bin/ code/src/main/java/ch/heig/dai/lab/SMTP/*.java
java bin/ch.heig.dai.lab.SMTP.SMTPrank
```

### Compilation avec Maven

Commencez par cloner le repository comme vu précédement
```sh 
git clone https://github.com/Fleury-Romain/DAI-Lab04-SMTP 
```
rendez-vous dans le dossier ```code``` qui contient le fichier pom.xml pour Maven

```shell
cd DAI-Lab04-SMTP/code/
```

Puis compiler les fichiers java avec Maven

```shell
mvn clean
mvn package
```
Vous pouvez maintenant lancer ```SMTPrank.jar``` qui se situe dans ```target/```

```shell
java target/SMTPrank.jar
```

#### Script complet de déploiement avec Maven
```shell
git clone https://github.com/Fleury-Romain/DAI-Lab04-SMTP
cd DAI-Lab04-SMTP/code/ 
mvn clean 
mvn package 
java -jar target/SMTPrank.jar

```

## Structure du Code

Le code est organisé en plusieurs classes offrant les fonctionnalités suivantes :

- `MainTest`: Initialise le programme en mode console ou lance l'envoi directement en fonction des arguments fournis.
- `CmdHandler`: Gère les commandes de l'utilisateur en mode console, permettant de configurer les adresses, les contenus et autres paramètres.
- `ConnectionHandler`: Établit une connexion SMTP avec le serveur pour l'envoi des courriels.
- `MailAddress` et `MailContent`: Gèrent respectivement les adresses e-mail et les contenus des e-mails.
- `MailAddressFileReader` et `MailContentFileReader`: Lisent les fichiers pour extraire les adresses et les contenus à partir de formats spécifiques.

## Utilisation

### Différence Entre Exécution avec et sans Arguments

- **Avec Arguments**: L'exécution avec des arguments en ligne de commande permet de configurer directement les adresses e-mail, les contenus et autres paramètres pour l'envoi des courriels. Les arguments disponibles sont les suivants :
    - `--ip [adresse]`: Définit l'adresse IP pour la connexion SMTP.
    - `--port [port]`: Définit le port pour la connexion SMTP.
    - `--mailaddress [fichier]`: Spécifie le fichier contenant les adresses e-mail.
    - `--mailcontent [fichier]`: Spécifie le fichier contenant le contenu des e-mails.
    - `--groupe [groupe]`: Définit le groupe d'adresses à utiliser.
    - `--mail [mail]`: Définit l'e-mail à envoyer.
    - `--size [taille]`: Définit la taille du groupe d'adresses.
 
### Commandes Possibles dans CmdHandler

Lorsque le programme est lancé en mode console (`CmdHandler`), les commandes suivantes sont disponibles :
- `set ip [adresse]`: Définit l'adresse IP pour la connexion SMTP.
- `set port [port]`: Définit le port pour la connexion SMTP.
- `set mailaddress [fichier]`: Spécifie le fichier contenant les adresses e-mail.
- `set mailcontent [fichier]`: Spécifie le fichier contenant le contenu des e-mails.
- `set groupe [groupe]`: Définit le groupe d'adresses à utiliser.
- `set mail [mail]`: Définit l'e-mail à envoyer.
- `set size [taille]`: Définit la taille du groupe d'adresses.
- `get groupe [ID]`: Affiche les adresses du groupe spécifié.
- `get mail [ID]`: Affiche le contenu de l'e-mail spécifié.
- `send`: Envoie les courriels en utilisant la configuration actuelle.

## Problèmes Possibles

Les utilisateurs pourraient rencontrer les problèmes suivants :
- **Format des Fichiers**: Les fichiers d'adresses e-mail et de contenu d'e-mails doivent respecter un format spécifique pour être lus correctement.
- **Erreurs de Paramètres**: Des erreurs peuvent survenir si les paramètres passés en ligne de commande ne sont pas corrects ou ne sont pas dans la plage attendue.

N'hésitez pas à consulter la documentation du code.
