// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'

const  modulesURL = '/v1/jurys';
const all= modulesURL+'/all';
const add = modulesURL+'/';

export const useJuryStore = defineStore('jury', {
  state: () => ({
    dataListeJury: [],  //  List des données à afficher pour la table
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
      { text: 'Nom', value: 'nom', align: 'start', sortable: true },
      { text: 'Numero', value: 'numero', align: 'start', sortable: true },
      { text: 'Centre', value: 'centre', align: 'start', sortable: true },
      { text: 'Ville', value: 'ville', align: 'start', sortable: true },
      { text: 'Academie', value: 'academie', align: 'start', sortable: true },
      { text: 'Actions', value: 'actions', sortable: false }
    ]
  }),

  getters: {
    getDataListeJury: (state) => state.dataListeJury
  },

  actions: {
    //  recupérer la liste des jurys et le mettre dans la tabel dataListe
    async all() {
      try {
        await axios.get(`${all}`) 
        .then((response) => {
          if(response.status === 200){

            let res = response.data.map( (element) => {
              let centreLabel = element.centre ? element.centre.libelleLong : null;
              let villeLabel =element.centre&& element.centre.ville ? element.centre.ville.libelleLong :null;
              let academieLabel =element.centre&& element.centre.ville.academie ? element.centre.ville.academie.libelleLong : null;
              return {
                id:element.id, 
                numero: element.numero,
                nom:element.nom,
                centre: centreLabel,
                ville: villeLabel,
                academie: academieLabel
              };
             
            });

            this.dataListeJury = res;
          } 
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    //  recupérer les informations d'un jury par son ide et le mettre dans la tabel dataDetails
    async one(jury) {
      try {
        await axios.get(`${modulesURL}/${jury}`) 
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
    //  ajouter un jury
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
    //  modifier un jury
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
    //  supprimer un jury
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
