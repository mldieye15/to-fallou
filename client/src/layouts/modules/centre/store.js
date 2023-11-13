// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'

const  modulesURL = '/centres';

export const useCentreStore = defineStore('centre', {
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
        route: 'src/modules/academie/routes.js',
      }
    ],*/
    headerTable: [
      { text: 'LibelleLong', value: 'libelleLong', align: 'start', sortable: true },
      { text: 'LibelleCourt', value: 'libelleCourt', align: 'start', sortable: true },
      { text: 'Ville', value: 'ville', align: 'start', sortable: true },
      { text: 'TypeCentre', value: 'typeCentre', align: 'start', sortable: true },
      { text: 'Actions', value: 'actions', sortable: false }
    ]
  }),

  getters: {
    getDataListe: (state) => state.dataListe
  },

  actions: {
    //  recupérer la liste des centres et le mettre dans la tabel dataListe
    async all() {
      try {
        await axios.get(modulesURL) 
        .then((response) => {
          if(response.status === 200){

            let res = response.data.map( (element) => ({
              id:element.id, 
              libelleLong: element.libelleLong,
              libelleCourt: element.libelleCourt,
              ville: element.ville.libelleLong,
              typeCentre: element.typeCentre.libelleLong
            }))

            this.dataListe = res;
          } 
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    //  recupérer les informations d'un centre par son ide et le mettre dans la tabel dataDetails
    async one(centre) {
      try {
        await axios.get(`${modulesURL}/${centre}`) 
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
    //  ajouter un centre
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
    //  modifier un centre
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
    //  supprimer un centre
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