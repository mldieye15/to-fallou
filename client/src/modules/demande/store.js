// Utilities
import { defineStore } from 'pinia';
import axios from '@/plugins/axios.js'

const  modulesURL = '/v1/demandes';
const all = modulesURL+'/all';
const allGroupedByUser = modulesURL+'/allGroupedByUser';
const demandesBySession = modulesURL+'/demandeBySession';
const add = modulesURL+'/addAll';
const accepter=modulesURL+'/accepter';
const valider=modulesURL+'/valider';
const allForUser=modulesURL+'/allForUser';
const hasAcceptedDemande=modulesURL+'/hasAcceptedDemande';
const quotaAccepte=modulesURL+'/quotaAccepte';

export const useDemandeStore = defineStore('demande', {
  state: () => ({
    dataListe: [],
    dataListeGroupedByUser: [],
    dataListeForUser: [],  //  List des données à afficher pour la table
    dataDetails: {},  //  Détails d'un élment,
    loading: true,
    hasAcceptedDemande: false,  //  utilisé pour le chargement
    etatCouleurs: {
      'ACCEPTE': 'orange',
      'EN ATTENTE': 'purple',
      'REJETE': 'red',
      'VALIDE': 'green',
      'OBSOLETE':'yellow',
      // Ajoutez d'autres états et couleurs selon vos besoins
},
    columns: [
      { label: 'Ville', field: 'ville',width: "200px",resizable: true},
      { label: 'Session', field: 'session',width: "200px",resizable: true },
      { label: 'Encours', field: 'encours',width: "200px",resizable: true },
      { label: 'Academie', field: 'academie',width: "200px",resizable: true },
      { label: 'Centre d/ecrit', field: 'centre',width: "200px" ,resizable: true},
      // { label: 'Affectable', field: 'affectable',width: "100px",resizable: true},
      { label: 'Points', field: 'note',width: "100px",resizable: true },
      { label: 'Statut', field: 'etatDemande',width: "200px",resizable: true},
      { label: 'Ordre Arrivee', field: 'ordreArrivee',width: "150px",resizable: true},
      // { label: 'Rang', field: 'rang',width: "120px",resizable: true},
      { label: 'Actions', field: 'actions',width: "100px",resizable: true }
      // Ajoutez d'autres colonnes selon vos besoins
    ],
  }),

  getters: {
    getDataListe: (state) => state.dataListe,
    getDataListeGroupedByUser: (state) => state.dataListeGroupedByUser,
    getDataListeForUser: (state) => state.dataListeForUser,
    getEtatCouleurs: (state) => state.etatCouleurs,
    getHasAcceptedDemande: (state) => state.hasAcceptedDemande,
  },

  actions: {
    //  recupérer la liste des demandes et le mettre dans la tabel dataListe
    // async all() {
    //   try {
    //     const response = await axios.get(`${all}`);
    //     if (response.status === 200) {
    //       let res = await Promise.all(response.data.map(async (element) => {
    //         let villeLabel = element.ville ? element.ville.libelleLong : null;
    //         let academieLabel = element.ville && element.ville.academie ? element.ville.academie.libelleLong : null;
    //         let sessionLabel = element.session ? element.session.libelleLong : null;
    //         let etatLabel = element.etatDemande ? element.etatDemande.libelleLong : null;
    //         let nomLabel = element.user ? element.user.prenoms : null;
    //         let idLabel = element.user ? element.user.id : null;
    //         let idLabelVille = element.ville ? element.ville.id : null;
    //         let centreLabel = element.centre ? element.centre.libelleLong : null;
    //         let hasAccepted = await this.hasAcceptedDemande(idLabel)? 'OUI' : 'NON';
    //         let quotaAccept = await this.quotaAccepteVille(idLabelVille)? 'OUI' : 'NON';
    
    //         return {
    //           id: element.id,
    //           ville: villeLabel,
    //           academie: academieLabel,
    //           session: sessionLabel,
    //           etatDemande: etatLabel,
    //           user: nomLabel,
    //           centre: centreLabel,
    //           userId: idLabel,
    //           villeId:idLabelVille,
    //           hasAcceptedDemande: hasAccepted,
    //           quota: quotaAccept,
    //         };
    //       }));
    
    //       this.dataListe = res;
    //     }
    //   } catch (error) {
    //     console.log(error);
    //     this.error = error;
    //   } finally {
    //     this.loading = false;
    //   }
    // },
    async allForUser() {
      try {
        await axios.get(`${allForUser}`) 
        .then((response) => {
          if(response.status === 200){

            let res = response.data.map( (element) => {
              let villeLabel = element.ville? element.ville.libelleLong:null;
              let academieLabel =element.ville && element.ville.academie? element.ville.academie.libelleLong:null;
              let sessionLabel = element.session ? element.session.libelleLong:null;
              let etatLabel = element.etatDemande ? element.etatDemande.libelleLong:null;
              let sessionLabelEncours =element.session.candidatureOuvert?'OUI' : 'NON';
              let nomLabel = element.user ? element.user.prenoms : null;
              let centreLabel=element.centre?element.centre.libelleLong:null;
              return{
                id:element.id, 
                candidatureOuvert:sessionLabelEncours,
                ville: villeLabel,
                academie:academieLabel,
                session:sessionLabel,
                etatDemande:etatLabel,
                user:nomLabel,
                centre:centreLabel,
              }
            })
            this.dataListeForUser = res;
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
        const response = await axios.get(`${all}`);
        
        if (response.status === 200) {
          let res = response.data;
          let formattedData = [];
      
          const promises = Object.keys(res).map(async (userId) => {
            const user = res[userId][0].user;
            const nomPrenomLabel = user ? `${user.prenoms} ${user.nom}` : null; 
            
            const demandes = await Promise.all(res[userId].map(async (element) => {
              let villeLabel = element.ville ? element.ville.libelleLong : null;
              let academieLabel = element.ville && element.ville.academie ? element.ville.academie.libelleLong : null;
              let sessionLabel = element.session ? element.session.libelleLong : null;
              let etatLabel = element.etatDemande ? element.etatDemande.libelleLong : null;
              let nomLabel = element.user ? element.user.prenoms : null;
              let idLabel = element.user ? element.user.id : null;
              let idLabelVille = element.ville ? element.ville.id : null;
              let affectableLabel= element.affectable? 'OUI' : 'NON';
              let centreLabel = element.centre ? element.centre.libelleLong : null;
              let hasAccepted = await this.hasAcceptedDemande(idLabel)? 'OUI' : 'NON';
              let quotaAccept = await this.quotaAccepteVille(idLabelVille)? 'OUI' : 'NON';
              
              return {
                id: element.demandeId,
                nom: element.nom,
                note: element.note,
                ordreArrivee:element.ordreArrivee,
                rang: element.rang,
                affectable: affectableLabel,
                ville: villeLabel,
                academie: academieLabel,
                session: sessionLabel,
                etatDemande: etatLabel,
                user: nomLabel,
                centre: centreLabel,
                userId: idLabel,
                villeId:idLabelVille,
                hasAcceptedDemande: hasAccepted,
                quota: quotaAccept,
              };
            }));
      
            formattedData.push({
              user: nomPrenomLabel,
              userId: userId,
              demandes: demandes,
            });
          });
          await Promise.all(promises);
          this.dataListe = formattedData;
          console.log("Donnees imbriquees", formattedData);
        }
      } catch (error) {
        console.error(error);
        this.error = error;
      } finally {
        this.loading = false;
      }
    },
    async demandeBySession(sessionId) {
      try {
        const response = await axios.get(`${demandesBySession}/${sessionId}`);
        
        if (response.status === 200) {
          let res = response.data;
          let formattedData = [];
      
          const promises = Object.keys(res).map(async (userId) => {
            const user = res[userId][0].user;
            const nomPrenomLabel = user ? `${user.prenoms} ${user.nom}` : null; 
            
            const demandes = await Promise.all(res[userId].map(async (element) => {
              let villeLabel = element.ville ? element.ville.libelleLong : null;
              let academieLabel = element.ville && element.ville.academie ? element.ville.academie.libelleLong : null;
              let sessionLabel = element.session ? element.session.libelleLong : null;
              let etatLabel = element.etatDemande ? element.etatDemande.libelleLong : null;
              let nomLabel = element.user ? element.user.prenoms : null;
              let idLabel = element.user ? element.user.id : null;
              let idLabelVille = element.ville ? element.ville.id : null;
              let affectableLabel= element.affectable? 'OUI' : 'NON';
              let centreLabel = element.centre ? element.centre.libelleLong : null;
              let hasAccepted = await this.hasAcceptedDemande(idLabel)? 'OUI' : 'NON';
              let quotaAccept = await this.quotaAccepteVille(idLabelVille)? 'OUI' : 'NON';
              
              return {
                id: element.demandeId,
                nom: element.nom,
                note: element.note,
                ordreArrivee:element.ordreArrivee,
                rang: element.rang,
                affectable: affectableLabel,
                ville: villeLabel,
                academie: academieLabel,
                session: sessionLabel,
                etatDemande: etatLabel,
                user: nomLabel,
                centre: centreLabel,
                userId: idLabel,
                villeId:idLabelVille,
                hasAcceptedDemande: hasAccepted,
                quota: quotaAccept,
              };
            }));
      
            formattedData.push({
              user: nomPrenomLabel,
              userId: userId,
              demandes: demandes,
            });
          });
          await Promise.all(promises);
          this.dataListeGroupedByUser = formattedData;
          console.log("Donnees imbriquees", formattedData);
        }
      } catch (error) {
        console.error(error);
        this.error = error;
      } finally {
        this.loading = false;
      }
    }, 
    async allGroupedByUser() {
      try {
        const response = await axios.get(`${allGroupedByUser}`);
        
        if (response.status === 200) {
          let res = response.data;
          let formattedData = [];
      
          const promises = Object.keys(res).map(async (userId) => {
            const user = res[userId][0].user;
            const nomPrenomLabel = user ? `${user.prenoms} ${user.nom}` : null; 
            
            const demandes = await Promise.all(res[userId].map(async (element) => {
              let villeLabel = element.ville ? element.ville.libelleLong : null;
              let academieLabel = element.ville && element.ville.academie ? element.ville.academie.libelleLong : null;
              let sessionLabel = element.session ? element.session.libelleLong : null;
              let etatLabel = element.etatDemande ? element.etatDemande.libelleLong : null;
              let nomLabel = element.user ? element.user.prenoms : null;
              let sessionLabelEncours =element.session.sessionOuvert?'OUI' : 'NON';
              let idLabel = element.user ? element.user.id : null;
              let idLabelVille = element.ville ? element.ville.id : null;
              let affectableLabel= element.affectable? 'OUI' : 'NON';
              let centreLabel = element.centre ? element.centre.libelleLong : null;
              let hasAccepted = await this.hasAcceptedDemande(idLabel)? 'OUI' : 'NON';
              let quotaAccept = await this.quotaAccepteVille(idLabelVille)? 'OUI' : 'NON';
              
              return {
                id: element.demandeId,
                nom: element.nom,
                encours:sessionLabelEncours,
                note: element.note,
                ordreArrivee:element.ordreArrivee,
                rang: element.rang,
                affectable: affectableLabel,
                ville: villeLabel,
                academie: academieLabel,
                session: sessionLabel,
                etatDemande: etatLabel,
                user: nomLabel,
                centre: centreLabel,
                userId: idLabel,
                villeId:idLabelVille,
                hasAcceptedDemande: hasAccepted,
                quota: quotaAccept,
              };
            }));
      
            formattedData.push({
              user: nomPrenomLabel,
              userId: userId,
              demandes: demandes,
            });
          });
          await Promise.all(promises);
          this.dataListeGroupedByUser = formattedData;
          console.log("Donnees imbriquees", formattedData);
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
