<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.ville.ville') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="handleSave" ref="villeForm" :value="formValid">
      <v-text-field
        id="libelleLong"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="libelleLong"
        density="compact"
        :label="$t('apps.forms.ville.nom')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.libelleLong"
        variant="solo"
        @blur="checkLibelleExistence"
       >
      </v-text-field>
      <div v-if="libelleError" class="error-message">{{ libelleErrorMessage }}</div>
      <v-text-field
        id="libelleCourt"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="libelleCourt"
        density="compact"
        :label="$t('apps.forms.ville.abreviation')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.libelleCourt"
        variant="solo"
        @blur="onCodeInput"
      ></v-text-field>
      <div v-if="codeError" class="error-message">{{ codeErrorMessage }}</div>
      <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        name="academie"
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
      ></v-select>

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
import { storeToRefs } from "pinia";
import { useAcademieStore } from "@/modules/academie/store";
import { useVilleStore } from "../store";
import { onMounted } from "vue";
import { useRouter } from 'vue-router';
const router = useRouter();

const instance = getCurrentInstance();
const academieStore = useAcademieStore();
const {dataListe}= storeToRefs(academieStore);
const villeStore = useVilleStore();

const rules = reactive({
  required: value => !!value || 'Champ obligatoire.',
  min: v => v.length >= 2 || '2 cractére au moins',
});
const libelleError = ref(false);
const libelleErrorMessage = ref("");
const codeError = ref(false);
const codeErrorMessage = ref("");
const isSubmitDisabled = ref(false);
watchEffect(() => {
  isSubmitDisabled.value = libelleError.value||codeError.value
});
const checkLibelleExistence = async () => {
  libelleError.value = false;
  libelleErrorMessage.value = "";
  if (inputForm.libelleLong) {
    try {
      const isAvailable = await villeStore.checkLibelleExistence(inputForm.libelleLong);
      console.log("Résultat de la vérification du libelle (isAvailable) :", isAvailable);
      if (!isAvailable) {
        libelleError.value = true;
        libelleErrorMessage.value = "Cet nom  est déjà utilisé.";
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
  router.push({ name: 'ville-liste'});
};
const checkCodeExistence = async () => {
  codeError.value = false;
  codeErrorMessage.value = "";
  if (inputForm.libelleCourt) {
    try {
      const isAvailable = await villeStore.checkCodeExistence(inputForm.libelleCourt);
      console.log("Résultat de la vérification du code (isAvailable) :", isAvailable);
      if (!isAvailable) {
        codeError.value = true;
        codeErrorMessage.value = "le code de cette ville   existe deja.";
        console.log('codeErrorMessage:', codeErrorMessage);
      }
    } catch (error) {
      console.error("Erreur lors de la vérification du code :", error);
      codeError.value = true;
      codeErrorMessage.value = "Erreur lors de la vérification du code. Veuillez réessayer.";
    }
  }
};
const onCodeInput = () => {
  // Vérifie s'il y a des espaces dans le matricule
  if (/\s/.test(inputForm.libelleCourt)) {
    // Si des espaces sont trouvés, affiche un message d'erreur
    codeError.value = true;
    codeErrorMessage.value = "Le code de la ville ne doit pas contenir d'espaces.";
  } else {
    // Sinon, effectue la vérification normale de l'existence du matricule
    checkCodeExistence ();
  }
};

const { inputForm, actionSubmit } = defineProps({
  inputForm: Object,
  actionSubmit: {
    type: Function,
  }
});

const handleSave = () => {
  if(instance.refs.villeForm.validate && !isSubmitDisabled.value){
    actionSubmit(inputForm);
  }
}
onMounted(()=>{
  academieStore.all();
})

</script>
<style>
.error-message {
  color: red; /* ou toute autre couleur de votre choix */
  margin-top: 5px; /* Ajustez la marge en fonction de vos besoins */
}
</style>