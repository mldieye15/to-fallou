// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'

const  modulesURL = '/typeCentres';

export const useTypeCentreStore = defineStore('typeCentre', {
  state: () => ({
    dataListeTypeCentre: [],  //  List des données à afficher pour la table
    dataDetails: {},  //  Détails d'un élment,
    loading: true,  //  utilisé pour le chargement
    /*breadcrumbs: [
      {
        text: 'Paramétrage',
        disabled: true,
        route: 'home',
      },
      {
        text: 'TypeCentres',
        disabled: true,
        route: 'src/modules/typeCentre/routes.js',
      }
    ],*/
    headerTable: [
      { text: 'LibelleLong', value: 'libelleLong', align: 'start', sortable: true },
      { text: 'LibelleCourt', value: 'libelleCourt', align: 'start', sortable: true },
      { text: 'Actions', value: 'actions', sortable: false }
    ]
  }),

  getters: {
    getDataListe: (state) => state.dataListeTypeCentre
  },

  actions: {
    //  recupérer la liste des types de centre et le mettre dans la tabel dataListe
    async all() {
      try {
        await axios.get(modulesURL) 
        .then((response) => {
          if(response.status === 200){
            this.dataListeTypeCentre = response.data;
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
    async one(typeCentre) {
      try {
        await axios.get(`${modulesURL}/${typeCentre}`) 
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
    //  ajouter un type de centre
    async add(payload) {
      try {
        await axios.post(modulesURL, payload) 
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
    //  modifier un type de centre
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
    //  supprimer un type de centre
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
    }
  },
  
})
