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
        prepend-inner-icon="mdi-alpha-a-circle"
        name="email"
        density="compact"
        :label="$t('apps.forms.codification.email')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.email"
        variant="solo"
        @blur="onEmailInput"
       >
        </v-text-field>
        <div v-if="emailError" class="error-message">{{ emailErrorMessage }}</div>
      <v-text-field
        id="code"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="code"
        density="compact"
        :label="$t('apps.forms.codification.code')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.code"
        variant="solo"
        @blur="onCodeInput"
       >
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
const router = useRouter();
const redirectToListe = () => {
  router.push({ name: 'codification-liste'});
};
const instance = getCurrentInstance();
const coddificationStore = useCodificationStore();
const rules = reactive({
  required: value => !!value || 'Champ obligatoire.',
  min: v => v.length >= 2 || '2 cractére au moins',
});
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
const onEmailInput = () => {
  // Vérifie s'il y a des espaces dans l'email
  if (/\s/.test(inputForm.email)) {
    // Si des espaces sont trouvés, affiche un message d'erreur
    emailError.value = true;
    emailErrorMessage.value = "L'adresse e-mail ne doit pas contenir d'espaces.";
  } else {
    // Sinon, effectue la vérification normale de l'existence de l'email
    checkEmailExistence();
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
    checkCodeExistence();
  }
};

const { inputForm, actionSubmit } = defineProps({
  inputForm: Object,
  actionSubmit: {
    type: Function,
  }
});


const handleSave = () => {
  if(instance.refs.codificationForm.validate&& !isSubmitDisabled.value){
    actionSubmit(inputForm);
  }
}

</script>
<style>
.error-message {
  color: red; /* ou toute autre couleur de votre choix */
  margin-top: 5px; /* Ajustez la marge en fonction de vos besoins */
}
</style>
