<template>
  <nav>
    <v-app-bar class="bg-blue-navbar darken-2" dark app inset padless flat>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" class="text-white"></v-app-bar-nav-icon>
      <v-avatar class="elevation-1">
        <v-img alt="Logo" class="shrink logo" rounded lazy-src="" :src="imageUrl" transition="scale-transition" width="40"  />
      </v-avatar>
      <v-toolbar-title class="text-uppercase text-white">
          <span class="font-weight-light">{{ appName }} </span>
          <span></span>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-menu transition="slide-y-transition">
        <!-- <template v-slot:activator="{ props }">
          <v-btn text v-bind="props" >
            <v-icon left size="36" class="text-white">mdi-apps</v-icon>
          </v-btn>
        </template> -->
        <v-list flat >
          <v-list-item  v-for="link in modules" :key="link.id" router  class="link-item active">
            <v-list-item-title @click="loadFonction(link.id)">{{link.libelle}}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>

      <Localisation iconColorClass="text-white" />

      <v-menu bottom min-width="250px" rounded offset-x class="px-12" >
        <template v-slot:activator="{ props }">
          <v-btn icon x-large v-bind="props">
            <v-avatar color="white" size="46" light >
              <!--<v-img alt="Logo" class="shrink logo" rounded lazy-src="" :src="imageUrl" transition="scale-transition" width="40"  />-->
              <span v-if="user != null" black class="black--text text-h5">{{ user.initiale }}</span>
            </v-avatar>
          </v-btn>
        </template>

        <v-card>
          <v-list-item-content class="justify-center">
            <div class="mx-auto text-center">
              <h3 v-if="user != null">{{ user.fullname }} </h3>
              <p class="text-caption mt-1" v-if="user != null">
                {{ user.username }}
              </p>
              <v-divider class="my-3"></v-divider>
              <router-link :to="{name:'profile'}" class="text">{{ $t('public.nav.top.profile') }}</router-link>
              <v-divider class="my-3"></v-divider>
              <v-btn class="ma-3 bg-blue" rounded text @click="handleLogout">
                {{ $t('public.nav.top.deconnexion') }}
              </v-btn>
            </div>
          </v-list-item-content>
        </v-card>
      </v-menu>
    </v-app-bar>

    <v-navigation-drawer v-model="drawer" dark app class="bg-white-navbar darken-6" >
      <v-layout column align-center>
        <v-flex class="mt-1 mx-auto">
            <v-avatar size="100">
              <v-img alt="Logo" class="shrink logo" rounded lazy-src="" :src="profileUrl" transition="scale-transition" width="60"  />
            </v-avatar>
            <p class="text-white subheading mt-1 text-center">mldieye</p>
        </v-flex>
      </v-layout>
      <v-divider class="mx-10 mt-1"></v-divider>
      <SidebarItem :items="fonctionItems.items"/>
    </v-navigation-drawer>
  </nav>
</template>

<script setup>
import imageUrl from '@/assets/imgs/logo_blanc.jpg'
import profileUrl from '@/assets/imgs/profile3.png'
import Localisation from '@/components/core/Localisation.vue';
import SidebarItem from '@/components/core/SidebarItem.vue';

import { ref, onMounted, reactive } from 'vue';
import { storeToRefs } from "pinia";
import { useRouter } from 'vue-router'
import { useAppStore } from "@/store/app";
import { useUserStore } from "@/store/user";
import { useNotificationStore } from "@/store/notification";

const appStore = useAppStore();
const userStore = useUserStore();
const { modules, fonctionnalites } = storeToRefs(appStore);
const { listeModules, listeFonctionnalitesByModule } = useAppStore();
const { user } = storeToRefs(userStore);
const { logout } = userStore;

const router = useRouter();

import { useI18n } from "vue-i18n";
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;


const drawer = ref(true);
const fonctionItems = reactive({ items: [] });

defineProps({
  appName: String,
  modules: [],
});

//
const defaultSideBarItems = reactive({ items: [
  {
      "id": 0,
      "title": "Tableau de bord",
      "translate": "dashboard",
      "code": "dashboard",
      "icon": "mdi-view-dashboard",
      "route": "dashboard"
  },
  {
    "id": 14,
    "title": "Codifications",
    "translate": "codification",
    "code": "codification",
    "icon": "mdi-school",
    "route": "codification-liste"
  },
  
  {
      "id": 1,
      "title": "Users",
      "translate": "user",
      "code": "user",
      "icon": "mdi-account-cowboy-hat-outline",
      "route": "user-liste"
  },
  {
    "id": 15,
    "title": "Candidats",
    "translate": "candidat",
    "code": "candidat",
    "icon": "mdi-school",
    "route": "candidat-liste"
  },
  {
    "id": 2,
    "title": "Académies",
    "translate": "academie",
    "code": "academie",
    "icon": "mdi-school",
    "route": "academie-liste"
  },
  {
    "id": 3,
    "title": "Villes",
    "translate": "ville",
    "code": "ville",
    "icon": "mdi-city",
    "route": "ville-liste"
  },
  {
    "id": 4,
    "title": "Jurys",
    "translate": "jury",
    "code": "jury",
    "icon": "mdi-align-vertical-center",
    "route": "jury-liste"
  },
  {
    "id": 5,
    "title": "Annees",
    "translate": "annee",
    "code": "annee",
    "icon": "mdi-clock-time-eight",
    "route": "annee-liste"
  },
  {
    "id": 6,
    "title": "Centres",
    "translate": "centre",
    "code": "centre",
    "icon": "mdi-cast-education",
    "route": "centre-liste"
  },
  {
    "id": 7,
    "title": "Etablissements",
    "translate": "etablissement",
    "code": "etablissement",
    "icon": "mdi-account-school",
    "route": "etablissement-liste"
  },
  {
    "id": 8,
    "title": "Type d'etablissements",
    "translate": "typeEtablissement",
    "code": "typeEtablissement",
    "icon": "mdi-account-school-outline",
    "route": "typeEtablissement-liste"
  },
  {
    "id": 9,
    "title": "Sessions",
    "translate": "session",
    "code": "session",
    "icon": "mdi-calendar-clock",
    "route": "session-liste"
  },
  {
    "id": 10,
    "title": "TypeCentres",
    "translate": "typeCentre",
    "code": "typeCentre",
    "icon": "mdi-school-outline",
    "route": "typeCentre-liste"
  },
  {
    "id": 11,
    "title": "Demandes",
    "translate": "demande",
    "code": "demande",
    "icon": "mdi-account-multiple-check",
    "route": "demande-liste"
  },
  {
    "id": 12,
    "title": "Fonctions",
    "translate": "fonction",
    "code": "fonction",
    "icon": "mdi-plus",
    "route": "fonction-liste"
  },
  {
    "id": 13,
    "title": "TypeSessions",
    "translate": "typeSession",
    "code": "typeSessions",
    "icon": "mdi-clock ",
    "route": "typeSession-liste"
  },
  // {
  //     "id": 3,
  //     "title": "Ville",
  //     "translate": "ville",
  //     "code": "ville",
  //     "icon": "mdi-city",
  //     "route": "profile"
  // }
] });
const sideBarItems = reactive({ items: [
  {
      "id": 0,
      "title": "Tableau de bord",
      "translate": "dashboard",
      "code": "dashboard",
      "icon": "mdi-view-dashboard",
      "route": "dashboard"
  },
  {
      "id": 1,
      "title": "Candidats",
      "translate": "candidat",
      "code": "candidat",
      "icon": "mdi-school",
      "route": "candidat-liste"
  }
]});

onMounted(()=>{
  listeModules();
  if(role=="ROLE_SUPERVISSEUR"){
    fonctionItems.items=sideBarItems.items;
  }else
  fonctionItems.items = defaultSideBarItems.items;
});

const loadFonction = async (module) => {
  await listeFonctionnalitesByModule(module).then( (response) => {
    fonctionItems.items = fonctionnalites.value;
  });
}
let role= localStorage.getItem('role');
//  deconnexion
const handleLogout = () => {
  console.log("handleLogout clicked");
  logout().then( () => {
      router.push( { name: 'login'});
      // addNotification({
      //   show: true,
      //   text:  i18n.t('welcome')+' '+user.fullname,
      //   color: 'black'
      // });
    });
}

</script>

<style scoped>
router-link, a{
    text-decoration: dashed !important;
}
.link-item:hover{
  cursor: pointer;
  background-color: #3490dc;
  color: white;
  font-weight: bold;
}
</style>
