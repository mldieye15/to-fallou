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
      <v-text-field
        prepend-inner-icon="mdi-alpha-a-circle"
        name="session"
        density="compact"
        :label="$t('apps.forms.session.nom')"
        color="balck"
        v-model="inputForm.sessionName"
        variant="solo"
        readonly
      >
      <input type="hidden" :v-model="inputForm.session">
    </v-text-field>

      <v-text-field
        prepend-inner-icon="mdi-alpha-a-circle"
        name="ville"
        density="compact"
        :label="$t('apps.forms.ville.nom')"
        color="balck"
        v-model="inputForm.villeName"
        variant="solo"
        readonly
      >
      <input type="hidden" :v-model="inputForm.ville">
    </v-text-field>

      <v-text-field
        prepend-inner-icon="mdi-alpha-a-circle"
        name="academie"
        density="compact"
        :label="$t('apps.forms.academie.nom')"
        color="balck"
        v-model="inputForm.academieName"
        variant="solo"
        readonly
      >
      <input type="hidden" :v-model="inputForm.academie">
    </v-text-field>
    <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        name="centre"
        density="compact"
        color="balck"
        v-model="inputForm.centre"
        variant="solo"
        :items="dataListeByVille"
        persistent-hint
        item-title="libelleLong"
        item-value="id"
      >
    </v-select>

      <v-btn block class="mt-2 mb-8" size="large" color="primary" @click="handleSave">{{ $t('apps.forms.enregistrer') }}</v-btn>
    </v-form>
    </v-card>
  </div>
</template>
  <script setup>
  import { reactive, getCurrentInstance, onMounted, computed, ref } from "vue";
  import { storeToRefs } from "pinia";
  import { useRouter, useRoute } from 'vue-router';
  import { useNotificationStore } from "@/store/notification";
  import { useCentreStore } from "@/modules/centre/store";
  import { useVilleStore } from "@/modules/ville/store";
  import { useAcademieStore } from "@/modules/academie/store";
  import { useSessionStore } from "@/modules/session/store";
  import { useI18n } from "vue-i18n";
  
  import { useDemandeStore } from "../store";
  const i18n = useI18n();
  
  const notificationStore = useNotificationStore();
  const { addNotification } = notificationStore;

  const villeStore = useVilleStore();
const {dataListeVille} = storeToRefs(villeStore);

const academieStore = useAcademieStore();
const { dataListe } = storeToRefs(academieStore);
const sesssionStore=useSessionStore();
const { dataListeSession } = storeToRefs(sesssionStore);
const centreStore=useCentreStore();
const { dataListeCentre,dataListeByVille} = storeToRefs(centreStore);
  
  const instance = getCurrentInstance();
  const router = useRouter();
  const route = useRoute();
  
  const demandeStore = useDemandeStore();
  const { dataDetails, loading } = storeToRefs(demandeStore);
  const { one, modify,accepterDemande } = demandeStore;
  
  const inputForm = reactive({
    villeName:"",
    sessionName:"",
    academieName:"",
    session:null,
    ville:null,
    academie:null,
    centre:null,
  });
  const handleSave = () => {
  const payload = {
    session: inputForm.session,
    ville: inputForm.ville,
    academie: inputForm.academie,
    centre: inputForm.centre,
  };
  accepterDemande(route.params.id, payload).then(() => {
    addNotification({
      show: true,
      text:  i18n.t('updated'),
      color: 'blue',
    });
    router.push({ name: 'demande-liste' });
  });
};
  onMounted(()=>{
    one(route.params.id ).then( () => {
      inputForm.session=dataDetails.value.session?dataDetails.value.session.id:null,
      inputForm.academie = dataDetails.value.academie?dataDetails.value.academie.id:null,
      inputForm.ville = dataDetails.value.ville?dataDetails.value.ville.id:null,
      inputForm.villeName=dataDetails.value.ville.libelleLong,
      inputForm.academieName=dataDetails.value.academie.libelleLong,
      inputForm.sessionName=dataDetails.value.session.libelleLong
      villeStore.all();
      academieStore.all();
      sesssionStore.all();
      centreStore.all();
      const villeId=dataDetails.value.ville.id;
      console.log(villeId)
      centreStore.centresByVille(villeId);
    });
  });
  
  
  
  </script>
  