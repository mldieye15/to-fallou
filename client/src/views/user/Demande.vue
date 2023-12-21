<template>
  <div> 
      <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="700"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.demande.demande') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="submit" ref="demandeForm" v-model="formValid">
      <div v-for="(inputForm,index) in requests" :key="index">
       <v-chip color="green" class="mt-3 mb-2"><h3>Choix N°{{ index }}</h3></v-chip> 
      <v-text-field
        :id="'session'+(index+1)"
        prepend-inner-icon="mdi-alpha-a-circle"
        :name="'session'+index"
        density="compact"
        :label="$t('apps.forms.session.nom')"
        color="balck"
        v-model="inputForm.libelleLong"
        variant="solo"
        readonly
      >
      <input type="hidden" :v-model="inputForm.session">
    </v-text-field>
      <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        :name="'academie'+index"
        density="compact"
        :label="$t('apps.forms.academie.nom')"
        color="balck"
        v-model="inputForm.academie"
        variant="solo"
        :items="dataListe"
        persistent-hint
        single-line
        item-title="libelleLong"
        item-value="id"
        @input="onAcademieChange(index)"
        :rules="[rules.required]"
      ></v-select>
      <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        :name="'ville'+index"
        density="compact"
        :label="$t('apps.forms.ville.nom')"
        color="balck"
        v-model="inputForm.ville"
        variant="solo"
        :items="inputForm.villes"
        persistent-hint
        :rules="[rules.required]"
        single-line
        item-title="libelleLong"
        item-value="id"
      ></v-select>
    </div>  
    </v-form>
    <v-btn block class="mt-2 mb-8" size="large" color="primary" @click="handleSave" :disabled="!formValid">{{ $t('apps.forms.ajouter') }}</v-btn>
    </v-card>
    
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance,watchEffect } from "vue";
import { onMounted,ref,onBeforeMount } from "vue"
import { storeToRefs } from "pinia";
import { useVilleStore } from "@/modules/ville/store";
import { useAcademieStore } from "@/modules/academie/store";
import { useSessionStore } from "@/modules/session/store";
import { useDemandeStore } from "@/modules/demande/store";
import { useNotificationStore } from "@/store/notification";
import { useI18n } from "vue-i18n";
import { useRouter, useRoute } from 'vue-router';


const instance = getCurrentInstance();

const villeStore = useVilleStore();
const {dataListeVille,dataListeByAcademie} = storeToRefs(villeStore);
const {villesByAcademie}=villeStore;
const academieStore = useAcademieStore();
const { dataListe } = storeToRefs(academieStore);
const sessionStore=useSessionStore();
const { dataListeSession,dataDetails} = storeToRefs(sessionStore);
const route = useRoute();
const router=useRouter();
const {one}=sessionStore;

const rules = reactive({
  required: value => !!value || 'Champ obligatoire.',
});
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;
const demandeStore = useDemandeStore();
const { add } = demandeStore;
const formValid = ref(false);
const handleSave = () => {
  console.log("Payload avant envoi:", requests.value);
    const payload = requests.value.map((inputForm) => ({
    ville: inputForm.ville,
    academie: inputForm.academie,
    session: inputForm.session,
  }));
  console.log("Payload transformé:", payload);
    add(payload)
      .then(() => {
        addNotification({
          show: true,
          text: i18n.t('added'),
          color: 'blue'
        });
        router.push({ name: 'accueil' });
      })
  };
const requests = ref([]);
const onAcademieChange = (index) => {
  console.log(`onAcademieChange appelée avec l'index : ${index}`);
  if (requests.value[index].academie !== null) {
    console.log(`Academie avant mise à jour des villes : ${requests.value[index].academie}`);
    updateVilles(index);
    console.log(`Academie après mise à jour des villes : ${requests.value[index].academie}`);
  }
};
const updateVilles = async (index) => {
  console.log(`updateVilles appelée avec l'index : ${index}`);
  console.log(`Academie avant mise à jour des villes : ${requests.value[index].academie}`);

  let academieId = requests.value[index].academie;
  console.log(`Academie après récupération : ${academieId}`);

  await villesByAcademie(academieId);
  // console.log(`Academie après mise à jour des villes : ${academieId}`);

  requests.value[index].villes = dataListeByAcademie.value;

  //console.log(`Villes mises à jour pour l'index ${index} :`, dataListeByAcademie);
};
onMounted(async () => {
  villeStore.all();
  academieStore.all();
  await one(route.params.id);
  
  if (dataDetails.value) {
    requests.value = Array.from({ length: dataDetails.value.nombreDemandeAutorise }, () => ({
      ville: null,
      academie:null,
      session: dataDetails.value.id,
      libelleLong: dataDetails.value.libelleLong,
    }));
  }
  watchEffect(() => {
  requests.value.forEach((inputForm, index) => {
    onAcademieChange(index);
  });
});
  console.log("Données initialisées :", dataListeVille.value, dataListe.value, dataDetails.value);
  console.log("Requests après initialisation :", requests.value);
});
</script>