<template>
    <div>
      <v-card
        class="mx-auto pa-12 pb-8 mt-1"
        elevation="8"
        max-width="900"
        rounded="lg"
      >
      <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.candidat.candidat') }}</h2>
      <v-divider class="my-3" color="white"></v-divider>
      <v-form @submit.prevent="submit" ref="candidatForm">
        <v-text-field 
          id="note"
          prepend-inner-icon="mdi-account"
          name="note"
          density="compact"
          :label="$t('apps.forms.candidat.note')"
          color="balck"
          :rules="[rules.required, rules.min]"
          v-model="inputForm.note"
          variant="solo" 
        ></v-text-field >
          <v-text-field
          id="appreciation"
          prepend-inner-icon="mdi-account"
          name="appreciation"
          density="compact"
          :label="$t('apps.forms.candidat.appreciation')"
          color="balck"
          :rules="[rules.required]"
          v-model="inputForm.appreciation"
          variant="solo"
        ></v-text-field>
        <v-btn block class="mt-2 mb-8" size="large" color="blue" @click="handleSave">{{ $t('apps.forms.enregistrer') }}</v-btn>
      </v-form>
      </v-card>
    </div>
  </template>
 <script setup>
import { reactive, getCurrentInstance, onMounted } from "vue";
import { storeToRefs } from "pinia";
import { useRouter, useRoute } from 'vue-router';
import { useNotificationStore } from "@/store/notification";
import { useI18n } from "vue-i18n";
import { useCandidatStore } from "../store";
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const candidatStore = useCandidatStore();
const { dataDetails, loading } = storeToRefs(candidatStore);
const { one, modify } = candidatStore;

const inputForm = reactive({
  noteSupervisseur: "",
  appreciation: "",
});

const handleSave = (payload) => {
  modify(route.params.id, payload).then( () => {
    addNotification({
        show: true,
        text:  i18n.t('noter'),
        color: 'blue'
      });
    router.push( { name: 'candidat-liste'});
  });
}
onMounted(()=>{
  one(route.params.id ).then( () => {
    inputForm.noteSupervisseur = dataDetails.value.noteSupervisseur,
    inputForm.appreciation = dataDetails.value.appreciation
  });
});
  </script>
  <style>
  .error-message {
    color: red; /* ou toute autre couleur de votre choix */
    margin-top: 5px; /* Ajustez la marge en fonction de vos besoins */
  }
  </style>