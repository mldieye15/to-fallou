<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-1"
      elevation="8"
      max-width="900"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.session.session') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="submit" ref="sessionForm" v-model="formValid">
      <v-row class="reduce-margin">
        <v-col>
          <v-text-field
        v-model="inputForm.delaisValidation"
        prepend-inner-icon="mdi-clock-time-four-outline"
        name="delaisValidation"
        density="compact"
        :label="$t('apps.forms.session.delaisValidation')"
        color="black"
        :rules="[rules.required, rules.validateHeur]"
        variant="solo"
      ></v-text-field>
        </v-col>
        <v-col>
          <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        name="typeSession"
        density="compact"
        :label="$t('apps.forms.typeSession.nom')"
        color="balck"
        v-model="inputForm.typeSession"
        variant="solo"
        :items="dataListeTypeSession"
        persistent-hint
        
        single-line
        item-title="libelleLong"
        item-value="id"
      ></v-select>
        </v-col>
        <v-col>
          <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        name="annee"
        density="compact"
        :label="$t('apps.forms.annee.libelle')"
        color="balck"
        v-model="inputForm.annee"
        variant="solo"
        :items="dataAnneeEnours"
        persistent-hint
        
        single-line
        item-title="libelleLong"
        item-value="id"
      ></v-select>
        </v-col>
      </v-row>
      <v-row >
        <v-col>
      <v-text-field 
        id="libelleLong"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="libelleLong"
        density="compact"
        :label="$t('apps.forms.session.nom')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="libelleLongSession"
        variant="solo"
        readonly
      ></v-text-field >
      </v-col>
      <v-col>
        <v-text-field
        id="nombreDemandeAutorise"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="nombreDemandeAutorise"
        density="compact"
        :label="$t('apps.forms.session.nombreDemandeAutorise')"
        color="balck"
        :rules="[rules.required,rules.validateNombre]"
        v-model="inputForm.nombreDemandeAutorise"
        variant="solo"
      ></v-text-field>
      </v-col>
       
    </v-row>
    <v-row >
      <v-col>
        <v-text-field
        id="dateDebut"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="dateDebut"
        density="compact"
        :label="$t('apps.forms.session.dateDebut')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.dateDebut"
        variant="solo"
        type="date"
         @input="validateDates"
      ></v-text-field>
      <div v-if="dateDebutError" class="error-message">{{ dateDebutErrorMessage }}</div>
      </v-col>
      <v-col>
        <v-text-field
        id="dateFin"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="dateFin"
        density="compact"
        :label="$t('apps.forms.session.dateFin')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.dateFin"
        type="date"
        variant="solo"
      ></v-text-field>
      </v-col>
    </v-row >
      <v-row >
        <v-col>
          <v-text-field
        id="dateDeOuvertureDepotCandidature"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="dateDeOuvertureDepotCandidature"
        density="compact"
        :label="$t('apps.forms.session.dateOuvertureDepotCandidature')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.dateOuvertureDepotCandidature"
        variant="solo"
        type="date"
        @change="validateDates"
        ></v-text-field>
        <div v-if="dateOuvertureError" class="error-message">{{ dateOuvertureErrorMessage }}</div>
        </v-col>
        <v-col>
      <v-text-field
        id="dateDeClotureDepotCandidature"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="dateDeClotureDepotCandidature"
        density="compact"
        :label="$t('apps.forms.session.dateClotureDepotCandidature')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.dateClotureDepotCandidature"
        variant="solo"
        type="date"
         @change="validateDates"
      ></v-text-field>
       <div v-if="dateClotureError" class="error-message">{{ dateClotureErrorMessage }}</div>
        </v-col>
      </v-row>
      <v-btn block class="mt-2 mb-8" size="large" color="blue" @click="handleSave" :disabled="!formValid">{{ $t('apps.forms.valider') }}</v-btn>
    </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance ,watchEffect,ref,computed} from "vue";
import { onMounted } from "vue"
import { storeToRefs } from "pinia";
import { useAnneeStore } from "@/modules/annee/store";
import { useTypeSessionStore } from "@/modules/typeSession/store";
import { format } from 'date-fns';
import { fr } from "date-fns/locale";

const instance = getCurrentInstance();

const anneeStore = useAnneeStore();
const typeSessionStore= useTypeSessionStore();
const { dataAnneeEnours } = storeToRefs(anneeStore);

const {dataListeTypeSession} = storeToRefs(typeSessionStore);
const rules = reactive({
  required: value => !!value || 'Champ obligatoire.',
  min: v => v.length >= 2 || '2 cractére au moins',
  validateHeur: v => /^[0-9]+$/.test(v) || 'Veuillez entrer un nombre valide d\'heures.',
  validateNombre: v => /^[0-9]+$/.test(v) || 'Veuillez entrer un nombre valide de demandes autorises.',
});
const formValid = ref(false);
const { inputForm, actionSubmit } = defineProps({
  inputForm: Object,
  actionSubmit: {
    type: Function,
  }
});
const dateDebutError = ref(false);
const dateDebutErrorMessage = ref("");
const dateOuvertureError = ref(false);
const dateOuvertureErrorMessage =ref("");
const dateClotureError = ref(false);
const dateClotureErrorMessage = ref("");
const isSubmitDisabled = ref(false);

const libelleLongSession = computed(() => {
  const session = inputForm.typeSession;
  const annee = inputForm.annee;
  const typeSessionLibelle = typeSessionStore.getLibelleById(session);
  const anneeLibelle = anneeStore.getLibelleById(annee);
  return `Session ${typeSessionLibelle} ${anneeLibelle}`.trim();
});
watchEffect(() => {
  validateDates();
  isSubmitDisabled.value=dateClotureError.value||dateOuvertureError.value||dateDebutError.value;
});
watchEffect(() => {
  if (
  inputForm.dateDebut,
  inputForm.dateFin,
  inputForm.dateOuvertureDepotCandidature,
  inputForm.dateClotureDepotCandidature 
  ) {
    // console.log('Avant formatage :', inputForm.dateClotureDepotCandidature);
    // console.log('Avant formatage :', inputForm.dateDebut);
    inputForm.dateDebut=formatDateForInput(inputForm.dateDebut);
    inputForm.dateFin=formatDateForInput(inputForm.dateFin);
    inputForm.dateOuvertureDepotCandidature= formatDateForInput(inputForm.dateOuvertureDepotCandidature);
    inputForm.dateClotureDepotCandidature = formatDateForInput(inputForm.dateClotureDepotCandidature);
  }
  // console.log('Après formatage :', inputForm.dateClotureDepotCandidature);
  // console.log('Apres formatage :', inputForm.dateDebut);
});

function formatDateForInput(date) {
  const formattedDate = format(new Date(date), 'yyyy-MM-dd', { locale: fr });
  return formattedDate;
}
function validateDates() {
  const dateOuverture = new Date(inputForm.dateOuvertureDepotCandidature);
  const dateCloture = new Date(inputForm.dateClotureDepotCandidature);
  const dateDebut = new Date(inputForm.dateDebut);
  const dateFin = new Date(inputForm.dateFin);

  // Réinitialisez les messages d'erreur à chaque validation
  dateOuvertureError.value=false;
  dateOuvertureErrorMessage.value = "";
  dateClotureError.value=false ;
  dateClotureErrorMessage.value = "";
  dateDebutError.value = false;
  dateDebutErrorMessage.value = "";

  if (dateOuverture <= dateDebut||dateOuverture>=dateFin) {
    dateOuvertureError.value=true
    dateOuvertureErrorMessage.value = "La date d'ouverture de dépôt de candidature doit être comprise entre la date de debut et la date de fin de session";
  }

  if (dateOuverture >= dateCloture) {
    dateOuvertureError.value=true
    dateOuvertureErrorMessage.value = "La date d'ouverture de dépôt de candidature doit être antérieure à la date de clôture de dépôt de candidature.";
  }

  if (dateDebut >= dateFin) {
    dateDebutError.value = true;
    dateDebutErrorMessage.value = "La date de début de la session doit être antérieure à la date de fin de la session.";
  }
  if (dateCloture >= dateFin) {
    dateClotureError.value = true;
    dateClotureErrorMessage.value = "La date de cloture de dépôt de candidature doit être antérieure à la date de fin de la session.";
  }
}

const handleSave = () => {
  console.log("isSubmitDisabled:", isSubmitDisabled.value);
  if(instance.refs.sessionForm.validate&&!isSubmitDisabled.value){
    actionSubmit(inputForm);
  }
}

onMounted(() => {
   anneeStore.anneeEnours();
    typeSessionStore.all();
  
});

</script>

<style>
.error-message {
  color: rgb(161, 13, 13); /* ou toute autre couleur de votre choix */
  margin-top: 5px; /* Ajustez la marge en fonction de vos besoins */
}
</style>

