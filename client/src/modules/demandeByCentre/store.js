// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'
import { id } from 'date-fns/locale';

const  modulesURL = '/v1/demandes';
const demandesByCentre = modulesURL+'/demandeByCentre';
const recap=modulesURL+'/recap';
const affecter=modulesURL+'/affecter';
const valider=modulesURL+'/valider';
const validerSecondary=modulesURL+'/validerSecondary';
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
      headerTable: [
        { text: 'Numero Compte', value: 'numeroCompte', align: 'start', sortable: true },
        { text: "Centre d'écrit", value: 'centre', align: 'start', sortable: true },
        { text: 'Ville', value: 'ville', align: 'start', sortable: true },
        { text: 'Prenom', value: 'prenoms', align: 'start', sortable: true },
        { text: 'Nom', value: 'nom', align: 'start', sortable: true },
        { text: 'Etablissement', value: 'etablissement', align: 'start', sortable: true },
        { text: 'Code', value: 'code', align: 'start', sortable: true },
      ],
    columns: [
      { label: 'Prenoms', field: 'prenoms' },
      { label: 'Nom', field: 'nom'},
      // { label: 'Session', field: 'session' },
      // { label: 'Academie', field: 'academie' },
      // { label: "Centre d'ecrit", field: 'centre'},
      { label: 'Jury', field: 'jury'},
      { label: 'Score', field: 'note' },
      { label: 'Statut', field: 'etatDemande'},
      { label: "Classement", field: 'ordreArrivee'},
      // { label: 'Rang', field: 'rang',width: "120px",resizable: true},
      { label: 'Actions', field: 'actions' }
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
            let nomLabel = element.user ? element.user.nom : null;
            let prenomsLabel = element.user ? element.user.prenoms : null;
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
              nom: nomLabel,
              prenoms:prenomsLabel,
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
    async recap() {
      try {
        await axios.get(`${recap}`) 
        .then((response) => {
          if(response.status === 200){
            let res = response.data.map( (element) => {
              let villeLabel = element.ville? element.ville.libelleLong:null;
              let juryLabel = element.jury? element.jury.numero:null;
              let prenomLabel =element.user? element.user.prenoms:null;
              let banqueLabel =element.user? element.user.banque:null;
              let codeBanqueLabel =element.user? element.user.codeBanque:null;
              let codeAgenceLabel =element.user? element.user.codeAgence:null;
              let matriculeLabel = element.user ? element.user.matricule : null;
              let telephoneLabel = element.user ? element.user.telephone : null;
              let numeroCompteLabel =element.user? element.user.numeroCompte:null;
              let cleRibLabel =element.user? element.user.cleRib:null;
              let etablissementLabel =element.user.etablissement? element.user.etablissement.libelleLong:null;
              let nomLabel =element.user? element.user.nom:null;
              let codeLabel =element.user? element.user.code:null;
              let centreLabel = element.centre ? element.centre.libelleLong:null;
              return{
              id:element.id, 
              jury: juryLabel,
              prenoms: prenomLabel,
              nom: nomLabel,
              code:codeLabel,
              centre:centreLabel,
              ville: villeLabel,
              etablissement: etablissementLabel,
              banque:banqueLabel,
              codeBanque:codeBanqueLabel,
              codeAgence:codeAgenceLabel,
              numeroCompte:numeroCompteLabel,
              cleRib:cleRibLabel,
              telephone:telephoneLabel,
              matricule:matriculeLabel
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
