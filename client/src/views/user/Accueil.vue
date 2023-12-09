<template>
  <v-main id="inspire" class="d-flex flex-column" justify="space-between">
    <Navbar :appName="appName"/>
    <Snackbar :notifications="notifications" :removeNotification="removeNotification"/>
    
    <v-container fluid >
      <div v-if="dataListeSession.length > 0">
      <h2>Sessions en cours</h2>
      <ul>
        <div v-for="session in dataListeSession" :key="session.id" :to="{ name: 'demande', params: { id: session.id } }">
          <span class="session-row">
           <v-chip color='green'><h2>{{ session.libelleLong }}:</h2></v-chip>
           <v-chip color='blue'>
            <div v-if="session.candidatureOuvert==='ouverte'">
            <router-link :to="{ name: 'demande', params: { id: session.id } }" class="candidater-link">
              Postuler ici
            </router-link>
            </div>
            <div v-else>
              <p>Depot candidature cloturée</p>

            </div>
           </v-chip>
            
          </span>
        </div>
      </ul>
    </div>
    <div v-else>
      <h2> Aucune Session en cours actuellement.</h2>
    </div>
    </v-container>
    
    <Footer :copyrightName="copyrightName" />
  </v-main>
</template>

<script setup>
import { onMounted,ref } from 'vue';
import { storeToRefs } from "pinia";
import { useAppStore } from "@/store/app";
import { useNotificationStore } from "@/store/notification";
import { useUserStore } from "@/store/user";
import Snackbar from '@/components/core/Snackbar.vue';
import { useSessionStore } from "@/modules/session/store";
const appStore = useAppStore();
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
<style>
.session-row {
  display: block;
  margin-bottom: 10px;
}

.session-row router-link {
  margin-left: 10px; 
}

.candidater-link {
  color: blue; 
  text-decoration: underline;
}

</style>
