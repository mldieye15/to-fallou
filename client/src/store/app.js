// Utilities
import { defineStore } from 'pinia';
/*import axios from '@/plugins/axios.js'

const  modulesURL = '/modules';
const  fonctionnalitesURL = '/fonctionnalites';
const  fonctionnalitesByModuleURL = '/fonctionnalites?moduleId=';
*/

export const useAppStore = defineStore('app', {
  state: () => ({
    modules: [
      {
        "id": 1,
        "libelle": "Accueil",
        "routeName": "accueil",
        "icon": ""
      },
      {
        "id": 2,
        "libelle": "Paramétrage",
        "routeName": "parametrage",
        "icon": ""
      },
      {
        "id": 3,
        "libelle": "Traitement",
        "routeName": "traitement",
        "icon": ""
      },
      {
        "id": 4,
        "libelle": "Administration",
        "routeName": "administration",
        "icon": ""
      }
    ],
    fonctionnalites: [
      {
        "id": 1,
        "title": "Tableau de bord",
        "translate": "dashboard",
        "code": "dashboard",
        "icon": "mdi-view-dashboard",
        "route": "dashboard",
        "moduleId": 1
      },
      {
        "id": 2,
        "title": "Profile",
        "translate": "profile",
        "code": "profile",
        "icon": "mdi-account-box",
        "route": "profile",
        "moduleId": 1
      },
      {
        "id": 3,
        "title": "Académies",
        "translate": "academie",
        "code": "academie",
        "icon": "mdi-school",
        "route": "academie-liste",
        "moduleId": 2
      },
      {
        "id": 4,
        "title": "Villes",
        "translate": "ville",
        "code": "ville",
        "icon": "mdi-city",
        "route": "ville-liste",
        "moduleId": 2
      }
    ],
    loading: true,
  }),

  getters: {
    geModules: (state) => state.modules,
    getFonctionnalites: (state) => state.fonctionnalites,
  },

  actions: {
    listeModules(payload) {
      this.loading = true;
      console.log("listeModules");
      this.loading = false;
    },

    async listeFonctionnalites() {
      this.loading = true;
      console.log("listeFonctionnalites");
      this.loading = false;
    },

    async listeFonctionnalitesByModule(module) {
      this.loading = true;
      console.log("listeFonctionnalitesByModule");
      this.loading = false;
    }
  }
})
