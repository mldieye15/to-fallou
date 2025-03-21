// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'

const  modulesURL = '/v1/demandes';
// const centreForProposition = modulesURL+'';
const demandesByVille = modulesURL+'/demandeByVille';
const accepter=modulesURL+'/accepter';
const valider=modulesURL+'/valider';
const hasAcceptedDemande=modulesURL+'/hasAcceptedDemande';
const hasPropositionDemande=modulesURL+'/hasPropositionDemande';
const quotaAccepte=modulesURL+'/quotaAccepte';
const quotaProposition=modulesURL+'/quotaProposition';
const nonAffectable=modulesURL+'/nonAffectable';

export const useDemandeByVilleStore = defineStore('demandeByVille', {
  state: () => ({
    dataListe: [],  //  List des données à afficher pour la table
    dataDetails: {},  //  Détails d'un élment,
    dataListeByVille: [],
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
      { label: 'Prenoms', field: 'prenoms'},
      { label: 'Nom', field: 'nom'},
      { label: 'Code', field: 'code'},
      // { label: 'Session', field: 'session' },
      // { label: 'Academie', field: 'academie' },
      { label: "Centre d'ecrit", field: 'centre'},
      // { label: 'Jury', field: 'jury'},
      { label: 'Score', field: 'note' },
      { label: 'Statut', field: 'etatDemande'},
      { label: "Classement", field: 'ordreArrivee'},
      // { label: 'Rang', field: 'rang',width: "120px",resizable: true},
      { label: 'Actions', field: 'actions' }
      // Ajoutez d'autres colonnes selon vos besoins
    ],
    columnDefs:[
      { headerName: "Prénoms", field: "prenoms", sortable: true, filter: true },
      { headerName: "Nom", field: "nom", sortable: true, filter: true },
      { headerName: "Code", field: "code", sortable: true, filter: true },
      { headerName: "Centre d'écrit", field: "centre", sortable: true, filter: true },
      { headerName: "Score", field: "note", sortable: true, filter: true },
      { headerName: "Statut", field: "etatDemande", sortable: true, filter: true },
      { headerName: "Classement", field: "ordreArrivee", sortable: true, filter: true },
      {
        headerName: "Actions",
        field: "actions",
        cellRenderer: (params) => {
          const btn = document.createElement('button');
          btn.innerText = 'Voir';
          btn.classList.add('btn-action');
          btn.addEventListener('click', () => handleView(params.data));
          return btn;
        },
      },
    ]
  }),
  getters: {
    getDataListe: (state) => state.dataListe,
    getEtatCouleurs: (state) => state.etatCouleurs,
    getHasAcceptedDemande: (state) => state.hasAcceptedDemande,
    getDataListeByVille: (state) => state.dataListeByVille,
  },

  actions: {
    //  recupérer la liste des demandes et le mettre dans la tabel dataListe
    async demandeByVille(villeId) {
      try {
        const response = await axios.get(`${demandesByVille}/${villeId}`);
        if (response.status === 200) {
          let res = response.data;
          let formattedData = [];

          formattedData = await Promise.all(res.map(async (element) => {
            let villeLabel = element.ville ? element.ville.libelleLong : null;
            let academieLabel = element.ville && element.ville.academie ? element.ville.academie.libelleLong : null;
            let sessionLabel = element.session ? element.session.libelleLong : null;
            let etatLabel = element.etatDemande ? element.etatDemande.libelleLong : null;
            let nomLabel = element.user ? element.user.nom : null;
            let PrenomsLabel = element.user ? element.user.prenoms : null;
            let codeLabel = element.user ? element.user.code : null;
            let idLabel = element.user ? element.user.id : null;
            let idLabelVille = element.ville ? element.ville.id : null;
            let affectableLabel = element.affectable ? 'OUI' : 'NON';
            let propositionLabel = element.proposition ? 'OUI' : 'NON';
            let centreLabel = element.centre ? element.centre.libelleLong : null;
            let hasAccepted = await this.hasAcceptedDemande(idLabel) ? 'OUI' : 'NON';
            let quotaAccept = await this.quotaAccepteVille(idLabelVille) ? 'OUI' : 'NON';
            let situation = '';

            if (etatLabel === 'en attente' &&
                quotaAccept === 'OUI' &&
                hasAccepted === 'NON' &&
                affectableLabel === 'OUI') {
              situation = 'affectable';
            } else if (etatLabel === 'déclinée' &&
                quotaAccept === 'OUI' &&
                hasAccepted === 'NON' &&
                affectableLabel === 'OUI') {
              situation = 'réaffectable';
            } else if (etatLabel === 'obsolète' &&
                quotaAccept === 'OUI' &&
                hasAccepted === 'NON' &&
                affectableLabel === 'OUI') {
              situation = 'affectable';
            } else if (etatLabel === 'en attente' && affectableLabel === 'NON') {
              situation = 'à rejeter';
            } else if (hasAccepted === 'OUI') {
              situation = 'déjà accepté';
            } else if (etatLabel === 'validée') {
              situation = 'déjà validé';
            } else if (etatLabel === 'rejetée') {
              situation = 'déjà rejeté';
            } else {
              situation = 'quota atteint';
            }
            return {
              id: element.demandeId,
              nom: element.nom,
              note: element.note,
              ordreArrivee: element.ordreArrivee,
              rang: element.rang,
              affectable: affectableLabel,
              ville: villeLabel,
              academie: academieLabel,
              session: sessionLabel,
              etatDemande: etatLabel,
              nom: nomLabel,
              prenoms: PrenomsLabel,
              code: codeLabel,
              centre: centreLabel,
              userId: idLabel,
              villeId: idLabelVille,
              hasAcceptedDemande: hasAccepted,
              quota: quotaAccept,
              proposition:propositionLabel,
              situation: situation
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
    async demandeByVilleProposition(villeId) {
      try {
        const response = await axios.get(`${demandesByVille}/${villeId}`);
        if (response.status === 200) {
          let res = response.data;
          let formattedData = [];

          formattedData = await Promise.all(res.map(async (element) => {
            let villeLabel = element.ville ? element.ville.libelleLong : null;
            let academieLabel = element.ville && element.ville.academie ? element.ville.academie.libelleLong : null;
            let sessionLabel = element.session ? element.session.libelleLong : null;
            let etatLabel = element.etatDemande ? element.etatDemande.libelleLong : null;
            let nomLabel = element.user ? element.user.nom : null;
            let PrenomsLabel = element.user ? element.user.prenoms : null;
            let codeLabel = element.user ? element.user.code : null;
            let idLabel = element.user ? element.user.id : null;
            let idLabelVille = element.ville ? element.ville.id : null;
            let affectableLabel = element.affectable ? 'OUI' : 'NON';
            let propositionLabel = element.proposition ? 'OUI' : 'NON';
            let centreLabel = element.centre ? element.centre.libelleLong : null;
            let hasAccepted = await this.hasPropositionDemande(idLabel) ? 'OUI' : 'NON';
            let quotaAccept = await this.quotaPropositionVille(idLabelVille) ? 'OUI' : 'NON';
            let situation = '';

            if (etatLabel === 'en attente' &&
                quotaAccept === 'OUI' &&
                hasAccepted === 'NON' &&
                affectableLabel === 'OUI') {
              situation = 'affectable';
            } else if (etatLabel === 'déclinée' &&
                quotaAccept === 'OUI' &&
                hasAccepted === 'NON' &&
                affectableLabel === 'OUI') {
              situation = 'réaffectable';
            } else if (etatLabel === 'obsolète' &&
                quotaAccept === 'OUI' &&
                hasAccepted === 'NON' &&
                affectableLabel === 'OUI') {
              situation = 'affectable';
            } else if (etatLabel === 'en attente' && affectableLabel === 'NON') {
              situation = 'à rejeter';
            } else if (hasAccepted === 'OUI' && propositionLabel === 'OUI') {
              situation = 'déjà proposer';
            } else if (hasAccepted === 'OUI' && propositionLabel === 'NON') {
              situation = 'déjà proposer dans une autre ville';
            }
            else if (etatLabel === 'validée') {
              situation = 'déjà validé';
            } else if (etatLabel === 'rejetée') {
              situation = 'déjà rejeté';
            } else {
              situation = 'quota atteint';
            }
            return {
              id: element.demandeId,
              nom: element.nom,
              note: element.note,
              ordreArrivee: element.ordreArrivee,
              rang: element.rang,
              affectable: affectableLabel,
              ville: villeLabel,
              academie: academieLabel,
              session: sessionLabel,
              etatDemande: etatLabel,
              nom: nomLabel,
              prenoms: PrenomsLabel,
              code: codeLabel,
              centre: centreLabel,
              userId: idLabel,
              villeId: idLabelVille,
              hasAcceptedDemande: hasAccepted,
              quota: quotaAccept,
              proposition:propositionLabel,
              situation: situation
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
    async oneProposition(demande) {
      try {
        await axios.get(`${modulesURL}/proposition/${demande}`)
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
    async accepterDemande(id, centreId,villeId) {
      try {
        console.log("Id: ", id);
        console.log("Payload: ", centreId);
        await axios.put(`${accepter}/${id}`, { centre: centreId })
        .then((response) => {
          if(response.status === 200 ){
            this.dataDetails = response.data;
            this.demandeByVille(villeId)
            this.oneProposition(villeId)
            this.centresByVilleForProposition(villeId)
            this.quotaPropositionVille(villeId)
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
    async hasPropositionDemande(userId) {
      try {
        const response = await axios.get(`${hasPropositionDemande}?userId=${userId}`);
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
    async quotaPropositionVille(villeId) {
      try {
        const response = await axios.get(`${quotaProposition}?villeId=${villeId}`);
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
    async centresByVilleForProposition(ville) {
      try {
        // Ajouter une vérification pour s'assurer que ville est un nombre non nul
        // if (typeof ville === 'number' && !isNaN(ville)) {
          await axios.get(`${modulesURL}/centreForProposition/${ville}`)
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
        // } else {
        //   console.error("La valeur de centre est invalide. La requête n'a pas été effectuée.");
        // }
      } catch (error) {
        console.log(error);
        this.error = error;
      } finally {
        this.loading = false;
      }
    },
  },

})
