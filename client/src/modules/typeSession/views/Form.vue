<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.typeSession.typeSession') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="handleSave" ref="typeSessionForm">
      <v-text-field
        id="libelleLong"
        prepend-inner-icon="mdi-timetable"
        name="libelleLong"
        density="compact"
        :label="$t('apps.forms.typeSession.nom')"
        :color="errors.libelleLong ? 'red' : 'black'"
        v-model="inputForm.libelleLong"
        variant="outlined"
        @blur="onLibelleInput"
        @keyup.enter="onLibelleInput"
        :error-messages="errors.libelleLong ? [errors.libelleLong] : []"
        @focus="clearErrors"
      >
         <template v-if="errors.libelleLong" v-slot:append>
            <v-icon  color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
        </v-text-field>
      <div v-if="libelleError" class="error-message">{{ libelleErrorMessage }}</div>
      <div class="d-flex justify-end">
        <v-btn class="mt-8 mb-8 mr-2" color="red" @click.prevent="redirectToListe()">{{ $t('apps.forms.annuler') }}</v-btn>
        <v-btn class="mt-8 mb-8" color="blue" @click="handleSave">{{ $t('apps.forms.valider') }}</v-btn>
      </div>
    </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance,ref,watchEffect } from "vue";
import { useTypeSessionStore } from "../store";
import { useRouter } from 'vue-router';
import * as yup from 'yup';

const schema = yup.object().shape({
  libelleLong: yup.string().required('Le libelle est requis'),
});
const router = useRouter();
const instance = getCurrentInstance();
const typeSessionStore = useTypeSessionStore();


const libelleError = ref(false);
const libelleErrorMessage = ref("");
const isSubmitDisabled = ref(false);
watchEffect(() => {
  isSubmitDisabled.value = libelleError.value
});
const checkLibelleExistence = async () => {
  libelleError.value = false;
  libelleErrorMessage.value = "";
  if (inputForm.libelleLong) {
    try {
      const isAvailable = await typeSessionStore.checkLibelleExistence(inputForm.libelleLong);
      console.log("Résultat de la vérification du libelle (isAvailable) :", isAvailable);
      if (!isAvailable) {
        libelleError.value = true;
        libelleErrorMessage.value = "Ce type de session existe deja.";
        console.log('libelleErrorMessage:', libelleErrorMessage);
      }
    } catch (error) {
      console.error("Erreur lors de la vérification du libelle :", error);
      libelleError.value = true;
      libelleErrorMessage.value = "Erreur lors de la vérification du libelle. Veuillez réessayer.";
    }
  }
};

const redirectToListe = () => {
  router.push({ name: 'typeSession-liste'});
};
const checkLibelleExistenceUp = async () => {
  libelleError.value = false;
  libelleErrorMessage.value = "";
  if (inputForm.libelleLong) {
    const typeId= inputForm.id;
    const libelleLong = inputForm.libelleLong;
    try {
      const isAvailable = await  typeSessionStore.checkLibelleExistenceUp({typeId ,libelleLong});
      console.log("academie ,libelleLong :",typeId,libelleLong );
      console.log("Résultat de la vérification du libelle (isAvailable) :", isAvailable);
      if (!isAvailable) {
        libelleError.value = true;
        libelleErrorMessage.value = "Ce type existe dèja.";
        console.log('libelleErrorMessage:', libelleErrorMessage);
      }
    } catch (error) {
      console.error("Erreur lors de la vérification du libelle :", error);
      libelleError.value = true;
      libelleErrorMessage.value = "Erreur lors de la vérification du libelle. Veuillez réessayer.";
    }
  }
};

const errors = reactive({
  libelleLong:'',
  error: false,
});
const clearErrors = () => {
  errors.libelleLong = '';

};
const onLibelleInput = () => { 
  if (isEdit) {
      checkLibelleExistenceUp();
    } else {
      checkLibelleExistence();
    } 
};
const { inputForm, actionSubmit,isEdit } = defineProps({
  isEdit:Boolean,
  inputForm: Object,
  actionSubmit: {
    type: Function,
  }
});

const handleSave = async () => {
  try {
    if (!isSubmitDisabled.value) {
      await schema.validate(inputForm, { abortEarly: false });
      console.log('Formulaire valide. Soumission en cours...');
      actionSubmit(inputForm); 
      // Vous pouvez ajouter ici votre logique pour la sauvegarde du formulaire
    } else {
      console.log('Le formulaire contient des erreurs. Veuillez corriger et réessayer.');
    }
  } catch (error) {
  // Si la validation échoue, afficher les messages d'erreur
  if (error instanceof yup.ValidationError) {
    error.inner.forEach(err => {
      if (err.path === 'libelleLong') {
        errors.libelleLong = err.message;
      } else if (err.path === 'nombrePoint') {
        errors.nombrePoint = err.message;
      }
      else if (err.path === 'fonction') {
        errors.fonction = err.message;
      }
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
