<template>
  <v-main id="inspire" class="d-flex flex-column" justify="space-between">
    <Navbar :appName="appName"/>
    <Snackbar :notifications="notifications" :removeNotification="removeNotification"/>
    
    <v-container fluid >
      <div v-if="dataListeSession.length > 0">
<div v-if="dataListeForUser.length> 0">
  <v-row>
    <!-- Première carte -->
    <v-col>
      <v-card class="ma-5">
        <v-card-title class="ml-10">
          <p>Demandes Utilisateurs</p>
        </v-card-title>

        <v-card-text>
          <div v-for="demande in dataListeForUser" :key="demande.id" class="demande-bloc ml-10"> 
            <p> Academie: {{ demande.academie }}</p>
            <p> Ville: {{ demande.ville }}</p>
            <p>Statut:
              <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" :color="etatCouleurs[demande.etatDemande]" variant="tonal">
                <span>{{ demande.etatDemande }}</span>
              </v-chip>
            </p>
            <div class="actions-wrapper" v-if="demande.candidatureOuvert==='OUI'" >
              <div v-if="demande.etatDemande==='obsolète' || demande.etatDemande==='en attente'">
                <div class="demandes"> Veillez cliquer sur le button en bleu pour modifier votre demande</div>
             <v-btn  variant="flat" color="light-blue-darken-4" size="small" @click.prevent="redirectToEditDemande(demande.id)" class="mt-3">
              modifier
             </v-btn>
              </div>
             
          </div>
            <div v-if="demande.etatDemande==='acceptée'" class="mt-4">
            <v-dialog transition="dialog-top-transition" width="50%" height="auto">
              <template v-slot:activator="{ props }">
                 <div class="demandes"> Veillez cliquer sur le button en vert pour valider votre demande</div>
                  <v-btn variant="flat" color="green" size="small" v-bind="props">
                   valider
                  </v-btn>
              </template>
              <template v-slot:default="{ isActive }">
                <v-card>
                  <v-toolbar color="primary" :title="$t('apps.forms.demande.demande')"></v-toolbar>
                  <v-card-text>
                    <div class="text-h6">{{ $t('apps.forms.validerMessage') }}</div>
                  </v-card-text>
                  <v-card-actions class="justify-end">
                    <v-btn variant="text" color="primary" @click="isActive.value = false">{{ $t('apps.forms.annuler') }}</v-btn>
                    <v-btn variant="outlined" color="black"  @click="valider(demande.id)">{{ $t('apps.forms.oui') }}</v-btn>
                  </v-card-actions>
                </v-card>
              </template>
            </v-dialog>
          </div>
          </div>
        </v-card-text>
      </v-card>
    </v-col>

    <!-- Deuxième carte -->
    <v-col>
      <v-card class="ma-5 ">
        <v-card-title>
          <p>Details Candidats</p>
        </v-card-title>

        <v-card-text>
          <!-- Ajoutez ici le contenu des détails des candidats -->
        </v-card-text>
      </v-card>
    </v-col>
  </v-row>
</div>
<div v-else>
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
import { useDemandeStore } from '@/modules/demande/store';
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";



const router = useRouter();
const appStore = useAppStore();
const sessionStore = useSessionStore();
const {dataListeSession}= storeToRefs(sessionStore);
const demandeStore = useDemandeStore();
const {dataListeForUser,etatCouleurs,}= storeToRefs(demandeStore);
const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;
const { removeNotification } = notificationStore;
const {validerDemande } = demandeStore;

const userStore = useUserStore();
//const { notifications, modules, fonctionnalites } = storeToRefs(userStore);
const { changeLoggedIn } = useUserStore();
const i18n = useI18n();


//  nom de l'application défin au niveau du fihcier .env
const appName = import.meta.env.VITE_APP_NAME;
const copyrightName = import.meta.env.VITE_APP_COPYRIGHT;

const valider = (id) => {
  validerDemande(id).then( () => {
    // addNotification({
    //     show: true,
    //     text:  i18n.t('valider'),
    //     color: 'blue'
    //   });
      dialog.value=false;
      all();
  });
}
onMounted(()=>{
  changeLoggedIn();
  sessionStore.enCoursSession();
  demandeStore.allForUser();
});
const redirectToEditDemande = (id) => {
  router.push({ name: 'edit', params: { id } });
};
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
.demande-bloc {
    border: 1px solid #ccc;
    padding: 10px;
    margin-bottom: 10px;
    max-width: 300px;
  }
  .demande-bloc p {
    border: 1px solid #ccc;
    padding: 8px; /* Ajustez le remplissage selon vos préférences */
  }
  .demandes{
    color: red; 
  }
  
</style>
