<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.etablissement.etablissement') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="handleSave" ref="etablissementForm">
      <v-text-field
        id="libelleLong"
        prepend-inner-icon="mdi-domain"
        name="libelleLong"
        density="compact"
        :label="$t('apps.forms.etablissement.nom')"
        color="balck"
        v-model="inputForm.libelleLong"
        variant="outlined"
        @blur="onLibelleInput"
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
        prepend-inner-icon="mdi-key"
        name="libelleCourt"
        density="compact"
        :label="$t('apps.forms.etablissement.abreviation')"
        color="balck"
        v-model="inputForm.libelleCourt"
        variant="outlined"
        >
         <!-- <template v-slot:append>
            <v-icon v-if="errors.libelleCourt" color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template> -->
        </v-text-field>

      <v-select
        prepend-inner-icon="mdi-domain"
        name="typeEtablissement"
        density="compact"
        :label="$t('apps.forms.typeEtablissement.nom')"
        color="balck"
        v-model="inputForm.typeEtablissement"
        variant="outlined"
        :items="dataListeTypeEtablissement"
        persistent-hint
        single-line
        item-title="libelleLong"
        item-value="id"
        autocomplete="off"
        :error-messages="errors.typeEtablissement ? [errors.typeEtablissement] : []"
        @focus="clearErrors"
      >
      <template v-if="errors.typeEtablissement" v-slot:append>
            <v-icon  color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
        </v-select>
      <v-autocomplete
        v-model="inputForm.ville"
        :items="dataListeVille"
        item-title="libelleLong"
        item-value="id"
        :label="$t('apps.forms.ville.nom')"
        dense
        outlined
        variant="outlined"
        prepend-inner-icon="mdi-city"
        clearable
        autocomplete="off"
        :error-messages="errors.ville ? [errors.ville] : []"
        @focus="clearErrors"
      >
      <template v-if="errors.ville" v-slot:append>
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
import { reactive, getCurrentInstance ,ref,watchEffect} from "vue";
import { onMounted } from "vue"
import { storeToRefs } from "pinia";
import { useEtablissementStore } from "../store";
import { useTypeEtablissementStore } from "@/modules/typeEtablissement/store";
import { useVilleStore } from "@/modules/ville/store";
import * as yup from 'yup';

const schema = yup.object().shape({
  libelleLong: yup.string().required('Le libelle est requis'),
  ville: yup.string().required('Veuillez selectionner une ville'),
  typeEtablissement: yup.string().required('Veuillez selectionner un type d\'etablissement'),
});
import { useRouter } from 'vue-router';
const router = useRouter();

    
const redirectToListe = () => {
  router.push({ name: 'etablissement-liste'});
};

const instance = getCurrentInstance();
const typeEtablissementStore = useTypeEtablissementStore();
const etablissementStore =useEtablissementStore();
const { dataListeTypeEtablissement } = storeToRefs(typeEtablissementStore);
const villeStore = useVilleStore();
const { dataListeVille } = storeToRefs(villeStore);

const errors = reactive({
  libelleLong:'',
  ville: null,
  typeEtablissement: null,
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
      const isAvailable = await etablissementStore.checkLibelleExistence(inputForm.libelleLong);
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
// const checkCodeExistence = async () => {
//   codeError.value = false;
//   codeErrorMessage.value = "";
//   if (inputForm.libelleCourt) {
//     try {
//       const isAvailable = await etablissementStore .checkCodeExistence(inputForm.libelleCourt);
//       console.log("Résultat de la vérification du code (isAvailable) :", isAvailable);
//       if (!isAvailable) {
//         codeError.value = true;
//         codeErrorMessage.value = "le code de ce centre   existe deja.";
//         console.log('codeErrorMessage:', codeErrorMessage);
//       }
//     } catch (error) {
//       console.error("Erreur lors de la vérification du code :", error);
//       codeError.value = true;
//       codeErrorMessage.value = "Erreur lors de la vérification du code. Veuillez réessayer.";
//     }
//   }
// };
const checkLibelleExistenceUp = async () => {
  libelleError.value = false;
  libelleErrorMessage.value = "";
  if (inputForm.libelleLong) {
    const etablissementId= inputForm.id;
    const libelleLong = inputForm.libelleLong;
    try {
      const isAvailable = await etablissementStore.checkLibelleExistenceUp({etablissementId ,libelleLong});
      console.log("academie ,libelleLong :",etablissementId,libelleLong );
      console.log("Résultat de la vérification du libelle (isAvailable) :", isAvailable);
      if (!isAvailable) {
        libelleError.value = true;
        libelleErrorMessage.value = " Etablissement existe dèja.";
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
  errors.ville = null;
  errors.typeEtablissement = null;
};
// const onCodeInput = () => {
//   // Vérifie s'il y a des espaces dans le matricule
//   if (/\s/.test(inputForm.libelleCourt)) {
//     // Si des espaces sont trouvés, affiche un message d'erreur
//     codeError.value = true;
//     codeErrorMessage.value = "Le code du centre ne doit pas contenir d'espaces.";
//   } else {
//     // Sinon, effectue la vérification normale de l'existence du matricule
//     checkCodeExistence ();
//   }
// };

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
        errors.libelleLong = err.message;
      } else if (err.path === 'ville') {
        errors.ville = err.message;
      } else if (err.path === 'typeEtablissement') {
        errors.typeEtablissement = err.message;
      }
    });
  } else {
    // Gérer d'autres erreurs ici, si nécessaire
    console.error(error);
  }
}
};

onMounted(()=>{
  typeEtablissementStore.all();
  villeStore.all();
});

</script>
<style>
.error-message {
  color: red; /* ou toute autre couleur de votre choix */
  margin-top: 5px; /* Ajustez la marge en fonction de vos besoins */
}
</style>
