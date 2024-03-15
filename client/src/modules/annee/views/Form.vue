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
        :rules="[rules.exactlylibelleLong]"
        v-model="inputForm.libelleLong"
        variant="outlined"
        @blur="onLibelleInput" 
      >
    </v-text-field>
    <div v-if="libelleError" class="error-message">{{ libelleErrorMessage }}</div>
    <div v-if="formSubmitted && !inputForm.libelleLong" class="required-message mb-0">
          Champ obligatoire
          <span class="required-icon">
            <i class="mdi mdi-alert"></i>
          </span>
        </div>

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
import * as yup from 'yup';
const router = useRouter();

const instance = getCurrentInstance();
const anneeStore=useAnneeStore();
const rules = reactive({
  exactlylibelleLong: value => value && value.length === 4 && /^\d+$/.test(value) || 'Le libelleLong doit comporter exactement 4 chiffres',  
});

const schema = yup.object().shape({
  libelleLong: yup.string().matches(/^\d{4}$/, 'Année invalide').required("L'année est requis'"),
 
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
        libelleErrorMessage.value = "L'annee   existe dèja.";
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
const  formSubmitted=ref(false);
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
    error.inner.forEach(err => {
      console.error(err.message);
    });
    console.log('Le formulaire contient des erreurs. Veuillez corriger et réessayer.');
    formSubmitted.value = true;
      console.log(formSubmitted)
  }
};

</script>
<style>
.error-message {
  color: red; /* ou toute autre couleur de votre choix */
  margin-top: 5px; /* Ajustez la marge en fonction de vos besoins */
}
.required-icon {
  color: orange; /* Couleur de l'icône pour les champs requis non remplis */
  margin-left: 5px; /* Ajustement de l'espacement entre le message d'erreur et l'icône */
}
.input-with-asterisk {
  position: relative; /* Permet de positionner l'astérisque par rapport à l'input */
}

.input-with-asterisk:after {
  content: "*"; /* Ajouter l'astérisque */
  color: red; /* Couleur rouge */
  font-size: larger; /* Taille de police plus grande */
  position: absolute; /* Position absolue */
  top: 0px; /* Ajuster la position verticale */
  right: 8px; /* Ajuster la position horizontale */
}
</style>