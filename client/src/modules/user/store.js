// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js';
import { format } from 'date-fns';
import { fr } from "date-fns/locale";

const  modulesURL = '/v1/users';
const  modulesURL1 = '/v1/candidatAuthorisers';
const adminURL = '/auth/v1';
const inscription =adminURL +'/inscription';
const all= modulesURL+'/all';
const allNonAffecter= modulesURL+'/allNonAffecter';
const admins= modulesURL+'/admins';
const users= modulesURL+'/users';
const planificateurs= modulesURL+'/planificateurs';
const supervisseurs= modulesURL+'/supervisseurs';
const add= modulesURL+'/';
const emailAvailability = modulesURL +'/email-availability';
const matriculeAvailability = modulesURL +'/matricule-availability';
const emailAvailabilityUp = modulesURL +'/email-availabilityUp';
const matriculeAvailabilityUp = modulesURL +'/matricule-availabilityUp';
const upProfileUser= modulesURL +'/upProfileUser';

export const useUtilisateurStore = defineStore('utilisateur', {
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
      { text: 'Prénom(s)', value: 'prenoms', align: 'start', sortable: true },
      { text: 'Nom', value: 'nom', align: 'start', sortable: true },
      { text: 'Matricule', value: 'matricule', align: 'start', sortable: true },
      { text: 'Code', value: 'code', align: 'start', sortable: true },
      { text: 'Adresse email', value: 'email', align: 'start', sortable: true },
      // { text: 'Username', value: 'username', align: 'start', sortable: true },
      // { text: 'Password', value: 'mdpasse', align: 'start', sortable: true },
      { text: 'Sexe', value: 'sexe', align: 'start', sortable: true },
      { text: 'Téléphone', value: 'telephone', align: 'start', sortable: true },
      { text: 'Liste rouge', value: 'listeRouge', sortable: false },
      { text: 'Liste noire', value: 'listeNoire', sortable: false },
      { text: 'Etat du compte', value: 'etat', sortable: false },
      { text: 'Actions', value: 'actions', sortable: false }
    ],
    headerTable1: [
      { text: 'Prénom(s)', value: 'prenoms', align: 'start', sortable: true },
      { text: 'Nom', value: 'nom', align: 'start', sortable: true },
      { text: 'Matricule', value: 'matricule', align: 'start', sortable: true },
      { text: 'Code', value: 'code', align: 'start', sortable: true },
      { text: 'Adresse email', value: 'email', align: 'start', sortable: true },
      // { text: 'Username', value: 'username', align: 'start', sortable: true },
      // { text: 'Password', value: 'mdpasse', align: 'start', sortable: true },
      { text: 'Sexe', value: 'sexe', align: 'start', sortable: true },
      { text: 'Téléphone', value: 'telephone', align: 'start', sortable: true },
      { text: 'Liste rouge', value: 'listeRouge', sortable: false },
      { text: 'Liste noire', value: 'listeNoire', sortable: false },
      { text: 'Etat du compte', value: 'etat', sortable: false },
      { text: 'Actions', value: 'actions', sortable: false }
    ],
    columns: [
      { label: 'Prénom(s)', field: 'prenoms',width: "200px",resizable: true},
      { label: 'Nom', field: 'nom',width: "100px",resizable: true },
      { label: 'Matricule', field: 'matricule',width: "100px",resizable: true },
      // { label: 'Date de Naissance', field: 'dateNaiss',width: "200px" ,resizable: true},
      { label: 'Adresse email', field: 'email',width: "100px",resizable: true},
      // { label: 'Username', field: 'username',width: "160px",resizable: true },
      { label: 'Sexe', field: 'sexe',width: "100px",resizable: true},
      { label: 'Téléphone', field: 'telephone',width: "120px",resizable: true},
      { label: 'Actions', field: 'actions',width: "200px",resizable: true }
      // Ajoutez d'autres colonnes selon vos besoins
    ],
  }),

  getters: {
    getDataListeUtilisateur: (state) => state.dataListeUtilisateur,
  },

  actions: {
    //  recupérer la liste des utilisqteurs et le mettre dans la tabel dataListe
    async all() {
      try {
        await axios.get(`${all}`)
        .then((response) => {
          if(response.status === 200){
            let res = response.data.map( (element) => {
              let fonctionLabel = element.fonction? element.fonction.libelleLong:null;
              let etablissementLabel = element.etablissement? element.etablissement.libelleLong:null;
              let etatLabel = element.locked? 'activé':'désactivé';
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
              etat:etatLabel
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
    async allNonAffecter() {
      try {
        await axios.get(`${allNonAffecter}`)
        .then((response) => {
          if(response.status === 200){
            let res = response.data.map( (element) => {
              let fonctionLabel = element.fonction? element.fonction.libelleLong:null;
              let etablissementLabel = element.etablissement? element.etablissement.libelleLong:null;
              let etatLabel = element.locked? 'activé':'désactivé';
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
              etat:etatLabel
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
                username: element.username,
                code: element.code,
                sexe: element.sexe,
                telephone: element.telephone,
                anciennete: element.anciennete,
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
                username: element.username,
                code: element.code,
                sexe: element.sexe,
                telephone: element.telephone,
                anciennete: element.anciennete,
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
                username: element.username,
                code: element.code,
                sexe: element.sexe,
                telephone: element.telephone,
                anciennete: element.anciennete,
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
              let etatLabel = element.enabled? 'activé':'désactivé';
              let listeRouge = element.accountNonLocked? 'NON':'OUI';
              let listeNoire = element.accountNonExpired? 'NON':'OUI';
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
                anciennete: element.anciennete,
                etat:etatLabel,
                listeRouge,
                listeNoire
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
    async inscription(payload) {
      try {
        await axios.post(`${inscription}`, payload)
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
    //  ajouter une sessio
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
    async upProfileUser(payload) {
      try {
        console.log("Payload: ", payload);
        await axios.put(upProfileUser, payload)
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
    async bloquer(id) {
      try {
        const response = await axios.put(`${modulesURL}/${id}/bloquer`);
        if (response.status === 200) {
          this.user();
        }
      } catch (error) {
        console.error(error);
        this.error = error;
      } finally {
        this.loading = false;
      }
    },
    async listeNoire(id) {
      try {
        const response = await axios.put(`${modulesURL}/${id}/listeNoire`);
        if (response.status === 200) {
          this.user();
        }
      } catch (error) {
        console.error(error);
        this.error = error;
      } finally {
        this.loading = false;
      }
    },
    async autorisation(id) {
      try {
        const response = await axios.put(`${modulesURL1}/${id}/autorisation`);
        if (response.status === 200) {
          this.user();
        }
      } catch (error) {
        console.error(error);
        this.error = error;
      } finally {
        this.loading = false;
      }
    },
  },

})
