<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.jury.jury') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="handleSave" ref="juryForm">
      <v-select
        prepend-inner-icon="mdi-calendar"
        name="session"
        density="compact"
        :label="$t('apps.forms.session.nom')"
        color="balck"
        v-model="inputForm.session"
        variant="outlined"
        :items="dataListeSession"
        persistent-hint
        
        single-line
        item-title="libelleLong"
        item-value="id"
        :error-messages="errors.session ? [errors.session] : []"
        @focus="clearErrors"
      >
         <template v-if="errors.session"  v-slot:append>
            <v-icon color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
      </v-select>
      <v-text-field
        id="numero"
        prepend-inner-icon="mdi-numeric"
        name="numero"
        density="compact"
        :label="$t('apps.forms.jury.numero')"
        color="balck"
        v-model="inputForm.numero"
        variant="outlined"
        :error-messages="errors.numero ? [errors.numero] : []"
        @focus="clearErrors"
        @blur="onNumeroInput"
      >
         <template v-if="errors.numero"  v-slot:append>
            <v-icon color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
      </v-text-field>
      <div v-if="numeroError" class="error-message">{{ numeroErrorMessage }}</div>
      <v-autocomplete
        v-model="inputForm.centre"
        :items="dataListeCentre"
        item-title="libelleLong"
        item-value="id"
        :label="$t('apps.forms.centre.nom')"
        dense
        outlined
        variant="outlined"
        prepend-inner-icon="mdi-pencil-box"
        clearable
        :error-messages="errors.centre ? [errors.centre] : []"
        @focus="clearErrors"
      >
         <template v-if="errors.centre"  v-slot:append>
            <v-icon color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
      </v-autocomplete>
      
      <v-text-field
        id="nom"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="nom"
        density="compact"
        :label="$t('apps.forms.jury.nom')"
        color="balck"
        v-model="nomJury"
        variant="outlined"
        readonly
      >
      </v-text-field>
      <div v-if="nomError" class="error-message">{{ nomErrorMessage }}</div>
      <div class="d-flex justify-end">
        <v-btn class="mt-8 mb-8 mr-2" color="red" @click.prevent="redirectToListe()">{{ $t('apps.forms.annuler') }}</v-btn>
        <v-btn class="mt-8 mb-8" color="blue" @click="handleSave">{{ $t('apps.forms.valider') }}</v-btn>
      </div>
    </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance } from "vue";
import { onMounted,ref,watchEffect,computed } from "vue"
import { storeToRefs } from "pinia";
import { useCentreStore } from "@/modules/centre/store";
import { useSessionStore } from "@/modules/session/store";
import { useJuryStore } from "../store";
import { useRouter } from 'vue-router';
import * as yup from 'yup';

const schema = yup.object().shape({
  session: yup.string().required('Veuillez selectionner une session'),
  numero: yup
  .number('le numéro doit etre un chiffre')
  .required('Le numero est requis')
  .typeError('Le numero doit être un nombre')
  .min(0, 'Le numero ne peut pas être négatif'),
   centre: yup.string().required('Veuillez selectionner un centre'),
});
const router = useRouter();

const instance = getCurrentInstance();
const juryStore=useJuryStore();
const centreStore = useCentreStore();
const { dataListeCentre } = storeToRefs(centreStore);
const sessionStore = useSessionStore();
const{dataListeSession}=storeToRefs(sessionStore);

const numeroError = ref(false);
const numeroErrorMessage = ref("");
const isSubmitDisabled = ref(false);

const { inputForm, actionSubmit,isEdit } = defineProps({
  inputForm: Object,
  isEdit: Boolean,
  actionSubmit: {
    type: Function,
  }
});
const checkNumeroExistence = async () => {
  numeroError.value = false;
  numeroErrorMessage.value = "";
  console.log('Vérification avant la condition (numero) :', numero);
  if (inputForm.numero) {
    console.log('Vérification dans la condition (numero) :', numero);
    try {
      const isAvailable = await juryStore.checkNumeroExistence(inputForm.numero);
      console.log("numero  :", inputForm.numero);
      console.log("Résultat de la vérification du numero (isAvailable) :", isAvailable);
      if (!isAvailable) {
        numeroError.value = true;
        numeroErrorMessage.value = "Ce numero  est déjà utilisé pour cette année.";
        console.log('numeroErrorMessage:', numeroErrorMessage);
      }
    } catch (error) {
      console.error("Erreur lors de la vérification du numero :", error);
      numeroError.value = true;
      numeroErrorMessage.value = "Erreur lors de la vérification du numero. Veuillez réessayer.";
    }
  }
};
const checkNumeroExistenceUp = async () => {
  numeroError.value = false;
  numeroErrorMessage.value = "";
  console.log('Vérification avant la condition (numero) :', numero);
  if (numero.value) {
    const juryId= inputForm.id;
    const numero = inputForm.numero;
    console.log('Vérification dans la condition (numero) :', numero);
    try {
      const isAvailable = await juryStore.checkNumeroExistenceUp({juryId, numero});
      console.log("numero et jury  :",juryId, numero);
      console.log("Résultat de la vérification du numero (isAvailable) :", isAvailable);
      if (!isAvailable) {
        numeroError.value = true;
        numeroErrorMessage.value = "Ce numero  est déjà utilisé pour cette année.";
        console.log('numeroErrorMessage:', numeroErrorMessage);
      }
    } catch (error) {
      console.error("Erreur lors de la vérification du numero :", error);
      numeroError.value = true;
      numeroErrorMessage.value = "Erreur lors de la vérification du numero. Veuillez réessayer.";
    }
  }
};
const clearErrors = () => {
  errors.session = null;
  errors.numero = null;
  errors.centre= null;
};
const redirectToListe = () => {
  router.push({ name: 'jury-liste'});
};
// const numeroJury = computed(() => {
//   const numero = inputForm.numero;
//   const session = inputForm.session;
//   const nomLabel = sessionStore.getAnneBySessionId(session);
//   return `Jury N° ${numero} de ${nomLabel}`.trim();
// });
const getNomJury = () => {
  const numero = inputForm.numero;
  const session = inputForm.session;
  const centre=inputForm.centre;
  const labelCentre=centreStore.getCentreById(centre);
  const nomLabel = sessionStore.getAnneBySessionId(session);
  return `JURY${numero}${labelCentre}${nomLabel}`.trim();
};
const nomJury = ref(getNomJury());
watchEffect(() => {
  isSubmitDisabled.value = numeroError.value; 
  nomJury.value = getNomJury();
});
const errors = reactive({
  session:null,
  numero:null,
  centre: null,
  error: false,
});

const onNumeroInput = () => { 
  if (isEdit) {
    checkNumeroExistenceUp();
    } else {
      checkNumeroExistence();
    } 
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
      if (err.path === 'session') {
        errors.session = err.message;
      } else if (err.path === 'numero') {
        errors.numero = err.message;
      } else if (err.path === 'centre') {
        errors.centre = err.message;
      } 
    });
  } else {
    // Gérer d'autres erreurs ici, si nécessaire
    console.error(error);
  }
}
};

onMounted(()=>{
  centreStore.all();
  sessionStore.enCoursSession();
});

</script>
<style>
.error-message {
  color: red; /* ou toute autre couleur de votre choix */
  margin-top: 5px; /* Ajustez la marge en fonction de vos besoins */
}
</style>