// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'

const  modulesURL = '/v1/typeSessions';
const add= modulesURL+'/';
const  all = modulesURL+'/all';
const libelleAvailability = modulesURL +'/libelle-availability';
const libelleAvailabilityUp = modulesURL +'/libelle-availabilityUp';

export const useTypeSessionStore = defineStore('typeSession', {
  state: () => ({
    dataListeTypeSession: [],  //  List des données à afficher pour la table
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
        route: 'src/modules/typeSession/routes.js',
      }
    ],*/
    headerTable: [
      { text: 'Libelle', value: 'libelleLong', align: 'start', sortable: true },
      { text: 'Actions', value: 'actions', sortable: false }
    ]
  }),

  getters: {
    getDataListeTypeSession: (state) => state.dataListeTypeSession
  },

  actions: {
    //  recupérer la liste des académies et le mettre dans la tabel dataListe
    async all() {
      try {
        await axios.get(`${all}`)
        .then((response) => {
          if(response.status === 200){
            console.log(response.data);
            this.dataListeTypeSession = response.data;
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
    async one(typeSession) {
      try {
        await axios.get(`${modulesURL}/${typeSession}`)
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
    getLibelleById(id) {
      const typeSession = this.dataListeTypeSession.find(item => item.id === id);
      return typeSession ? typeSession.libelleLong : '';
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
    async checkLibelleExistence(libelleLong) {
      try {
        const response = await axios.get(`${libelleAvailability}?libelleLong=${libelleLong}`);
        console.log("Réponse de libelleAvailability :", response);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error('Erreur lors de la vérification du nom :', error);
        return false;
      }
    },
    async checkLibelleExistenceUp({ typeId, libelleLong }) {
      try {
        const response = await axios.get(`${libelleAvailabilityUp}?typeId=${typeId}&libelleLong=${libelleLong}`);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error("Erreur lors de la vérification du libelleLong :", error);
        return false;
      }
    },
  },

})
