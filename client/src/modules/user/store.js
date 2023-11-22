// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js';
import { format } from 'date-fns';
import { fr } from "date-fns/locale";

const  modulesURL = '/v1/users';
const all= modulesURL+'/all';
const add= modulesURL+'/';
const emailAvailability = modulesURL +'/email-availability';
const usernameAvailability = modulesURL +'/username-availability';
const matriculeAvailability = modulesURL +'/matricule-availability';

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
      { text: 'Prenoms', value: 'prenoms', align: 'start', sortable: true },
      { text: 'Nom', value: 'nom', align: 'start', sortable: true },
      { text: 'Matricule', value: 'matricule', align: 'start', sortable: true },
      { text: 'Date de Naissance', value: 'dateNaiss', align: 'start', sortable: true },
      { text: 'Email', value: 'email', align: 'start', sortable: true },
      { text: 'Username', value: 'username', align: 'start', sortable: true },
      // { text: 'Password', value: 'mdpasse', align: 'start', sortable: true },
      { text: 'Sexe', value: 'sexe', align: 'start', sortable: true },
      { text: 'Code', value: 'code', align: 'start', sortable: true },
      { text: 'Telephone', value: 'telephone', align: 'start', sortable: true },
      { text: 'Anciennete', value: 'anciennete', align: 'start', sortable: true },
      { text: 'Fonction', value: 'fonction', align: 'start', sortable: true },
      { text: 'Etablissement de Provenance', value: 'etablissement', align: 'start', sortable: true },
      { text: 'Actions', value: 'actions', sortable: false }
    ]
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
              return{
                id:element.id, 
              prenoms: element.prenoms,
              nom: element.nom,
              matricule: element.matricule,
              dateNaiss: this.formatDate(element.dateNaiss),
              email: element.email,
              username: element.username,
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
    async emailAvailability(email) {
      try {
        const response = await axios.get(`${emailAvailability}?email=${email}`);
        return response.data.isAvailable;
      } catch (error) {
        console.error('Erreur lors de la vérification de l\'email :', error);
        return false;
      }
    },

    async usernameAvailability(username) {
      try {
        const response = await axios.get(`${usernameAvailability}?username=${username}`);
        return response.data.isAvailable;
      } catch (error) {
        console.error('Erreur lors de la vérification du nom d\'utilisateur :', error);
        return false;
      }
    },

    async matriculeAvailability(matricule) {
      try {
        const response = await axios.get(`${matriculeAvailability}?matricule=${matricule}`);
        return response.data.isAvailable;
      } catch (error) {
        console.error('Erreur lors de la vérification du matricule :', error);
        return false;
      }
    },
    formatDate(date) {
      return format(new Date(date), 'dd-MM-yyyy', { locale: fr }); // Exemple de format, ajustez selon vos besoins
    },
  },
  
})
