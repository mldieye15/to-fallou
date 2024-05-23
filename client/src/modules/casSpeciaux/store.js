// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'

const  modulesURL = '/v1/demandes';
const all = modulesURL+'/allValider';
const accepter=modulesURL+'/accepter';
const annuler=modulesURL+'/annuler';
const nonAffectable=modulesURL+'/nonAffectable';
const hasAcceptedDemande=modulesURL+'/hasAcceptedDemande';
const quotaAccepte=modulesURL+'/quotaAccepte';

export const useCasStore = defineStore('cas', {
  state: () => ({
    dataListe: [],
    dataListeGroupedByUser: [],
    dataListeForUser: [],  //  List des données à afficher pour la table
    dataDetails: {},  //  Détails d'un élment,
    loading: true,
    hasAcceptedDemande: false,  //  utilisé pour le chargement
    error: null,
    etatCouleurs: {
      'acceptée': 'orange',
      'en attente': 'purple',
      'rejetée': 'red',
      'validée': 'green',
      'obsolète':'amber',
      'déclinée':'blue-grey'
      // Ajoutez d'autres états et couleurs selon vos besoins
},
    columns: [
      { label: 'Session', field: 'session',sortable: true },
      // { label: 'Encours', field: 'encours',width: "auto",sortable: true },
      { label: 'Academie', field: 'academie',sortable: true },
      { label: 'Ville', field: 'ville',sortable: true},
      { label: "Centre d'écrit", field: "centre",sortable: true},
      // { label: 'Affectable', field: 'affectable',width: "100px",sortable: true},
      { label: 'Score', field: 'note',sortable: true },
      { label: 'Statut', field: 'etatDemande',sortable: true},
      // { label: "Classement", field: 'ordreArrivee',sortable: true},
      // { label: 'Rang', field: 'rang',width: "120px",sortable: true},
      { label: 'Actions', field: 'actions',sortable: true }
      // Ajoutez d'autres colonnes selon vos besoins
    ],
    headerTable: [
      { text: 'Nom', value: 'nom', align: 'start', sortable: true },
      { text: 'Prenoms', value: 'prenoms', align: 'start', sortable: true },
      { text: 'Ville', value: 'ville', align: 'start', sortable: true },
      { text: "Centre d'écrit", value: "centre", align: 'start', sortable: true },
      { text: 'Ville', value: 'ville', align: 'start', sortable: true },
      { text: 'Statut', value: 'etatDemande', align: 'start', sortable: true },
      { text: 'Actions', value: 'actions', sortable: false }
    ]
  }),

  getters: {
    getDataListe: (state) => state.dataListe,
    getDataListeGroupedByUser: (state) => state.dataListeGroupedByUser,
    getDataListeForUser: (state) => state.dataListeForUser,
    getEtatCouleurs: (state) => state.etatCouleurs,
    getHasAcceptedDemande: (state) => state.hasAcceptedDemande,
    getError: (state) => state.error
  },

  actions: {
    //  recupérer la liste des demandes et le mettre dans la tabel dataListe
    async all() {
      try {
        const response = await axios.get(`${all}`);
        if (response.status === 200) {
          let res = await Promise.all(response.data.map(async (element) => {
            let villeLabel = element.ville ? element.ville.libelleLong : null;
            let academieLabel = element.ville && element.ville.academie ? element.ville.academie.libelleLong : null;
            let sessionLabel = element.session ? element.session.libelleLong : null;
            let etatLabel = element.etatDemande ? element.etatDemande.libelleLong : null;
            let prenomLabel = element.user ? element.user.prenoms : null;
            let nomLabel = element.user ? element.user.nom : null;
            let idLabel = element.user ? element.user.id : null;
            let idLabelVille = element.ville ? element.ville.id : null;
            let centreLabel = element.centre ? element.centre.libelleLong : null;
            let hasAccepted = await this.hasAcceptedDemande(idLabel)? 'OUI' : 'NON';
            let quotaAccept = await this.quotaAccepteVille(idLabelVille)? 'OUI' : 'NON';
    
            return {
              id: element.id,
              ville: villeLabel,
              academie: academieLabel,
              session: sessionLabel,
              etatDemande: etatLabel,
              nom: nomLabel,
              centre: centreLabel,
              prenoms: prenomLabel,
              villeId:idLabelVille,
              hasAcceptedDemande: hasAccepted,
              quota: quotaAccept,
            };
          }));
    
          this.dataListe = res;
        }
      } catch (error) {
        console.log(error);
        this.error = error;
      } finally {
        this.loading = false;
      }
    },   
    async one(demande) {
      try {
        this.error = null;
        await axios.get(`${modulesURL}/${demande}`) 
        .then((response) => {
          if(response.status === 200){
            this.dataDetails = response.data;
          } 
        })
      } catch (error) {
        console.log(error);
        this.error = error
        throw error;
      } finally {
        this.loading = false
      }
    },

    async accepterDemande(id, payload) {
      try {
        this.error = null;
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
        throw error;
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
    async hasAcceptedDemande(userId) {
      try {
        const response = await axios.get(`${hasAcceptedDemande}?userId=${userId}`);
        if (response.status === 200) {
          console.log(response.data);
          return response.data === true; // Retourne true si la réponse est true, sinon retourne false
        }
      } catch (error) {
        console.error(error);
        return false; // En cas d'erreur, retourne false
      } finally {
        this.loading = false;
      }
    },
    async quotaAccepteVille(villeId) {
      try {
        const response = await axios.get(`${quotaAccepte}?villeId=${villeId}`);
        if (response.status === 200) {
          console.log(response.data);
          return response.data === true; // Retourne true si la réponse est true, sinon retourne false
        }
      } catch (error) {
        console.error(error);
        return false; // En cas d'erreur, retourne false
      } finally {
        this.loading = false;
      }
    }, 
    async annuler(id) {
      try {
        await axios.put(`${annuler}/${id}`)
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
    async rejeter(id) {
      try {
        await axios.put(`${nonAffectable}/${id}`)
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
  },
  
})
