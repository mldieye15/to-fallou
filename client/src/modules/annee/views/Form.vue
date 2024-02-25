<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.annee.annee') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="handleSave"  ref="anneeForm">
      <v-text-field
        id="libelleLong"
        prepend-inner-icon="mdi-calendar-blank-multiple"
        name="libelleLong"
        density="compact"
        :label="$t('apps.forms.annee.libelle')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.libelleLong"
        variant="solo"
        @blur="onLibelleInput" 
      >
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
import { useAnneeStore } from "../store";
import { useRouter } from 'vue-router';
const router = useRouter();

const instance = getCurrentInstance();
const anneeStore=useAnneeStore();

const rules = reactive({
  required: value => !!value || 'Champ obligatoire.',
  min: v => v.length >= 2 || '2 cractére au moins',
});
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
      const isAvailable = await anneeStore.checkLibelleExistence(inputForm.libelleLong);
      console.log("Résultat de la vérification du libelle (isAvailable) :", isAvailable);
      if (!isAvailable) {
        libelleError.value = true;
        libelleErrorMessage.value = "L'annee   existe deja.";
        console.log('libelleErrorMessage:', libelleErrorMessage);
      }
    } catch (error) {
      console.error("Erreur lors de la vérification du libelle :", error);
      libelleError.value = true;
      libelleErrorMessage.value = "Erreur lors de la vérification du libelle. Veuillez réessayer.";
    }
  }
};

const { inputForm, actionSubmit } = defineProps({
  inputForm: Object,
  actionSubmit: {
    type: Function,
  }
});
const onLibelleInput = () => {
  // Vérifie s'il y a des espaces dans l'Libelle
  if (/\s/.test(inputForm.libelleLong)) {
    // Si des espaces sont trouvés, affiche un message d'erreur
    libelleError.value = true;
    libelleErrorMessage.value = "Le libelle de l'année ne doit pas contenir d'espaces.";
  } else {
    // Sinon, effectue la vérification normale de l'existence de l'email
    checkLibelleExistence(); 
  }
};
const redirectToListe = () => {
  router.push({ name: 'annee-liste'});
};
const handleSave = () =>{
  if(instance.refs.anneeForm.validate && !isSubmitDisabled.value){
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