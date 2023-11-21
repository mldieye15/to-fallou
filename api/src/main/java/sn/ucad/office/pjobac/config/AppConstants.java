package sn.ucad.office.pjobac.config;

public class AppConstants {
    //  ***********************************************
    // constants pour les formats horaires
    public static final String DATE_FR_FORMAT_TIRET = "dd/MM/yyyy";
    public static final String DATE_FR_FORMAT_SALASH = "yyyy-MM-dd";
    public static final String DATE_EN_FORMAT_TIRET = "yyyy-MM-dd";
    public static final String DATE_EN_FORMAT_SALASH = "yyyy/MM/dd";

    public static final String HOUR_MIN_FR_FORMAT_DOT = "HH.mm";
    public static final String HOUR_MIN_SEC_FR_FORMAT_DOT = "HH.mm.ss";
    //  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    //  *************************************************
    // constants pour les formats horaires
    public static final String MESS_ACTIV_COMPTE_OBJET = "Activation compte";
    public static final String MESS_ACTIV_COMPTE_CONTENT = "Bienvenue sur la plATFORME  pjobac, merci de cliquer sur le lien ci-dessous pour activer votre compte.";
    public static final String MESS_ACTIV_COMPTE_FIN = "Bonne réception.";
    //  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    //  *************************************************
    // liens
    public static final String LIEN_ACTIV_COMPTE = "http://localhost:8201/pjobac/api/auth/v1/verif-token";

    //  $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    //  *************************************************
    //  Page
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";
    public static final String LISTE_DEMANDE_IMPUTATION_URL = "https://entpersonnel.ucad.sn/api/list/demandes/imputations/etat_traitement";
    // Utilse pour régler les date non renseigné
    public static final String FIXED_DEFAULT_DATE = "01/01/2020";
    //  Imputation entêt
    public static final String TITLE_UCAD = "Université Cheikh Anta Diop de Dakar";
    public static final String TITLE_RECTORAT = "Rectorat";
    public static final String TITLE_BP = "BP. 5005-Dakar-Fann-Sénégal";
    public static final String TITLE_TELE_FAX = "Téléfax: (221) 33 825 28 33 - Tél.: 33 825 05 30";
    public static final String TITLE_IMPUTATION = "IMP du Service Rectorat de l'Université Cheikh Anata Diop de Dakar";
}
