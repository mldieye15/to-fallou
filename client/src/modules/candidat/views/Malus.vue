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
        id="malus"
        prepend-inner-icon="mdi-minus-circle-outline"
        name="malus"
        density="compact"
        :label="$t('apps.forms.candidat.malus')"
        color="balck"
        v-model="inputForm.malus"
        variant="outlined" 
        :error="inputForm.error" 
        :error-messages="errors.malus?[errors.malus]:[]"
        @focus="clearErrors" 
      >
          <template v-slot:append>
            <v-icon v-if="errors.malus" color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
    </v-text-field >
     
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
const { one,malus } = candidatStore;
const schema = yup.object().shape({
  malus: yup
    .number()
    .required('Le malus est requis') // Définition de la règle "required"
    .typeError('Le malus doit être un nombre')
    .min(0, 'Le malus ne peut pas être négatif')
    .max(100, 'Le malus doit être inférieur ou égal à 100'),
});

const errors = reactive({
  malus: "",
  error: false,
});
const clearErrors = () => {
  errors.malus = null;
};
const inputForm = reactive({
malus:"",
});
const redirectToListe = () => {
  router.push({ name: 'candidat-details'});
};
const handleSave = async (event) => {
  event.preventDefault();
  
  try {
  
    // Validation Yup du formulaire
    await schema.validate(inputForm, { abortEarly: false });

    const payload = {
      malus: inputForm.malus,
    };
    // Appel à votre API pour sauvegarder les données
    await malus(route.params.id, payload);

    // Affichage du message de succès
    toast.success(i18n.t('note'));

    // Redirection vers la page des détails du candidat
    router.push({ name: 'candidat-details' });
  } catch (error) {
    // Si la validation échoue, afficher les messages d'erreur
    if (error instanceof yup.ValidationError) {
      error.errors.forEach(errorMessage => {
        // Afficher les messages d'erreur associés à chaque champ du formulaire
        errors.malus = errorMessage;
        inputForm.error = true;
        // toast.error(errorMessage);
      });
    } else {
      // Gérer d'autres erreurs ici, si nécessaire
      console.error(error);
    }
  }
};
</script>
<style>
.error-message {
  color: red; /* ou toute autre couleur de votre choix */
  margin-top: 5px; /* Ajustez la marge en fonction de vos besoins */
}
</style>