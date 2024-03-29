<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">Ville</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="handleSave" ref="villeForm" :value="formValid">
      <v-text-field
        id="libelleLong"
        prepend-inner-icon="mdi-city"
        name="libelleLong"
        density="compact"
        :label="$t('apps.forms.ville.nom')"
        color="balck"
        v-model="inputForm.libelleLong"
        variant="outlined"
        @blur="onLibelleInput"
        :error-messages="errors.libelleLong ? [errors.libelleLong] : []"
        @focus="clearErrors"
      >
         <template v-if="errors.libelleLong"  v-slot:append>
            <v-icon color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
      </v-text-field>
      <div v-if="libelleError" class="error-message">{{ libelleErrorMessage }}</div>
      <v-text-field
        id="libelleCourt"
        prepend-inner-icon="mdi-city"
        name="libelleCourt"
        density="compact"
        :label="$t('apps.forms.ville.abreviation')"
        color="balck"
        v-model="inputForm.libelleCourt"
        variant="outlined"
        @blur="onCodeInput"
      >
    </v-text-field>
      <div v-if="codeError" class="error-message">{{ codeErrorMessage }}</div>
      <v-autocomplete
        prepend-inner-icon="mdi-school"
        name="academie"
        density="compact"
        :label="$t('apps.forms.academie.nom')"
        color="balck"
        v-model="inputForm.academie"
        variant="outlined"
        :items="dataListe"
        persistent-hint
        
        single-line
        item-title="libelleLong"
        item-value="id"
        clearable 
        :error-messages="errors.academie?[errors.academie]:[]"
        @focus="clearErrors"
      >
      <template v-if="errors.academie" v-slot:append>
            <v-icon  color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
    </v-autocomplete>

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
import * as yup from 'yup';

const schema = yup.object().shape({
  libelleLong: yup.string().required('Le libelle est requis'),
  // libelleCourt: yup.string().required('Le code est requis'),
  academie: yup.string().required('Veuillez selectionner une académie'),
});
const router = useRouter();

const instance = getCurrentInstance();
const academieStore = useAcademieStore();
const {dataListe}= storeToRefs(academieStore);
const villeStore = useVilleStore();

// const rules = reactive({
//   required: value => !!value || 'Champ obligatoire.',
//   min: v => v.length >= 2 || '2 cractére au moins',
// });
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
const errors = reactive({
  libelleLong:'',
  academie: null,
  error: false,
});
const checkLibelleExistenceUp = async () => {
  libelleError.value = false;
  libelleErrorMessage.value = "";
  if (inputForm.libelleLong) {
    const villeId= inputForm.id;
    const libelleLong = inputForm.libelleLong;
    try {
      const isAvailable = await villeStore.checkLibelleExistenceUp({villeId ,libelleLong});
      console.log("academie ,libelleLong :",villeId,libelleLong );
      console.log("Résultat de la vérification du libelle (isAvailable) :", isAvailable);
      if (!isAvailable) {
        libelleError.value = true;
        libelleErrorMessage.value = "Ce ville existe dèja.";
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
  errors.academie= null;
};
const onCodeInput = () => {
  // Vérifie s'il y a des espaces dans le matricule
  if (/\s/.test(inputForm.libelleCourt)) {
    // Si des espaces sont trouvés, affiche un message d'erreur
    codeError.value = true;
    codeErrorMessage.value = "Le code de l'académie ne doit pas contenir d'espaces.";
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
        errors.libelleCourt = err.message;
      } else if (err.path === 'academie') {
        errors.academie = err.message;
      } 
    });
  } else {
    // Gérer d'autres erreurs ici, si nécessaire
    console.error(error);
  }
}
};

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