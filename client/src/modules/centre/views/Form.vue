<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-2"
      elevation="8"
      max-width="700"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.centre.centre') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="submit" ref="centreForm" :value="formValid">
      <v-text-field
        id="libelleLong"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="libelleLong"
        density="compact"
        :label="$t('apps.forms.centre.nom')"
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
        :label="$t('apps.forms.centre.abreviation')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.libelleCourt"
        variant="solo"
      ></v-text-field>
       <v-select
            prepend-inner-icon="mdi-alpha-a-circle"
            name="ville"
            density="compact"
            :label="$t('apps.forms.ville.nom')"
            color="balck"
            v-model="inputForm.ville"
            variant="solo"
            :items="dataListeVille"
            persistent-hint
            
            single-line
            item-title="libelleLong"
            item-value="id"
        ></v-select>
        <v-select
              prepend-inner-icon="mdi-alpha-a-circle"
              name="typeCentre"
              density="compact"
              :label="$t('apps.forms.typeCentre.nom')"
              color="balck"
              v-model="inputForm.typeCentre"
              variant="solo"
              :items="dataListeTypeCentre"
              persistent-hint
              
              single-line
              item-title="libelleLong"
              item-value="id"
        ></v-select>
      
      <v-btn block class="mt-2 mb-8" size="large" color="blue" @click="handleSave">{{ $t('apps.forms.enregistrer') }}</v-btn>
    </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance,ref,watchEffect } from "vue";
import { onMounted } from "vue"
import { storeToRefs } from "pinia";
import { useCentreStore } from "../store";
import { useVilleStore } from "@/modules/ville/store";
import { useTypeCentreStore } from "@/modules/typeCentre/store";


const instance = getCurrentInstance();
const centreStore=useCentreStore();
const villeStore = useVilleStore();
const { dataListeVille } = storeToRefs(villeStore);

const typeCentreStore = useTypeCentreStore();
const { dataListeTypeCentre } = storeToRefs(typeCentreStore);

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
      const isAvailable = await centreStore.checkLibelleExistence(inputForm.libelleLong);
      console.log("Résultat de la vérification du libelle (isAvailable) :", isAvailable);
      if (!isAvailable) {
        libelleError.value = true;
        libelleErrorMessage.value = "Ce centre   existe deja.";
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
  if(instance.refs.centreForm.validate  && !isSubmitDisabled.value){
    actionSubmit(inputForm);
  }
}

onMounted(()=>{
  villeStore.all();
  typeCentreStore.all();
});

</script>
<style>
.error-message {
  color: red; /* ou toute autre couleur de votre choix */
  margin-top: 5px; /* Ajustez la marge en fonction de vos besoins */
}
</style>
