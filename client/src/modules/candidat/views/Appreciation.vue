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
        :label="$t('apps.forms.candidat.appreciation')"
        color="balck"
        v-model="inputForm.noteSupervisseur"
        variant="outlined" 
        :error="inputForm.error" 
        :error-messages="errors.noteSupervisseur?[errors.noteSupervisseur]:[]" 
        @focus="clearErrors"
      >
          <template v-slot:append>
            <v-icon v-if="errors.noteSupervisseur" color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
    </v-text-field >
        <v-textarea
        id="appreciation"
        name="appreciation"
        density="compact"
        :label="$t('apps.forms.candidat.appreciation')"
        color="balck"
        v-model="inputForm.appreciation"
        variant="outlined"
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
import * as yup from 'yup';

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
const schema = yup.object().shape({
  noteSupervisseur: yup
    .number()
    .required('La note est requise') // Définition de la règle "required"
    .typeError('La note  doit être un nombre')
    .min(0, 'La note  ne peut être négative')
    .max(60, 'La note  doit être inférieure ou égale à 60'),
});
const clearErrors = () => {
  errors.noteSupervisseur = null;
};
const errors = reactive({
  noteSupervisseur: "",
  error: false,
});
const inputForm = reactive({
noteSupervisseur:"",
appreciation:"",
});
const handleSave = async (event) => {
  event.preventDefault();
  
  try {
  
    // Validation Yup du formulaire
    await schema.validate(inputForm, { abortEarly: false });

    const payload = {
      noteSupervisseur: inputForm.noteSupervisseur,
      appreciation: inputForm.appreciation,
    };
    // Appel à votre API pour sauvegarder les données
    await appreciation(route.params.id, payload);

    // Affichage du message de succès
    toast.success(i18n.t('note'));

    // Redirection vers la page des détails du candidat
    router.push({ name: 'candidat-liste' });
  } catch (error) {
    // Si la validation échoue, afficher les messages d'erreur
    if (error instanceof yup.ValidationError) {
      error.errors.forEach(errorMessage => {
        // Afficher les messages d'erreur associés à chaque champ du formulaire
        errors.noteSupervisseur = errorMessage;
        inputForm.error = true;
        // toast.error(errorMessage);
      });
    } else {
      // Gérer d'autres erreurs ici, si nécessaire
      console.error(error);
    }
  }
};
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