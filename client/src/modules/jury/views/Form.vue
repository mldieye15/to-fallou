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
        prepend-inner-icon="mdi-alpha-a-circle"
        name="session"
        density="compact"
        :label="$t('apps.forms.session.nom')"
        color="balck"
        v-model="inputForm.session"
        variant="solo"
        :items="dataListeSession"
        persistent-hint
        
        single-line
        item-title="libelleLong"
        item-value="id"
      ></v-select>
      <v-text-field
        id="numero"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="numero"
        density="compact"
        :label="$t('apps.forms.jury.numero')"
        color="balck"
        :rules="[rules.required,rules.validateNombre]"
        v-model="inputForm.numero"
        variant="solo"
      >
      </v-text-field>
      <v-autocomplete
        v-model="inputForm.centre"
        :items="dataListeCentre"
        item-title="libelleLong"
        item-value="id"
        :label="$t('apps.forms.centre.nom')"
        dense
        outlined
        variant="solo"
        prepend-inner-icon="mdi-alpha-a-circle"
        clearable
      ></v-autocomplete>
      
      <v-text-field
        id="nom"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="nom"
        density="compact"
        :label="$t('apps.forms.jury.nom')"
        color="balck"
        v-model="nomJury"
        variant="solo"
        readonly
        @blur="checkNomExistence"
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
const router = useRouter();

const instance = getCurrentInstance();
const juryStore=useJuryStore();
const centreStore = useCentreStore();
const { dataListeCentre } = storeToRefs(centreStore);
const sessionStore = useSessionStore();
const{dataListeSession}=storeToRefs(sessionStore);

const rules = reactive({
  required: value => !!value || 'Champ obligatoire.',
  validateNombre: v => /^[0-9]+$/.test(v) && !/\s/.test(v) || 'Veuillez entrer un numero de jury valide.',
});
const nomError = ref(false);
const nomErrorMessage = ref("");
const isSubmitDisabled = ref(false);
const checkNomExistence = async () => {
  nomError.value = false;
  nomErrorMessage.value = "";
  console.log('Vérification avant la condition (nomJury) :', nomJury);
  if (nomJury.value) {
    console.log('Vérification dans la condition (nomJury) :', nomJury);
    try {
      const isAvailable = await juryStore.checkNomExistence(nomJury.value);
      console.log("Résultat de la vérification du nom (isAvailable) :", isAvailable);
      if (!isAvailable) {
        nomError.value = true;
        nomErrorMessage.value = "Ce nom  est déjà utilisé.";
        console.log('nomErrorMessage:', nomErrorMessage);
      }
    } catch (error) {
      console.error("Erreur lors de la vérification du nom :", error);
      nomError.value = true;
      nomErrorMessage.value = "Erreur lors de la vérification du nom. Veuillez réessayer.";
    }
  }
};
const redirectToListe = () => {
  router.push({ name: 'jury-liste'});
};
const { inputForm, actionSubmit } = defineProps({
  inputForm: Object,
  actionSubmit: {
    type: Function,
  }
});
// const nomJury = computed(() => {
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
  isSubmitDisabled.value = nomError.value; 
  nomJury.value = getNomJury();
});

const handleSave = () => {
  if(!isSubmitDisabled.value){
    actionSubmit(inputForm);
  }
}

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