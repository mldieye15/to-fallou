<template>
  <div >
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.demande.demande') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <div v-if="inputForm.etatDemande==='obsolète' || inputForm.etatDemande==='en attente'">
      <v-form @submit.prevent="handleSave" ref="demandeForm" :value="formValid">
      <v-select
       prepend-inner-icon="mdi-calendar"
        name="session"
        density="compact"
        :label="$t('apps.forms.session.nom')"
        color="balck"
        v-model="inputForm.session"
        variant="solo"
        :items="dataListeSession"
        persistent-hint
        readonly
        single-line
        item-title="libelleLong"
        item-value="id"
      ></v-select>
      <v-autocomplete
        prepend-inner-icon="mdi-school"
        name="academie"
        v-model="inputForm.academie"
        :items="dataListeForUser"
        item-title="libelleLong"
        item-value="id"
        :label="$t('apps.forms.academie.nom')"
        dense
        outlined
        variant="solo"
        density="compact"
        clearable
        @change="handleAcademieChange"
        persistent-hint
      ></v-autocomplete>
      <!-- <v-select

        variant="solo"
        
        persistent-hint
        single-line
      ></v-select> -->
      <v-autocomplete
        name="ville"
        v-model="inputForm.ville"
       :items="dataListeByAcademieAndUser"
        item-title="libelleLong"
        item-value="id"
        :label="$t('apps.forms.ville.nom')"
        dense
        outlined
        variant="solo"
        prepend-inner-icon="mdi-city"
        density="compact"
        clearable
        persistent-hint
        single-line
      ></v-autocomplete>
      <div class="d-flex justify-end">
        <v-btn class="mt-8 mb-8 mr-2" color="red" @click.prevent="redirectToListe()">{{ $t('apps.forms.annuler') }}</v-btn>
        <v-btn class="mt-8 mb-8" color="blue" @click="handleSave">{{ $t('apps.forms.valider') }}</v-btn>
      </div>
    </v-form>
    </div>
    <div v-else>
      <h2>
        <p class="error-message">
          imposible de modifier cette demande
        </p>
      </h2>
  </div>
    </v-card>
  </div>
</template> 
  <script setup>
  import { reactive, getCurrentInstance, onMounted,watchEffect,ref } from "vue";
  import { storeToRefs } from "pinia";
  import { useRouter, useRoute } from 'vue-router';
  import { useNotificationStore } from "@/store/notification";
  import { useCentreStore } from "@/modules/centre/store";
  import { useVilleStore } from "@/modules/ville/store";
  import { useAcademieStore } from "@/modules/academie/store";
  import { useSessionStore } from "@/modules/session/store";
  import { useI18n } from "vue-i18n";
  import { useDemandeStore } from '@/modules/demande/store';
  import { useToast } from 'vue-toastification';


const toast= useToast();



     

const redirectToListe = () => {
  router.push({ name: 'accueil'});
};


  const i18n = useI18n();
  const notificationStore = useNotificationStore();
  const { addNotification } = notificationStore;

  const villeStore = useVilleStore();
const {dataListeVille,dataListeByAcademieAndUser} = storeToRefs(villeStore);
const {availableVillesForUserAndAcademy}=villeStore;

const academieStore = useAcademieStore();
const { dataListe,dataListeForUser } = storeToRefs(academieStore);
const sesssionStore=useSessionStore();
const { dataListeSession } = storeToRefs(sesssionStore);
  
  const instance = getCurrentInstance();
  const router = useRouter();
  const route = useRoute();
  
  const demandeStore = useDemandeStore();
  const { dataDetails, loading } = storeToRefs(demandeStore);
  const { one, modify,accepterDemande } = demandeStore;
  
  const inputForm = reactive({
    session:null,
    ville:null,
    academie:null,
    etatDemande:null,
  });
  
  const handleSave = () => {
  const payload = {
    session: inputForm.session,
    ville: inputForm.ville,
    academie: inputForm.academie,
  };
  modify(route.params.id, payload).then(() => {
    // addNotification({
    //   show: true,
    //   text:  i18n.t('updated'),
    //   color: 'blue',
    // });
    toast.success(i18n.t('updated'));
    router.push({ name: 'accueil' });
  });
};
const handleAcademieChange = async () => {
  if (inputForm.academie) {
    try {
      await availableVillesForUserAndAcademy(inputForm.academie);
    } catch (error) {
      console.error("Erreur lors de la récupération des villes:", error);
    }
  }
};
watchEffect(() => {
  handleAcademieChange();
});
const demandeId = ref(route.params.id);
  onMounted(()=>{
    one(route.params.id ).then( () => {
      inputForm.session=dataDetails.value.session?dataDetails.value.session.id:null,
      inputForm.etatDemande=dataDetails.value.etatDemande?dataDetails.value.etatDemande.libelleLong:null,
      villeStore.all();
      academieStore.availableAcademiesForUser(demandeId.value);
      sesssionStore.all();
      console.log(demandeId.value)
    });
  });
  
  
  </script>
  <style>
  .error-message {
    color: red; /* ou toute autre couleur de votre choix */
    margin-top: 5px; /* Ajustez la marge en fonction de vos besoins */
  }
  </style>