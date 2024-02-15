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

@ip: 172.16.1.7
user: pjbac_user3
@mdp:Pjb@c#pwd3
bdd: pjbac_test
mysql -u pjbac_user3 -p Pjb@c#pwd3

mysql -u pjbac_user3 -h 172.16.1 -p pjbac_test
mysql -u pjbac_user3 -h 172.16.1.7 -pPjb@c#pwd3 pjbac_test

GRANT EVENT ON *.* TO 'pjbac_user3'@'172.16.1.7' pour que l'utilisateur puisse creer des evenements
SHOW GRANTS FOR 'pjbac_user3'@'172.16.1.7'; pour voir les privileges
SET GLOBAL event_scheduler="ON"
SHOW GLOBAL event_scheduler; pour activer le planificateur
SHOW VARIABLES LIKE 'event_scheduler' pour verifier le planificateur;
