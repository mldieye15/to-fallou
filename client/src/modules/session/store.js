// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js';
import { format } from 'date-fns';
import { fr } from "date-fns/locale";

const  modulesURL = '/v1/sessions';
const all= modulesURL+'/all';
const sessionsArchive= modulesURL+'/sessionsArchive';
const enCoursSession=modulesURL+'/enCoursSession';
const sessionsOuvertes=modulesURL+'/sessionsOuvertes';
const candidaturesOuvertes=modulesURL+'/candidaturesOuvertes';
const add= modulesURL+'/';

export const useSessionStore = defineStore('session', {
  state: () => ({
    dataListeSession: [],  //  List des données à afficher pour la table
    dataDetails: {},  //  Détails d'un élment,
    loading: true,
    demandesAutorisees: 0,  //  utilisé pour le chargement
    /*breadcrumbs: [
      {
        text: 'Paramétrage',
        disabled: true,
        route: 'home',
      },
      {
        text: 'Académies',
        disabled: true,
        route: 'src/modules/session/routes.js',
      }
    ],*/
    headerTable: [
      { text: 'Libelle Long', value: 'libelleLong', align: 'start', sortable: true },
      { text: 'Date Debut', value: 'dateDebut', align: 'start', sortable: true },
      { text: 'Date Fin', value: 'dateFin', align: 'start', sortable: true },
      { text: 'Etat Session', value: 'ouvert', align: 'start', sortable: true },
      { text: 'Nombre Demande Autorise', value: 'nombreDemandeAutorise', align: 'start', sortable: true },
      { text: 'Delais Validation', value: 'delaisValidation', align: 'start', sortable: true },
      { text: 'Modification', value: 'modification', align: 'start', sortable: true },
      { text: 'Date Ouverture Depot Candidature', value: 'dateOuvertureDepotCandidature', align: 'start', sortable: true },
      { text: 'Date Cloture Depot Candidature', value: 'dateClotureDepotCandidature', align: 'start', sortable: true },
      { text: 'Candidature', value: 'candidature', align: 'start', sortable: true },
      { text: 'Annee', value: 'annee', align: 'start', sortable: true },
      { text: 'TypeSession', value: 'typeSession', align: 'start', sortable: true },
      { text: 'Actions', value: 'actions', sortable: false }
    ]
  }),

  getters: {
    getDataListe: (state) => state.dataListeSession
  },

  actions: {
    //  recupérer la liste des académies et le mettre dans la tabel dataListe
    async all() {
      try {
        await axios.get(`${all}`) 
        .then((response) => {
          if(response.status === 200){
            let res = response.data.map( (element) => {
              let anneeLabel = element.annee ? element.annee.libelleLong : null;
              let typeSessionLabel = element.typeSession ? element.typeSession.libelleLong : null;
              let ouvertLabel = element.ouvert ? 'ouverte' : 'fermée';
              let candidatureLabel = element.candidature ? 'ouverte' : 'fermée';
              let modificationLabel = element.modification ? 'ouverte' : 'fermée';

              return{
                id:element.id, 
                libelleLong: element.libelleLong,
                ouvert:ouvertLabel,
                candidature:candidatureLabel,
                modification:modificationLabel,
                dateDebut:this.formatDate(element.dateDebut) ,
                dateFin: this.formatDate(element.dateFin),
                nombreDemandeAutorise: element.nombreDemandeAutorise,
                delaisValidation: element.delaisValidation,
                dateOuvertureDepotCandidature: this.formatDate(element.dateOuvertureDepotCandidature),
                dateClotureDepotCandidature: this.formatDate(element.dateClotureDepotCandidature),
                annee: anneeLabel,
                typeSession:typeSessionLabel,
              } ;
            });
            console.log('Données avant modification :', this.dataListeSession);
            this.dataListeSession = res;
            // this.demandesAutorisees=nombreDemandeAutorise;
          } 
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    async sessionsArchive() {
      try {
        await axios.get(`${sessionsArchive}`) 
        .then((response) => {
          if(response.status === 200){
            let res = response.data.map( (element) => {
              let anneeLabel = element.annee ? element.annee.libelleLong : null;
              let typeSessionLabel = element.typeSession ? element.typeSession.libelleLong : null;
              let ouvertLabel = element.ouvert ? 'ouverte' : 'fermée';
              let candidatureLabel = element.candidature ? 'ouverte' : 'fermée';
              let modificationLabel = element.modification ? 'ouverte' : 'fermée';

              return{
                id:element.id, 
                libelleLong: element.libelleLong,
                ouvert:ouvertLabel,
                candidature:candidatureLabel,
                modification:modificationLabel,
                dateDebut:this.formatDate(element.dateDebut) ,
                dateFin: this.formatDate(element.dateFin),
                nombreDemandeAutorise: element.nombreDemandeAutorise,
                delaisValidation: element.delaisValidation,
                dateOuvertureDepotCandidature: this.formatDate(element.dateOuvertureDepotCandidature),
                dateClotureDepotCandidature: this.formatDate(element.dateClotureDepotCandidature),
                annee: anneeLabel,
                typeSession:typeSessionLabel,
              } ;
            });
            console.log('Données avant modification :', this.dataListeSession);
            this.dataListeSession = res;
            // this.demandesAutorisees=nombreDemandeAutorise;
          } 
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    async candidaturesOuvertes() {
      try {
        await axios.get(`${candidaturesOuvertes}`) 
        .then((response) => {
          if(response.status === 200){
            let res = response.data.map( (element) => {
              let anneeLabel = element.annee ? element.annee.libelleLong : null;
              let typeSessionLabel = element.typeSession ? element.typeSession.libelleLong : null;
              let ouvertLabel = element.ouvert ? 'ouverte' : 'fermée';
              let candidatureLabel = element.candidature ? 'ouverte' : 'fermée';
              let modificationLabel = element.modification ? 'ouverte' : 'fermée';

              return{
                id:element.id, 
                libelleLong: element.libelleLong,
                ouvert:ouvertLabel,
                candidature:candidatureLabel,
                modification:modificationLabel,
                dateDebut:this.formatDate(element.dateDebut) ,
                dateFin: this.formatDate(element.dateFin),
                nombreDemandeAutorise: element.nombreDemandeAutorise,
                delaisValidation: element.delaisValidation,
                dateOuvertureDepotCandidature: this.formatDate(element.dateOuvertureDepotCandidature),
                dateClotureDepotCandidature: this.formatDate(element.dateClotureDepotCandidature),
                annee: anneeLabel,
                typeSession:typeSessionLabel,
              } ;
            });
            console.log('Données avant modification :', this.dataListeSession);
            this.dataListeSession = res;
          } 
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    async sessionsOuvertes() {
      try {
        await axios.get(`${sessionsOuvertes}`) 
        .then((response) => {
          if(response.status === 200){
            let res = response.data.map( (element) => {
              let anneeLabel = element.annee ? element.annee.libelleLong : null;
              let typeSessionLabel = element.typeSession ? element.typeSession.libelleLong : null;
              let ouvertLabel = element.ouvert ? 'ouverte' : 'fermée';
              let candidatureLabel = element.candidature ? 'ouverte' : 'fermée';
              let modificationLabel = element.modification ? 'ouverte' : 'fermée';

              return{
                id:element.id, 
                libelleLong: element.libelleLong,
                ouvert:ouvertLabel,
                candidature:candidatureLabel,
                modification:modificationLabel,
                dateDebut:this.formatDate(element.dateDebut) ,
                dateFin: this.formatDate(element.dateFin),
                nombreDemandeAutorise: element.nombreDemandeAutorise,
                delaisValidation: element.delaisValidation,
                dateOuvertureDepotCandidature: this.formatDate(element.dateOuvertureDepotCandidature),
                dateClotureDepotCandidature: this.formatDate(element.dateClotureDepotCandidature),
                annee: anneeLabel,
                typeSession:typeSessionLabel,
              } ;
            });
            console.log('Données avant modification :', this.dataListeSession);
            this.dataListeSession = res;
          } 
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    async enCoursSession() {
      try {
        await axios.get(`${enCoursSession}`) 
        .then((response) => {
          if(response.status === 200){
            let res = response.data.map( (element) => {
              let anneeLabel = element.annee ? element.annee.libelleLong : null;
              let typeSessionLabel = element.typeSession ? element.typeSession.libelleLong : null;
              let ouvertLabel = element.ouvert ? 'ouverte' : 'fermée';
              let candidatureLabel = element.candidature ? 'ouverte' : 'fermée';
              let modificationLabel = element.modification ? 'ouverte' : 'fermée';

              return{
                id:element.id, 
                libelleLong: element.libelleLong,
                ouvert:ouvertLabel,
                candidature:candidatureLabel,
                modification:modificationLabel,
                dateDebut:this.formatDate(element.dateDebut) ,
                dateFin: this.formatDate(element.dateFin),
                nombreDemandeAutorise: element.nombreDemandeAutorise,
                delaisValidation: element.delaisValidation,
                dateOuvertureDepotCandidature: this.formatDate(element.dateOuvertureDepotCandidature),
                dateClotureDepotCandidature: this.formatDate(element.dateClotureDepotCandidature),
                annee: anneeLabel,
                typeSession:typeSessionLabel,
              } ;
            });
            console.log('Données après modification :', res);
            if (res.length > 0) {
              this.dataListeSession = res;
            } else {
              console.log('Aucune session en cours.');
            }
          } 
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    //  recupérer les informations d'une session par son ide et le mettre dans la tabel dataDetails
    async one(session) {
      try {
        await axios.get(`${modulesURL}/${session}`) 
        .then((response) => {
          if(response.status === 200){
            this.dataDetails = response.data;
          } 
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    getAnneBySessionId(id) {
      const session = this.dataListeSession.find(item => item.id === id);
      return session ? session.annee: '';
    },
    //  ajouter une session
    async add(payload) {
      try {
        await axios.post(`${add}`, payload) 
        .then((response) => {
          if(response.status === 200 ){
            this.dataDetails = response.data;
            console.log("Response: ", this.dataDetails);
          }
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    //  modifier une session
    async modify(id, payload) {
      try {
        console.log("Id: ", id);
        console.log("Payload: ", payload);
        await axios.put(`${modulesURL}/${id}`, payload)
        .then((response) => {
          if(response.status === 200 ){
            this.dataDetails = response.data;
          }
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    //  supprimer une session
    async destroy(id) {
      try {
        await axios.delete(`${modulesURL}/${id}`) 
        .then((response) => {
          if(response.status === 200 ){
            this.dataDetails = response.data;
          }
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    formatDate(date) {
      return format(new Date(date), 'dd-MM-yyyy', { locale: fr }); // Exemple de format, ajustez selon vos besoins
    },
    // ouvrir et clocturer session
    async toggleSessionState(id) {
      try {
        const response = await axios.put(`${modulesURL}/${id}/etatSession`);
        if (response.status === 200) {
          // Mettre à jour la liste après le basculement d'état
          this.all();
        }
      } catch (error) {
        console.error(error);
        this.error = error;
      } finally {
        this.loading = false;
      }
    },
    // ouvrir et clocturer candidature
    async toggleCandidatureState(id) {
      try {
        const response = await axios.put(`${modulesURL}/${id}/etatCandidature`);
        if (response.status === 200) {
          // Mettre à jour la liste après le basculement d'état
          this.all();
        }
      } catch (error) {
        console.error(error);
        this.error = error;
      } finally {
        this.loading = false;
      }
    },
    async toggleModificationState(id){
      try {
        const response = await axios.put(`${modulesURL}/${id}/etatModification`);
        if (response.status === 200) {
          // Mettre à jour la liste après le basculement d'état
          this.all();
        }
      } catch (error) {
        console.error(error);
        this.error = error;
      } finally {
        this.loading = false;
      }
    }
  },
  
})
