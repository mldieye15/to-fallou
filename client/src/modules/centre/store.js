// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'

const  modulesURL = '/v1/centres';
const all = modulesURL+'/all';
const add = modulesURL+'/';
const centreByville = modulesURL+'/by-ville';
const libelleAvailability = modulesURL +'/libelle-availability';

export const useCentreStore = defineStore('centre', {
  state: () => ({
    dataListeCentre: [],
    dataListeByVille: [],  //  List des données à afficher pour la table
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
      { text: 'nombre de Jury', value: 'nombreJury', align: 'start', sortable: true },
      { text: 'TypeCentre', value: 'typeCentre', align: 'start', sortable: true },
      { text: 'Ville', value: 'ville', align: 'start', sortable: true },
      { text: 'Academie', value: 'academie', align: 'start', sortable: true },
      { text: 'Actions', value: 'actions', sortable: false }
    ]
  }),

  getters: {
    getDataListeCentre: (state) => state.dataListeCentre,
    getDataListeByVille: (state) => state.dataListeByVille
  },

  actions: {
    //  recupérer la liste des centres et le mettre dans la tabel dataListe
    async all() {
      try {
        await axios.get(`${all}`) 
        .then((response) => {
          if(response.status === 200){

            let res = response.data.map( (element) => {
              let villeLabel = element.ville? element.ville.libelleLong:null;
              let academieLabel =element.ville && element.ville.academie? element.ville.academie.libelleLong:null;
              let typeCentreLabel = element.typeCentre ? element.typeCentre.libelleLong:null;
              return{
              id:element.id, 
              libelleLong: element.libelleLong,
              libelleCourt: element.libelleCourt,
              nombreJury: element.nombreJury,
              ville: villeLabel,
              typeCentre: typeCentreLabel,
              academie: academieLabel,
              }
              

            })

            this.dataListeCentre = res;
          } 
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    async centresByVille(ville) {
      try {
        // Ajouter une vérification pour s'assurer que ville est un nombre non nul
        if (typeof ville === 'number' && !isNaN(ville)) {
          await axios.get(`${centreByville}/${ville}`)
            .then((response) => {
              if (response.status === 200) {
                let res = response.data.map((element) => {
                  let villeLabel = element.ville ? element.ville.libelleLong : null;
                  return {
                    id: element.id,
                    libelleLong: element.libelleLong,
                    libelleCourt: element.libelleCourt,
                    ville: villeLabel
                  };
                });
                this.dataListeByVille = res;
              }
            });
        } else {
          console.error("La valeur de centre est invalide. La requête n'a pas été effectuée.");
        }
      } catch (error) {
        console.log(error);
        this.error = error;
      } finally {
        this.loading = false;
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
  },
  
})
