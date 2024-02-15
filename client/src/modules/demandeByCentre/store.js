// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'
import { id } from 'date-fns/locale';

const  modulesURL = '/v1/demandes';
const demandesByCentre = modulesURL+'/demandeByCentre';
const affecter=modulesURL+'/affecter';
const valider=modulesURL+'/valider';
const hasAcceptedDemande=modulesURL+'/hasAcceptedDemande';
const quotaAccepte=modulesURL+'/quotaAccepte';

export const useDemandeByCentreStore = defineStore('demandeByCentre', {
  state: () => ({
    dataListe: [],  //  List des données à afficher pour la table
    dataDetails: {},  //  Détails d'un élment,
    loading: true,
    hasAcceptedDemande: false,  //  utilisé pour le chargement
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
      { label: 'Nom et Prenoms', field: 'user',width: "200px",resizable: true},
      { label: 'Session', field: 'session',width: "200px",resizable: true },
      { label: 'Academie', field: 'academie',width: "200px",resizable: true },
      { label: "Centre d'ecrit", field: 'centre',width: "200px" ,resizable: true},
      { label: 'Jury', field: 'jury',width: "100px",resizable: true},
      { label: 'Score', field: 'note',width: "100px",resizable: true },
      { label: 'Statut', field: 'etatDemande',width: "200px",resizable: true},
      { label: "Ordre d'Arrivee", field: 'ordreArrivee',width: "140px",resizable: true},
      // { label: 'Rang', field: 'rang',width: "120px",resizable: true},
      { label: 'Actions', field: 'actions',width: "100px",resizable: true }
      // Ajoutez d'autres colonnes selon vos besoins
    ],
  }),

  getters: {
    getDataListe: (state) => state.dataListe,
    getEtatCouleurs: (state) => state.etatCouleurs,
    getHasAcceptedDemande: (state) => state.hasAcceptedDemande,
  },

  actions: {
    //  recupérer la liste des demandes et le mettre dans la tabel dataListe 
    async demandeByCentre(CentreId) {
      try {
        const response = await axios.get(`${demandesByCentre}/${CentreId}`);
        if (response.status === 200) {
          let res = response.data;
          let formattedData = [];
    
          formattedData = await Promise.all(res.map(async (element) => {
            let villeLabel = element.ville ? element.ville.libelleLong : null;
            let academieLabel = element.ville && element.ville.academie ? element.ville.academie.libelleLong : null;
            let sessionLabel = element.session ? element.session.libelleLong : null;
            let etatLabel = element.etatDemande ? element.etatDemande.libelleLong : null;
            let nomLabel = element.user ? element.user.prenoms : null;
            let idLabel = element.user ? element.user.id : null;
            let idLabelVille = element.ville ? element.ville.id : null;
            let idLabelJury = element.jury ? element.jury.id : null;
            let affectableLabel = element.affectable ? 'OUI' : 'NON';
            let centreLabel = element.centre ? element.centre.libelleLong : null;
            let juryLabel = element.jury ? element.jury.numero : null;
            let hasAccepted = await this.hasAcceptedDemande(idLabel) ? 'OUI' : 'NON';
            let quotaAccept = await this.quotaAccepteVille(idLabelVille) ? 'OUI' : 'NON';
    
            return {
              id: element.demandeId,
              nom: element.nom,
              note: element.note,
              ordreArrivee: element.ordreArrivee,
              rang: element.rang,
              affectable: affectableLabel,
              juryId:idLabelJury,
              ville: villeLabel,
              academie: academieLabel,
              jury:juryLabel,
              session: sessionLabel,
              etatDemande: etatLabel,
              user: nomLabel,
              centre: centreLabel,
              userId: idLabel,
              villeId: idLabelVille,
              hasAcceptedDemande: hasAccepted,
              quota: quotaAccept,
            };
          }));
    
          this.dataListe = formattedData;
          console.log("Données filtrées par ville", formattedData);
        }
      } catch (error) {
        console.error(error);
        this.error = error;
      } finally {
        this.loading = false;
      }
    }, 
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
    async affecterJury(id, payload) {
      try {
        console.log("Id: ", id);
        console.log("Payload: ", payload);
        await axios.put(`${affecter}/${id}`, payload)
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
    async validerDemande(id) {
      try {
        await axios.put(`${valider}/${id}`)
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
  },
  
})
