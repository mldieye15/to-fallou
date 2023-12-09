<template>
  <v-main id="inspire" class="d-flex flex-column" justify="space-between">
    <Navbar :appName="appName"/>
    <Snackbar :notifications="notifications" :removeNotification="removeNotification"/>
    
    <v-container fluid >
      <router-view/>
    </v-container>
    
    <Footer :copyrightName="copyrightName" />
  </v-main>
  
  
</template>

<script setup>
import Navbar from './Navbar.vue';
import Footer from './Footer.vue';
import { onMounted,ref } from 'vue';
import { storeToRefs } from "pinia";
import { useAppStore } from "@/store/app";
import { useNotificationStore } from "@/store/notification";
import { useUserStore } from "@/store/user";
import Snackbar from '@/components/core/Snackbar.vue';
import { useSessionStore } from "@/modules/session/store";

const appStore = useAppStore();
const { modules, fonctionnalites } = storeToRefs(appStore);
const sessionStore = useSessionStore();
const {dataListeSession}= storeToRefs(sessionStore);

const notificationStore = useNotificationStore();
const { notifications,  } = storeToRefs(notificationStore);
const { removeNotification } = notificationStore;

const userStore = useUserStore();
//const { notifications, modules, fonctionnalites } = storeToRefs(userStore);
const { changeLoggedIn } = useUserStore();


//  nom de l'application défin au niveau du fihcier .env
const appName = import.meta.env.VITE_APP_NAME;
const copyrightName = import.meta.env.VITE_APP_COPYRIGHT;

onMounted(()=>{
  changeLoggedIn();
  sessionStore.enCoursSession();
});

</script>
