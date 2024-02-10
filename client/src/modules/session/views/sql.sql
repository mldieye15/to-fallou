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
   1. C:\Users\Mouhamadou Fadilou\Desktop\pers0\pjbac\deploy>scp application.yml mariama@172.16.1.7:/home/mariama/deployJava
   2. C:\Users\Mouhamadou Fadilou\Desktop\pers0\pjbac\deploy>scp office-0.0.1-SNAPSHOT.jar mariama@172.16.1.7:/home/mariama/deployJava
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
scp -r dist mariama@172.16.1.7:/home/mariama/deployJava
une fois faite on copie le le fichier service.sh dans le serveur et on le rend executable avec chmod o+x service.sh 
./service.sh start pour demarer  ./service.sh stop pour arreter
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
