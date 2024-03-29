DELIMITER //

CREATE EVENT update_expired_accepted_demands
ON SCHEDULE EVERY 10 MINUTE
DO
BEGIN
    
    UPDATE demande d
    SET d.etat_demande_id = (SELECT id FROM etat_demande WHERE libelle_long = 'déclinée'), 
        d.centre_id = NULL
    WHERE d.date_rejet_demande <= NOW()
    AND d.etat_demande_id  = (SELECT id FROM etat_demande WHERE libelle_long = 'acceptée');
    
END//

DELIMITER;

CREATE EVENT update_expired_accepted_demands
ON SCHEDULE EVERY 10 MINUTE
STARTS '2024-02-15 15:42:31.000'
ON COMPLETION NOT PRESERVE
ENABLE
DO BEGIN
    
    UPDATE demande d
    SET d.etat_demande_id = (SELECT id FROM etat_demande WHERE libelle_long = 'déclinée'), 
        d.centre_id = NULL
    WHERE d.date_rejet_demande <= UTC_TIMESTAMP() -- Comparaison directe avec la date actuelle en UTC
    AND d.etat_demande_id  = (SELECT id FROM etat_demande WHERE libelle_long = 'acceptée');
    
END;

@ip: 172.16.1.7
user: pjbac_user3
@mdp:Pjb@c#pwd3
bdd: pjbac_test
mysql -u pjbac_user3 -p Pjb@c#pwd3

mysql -u pjbac_user3 -h 172.16.1 -p pjbac_test
mysql -u pjbac_user3 -h 172.16.1.7 -pPjb@c#pwd3 pjbac_test

GRANT EVENT ON *.* TO 'pjbac_user3'@'172.16.1.7' pour que l'utilisateur puisse creer des evenements
SHOW GRANTS FOR 'pjbac_user3'@'172.16.1.7'; pour voir les privileges
SET GLOBAL event_scheduler="ON" ; pour activer le planificateur
SHOW VARIABLES LIKE 'event_scheduler' pour verifier le planificateur;




sudo find / -name my.cnf pour rechercher les fichiers de configuration

sudo nano /etc/mysql/mariadb.conf.d/50-server.cnf pour modifier le fichier

Ajoutez la ligne suivante  (default-time-zone = '+00:00') sous la section [mysqld] pour définir le fuseau horaire par défaut en UTC :
sql

Enregistrez les modifications en appuyant sur Ctrl + O, puis appuyez sur Enter. Ensuite, appuyez sur Ctrl + X pour quitter l'éditeur Nano

sudo systemctl restart mysql



 @Query("SELECT c FROM Centre c " +
            "WHERE c.ville = :ville " +
            "AND c.nombreJury > (SELECT COUNT(d) FROM Demande d WHERE d.centre = c AND d.etatDemande.libelleLong IN ('acceptée', 'validée') AND d.session IN (SELECT s FROM Session s WHERE s.annee.encours = true))")
List<Centre> findCentresQuotaNonAtteintParVille(@Param("ville") Ville ville);



<v-btn @click.prevent="redirectToAdd()" class="ma-0" variant="outlined" color="cyan-darken-1"> {{ $t('apps.forms.ajouter') }} </v-btn>