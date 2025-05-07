// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'

const  modulesURL = '/v1/jurys';
const all= modulesURL+'/all';
const allBySession= modulesURL+'/allBySession';
const add = modulesURL+'/';
const juryByCentre = modulesURL+'/by-centre';
const numeroAvailability=modulesURL+'/numero-availability';
const numeroAvailabilityUp=modulesURL+'/numero-availabilityUp';
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
      { text: 'Technique', value: 'technique', align: 'start', sortable: true },
      { text: 'Session', value: 'session', align: 'start', sortable: true },
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
              let sessionLabel = element.session ? element.session.libelleLong : null;
              let techniqueLabel =element.technique? 'OUI' :'NON';
              let academieLabel =element.centre&& element.centre.ville.academie ? element.centre.ville.academie.libelleLong : null;
              return {
                id:element.id,
                numero: element.numero,
                nom:element.nom,
                centre: centreLabel,
                ville: villeLabel,
                academie: academieLabel,
                technique:techniqueLabel,
                session: sessionLabel
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
    async allBySession() {
      try {
        await axios.get(`${allBySession}`)
        .then((response) => {
          if(response.status === 200){

            let res = response.data.map( (element) => {
              let centreLabel = element.centre ? element.centre.libelleLong : null;
              let sessionLabel = element.session ? element.session.libelleLong : null;
              let villeLabel =element.centre&& element.centre.ville ? element.centre.ville.libelleLong :null;
              let techniqueLabel =element.technique? 'OUI' :'NON';
              let academieLabel =element.centre&& element.centre.ville.academie ? element.centre.ville.academie.libelleLong : null;
              return {
                id:element.id,
                numero: element.numero,
                nom:element.nom,
                centre: centreLabel,
                ville: villeLabel,
                academie: academieLabel,
                technique:techniqueLabel,
                session: sessionLabel
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
    async jurysBycentre(centre) {
      try {
        // Ajouter une vérification pour s'assurer que centre est un nombre non nul
        if (typeof centre === 'number' && !isNaN(centre)) {
          await axios.get(`${juryByCentre}/${centre}`)
            .then((response) => {
              if (response.status === 200) {
                let res = response.data.map((element) => {
                  let centreLabel = element.centre ? element.centre.libelleLong : null;
                  return {
                    id: element.id,
                    nom: element.nom,
                    numero: element.numero,
                    centre: centreLabel
                  };
                });
                this.dataListeJury= res;
              }
            });
        } else {
          console.error("La valeur de jury est invalide. La requête n'a pas été effectuée.");
        }
      } catch (error) {
        console.log(error);
        this.error = error;
      } finally {
        this.loading = false;
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
        console.log("ID à supprimer :", id)
        .then((response) => {
          if(response.status === 200 ){
            this.dataDetails = response.data;
          }
          this.allBySession()
        })
      } catch (error) {
        console.log(error);
        this.error = error
      } finally {
        this.loading = false
      }
    },
    async checkNomExistence(nom) {
      try {
        const response = await axios.get(`${nomAvailability}?nom=${nom}`);
        console.log("Réponse de juryAvailability :", response);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error("Erreur lors de la vérification du jury :", error);
        return false;
      }
    },
    async checkJuryExistence(nom) {
      try {
        const response = await axios.get(`${nomAvailability}?nom=${nom}`);
        console.log("Réponse de libelleAvailability :", response);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error('Erreur lors de la vérification du nom :', error);
        return false;
      }
    },
    async checkNomExistenceUp({ juryId, numero }) {
      try {
        const response = await axios.get(`${nomAvailabilityUp}?juryId=${juryId}&numero=${numero}`);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error("Erreur lors de la vérification du numero :", error);
        return false;
      }
    },
    async checkNumeroExistence(numero) {
      try {
        const response = await axios.get(`${numeroAvailability}?numero=${numero}`);
        console.log("Réponse de libelleAvailability :", response);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error('Erreur lors de la vérification du numero :', error);
        return false;
      }
    },
    async checkNumeroExistenceUp({ juryId, numero }) {
      try {
        const response = await axios.get(`${numeroAvailabilityUp}?juryId=${juryId}&numero=${numero}`);
        response.data=response.data.isAvailable;
        return true;
      } catch (error) {
        console.error("Erreur lors de la vérification du libelleLong :", error);
        return false;
      }
    },
  },

})
