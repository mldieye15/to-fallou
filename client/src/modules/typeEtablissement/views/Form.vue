<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.typeEtablissement.typeEtablissement') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="submit" ref="typeEtablissementForm" :value="formValid">
      <v-text-field
        id="libelleLong"
        prepend-inner-icon="mdi-school"
        name="libelleLong"
        density="compact"
        :label="$t('apps.forms.typeEtablissement.nom')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.libelleLong"
        variant="solo"
        @input="checkLibelleExistence"
      >
      </v-text-field>
      <div v-if="libelleError" class="error-message">{{ libelleErrorMessage }}</div>

      <v-text-field
        id="libelleCourt"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="libelleCourt"
        density="compact"
        :label="$t('apps.forms.typeEtablissement.abreviation')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.libelleCourt"
        variant="solo"
      ></v-text-field>
      <v-text-field
        id="nombrePoint"
        prepend-inner-icon="mdi-vector-point-plus"
        name="nombrePoint"
        density="compact"
        :label="$t('apps.forms.fonction.nombrePoint')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.nombrePoint"
        variant="solo"
      ></v-text-field>

      <v-btn block class="mt-2 mb-8" size="large" color="primary" @click="handleSave">{{ $t('apps.forms.enregistrer') }}</v-btn>
    </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance,ref,watchEffect } from "vue";
import { useTypeEtablissementStore } from "../store";

const instance = getCurrentInstance();
const typeEtablissementStore = useTypeEtablissementStore();

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
      const isAvailable = await typeEtablissementStore.checkLibelleExistence(inputForm.libelleLong);
      console.log("Résultat de la vérification du libelle (isAvailable) :", isAvailable);
      if (!isAvailable) {
        libelleError.value = true;
        libelleErrorMessage.value = "Ce type d'etablissement existe deja.";
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

const handleSave = () => {
  if(instance.refs.typeEtablissementForm.validate&& !isSubmitDisabled.value){
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
