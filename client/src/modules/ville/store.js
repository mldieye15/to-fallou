// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'

const  modulesURL = '/v1/villes';
const  all = modulesURL+'/all';
const add=modulesURL+'/';
const villesByAcademie=modulesURL+'/by-academie'
const availableVillesForUserAndAcademy= modulesURL+'/availableVillesForUserAndAcademy'
const libelleAvailability = modulesURL +'/libelle-availability';
const libelleAvailabilityUp = modulesURL +'/libelle-availabilityUp';
const codeAvailability = modulesURL +'/code-availability';
export const useVilleStore = defineStore('ville', {
  state: () => ({
    dataListeVille: [],
    dataListeByAcademie:[], 
    dataListeByAcademieAndUser:[], //  List des données à afficher pour la table
    dataDetails: {},  //  Détails d'un élment,
    loading: true,  //  utilisé pour le chargement
    headerTable: [
      { text: 'LibelleLong', value: 'libelleLong', align: 'start', sortable: true },
      { text: 'Abreviation', value: 'libelleCourt', align: 'start', sortable: true },
      { text: 'Nombre de Jurys', value: 'totalJury', align: 'start', sortable: true },
      { text: 'Academie', value: 'academie', align: 'start', sortable: true },
      { text: 'Actions', value: 'actions', sortable: false }
    ],
    columns: [
      { label: 'LibelleLong', field: 'libelleLong'},
      { label: 'Abreviation', field: 'libelleCourt' },
      { label: 'Academie', field: 'academie'},
      { label: 'Nombre de Jurys', field: 'totalJury'},
      { label: 'Rapport', field: 'rapport'},
      // { label: 'Quota', field: 'quota'},
      { label: 'Planification', field: 'totalDemandes'},
    ],
  }),

  getters: {
    getDataListeVille: (state) => state.dataListeVille,
    getDataListeByAcademie: (state) => state.dataListeByAcademie,
    getDataListeByAcademieAndUser: (state) => state.dataListeByAcademieAndUser,
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
            totalJury: element.totalJury,
            totalDemandes: element.totalDemandes,
            quota: element.quota,
            rapport: element.rapportJuryDemande,
            academie: academieLabel,
            academieId:academieIdLabel,

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
    async availableVillesForUserAndAcademy(academieId) {
      console.log("Valeur de academie reçue:", academieId);
      try {
        // Vérifiez que academieId est un nombre valide
        const response = await axios.get(`${availableVillesForUserAndAcademy}/${academieId}`);
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
            this.dataListeByAcademieAndUser = res;
            console.log("Villes récupérées pour l'académie", academieId, ":", this.dataListeByAcademieAndUser);
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
    async checkLibelleExistenceUp({ villeId, libelleLong }) {
      try {
        const response = await axios.get(`${libelleAvailabilityUp}?villeId=${villeId}&libelleLong=${libelleLong}`);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error("Erreur lors de la vérification du libelleLong :", error);
        return false;
      }
    },
  },
  
})
