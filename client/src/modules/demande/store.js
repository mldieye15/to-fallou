// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'

const  modulesURL = '/v1/demandes';
const all = modulesURL+'/all';
const add = modulesURL+'/addAll';
const accepter=modulesURL+'/accepter';

export const useDemandeStore = defineStore('demande', {
  state: () => ({
    dataListe: [],  //  List des données à afficher pour la table
    dataDetails: {},  //  Détails d'un élment,
    loading: true,  //  utilisé pour le chargement
    etatCouleurs: {
      'ACCEPTE': 'orange',
      'EN ATTENTE': 'grey',
      'REJETE': 'red',
      'VALIDE': 'green',
      'OBSELETE':'yellow',
      // Ajoutez d'autres états et couleurs selon vos besoins
},
    headerTable: [
      { text: 'Prenoms', value: 'user', align: 'start', sortable: true },
      { text: 'Ville', value: 'ville', align: 'start', sortable: true },
      { text: 'Academie', value: 'academie', align: 'start', sortable: true },
      { text: 'Session', value: 'session', align: 'start', sortable: true },
      { text: 'Centre d/ecrit', value: 'centre', align: 'start', sortable: true },
      { text: 'Statut', value: 'etatDemande', align: 'start', sortable: true },
      { text: 'Actions', value: 'actions', sortable: false }
    ]
  }),

  getters: {
    getDataListe: (state) => state.dataListe,
    getEtatCouleurs: (state) => state.etatCouleurs,
  },

  actions: {
    //  recupérer la liste des demandes et le mettre dans la tabel dataListe
    async all() {
      try {
        await axios.get(`${all}`) 
        .then((response) => {
          if(response.status === 200){

            let res = response.data.map( (element) => {
              let villeLabel = element.ville? element.ville.libelleLong:null;
              let academieLabel =element.ville && element.ville.academie? element.ville.academie.libelleLong:null;
              let sessionLabel = element.session ? element.session.libelleLong:null;
              let etatLabel = element.etatDemande ? element.etatDemande.libelleLong:null;
              let nomLabel = element.user ? element.user.prenoms : null;
              let centreLabel=element.centre?element.centre.libelleLong:null;
              return{
                id:element.id, 
                nom: element.nom,
                ville: villeLabel,
                academie:academieLabel,
                session:sessionLabel,
                etatDemande:etatLabel,
                user:nomLabel,
                centre:centreLabel,
              }
              
            })

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
    //  recupérer les informations d'une demande par son ide et le mettre dans la tabel dataDetails
    async one(demande) {
      try {
        await axios.get(`${modulesURL}/${demande}`) 
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
    //  ajouter une demande
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
    //  modifier une demande
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
    async accepterDemande(id, payload) {
      try {
        console.log("Id: ", id);
        console.log("Payload: ", payload);
        await axios.put(`${accepter}/${id}`, payload)
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
    //  supprimer une demande
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
    getColorForEtat(etat) {
      console.log(`État demandé : ${etat}`);
      const couleur = this.etatCouleurs[etat] || 'grey';
      console.log(`Couleur choisie : ${couleur}`);
      return couleur;
    },
  },
  
})
