<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-1"
      elevation="8"
      max-width="600"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.candidat.candidat') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="handleSave" ref="candidatForm">
      <v-text-field 
        id="noteSupervisseur"
        prepend-inner-icon="mdi-account"
        name="noteSupervisseur"
        density="compact"
        :label="$t('apps.forms.candidat.note')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.noteSupervisseur"
        variant="solo" 
      ></v-text-field >
        <v-textarea
        id="appreciation"
        name="appreciation"
        density="compact"
        :label="$t('apps.forms.candidat.appreciation')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.appreciation"
        variant="solo"
      ></v-textarea>
      
      <div class="d-flex justify-end">
        <v-btn class="mt-8 mb-8 mr-2" color="red" @click.prevent="redirectToListe()">{{ $t('apps.forms.annuler') }}</v-btn>
        <v-btn class="mt-8 mb-8" color="blue" @click="handleSave">{{ $t('apps.forms.valider') }}</v-btn>
      </div>
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
import { useToast } from 'vue-toastification';


const toast= useToast();
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const candidatStore = useCandidatStore();
const { dataDetails, loading } = storeToRefs(candidatStore);
const { one, appreciation } = candidatStore;
const rules = reactive({
  required: value => !!value || 'Champ obligatoire.',
  min: v => v.length >= 2 || '2 cractére au moins',
});
const inputForm = reactive({
noteSupervisseur:"",
appreciation:"",
});
const handleSave = (event) => {
  event.preventDefault();
  const payload = {
    noteSupervisseur: inputForm.noteSupervisseur,
    appreciation: inputForm.appreciation,
  };
appreciation(route.params.id, payload).then( () => {
  // addNotification({
  //     show: true,
  //     text:  i18n.t('noter'),
  //     color: 'blue'
  //   });
  toast.success(i18n.t('note'));
  router.push( { name: 'candidat-liste'});
});
}
const redirectToListe = () => {
  router.push({ name: 'candidat-liste'});
};
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