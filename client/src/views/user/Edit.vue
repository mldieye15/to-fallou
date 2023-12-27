<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.demande.demande') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="submit" ref="demandeForm" :value="formValid">
      <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
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
      <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        name="academie"
        density="compact"
        color="balck"
        v-model="inputForm.academie"
        variant="solo"
        :items="dataListeForUser"
        persistent-hint
        item-title="libelleLong"
        item-value="id"
        @change="handleAcademieChange"
      >
    </v-select>

      <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        name="ville"
        density="compact"
        :label="$t('apps.forms.ville.nom')"
        color="balck"
        v-model="inputForm.ville"
        variant="solo"
        :items="dataListeByAcademieAndUser"
        persistent-hint
        single-line
        item-title="libelleLong"
        item-value="id"
      ></v-select>
      <v-btn block class="mt-2 mb-8" size="large" color="primary" @click="handleSave">{{ $t('apps.forms.enregistrer') }}</v-btn>
    </v-form>
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
  });
  
  const handleSave = () => {
  const payload = {
    session: inputForm.session,
    ville: inputForm.ville,
    academie: inputForm.academie,
  };
  modify(route.params.id, payload).then(() => {
    addNotification({
      show: true,
      text:  i18n.t('updated'),
      color: 'blue',
    });
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
      villeStore.all();
      academieStore.availableAcademiesForUser(demandeId.value);
      sesssionStore.all();
      console.log(demandeId.value)
    });
  });
  
  
  </script>