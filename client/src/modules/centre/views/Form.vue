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
    <v-form @submit.prevent="handleSave" ref="centreForm" >
      <v-text-field
        id="libelleLong"
        prepend-inner-icon="mdi-office-building"
        name="libelleLong"
        density="compact"
        :label="$t('apps.forms.centre.nom')"
        color="balck"
        v-model="inputForm.libelleLong"
        variant="outlined"
        @blur="onLibelleInput"
        @keyup.enter="onLibelleInput"
        :error="inputForm.error" 
        :error-messages="errors.libelleLong ? [errors.libelleLong] : []"
        @focus="clearErrors"
      >
         <template v-slot:append>
            <v-icon v-if="errors.libelleLong" color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
      </v-text-field>
      <div v-if="libelleError" class="error-message">{{ libelleErrorMessage }}</div>

      <v-text-field
        id="libelleCourt"
        prepend-inner-icon="mdi-office-building"
        name="libelleCourt"
        density="compact"
        :label="$t('apps.forms.centre.abreviation')"
        color="balck"
        v-model="inputForm.libelleCourt"
        variant="outlined"
        @blur="onCodeInput"
        @keyup.enter="onCodeInput" 
        :error-messages="errors.libelleCourt? [errors.libelleCourt] : []"
        @focus="clearErrors"
      >
         <template v-slot:append>
            <v-icon v-if="errors.libelleCourt" color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
    </v-text-field>
      <div v-if="codeError" class="error-message">{{ codeErrorMessage }}</div>
        <v-autocomplete
        v-model="inputForm.ville"
        :items="dataListeVille"
        item-title="libelleLong"
        item-value="id"
        :label="$t('apps.forms.ville.nom')"
        dense
        outlined
        color="black"
        variant="outlined"
        prepend-inner-icon="mdi-city-variant-outline"
        clearable 
        :error-messages="errors.ville?[errors.ville]:[]"
        @focus="clearErrors"
      >
      <template v-slot:append>
            <v-icon v-if="errors.ville" color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
    </v-autocomplete>
        <v-select
              prepend-inner-icon="mdi-factory"
              name="typeCentre"
              density="compact"
              :label="$t('apps.forms.typeCentre.nom')"
              color="balck"
              v-model="inputForm.typeCentre"
              variant="outlined"
              :items="dataListeTypeCentre"
              persistent-hint
              
              single-line
              item-title="libelleLong"
              item-value="id"
              :error="inputForm.error" 
              :error-messages="errors.typeCentre?[errors.typeCentre]:[]"
              @focus="clearErrors"
        >
         <template v-slot:append>
            <v-icon v-if="errors.typeCentre" color="red">
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
import { reactive, getCurrentInstance,ref,watchEffect } from "vue";
import { onMounted } from "vue"
import { storeToRefs } from "pinia";
import { useCentreStore } from "../store";
import { useVilleStore } from "@/modules/ville/store";
import { useTypeCentreStore } from "@/modules/typeCentre/store";
import { useRouter } from 'vue-router';
import * as yup from 'yup';

const schema = yup.object().shape({
  libelleLong: yup.string().required('Le libelle est requis'),
  libelleCourt: yup.string().required('Le code est requis'),
  ville: yup.string().required('Veuillez selectionner une ville'),
  typeCentre: yup.string().required('Veuillez selectionner un type de centre'),
});
const router = useRouter();


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
const errors = reactive({
  libelleLong:'',
  libelleCourt:'',
  ville: null,
  typeCentre: null,
  error: false,
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
const checkCodeExistence = async () => {
  codeError.value = false;
  codeErrorMessage.value = "";
  if (inputForm.libelleCourt) {
    try {
      const isAvailable = await centreStore.checkCodeExistence(inputForm.libelleCourt);
      console.log("Résultat de la vérification du code (isAvailable) :", isAvailable);
      if (!isAvailable) {
        codeError.value = true;
        codeErrorMessage.value = "le code de ce centre   existe deja.";
        console.log('codeErrorMessage:', codeErrorMessage);
      }
    } catch (error) {
      console.error("Erreur lors de la vérification du code :", error);
      codeError.value = true;
      codeErrorMessage.value = "Erreur lors de la vérification du code. Veuillez réessayer.";
    }
  }
};
const checkLibelleExistenceUp = async () => {
  libelleError.value = false;
  libelleErrorMessage.value = "";
  if (inputForm.libelleLong) {
    const centreId= inputForm.id;
    const libelleLong = inputForm.libelleLong;
    try {
      const isAvailable = await centreStore.checkLibelleExistenceUp({centreId ,libelleLong});
      console.log("academie ,libelleLong :",centreId,libelleLong );
      console.log("Résultat de la vérification du libelle (isAvailable) :", isAvailable);
      if (!isAvailable) {
        libelleError.value = true;
        libelleErrorMessage.value = "Ce centre existe dèja.";
        console.log('libelleErrorMessage:', libelleErrorMessage);
      }
    } catch (error) {
      console.error("Erreur lors de la vérification du libelle :", error);
      libelleError.value = true;
      libelleErrorMessage.value = "Erreur lors de la vérification du libelle. Veuillez réessayer.";
    }
  }
};
const clearErrors = () => {
  errors.libelleLong = '';
  errors.libelleCourt = '';
  errors.ville = null;
  errors.typeCentre = null;
};
const onCodeInput = () => {
  // Vérifie s'il y a des espaces dans le matricule
  if (/\s/.test(inputForm.libelleCourt)) {
    // Si des espaces sont trouvés, affiche un message d'erreur
    codeError.value = true;
    codeErrorMessage.value = "Le code du centre ne doit pas contenir d'espaces.";
  } else {
    // Sinon, effectue la vérification normale de l'existence du matricule
    checkCodeExistence ();
  }
};

const onLibelleInput = () => { 
  if (isEdit) {
      checkLibelleExistenceUp();
    } else {
      checkLibelleExistence();
    } 
};
const { inputForm, actionSubmit,isEdit } = defineProps({
  inputForm: Object,
  isEdit: Boolean,
  actionSubmit: {
    type: Function,
  }
});
const redirectToListe = () => {
  router.push({ name: 'centre-liste'});
};
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
      } else if (err.path === 'libelleCourt') {
        errors.libelleCourt = err.message;
      } else if (err.path === 'ville') {
        errors.ville = err.message;
      } else if (err.path === 'typeCentre') {
        errors.typeCentre = err.message;
      }
    });
  } else {
    // Gérer d'autres erreurs ici, si nécessaire
    console.error(error);
  }
}
};

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
