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
      { text: 'Nombre de Jury', value: 'totalJury', align: 'start', sortable: true },
      { text: 'Jury Affecte', value: 'totalJuryAffecte', align: 'start', sortable: true },
      { text: 'Quota atteint', value: 'quota', align: 'start', sortable: true },
      { text: 'Nbr demande Accepter ', value: 'totalDemandeAccepte', align: 'start', sortable: true },
      { text: 'Quota accpepte', value: 'quotaDemandeAccepte', align: 'start', sortable: true },
      { text: 'Academie', value: 'academie', align: 'start', sortable: true },
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
            let quotaLabel = element.quota ? 'OUI' : 'NON';
            let quotaAccepte = element.quotaDemandeAccepte ? 'OUI' : 'NON';
            return{
              id: element.id,
            libelleLong: element.libelleLong,
            libelleCourt: element.libelleCourt,
            totalJury: element.totalJury,
            totalJuryAffecte: element.totalJuryAffecte,
            totalDemandeAccepte: element.totalDemandeAccepte,
            academie: academieLabel,
            academieId:academieIdLabel,
            quota: quotaLabel,
            quotaDemandeAccepte:quotaAccepte,

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
        // Ajouter une vérification pour s'assurer que academie est un nombre non nul
        if (typeof academie === 'number' && !isNaN(academie)) {
          await axios.get(`${villesByAcademie}/${academie}`)
            .then((response) => {
              if (response.status === 200) {
                let res = response.data.map((element) => {
                  let academieLabel = element.academie ? element.academie.libelleLong : null;
                  return {
                    id: element.id,
                    libelleLong: element.libelleLong,
                    libelleCourt: element.libelleCourt,
                    academie: academieLabel
                  };
                });
                this.dataListeByAcademie = res;
                console.log("Villes récupérées pour l'académie", academie, ":", this.dataListeByAcademie);
              }
            });
        } else {
          console.error("La valeur de academie est invalide. La requête n'a pas été effectuée.");
        }
      } catch (error) {
        console.log(error);
        this.error = error;
      } finally {
        this.loading = false;
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
