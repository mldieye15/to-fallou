// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'

const  modulesURL = '/v1/villes';
const  all = modulesURL+'/all';
const add=modulesURL+'/';
const villesByAcademie=modulesURL+'/by-academie'

export const useVilleStore = defineStore('ville', {
  state: () => ({
    dataListeVille: [],
    dataListeByAcademie:[],  //  List des données à afficher pour la table
    dataDetails: {},  //  Détails d'un élment,
    loading: true,  //  utilisé pour le chargement
    headerTable: [
      { text: 'LibelleLong', value: 'libelleLong', align: 'start', sortable: true },
      { text: 'Abreviation', value: 'libelleCourt', align: 'start', sortable: true },
      { text: 'Academie', value: 'academie', align: 'start', sortable: true },
      { text: 'AcademieID', value: 'academieId', align: 'start', sortable: true },
      { text: 'Actions', value: 'actions', sortable: false }
    ]
  }),

  getters: {
    getDataListeVille: (state) => state.dataListeVille,
    getDataListeByAcademie: (state) => state.dataListeByAcademie,
  },

  actions: {
    //  recupérer la liste des villes et le mettre dans la tabel dataListeVille
    async all() {
      try {
        await axios.get(`${all}`)
        .then((response) => {
          if(response.status === 200){
           let res = response.data.map((element)=>{
            let academieLabel=element.academie?element.academie.libelleLong:null;
            let academieIdLabel = element.academie?element.academie.id:null;
            return{
              id: element.id,
            libelleLong: element.libelleLong,
            libelleCourt: element.libelleCourt,
            academie: academieLabel,
            academieId:academieIdLabel
            };
            
           });
           this.dataListeVille=res;
          } 
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    //  recupérer la liste des villes par academie et le mettre dans la tabel dataListeByAcademie
    async villesByAcademie(academie) {
      try {
        await axios.get(`${villesByAcademie}/${academie}`)
        .then((response) => {
          if(response.status === 200){
           let res = response.data.map((element)=>{
            let academieLabel=element.academie?element.academie.libelleLong:null;
            return{
              id: element.id,
            libelleLong: element.libelleLong,
            libelleCourt: element.libelleCourt,
            academie: academieLabel
            };
            
           });
           this.dataListeByAcademie=res;
           console.log("Villes récupérées pour l'académie", academie, ":", this.dataListeByAcademie);
          } 
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    //  recupérer les informations d'une ville par son ide et le mettre dans la tabel dataDetails
    async one(ville) {
      try {
        await axios.get(`${modulesURL}/${ville}`) 
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
    }
  },
  
})
