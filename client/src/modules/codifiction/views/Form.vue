<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.codification.codification') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="handleSave" ref="codificationForm" :value="formValid">
      <v-text-field
        id="email"
        prepend-inner-icon="mdi-email"
        name="email"
        density="compact"
        :label="$t('apps.forms.codification.email')"
        color="balck"
        v-model="inputForm.email"
        variant="outlined"
        @blur="onEmailInput"
        :error-messages="errors.email ? [errors.email] : []"
        @focus="clearErrors"
       >
       <template v-slot:append>
            <v-icon v-if="errors.email" color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
        </v-text-field>
        <div v-if="emailError" class="error-message">{{ emailErrorMessage }}</div>
      <v-text-field
        id="code"
        prepend-inner-icon="mdi-account-key"
        name="code"
        density="compact"
        :label="$t('apps.forms.codification.code')"
        color="balck"
        v-model="inputForm.code"
        variant="outlined"
        @blur="onCodeInput"
        :error-messages="errors.code ? [errors.code] : []"
        @focus="clearErrors"
       >
       <template v-slot:append>
            <v-icon v-if="errors.code" color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
        </v-text-field>
        <div v-if="codeError" class="error-message">{{ codeErrorMessage }}</div>
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
import { useCodificationStore } from "../store";
import { useRouter } from 'vue-router';
import * as yup from 'yup';

const schema = yup.object().shape({
  email: yup.string().email('Veuillez saisir une adresse email valide').required('L\'email est requis'),
  code: yup.string().required('Le code est requis'),
});
const router = useRouter();
const redirectToListe = () => {
  router.push({ name: 'codification-liste'});
};
const instance = getCurrentInstance();
const coddificationStore = useCodificationStore();
const errors = reactive({
  email:'',
  code:'',
  error: false,
});
const clearErrors = () => {
  errors.email = '';
  errors.code = '';
};
const emailError = ref(false);
const emailErrorMessage = ref("");
const codeError = ref(false);
const codeErrorMessage = ref("");
const isSubmitDisabled = ref(false);
watchEffect(() => {
  isSubmitDisabled.value = emailError.value||codeError.value
});
const checkEmailExistence = async () => {
  emailError.value = false;
  emailErrorMessage.value = "";
  if (inputForm.email) {
    try {
      const isAvailable = await coddificationStore.checkEmailExistence(inputForm.email);
      console.log("Résultat de la vérification du email (isAvailable) :", isAvailable);
      if (!isAvailable) {
        emailError.value = true;
        emailErrorMessage.value = "Cet e-mail   existe deja.";
        console.log('emailErrorMessage:', emailErrorMessage);
      }
    } catch (error) {
      console.error("Erreur lors de la vérification du email :", error);
      emailError.value = true;
      emailErrorMessage.value = "Erreur lors de la vérification du email. Veuillez réessayer.";
    }
  }
};
const checkCodeExistence = async () => {
  codeError.value = false;
  codeErrorMessage.value = "";
  if (inputForm.code) {
    try {
      const isAvailable = await coddificationStore.checkCodeExistence(inputForm.code);
      console.log("Résultat de la vérification du code (isAvailable) :", isAvailable);
      if (!isAvailable) {
        codeError.value = true;
        codeErrorMessage.value = "Ce code   existe deja.";
        console.log('codeErrorMessage:', codeErrorMessage);
      }
    } catch (error) {
      console.error("Erreur lors de la vérification du code :", error);
      codeError.value = true;
      codeErrorMessage.value = "Erreur lors de la vérification du code. Veuillez réessayer.";
    }
  }
};
const checkEmailExistenceUp = async () => {
  emailError.value = false;
  emailErrorMessage.value = "";
  if (inputForm.email) {
    const codificationId= inputForm.id;
    const email = inputForm.email;
    try {
      const isAvailable = await coddificationStore.checkEmailExistenceUp({ codificationId, email });
      console.log("Résultat de la vérification du email (isAvailable) :", isAvailable);
      console.log("email, codificationId:", email, codificationId);
      if (!isAvailable) {
        emailError.value = true;
        emailErrorMessage.value = "Cet email  est déjà utilisé.";
        // console.log('emailErrorMessage:', emailErrorMessage);
      }
    } catch (error) {
      // console.error("Erreur lors de la vérification de l'email :", error);
      emailError.value = true;
      emailErrorMessage.value = "Erreur lors de la vérification de l'email. Veuillez réessayer.";
    }
  }
};
const checkCodeExistenceUp = async () => {
  codeError.value = false;
  codeErrorMessage.value = "";
  if (inputForm.code) {
    const codificationId = inputForm.id;
    const code = inputForm.code;
    try {
      const isAvailable = await coddificationStore.checkCodeExistenceUp({codificationId,code});
      // console.log("codificationIdet :code) :", userId, code);
      // console.log("Résultat de la vérification du code (isAvailable) :", isAvailable);
      if (!isAvailable) {
        codeError.value = true;
        codeErrorMessage.value = "Ce code  est déjà utilisé.";
        // console.log('codeErrorMessage:', codeErrorMessage);
      }
    } catch (error) {
      // console.error("Erreur lors de la vérification du code :", error);
      codeError.value = true;
      codeErrorMessage.value = "Erreur lors de la vérification du code. Veuillez réessayer.";
    }
  }
};
const onEmailInput = () => {
  // Vérifie s'il y a des espaces dans l'email
  if (/\s/.test(inputForm.email)) {
    // Si des espaces sont trouvés, affiche un message d'erreur
    emailError.value = true;
    emailErrorMessage.value = "L'adresse e-mail ne doit pas contenir d'espaces.";
  } else {
    if(isEdit){
      checkEmailExistenceUp(); 
    }else{
      checkEmailExistence();
    }
    // Sinon, effectue la vérification normale de l'existence de l'email
    
  }
};
const onCodeInput = () => {
  // Vérifie s'il y a des espaces dans l'code
  if (/\s/.test(inputForm.code)) {
    // Si des espaces sont trouvés, affiche un message d'erreur
    codeError.value = true;
    codeErrorMessage.value = "Le code ne doit pas contenir d'espaces.";
  } else {
    // Sinon, effectue la vérification normale de l'existence du code
    if(isEdit){
      checkCodeExistenceUp();
    }else{
      checkCodeExistence();
    }
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
      if (err.path === 'email') {
        errors.email = err.message;
      } else if (err.path === 'code') {
        errors.code = err.message;
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
