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
    <v-form @submit.prevent="handleSave" ref="typeEtablissementForm" :value="formValid">
      <v-text-field
        id="libelleLong"
        prepend-inner-icon="mdi-human-male-board"
        name="libelleLong"
        density="compact"
        :label="$t('apps.forms.typeEtablissement.nom')"
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
      <v-text-field
        id="libelleCourt"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="libelleCourt"
        density="compact"
        :label="$t('apps.forms.typeEtablissement.abreviation')"
        color="balck"
        v-model="inputForm.libelleCourt"
        variant="outlined"
      ></v-text-field>
      <v-text-field
        id="nombrePoint"
        prepend-inner-icon="mdi-vector-point-plus"
        name="nombrePoint"
        density="compact"
        :label="$t('apps.forms.typeEtablissement.nombrePoint')"
        color="balck"
        v-model="inputForm.nombrePoint"
        variant="outlined"
        :error-messages="errors.nombrePoint ? [errors.nombrePoint] : []"
        @focus="clearErrors" 
      >
          <template  v-if="errors.nombrePoint" v-slot:append>
            <v-icon  color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
    </v-text-field>
      <v-select
        prepend-inner-icon="mdi-briefcase"
        name="fonction"
        density="compact"
        :label="$t('apps.forms.fonction.nom')"
        color="balck"
        v-model="inputForm.fonction"
        variant="outlined"
        :items="dataListe"
        persistent-hint
        autocomplete="off"
        single-line
        item-title="libelleLong"
        item-value="id"
        :error-messages="errors.fonction ? [errors.fonction] : []"
        @focus="clearErrors" 
      >
         <template v-if="errors.fonction" v-slot:append>
            <v-icon  color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
    </v-select>
      <div class="d-flex justify-end">
        <v-btn class="mt-8 mb-8 mr-2" color="red" @click.prevent="redirectToListe()">{{ $t('apps.forms.annuler') }}</v-btn>
        <v-btn class="mt-8 mb-8" color="blue" @click="handleSave">{{ $t('apps.forms.valider') }}</v-btn>
      </div>
    </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance,ref,watchEffect, onMounted } from "vue";
import { useTypeEtablissementStore } from "../store";
import { storeToRefs } from "pinia";
import { useRouter } from 'vue-router';
import { useFonctionStore } from "@/modules/fonction/store";
import * as yup from 'yup';

const schema = yup.object().shape({
  libelleLong: yup.string().required('Le libelle est requis'),
  fonction: yup.string().required('Veuillez selectionner une fonction'),
  nombrePoint: yup
    .number()
    .required('Le nombre de point est requise') // Définition de la règle "required"
    .typeError('Le nombre de poin  doit être un nombre')
    .min(0, 'Le nombre de poin  ne peut pas être négatif')
    .max(60, 'Le nombre de poin  doit être inférieure ou égale à 60'),

});
const router = useRouter();

const redirectToListe = () => {
  router.push({ name: 'typeEtablissement-liste'});
};

const instance = getCurrentInstance();
const typeEtablissementStore = useTypeEtablissementStore();
const fonctionStore = useFonctionStore();
const { dataListe } = storeToRefs(fonctionStore);
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
const checkLibelleExistenceUp = async () => {
  libelleError.value = false;
  libelleErrorMessage.value = "";
  if (inputForm.libelleLong) {
    const typeId= inputForm.id;
    const libelleLong = inputForm.libelleLong;
    try {
      const isAvailable = await  typeEtablissementStore.checkLibelleExistenceUp({typeId ,libelleLong});
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
  nombrePoint:null,
  fonction:null,
  error: false,
});
const clearErrors = () => {
  errors.libelleLong = '';
  errors.nombrePoint = '';
  errors.fonction = null;

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
onMounted(()=>{
  fonctionStore.all();
});
</script>
<style>
.error-message {
  color: red; /* ou toute autre couleur de votre choix */
  margin-top: 5px; /* Ajustez la marge en fonction de vos besoins */
}
</style>
