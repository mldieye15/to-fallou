// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'

const  modulesURL = '/v1/candidatAuthorisers';
const add= modulesURL+'/';
const  all = modulesURL+'/all';
const  allNotAutoriser = modulesURL+'/allNotAutoriser';
const libelleAvailability = modulesURL +'/libelle-availability';
const libelleAvailabilityUp = modulesURL +'/libelle-availabilityUp';
const codeAvailability = modulesURL +'/code-availability';
const hasEnabledNonAutoriserUsers = modulesURL +'/hasEnabledNonAutoriserUsers';
const disableNonAutoriser = modulesURL +'/disableNonAutoriser';

export const useCandidatAuthoriserStore = defineStore('candidatAuthoriser', {
  state: () => ({
    dataListe: [],
    dataListeForUser: [],   //  List des données à afficher pour la table
    dataDetails: {},  //  Détails d'un élment,
    loading: true,
    isAvailable: false,  //  utilisé pour le chargement
    disable:false,
    /*breadcrumbs: [
      {
        text: 'Paramétrage',
        disabled: true,
        route: 'home',
      },
      {
        text: 'Académies',
        disabled: true,
        route: 'src/modules/candidatAuthoriser/routes.js',
      }
    ],*/
    headerTable: [
      { text: 'Prénom(s)', value: 'prenoms', align: 'start', sortable: true },
      { text: 'Nom', value: 'nom', align: 'start', sortable: true },
      { text: 'Matricule', value: 'matricule', align: 'start', sortable: true },
      { text: 'Code', value: 'code', align: 'start', sortable: true },
      { text: 'Etablissement', value: 'etablisement', align: 'start', sortable: true },
      { text: 'Téléphone', value: 'telephone', align: 'start', sortable: true },
      { text: 'Actions', value: 'actions', sortable: false }
    ],
    headerTable1: [
      { text: 'Prénom(s)', value: 'prenoms', align: 'start', sortable: true },
      { text: 'Nom', value: 'nom', align: 'start', sortable: true },
      { text: 'Matricule', value: 'matricule', align: 'start', sortable: true },
      { text: 'Code', value: 'code', align: 'start', sortable: true },
      { text: 'Etablissement', value: 'etablissement', align: 'start', sortable: true },
      { text: 'Téléphone', value: 'telephone', align: 'start', sortable: true },
      { text: 'Etat du compte', value: 'etat', sortable: false },
      { text: 'Actions', value: 'actions', sortable: false }
    ]
  }),

  getters: {
    getDataListe: (state) => state.dataListe,
    getDataListeForUser: (state) => state.dataListeForUser
  },

  actions: {
    //  recupérer la liste des académies et le mettre dans la tabel dataListe
    async hasEnabledNonAutoriserUsers() {
      try {
        await axios.get(`${hasEnabledNonAutoriserUsers}`)
        .then((response) => {
          if(response.status === 200){
            console.log(response.data);
            this.disable = response.data;
            console.log('response',this.disable);
          }
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
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
    async allNotAutoriser() {
      try {
        await axios.get(`${allNotAutoriser}`)
        .then((response) => {
          if(response.status === 200){
            let res = response.data.map( (element) => {
              let etatLabel = element.enabled? 'activé':'désactivé';
              let etablissementLabel = element.etablissement? element.etablissement.libelleLong:null;
              return{
              id:element.id,
              prenoms: element.prenoms,
              nom: element.nom,
              matricule: element.matricule,
              code: element.code,
              telephone: element.telephone,
              etablissement: etablissementLabel,
              etat:etatLabel
              }

            })

            this.dataListe = res
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
    async one(candidatAuthoriser) {
      try {
        await axios.get(`${modulesURL}/${candidatAuthoriser}`)
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
    async checkEnabledNonAutoriserUsers() {
      this.loading = true;
      try {
        const response = await axios.get(`${modulesURL}/${hasEnabledNonAutoriserUsers}`);
        this.hasEnabledNonAutoriser = response.status === 200 && response.data === true;
      } catch (error) {
        console.error("Erreur lors de la vérification :", error);
        this.hasEnabledNonAutoriser = false;
      } finally {
        this.loading = false;
      }
    },
    async disableNonAutoriserUsers() {
      this.loading = true;
      try {
        const response = await axios.put(`${disableNonAutoriser}`);
        if (response.status === 200) {
          console.log("Utilisateurs désactivés avec succès !");
          await this.hasEnabledNonAutoriserUsers();
          this.allNotAutoriser(); // Rafraîchir l'état après la mise à jour
        }
      } catch (error) {
        console.error("Erreur lors de la désactivation :", error);
      } finally {
        this.loading = false;
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
    async checkLibelleExistenceUp({ candidatAuthoriserId, libelleLong }) {
      try {
        const response = await axios.get(`${libelleAvailabilityUp}?candidatAuthoriserId=${candidatAuthoriserId}&libelleLong=${libelleLong}`);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error("Erreur lors de la vérification du libelleLong :", error);
        return false;
      }
    },
    async autorisation(id) {
      try {
        const response = await axios.put(`${modulesURL}/${id}/autorisation`);
        if (response.status === 200) {
          this.hasEnabledNonAutoriserUsers();
          // Mettre à jour la liste après le basculement d'état
          this.allNotAutoriser();
        }
      } catch (error) {
        console.error(error);
        this.error = error;
      } finally {
        this.loading = false;
      }
    },
  },

})
