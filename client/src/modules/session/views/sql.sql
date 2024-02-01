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

    C:\Users\Mouhamadou Fadilou\Desktop\pers0\pjbac\deploy>scp application.yml mariama@172.16.1.7:/home/mariama/deployJava
    scp pour copier de la machine local au serveur