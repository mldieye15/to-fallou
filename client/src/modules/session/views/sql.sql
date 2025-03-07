CREATE USER 'nom_utilisateur'@'localhost' IDENTIFIED BY 'mot_de_passe';
GRANT ALL PRIVILEGES ON nom_de_votre_base.* TO 'nom_utilisateur'@'localhost';
FLUSH PRIVILEGES;
mysql -u nom_utilisateur -p

  datasource:
    url: jdbc:mariadb://172.16.1.7:3306/pjbac_test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
    username: pjbac_user3
    password: Pjb@c#pwd3
    driver-class-name: org.mariadb.jdbc.Driver

    # thes 2 lines are used for productin
    #tomcat:
    #test-while-idle: true
    #validation-query: SELECT 1
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MariaDB103Dialect
    show-sql: true
    hibernate:
      ddl-auto: update
      dialect: org.hibernate.dialect.MariaDB103Dialect
      format_sql: true
  thymeleaf:
    enabled: true
deploiemnt de java on installer openjavajdk et la base de donne (mysql)
on genere le jar avec mvn install 
on copie le jar et le pod dans un dossier creer dans le serveur avec les commandes suivants
   1. C:\Users\Mouhamadou Fadilou\Desktop\pers0\pjbac\deploy>scp application.yml mariama@196.1.95.113:/home/mariama/deployJava
   2. C:\Users\Mouhamadou Fadilou\Desktop\pers0\pjbac\deploy>scp office-0.0.1-SNAPSHOT.jar mariama@196.1.95.113:/home/mariama/deployJava
   scp  mariama@196.1.95.113:/etc/nginx/ssl/ cle.p12 "C:\Users\Mouhamadou Fadilou\Desktop"
    scp pour copier de la machine local au serveur

    on modifie avec nano le fichier application.yml et on met les identifiant de l'utilisateur de la base de donnees deja creer dans dans notre base de doonees'
    on tape cette commande java -jar -Dspring.config.location=application.yml office-0.0.1-SNAPSHOT.jar pour demarer le service

    <start-class>sn.ucad.office.pjobac.DemandeServiceApplication</start-class> on ajoute cette ligne dans le pom apres
    <java.version>17</java.version>
    et ces plugins 
    <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
cote front end mpn run build et ensuite on copie le dossier dist dans le serveur avec cette comande:
scp -r dist mariama@196.1.95.113:/home/mariama/deployJava
une fois faite on copie le le fichier service.sh dans le serveur et on le rend executable avec chmod o+x service.sh 
./service.sh start pour demarer  ./service.sh stop pour arreter
cote front
bash
npm run build
Cette commande générera un dossier dist dans votre répertoire de projet, contenant les fichiers statiques prêts à être servis.

2. Configuration de Nginx
Assurez-vous que Nginx est installé sur votre serveur. Créez un fichier de configuration Nginx pour votre application. 
Par exemple, créez un fichier nano /etc/nginx/sites-available/pjbac avec le contenu suivant :

server {
    listen 80;
    server_name 172.16.1.7;  

    location / {
        root /home/mariama/deployJava/dist;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    error_page 500 502 503 504 /50x.html;
    location = /50x.html {
        root /usr/share/nginx/html;
    }
}
3. Création d'un lien symbolique
Créez un lien symbolique vers le fichier de configuration dans le répertoire sites-enabled :
sudo ln -s /etc/nginx/sites-available/pjbac /etc/nginx/sites-enabled
 Utilisez la commande suivante pour définir les permissions correctes :

Nginx fonctionne dans le répertoire, donc si vous ne pouvez pas cdaccéder à ce répertoire depuis l'utilisateur nginx, il échouera (tout comme la statcommande dans votre journal). Assurez-vous que la www-userboîte cdjusqu'au /username/test/static. Vous pouvez confirmer que l' statopération échouera ou réussira en exécutant

sudo -u www-data stat /username/test/static
Dans votre cas, le /username répertoire est probablement le problème ici. www-data N'a généralement pas d'autorisations sur cd les répertoires personnels des autres utilisateurs.

La meilleure solution dans ce cas serait d'ajouter www-dataau username groupe :

gpasswd -a www-data mariama
et assurez-vous que ce usernamegroupe peut accéder à tous les répertoires le long du chemin :

 chmod g+x /home/mariama && chmod g+x /home/mariama/deployJava && chmod g+x /home/mariama/deployJava/dist
 sudo chown -R www-data:www-data /home/mariama/deployJava/dist
 tail -n 20 /var/log/nginx/error.log

Pour que vos modifications fonctionnent, redémarrez nginx

sudo service nginx reload
4. Redémarrage de Nginx
Redémarrez Nginx pour appliquer les modifications de configuration :

bash
systemctl restart nginx

Client ID:6KshAn1STvhkeYSrwpcacoBHvVSG1pfb
Client secret:QmqiBuGi7mZdRWwa



INSERT INTO `users` (
  `id`, 
  `account_non_expired`, 
  `account_non_locked`, 
  `anciennete`, 
  `code`, 
  `credentials_non_expired`, 
  `date_creation`, 
  `date_modification`, 
  `date_naiss`, 
  `email`, 
  `identifiant`, 
  `is_enabled`, 
  `is_locked`, 
  `join_date`, 
  `last_login_date`, 
  `matricule`, 
  `mdpasse`, 
  `nom`, 
  `prenoms`, 
  `profile_image_url`, 
  `sexe`, 
  `telephone`, 
  `username`, 
  `uti_cree`, 
  `uti_modifie`, 
  `fonction_id`, 
  `etablissement_id`
) VALUES (
  40, 
  0,
  0, 
  NULL, 
  NULL, 
  0,
  '2024-01-16', 
  '2024-01-23', 
  '2024-12-08', 
  'falloudiarra5@gmail.com', 
  NULL, 
  1, 
  0,
  NULL, 
  NULL, 
  'matriculepl1', 
  '$2a$10$QIA8F3a7JwJLP9MAbulNMeysQGQdC.R/oKHIrAjF32quWbC.c/TcW', 
  'DIOUF', 
  'fallou', 
  NULL, 
  'masculin', 
  '778901212', 
  'planif1', 
  NULL, 
  NULL, 
  NULL, 
  NULL
);
INSERT INTO `users_roles` (`app_user_id`, `roles_id`) VALUES
(1, 3);


pg_dump -U mon_utilisateur -d ma_base -f export.sql

DELIMITER //

CREATE EVENT update_expired_accepted_demands
ON SCHEDULE EVERY 10 MINUTE
DO
BEGIN
    DECLARE dateVerification DATETIME;
    SET dateVerification = NOW();
    
    UPDATE demands d
    JOIN etat_demande_table et ON d.etat_demande_id = et.etat_demande_id
    SET d.etat_demande_id = (SELECT etat_demande_id FROM etat_demande_table WHERE libelle = 'EN ATTENTE'), 
        d.centre = NULL
    WHERE d.date_rejet_demande > dateVerification
    AND et.libelle = 'ACCEPTÉE';
    
    -- Sélectionnez les informations nécessaires pour envoyer un e-mail de notification
    -- Utilisez les résultats pour envoyer l'e-mail
END//

DELIMITER ;

INSERT INTO users_roles (app_user_id, roles_id)
SELECT id, 1
FROM users;

INSERT INTO users_roles (app_user_id, roles_id)
SELECT id, 2
FROM users
WHERE id IN (2111,40);
INSERT INTO users_roles (app_user_id, roles_id)
SELECT id, 3
FROM users
WHERE id IN (2110,219);

INSERT INTO users_roles (app_user_id, roles_id)
SELECT id, 4
FROM users
WHERE id IN ( 2125, 2115, 2126, 2119, 2123, 2116, 2113, 2117, 2121,2133, 2118, 2124, 2132,2122, 2114, 2120, 2129, 2130, 2127, 2128,70);

select * from admin_users where "role" =2


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface DemandeRepository extends MongoRepository<Demande, String> {

    @Query("{'user': ?0, 'date': ?1, 'lieu': ?2, 'ville': ?3}")
    List<Demande> findByUserAndDateAndLieuAndVille(String user, String date, String lieu, String ville);
}

rewaly85@yahoo.fr
MARI5@MA

SELECT DISTINCT *
FROM users u
JOIN demande d ON u.id = d.user_id
WHERE u.code ='s9731';

SELECT SUM(total_jury) AS total_jury_sum
FROM ville;

SELECT DISTINCT u.*
FROM users u
JOIN demande d ON u.id = d.user_id
WHERE session_id =6 AND (d.etat_demande_id=1 OR d.etat_demande_id=4)

 Etablissement scolaire : INSTITUT TE CCART
 Adresse de l’Etablissement : 3030, rue Hochelaga, Montréal (Québec) Canada H1W 1G2

