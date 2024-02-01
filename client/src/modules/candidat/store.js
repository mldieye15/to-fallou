// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js';
import { format } from 'date-fns';
import { fr } from "date-fns/locale";

const  modulesURL = '/v1/detailsCandidats';
const all= modulesURL+'/all';
const allBySession= modulesURL+'/allBySession';
const note= modulesURL+'/note';
const bonus= modulesURL+'/bonus';
const malus= modulesURL+'/malus';
export const useCandidatStore = defineStore('candidat', {
  state: () => ({
    dataListeCandidat: [],  //  List des données à afficher pour la table
    dataDetails: {},  //  Détails d'un élment,
    loading: true,  //  utilisé pour le chargement
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
      { text: 'Prenoms', value: 'prenoms', align: 'start', sortable: true },
      { text: 'Nom', value: 'nom', align: 'start', sortable: true },
      { text: 'Matricule', value: 'matricule', align: 'start', sortable: true },
      { text: 'Date de Naissance', value: 'dateNaiss', align: 'start', sortable: true },
      // { text: 'Email', value: 'email', align: 'start', sortable: true },
      { text: 'Sexe', value: 'sexe', align: 'start', sortable: true },
      { text: 'Telephone', value: 'telephone', align: 'start', sortable: true },
      { text: 'Fonction', value: 'fonction', align: 'start', sortable: true },
      { text: 'Etablissement de Provenance', value: 'etablissement', align: 'start', sortable: true },
      { text: 'Affectable', value: 'affectable', align: 'start', sortable: true },
      { text: 'Actions', value: 'actions', sortable: false }
    ]
  }),

  getters: {
    getDataListeCandidat: (state) => state.dataListeCandidat,
  },

  actions: {
    //  recupérer la liste des utilisqteurs et le mettre dans la tabel dataListe
    async all() {
      try {
        await axios.get(`${all}`) 
        .then((response) => {
          if(response.status === 200){
            let res = response.data.map( (element) => {
              let fonctionLabel = element.candidat.fonction? element.candidat.fonction.libelleLong:null;
              let etablissementLabel = element.candidat.etablissement? element.candidat.etablissement.libelleLong:null;
              let nomLabel = element.candidat? element.candidat.nom:null;
              let matriculeLabel = element.candidat? element.candidat.matricule:null;
              let dateNaissLabel = element.candidat? element.candidat.dateNaiss:null;
              // let emailLabel = element.candidat? element.candidat.email:null;
              let sexeLabel = element.candidat? element.candidat.sexe:null;
              let telephoneLabel = element.candidat? element.candidat.telephone:null;
              let prenomsLabel = element.candidat? element.candidat.prenoms:null;
              let affectableLabel = element.affectable? 'OUI':'NON';
              return{
              id:element.id, 
              affectable: affectableLabel,
              nom: nomLabel,
              prenoms: prenomsLabel,
              matricule: matriculeLabel,
              dateNaiss: this.formatDate(dateNaissLabel),
              // email: emailLabel,
              sexe: sexeLabel,
              telephone: telephoneLabel,
              fonction: fonctionLabel,
              etablissement: etablissementLabel,
              }
              
            })

            this.dataListeCandidat = res;
            console.log(this.dataListeCandidat);
          } 
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    async allBySession() {
      try {
        await axios.get(`${allBySession}`) 
        .then((response) => {
          if(response.status === 200){
            let res = response.data.map( (element) => {
              let fonctionLabel = element.candidat.fonction? element.candidat.fonction.libelleLong:null;
              let etablissementLabel = element.candidat.etablissement? element.candidat.etablissement.libelleLong:null;
              let nomLabel = element.candidat? element.candidat.nom:null;
              let matriculeLabel = element.candidat? element.candidat.matricule:null;
              let dateNaissLabel = element.candidat? element.candidat.dateNaiss:null;
              // let emailLabel = element.candidat? element.candidat.email:null;
              let sexeLabel = element.candidat? element.candidat.sexe:null;
              let telephoneLabel = element.candidat? element.candidat.telephone:null;
              let prenomsLabel = element.candidat? element.candidat.prenoms:null;
              let affectableLabel = element.affectable? 'OUI':'NON';
              return{
              id:element.id, 
              affectable: affectableLabel,
              nom: nomLabel,
              prenoms: prenomsLabel,
              matricule: matriculeLabel,
              dateNaiss: this.formatDate(dateNaissLabel),
              // email: emailLabel,
              sexe: sexeLabel,
              telephone: telephoneLabel,
              fonction: fonctionLabel,
              etablissement: etablissementLabel,
              }
              
            })

            this.dataListeCandidat = res;
            console.log(this.dataListeCandidat);
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
    async appreciation(id, payload) {
      try {
        console.log("Id: ", id);
        console.log("Payload: ", payload);
        await axios.put(`${note}/${id}`, payload)
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
    async bonus(id, payload) {
      try {
        console.log("Id: ", id);
        console.log("Payload: ", payload);
        await axios.put(`${bonus}/${id}`, payload)
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
    async malus(id, payload) {
      try {
        console.log("Id: ", id);
        console.log("Payload: ", payload);
        await axios.put(`${malus}/${id}`, payload)
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
    async checkEmailExistence(email) {
      try {
        const response = await axios.get(`${emailAvailability}?email=${email}`);
        console.log("Réponse de emailAvailability :", response);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error("Erreur lors de la vérification de l'email :", error);
        return false;
      }
    },
    async checkUsernameExistence(username) {
      try {
        const response = await axios.get(`${usernameAvailability}?username=${username}`);
        console.log("Réponse de usernameAvailability :", response);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error("Erreur lors de la vérification du nom d'Candidat :", error);
        return false;
      }
    },
    async checkMatriculeExistence(matricule) {
      try {
        const response = await axios.get(`${matriculeAvailability}?matricule=${matricule}`);
        console.log("Réponse de matriculeAvailability :", response);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error("Erreur lors de la vérification du matricule :", error);
        return false;
      }
    },
    formatDate(date) {
      return format(new Date(date), 'dd-MM-yyyy', { locale: fr }); // Exemple de format, ajustez selon vos besoins
    },
  },
  
})
