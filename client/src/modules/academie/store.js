// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'

const  modulesURL = '/v1/academies';
const add= modulesURL+'/';
const  all = modulesURL+'/all';
const availableAcademiesForUser= modulesURL+'/availableAcademiesForUser'
const allForSecondary= modulesURL+'/allForSecondary'
const libelleAvailability = modulesURL +'/libelle-availability';
const libelleAvailabilityUp = modulesURL +'/libelle-availabilityUp';
const codeAvailability = modulesURL +'/code-availability';

export const useAcademieStore = defineStore('academie', {
  state: () => ({
    dataListe: [],
    dataListeForUser: [],   //  List des données à afficher pour la table
    dataDetails: {},  //  Détails d'un élment,
    loading: true,
    isAvailable: false,  //  utilisé pour le chargement
    /*breadcrumbs: [
      {
        text: 'Paramétrage',
        disabled: true,
        route: 'home',
      },
      {
        text: 'Académies',
        disabled: true,
        route: 'src/modules/academie/routes.js',
      }
    ],*/
    headerTable: [
      { text: 'Libelle', value: 'libelleLong', align: 'start', sortable: true },
      { text: 'Abreviation', value: 'libelleCourt', align: 'start', sortable: true },
      { text: 'Actions', value: 'actions', sortable: false }
    ]
  }),

  getters: {
    getDataListe: (state) => state.dataListe,
    getDataListeForUser: (state) => state.dataListeForUser
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
    async allForSecondary() {
      try {
        await axios.get(`${allForSecondary}`)
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
    async availableAcademiesForUser(demande) {
      try {
        await axios.get(`${availableAcademiesForUser}?demandeId=${demande}`)
        .then((response) => {
          if(response.status === 200){
            console.log(response.data);
            this.dataListeForUser = response.data;
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
    async one(academie) {
      try {
        await axios.get(`${modulesURL}/${academie}`)
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
    async checkCodeExistence(libelleCourt) {
      try {
        const response = await axios.get(`${codeAvailability}?libelleCourt=${libelleCourt}`);
        console.log("Réponse de CodeAvailability :", response);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error('Erreur lors de la vérification du nom :', error);
        return false;
      }
    },
    async checkLibelleExistenceUp({ academieId, libelleLong }) {
      try {
        const response = await axios.get(`${libelleAvailabilityUp}?academieId=${academieId}&libelleLong=${libelleLong}`);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error("Erreur lors de la vérification du libelleLong :", error);
        return false;
      }
    },
  },

})
