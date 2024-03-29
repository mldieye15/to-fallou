// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'

const  modulesURL = '/v1/codifications';
const add= modulesURL+'/';
const  all = modulesURL+'/all';
const emailAvailability = modulesURL +'/email-availability';
const codeAvailability = modulesURL +'/code-availability';
const emailAvailabilityUp = modulesURL +'/email-availabilityUp';
const codeAvailabilityUp = modulesURL +'/code-availabilityUp';
export const useCodificationStore = defineStore('codification', {
  state: () => ({
    dataListe: [],  //  List des données à afficher pour la table
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
        route: 'src/modules/codification/routes.js',
      }
    ],*/
    headerTable: [
      { text: 'Adresse email', value: 'email', align: 'start', sortable: true },
      { text: 'Code', value: 'code', align: 'start', sortable: true },
      { text: 'Actions', value: 'actions', sortable: false }
    ]
  }),

  getters: {
    getDataListe: (state) => state.dataListe
  },

  actions: {
    //  recupérer la liste des académies et le mettre dans la tabel dataListe
    async all() {
      try {
        await axios.get(`${all}`)
        .then((response) => {
          if(response.status === 200){
            console.log(response.data);
            this.dataListe = response.data;
          }
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    //  recupérer les informations d'une académie par son ide et le mettre dans la tabel dataDetails
    async one(codification) {
      try {
        await axios.get(`${modulesURL}/${codification}`)
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
    //  ajouter une academéie
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
    //  modifier une academéie
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
    //  modifier une academéie
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
        console.error('Erreur lors de la vérification du nom :', error);
        return false;
      }
    },
    async checkEmailExistenceUp({ codificationId, email }) {
      try {
        const response = await axios.get(`${emailAvailabilityUp}?codificationId=${codificationId}&email=${email}`);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error("Erreur lors de la vérification du libelleLong :", error);
        return false;
      }
    },
    async checkCodeExistence(code) {
      try {
        const response = await axios.get(`${codeAvailability}?code=${code}`);
        console.log("Réponse de codeAvailability :", response);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error('Erreur lors de la vérification de l\' email :', error);
        return false;
      }
    },
    async checkCodeExistenceUp({ codificationId, code }) {
      try {
        const response = await axios.get(`${codeAvailabilityUp}?codificationId=${codificationId}&code=${code}`);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error("Erreur lors de la vérification du code :", error);
        return false;
      }
    },
  },

})
