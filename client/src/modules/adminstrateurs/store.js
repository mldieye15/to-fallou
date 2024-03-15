// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js';
import { format } from 'date-fns';
import { fr } from "date-fns/locale";

const  modulesURL = '/v1/users';
const adminURL = '/auth/v1';
const addAdmin =adminURL +'/addAdmin';
const addPlanificateur=adminURL +'/addPlanificateur';
const addSupervisseur =adminURL +'/addSupervisseur';
const upAdmin= modulesURL +'/upAdmin';
const admins= modulesURL+'/admins';
const users= modulesURL+'/users';
const planificateurs= modulesURL+'/planificateurs';
const supervisseurs= modulesURL+'/supervisseurs';
const add= modulesURL+'/';
const emailAvailability = modulesURL +'/email-availability';
const matriculeAvailability = modulesURL +'/matricule-availability';
const emailAvailabilityUp = modulesURL +'/email-availabilityUp';
const matriculeAvailabilityUp = modulesURL +'/matricule-availabilityUp';
const upProfile= modulesURL +'/upProfileAdmin';

export const useAdminStore = defineStore('admin', {
  state: () => ({
    dataListeUtilisateur: [],  //  List des données à afficher pour la table
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
      // { text: 'Date de Naissance', value: 'dateNaiss', align: 'start', sortable: true },
      { text: 'Email', value: 'email', align: 'start', sortable: true },
      // { text: 'Username', value: 'username', align: 'start', sortable: true },
      // { text: 'Password', value: 'mdpasse', align: 'start', sortable: true },
      { text: 'Sexe', value: 'sexe', align: 'start', sortable: true },
      { text: 'Telephone', value: 'telephone', align: 'start', sortable: true },
      { text: 'Actions', value: 'actions', sortable: false }
    ],
    columns: [
      { label: 'Prénom(s)', field: 'prenoms'},
      { label: 'Nom', field: 'nom'},
      { label: 'Matricule', field: 'matricule'},
      // { label: 'Date de Naissance', field: 'dateNaiss',width: "200px" ,resizable: true},
      { label: 'Adresse email', field: 'email'},
      // { label: 'Username', field: 'username'},
      { label: 'Sexe', field: 'sexe'},
      { label: 'Téléphone', field: 'telephone'},
      { label: 'Actions', field: 'actions' }
      // Ajoutez d'autres colonnes selon vos besoins
    ],
  }),

  getters: {
    getDataListeUtilisateur: (state) => state.dataListeUtilisateur,
  },

  actions: {
    async all() {
      try {
        await axios.get(`${all}`) 
        .then((response) => {
          if(response.status === 200){
            let res = response.data.map( (element) => {
              let fonctionLabel = element.fonction? element.fonction.libelleLong:null;
              let etablissementLabel = element.etablissement? element.etablissement.libelleLong:null;
              return{
                id:element.id, 
              prenoms: element.prenoms,
              nom: element.nom,
              matricule: element.matricule,
              // dateNaiss: this.formatDate(element.dateNaiss),
              email: element.email,
              // username: element.username,
              code: element.code,
              // mdpasse: element.mdpasse,
              sexe: element.sexe,
              telephone: element.telephone,
              anciennete: element.anciennete,
              fonction: fonctionLabel,
              etablissement: etablissementLabel,
              }
              
            })

            this.dataListeUtilisateur = res;
          } 
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    }, //  recupérer la liste des utilisqteurs et le mettre dans la tabel dataListe
    async admin() {
      try {
        await axios.get(`${admins}`) 
        .then((response) => {
          if(response.status === 200){
            let res = response.data.map( (element) => {
              return{
                id:element.id, 
                prenoms: element.prenoms,
                nom: element.nom,
                matricule: element.matricule,
                // dateNaiss: this.formatDate(element.dateNaiss),
                email: element.email,
                telephone: element.telephone,
                sexe: element.sexe,
              }
              
            })

            this.dataListeUtilisateur = res;
          } 
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    async planificateur() {
      try {
        await axios.get(`${planificateurs}`) 
        .then((response) => {
          if(response.status === 200){
            let res = response.data.map( (element) => {
              return{
                id:element.id, 
                prenoms: element.prenoms,
                nom: element.nom,
                matricule: element.matricule,
                // dateNaiss: this.formatDate(element.dateNaiss),
                email: element.email,
                // username: element.username,
                sexe: element.sexe,
                telephone: element.telephone
              }
              
            })

            this.dataListeUtilisateur = res;
          } 
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    async supervisseur() {
      try {
        await axios.get(`${supervisseurs}`) 
        .then((response) => {
          if(response.status === 200){
            let res = response.data.map( (element) => {
              return{
                id:element.id, 
                prenoms: element.prenoms,
                nom: element.nom,
                matricule: element.matricule,
                // dateNaiss: this.formatDate(element.dateNaiss),
                email: element.email,
                // username: element.username,
                sexe: element.sexe,
                telephone: element.telephone
              }
              
            })

            this.dataListeUtilisateur = res;
          } 
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    async user() {
      try {
        await axios.get(`${users}`) 
        .then((response) => {
          if(response.status === 200){
            let res = response.data.map( (element) => {
              return{
                id:element.id, 
                prenoms: element.prenoms,
                nom: element.nom,
                matricule: element.matricule,
                // dateNaiss: this.formatDate(element.dateNaiss),
                email: element.email,
                // username: element.username,
                code: element.code,
                sexe: element.sexe,
                telephone: element.telephone,
              }
              
            })

            this.dataListeUtilisateur = res;
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
    async addAdmin(payload) {
      try {
        await axios.post(`${addAdmin}`, payload) 
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
    async addSupervisseur(payload) {
      try {
        await axios.post(`${addSupervisseur}`, payload) 
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
    async addPlanificateur(payload) {
      try {
        await axios.post(`${addPlanificateur}`, payload) 
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
        await axios.put(`${upAdmin}/${id}`, payload)
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
    async upProfile(payload) {
      try {
        console.log("Payload: ", payload);
        await axios.put(upProfile, payload)
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
    async checkEmailExistenceUp({ userId, email }) {
      try {
        const response = await axios.get(`${emailAvailabilityUp}?userId=${userId}&email=${email}`);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error("Erreur lors de la vérification de l'email :", error);
        return false;
      }
    },
    async checkMatriculeExistenceUp({ userId, matricule }) {
      try {
        const response = await axios.get(`${matriculeAvailabilityUp}?userId=${userId}&matricule=${matricule}`);
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
