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
        :id="'session'+index"
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
        @click="updateVilles(index)"
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
import { reactive, getCurrentInstance,watch } from "vue";
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
const {dataListeVille} = storeToRefs(villeStore);
const{villesByAcademie}=villeStore;
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
const updateVilles = async (index) => {
  console.log("updateVilles appelée avec l'index :", index);

  const idAcademie = requests.value[index].academie;

  // Charger les villes en fonction de l'académie choisie
  await villesByAcademie(idAcademie);

  // Mettre à jour la liste des villes pour le formulaire actuel
  requests.value[index].villes = this.dataListeByAcademie;

  console.log("Villes mises à jour pour l'index", index, ":", this.dataListeByAcademie);
};

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
onMounted(async () => {
  villeStore.all();
  academieStore.all();
  await one(route.params.id);
  
  if (dataDetails.value) {
    requests.value = Array.from({ length: dataDetails.value.nombreDemandeAutorise }, () => ({
      ville: null,
      academie: null,
      session: dataDetails.value.id,
      libelleLong: dataDetails.value.libelleLong,
    }));
  }
  console.log("Données initialisées :", dataListeVille.value, dataListe.value, dataDetails.value);
});
</script>