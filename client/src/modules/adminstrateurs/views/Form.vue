<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-1"
      elevation="8"
      max-width="900"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.user.user') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="submit" ref="userForm">
      <v-row style="height: 15vh">
        <v-col>
      <v-text-field 
        id="prenoms"
        prepend-inner-icon="mdi-account"
        name="prenoms"
        density="compact"
        :label="$t('apps.forms.user.prenoms')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.prenoms"
        variant="solo" 
      ></v-text-field >
      </v-col>
      <v-col sm="6" cols="12">
        <v-text-field
        id="nom"
        prepend-inner-icon="mdi-account"
        name="nom"
        density="compact"
        :label="$t('apps.forms.user.nom')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.nom"
        variant="solo"
      ></v-text-field>
      </v-col>
       
    </v-row>
    <v-row style="height: 15vh">
        <v-col>
      <v-text-field 
        id="matricule"
        prepend-inner-icon="mdi-card-account-details"
        name="matricule"
        density="compact"
        :label="$t('apps.forms.user.matricule')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.matricule"
        variant="solo" 
        @blur="checkMatriculeExistence"
    >
    </v-text-field >
    <div v-if="matriculeError" class="error-message">{{ matriculeErrorMessage }}</div>
      </v-col>
      <v-col>
        <v-text-field
        id="dateNaiss"
        prepend-inner-icon="mdi-calendar"
        name="dateNaiss"
        density="compact"
        :label="$t('apps.forms.user.dateNaiss')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.dateNaiss"
        variant="solo"
        type="date"
      ></v-text-field>
      </v-col> 
    </v-row>
    <v-row style="height: 15vh">
      <v-col>
        <v-text-field
        id="username"
        prepend-inner-icon="mdi-account-circle"
        name="username"
        density="compact"
        :label="$t('apps.forms.user.username')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.username"
        variant="solo" 
        @blur="checkUsernameExistence"   
      >
    </v-text-field> 
    
    <div v-if="usernameError" class="error-message">{{ usernameErrorMessage }}</div> 
      </v-col>
      <v-col>
        <v-text-field
        id="mdpasse"
        prepend-inner-icon="mdi-lock"
        name="mdpasse"
        density="compact"
        :label="$t('apps.forms.user.mdpasse')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.mdpasse"
        variant="solo"
      ></v-text-field>
      </v-col>
      <v-col>
        <v-select
        id="sexe"
        prepend-inner-icon="mdi-gender-male-female"
        name="sexe"
        density="compact"
        :label="$t('apps.forms.user.sexe')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.sexe"
        variant="solo"
        :items="['Homme', 'Femme']"
      >
    </v-select>
      </v-col>
    </v-row >
      <v-row style="height: 15vh">
        <v-col>
          <v-text-field
        id="telephone"
        prepend-inner-icon="mdi-phone"
        name="telephone"
        density="compact"
        :label="$t('apps.forms.user.telephone')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.telephone"
        variant="solo"
      ></v-text-field>
        </v-col>
        <v-col>
        <v-text-field
        id="email"
        prepend-inner-icon="mdi-email"
        name="email"
        density="compact"
        :label="$t('apps.forms.user.email')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.email"
        variant="solo"
        @blur="checkEmailExistence"
      >
    </v-text-field>
    <div v-if="emailError" class="error-message ma-1">{{ emailErrorMessage }}</div>
      </v-col> 
      </v-row>
      <v-btn block class="mt-8 mb-8" size="large" color="blue" @click="handleSave">{{ $t('apps.forms.enregistrer') }}</v-btn>
    </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance,watchEffect,ref} from "vue";
import { useUtilisateurStore } from "../store";
import { onMounted } from "vue"
import { storeToRefs } from "pinia";
import { useFonctionStore } from "@/modules/fonction/store";
import { useEtablissementStore } from "@/modules/etablissement/store";
import { useCodeStore } from "@/store/codification";
import { format } from 'date-fns';
import { fr } from "date-fns/locale";

const instance = getCurrentInstance();
const utilisateurStore= useUtilisateurStore();
const fonctionStore = useFonctionStore();
const etablissementStore= useEtablissementStore();
const codeStore = useCodeStore();
const { dataListe } = storeToRefs(fonctionStore);
const { dataListeEtab } = storeToRefs(etablissementStore);
const rules = reactive({
  required: value => !!value || 'Champ obligatoire.',
  min: v => v.length >= 2 || '2 cractére au moins',
});

const { inputForm, actionSubmit } = defineProps({
  inputForm: Object,
  actionSubmit: {
    type: Function,
  }
});

const codeError = ref(false);
const codeErrorMessage = ref("");
const emailError = ref(false);
const emailErrorMessage = ref("");
const usernameError = ref(false);
const usernameErrorMessage = ref("");
const matriculeError = ref("");
const matriculeErrorMessage = ref("");
const isSubmitDisabled = ref(false);
watchEffect(() => {
  isSubmitDisabled.value = codeError.value||emailError.value ||matriculeError.value||usernameError.value
});

const checkCodeValidity = async () => {
  codeError.value = false;
  codeErrorMessage.value = "";

  if (inputForm.code && inputForm.email) {
    try {
      const isCodeValid = await codeStore.verifyCode(inputForm.code, inputForm.email);
      console.log("Après la vérification de l'e-mail");

      if (!isCodeValid) {
        codeError.value = true;
        codeErrorMessage.value = "Code invalide. Veuillez vérifier le code saisi.";
      }
    } catch (error) {
      console.error("Erreur lors de la vérification du code :", error);
      codeError.value = true;
      codeErrorMessage.value = "Erreur lors de la vérification du code. Veuillez réessayer.";
    }
  }
};
const checkEmailExistence = async () => {
  emailError.value = false;
  emailErrorMessage.value = "";
  if (inputForm.email) {
    try {
      const isAvailable = await utilisateurStore.checkEmailExistence(inputForm.email);
      console.log("Résultat de la vérification du email (isAvailable) :", isAvailable);
      if (!isAvailable) {
        emailError.value = true;
        emailErrorMessage.value = "Cet email  est déjà utilisé.";
        console.log('emailErrorMessage:', emailErrorMessage);
      }
    } catch (error) {
      console.error("Erreur lors de la vérification de l'email :", error);
      emailError.value = true;
      emailErrorMessage.value = "Erreur lors de la vérification de l'email. Veuillez réessayer.";
    }
  }
};
const checkUsernameExistence = async () => {
  usernameError.value = false;
  usernameErrorMessage.value = "";
  if (inputForm.username) {
    try {
      const isAvailable = await utilisateurStore.checkUsernameExistence(inputForm.username);
      console.log("Résultat de la vérification du nom d'utilisateur (isAvailable) :", isAvailable);
      if (!isAvailable) {
        usernameError.value = true;
        usernameErrorMessage.value = "Ce nom d'utilisateur est déjà utilisé.";
        console.log('usernameErrorMessage:', usernameErrorMessage);
      }
    } catch (error) {
      console.error("Erreur lors de la vérification du nom d'utilisateur :", error);
      usernameError.value = true;
      usernameErrorMessage.value = "Erreur lors de la vérification du nom d'utilisateur. Veuillez réessayer.";
    }
  }
};
const checkMatriculeExistence = async () => {
  matriculeError.value = false;
  matriculeErrorMessage.value = "";
  if (inputForm.matricule) {
    try {
      const isAvailable = await utilisateurStore.checkMatriculeExistence(inputForm.matricule);
      console.log("Résultat de la vérification du matricule (isAvailable) :", isAvailable);
      if (!isAvailable) {
        matriculeError.value = true;
        matriculeErrorMessage.value = "Ce matricule  est déjà utilisé.";
        console.log('matriculeErrorMessage:', matriculeErrorMessage);
      }
    } catch (error) {
      console.error("Erreur lors de la vérification du matricule :", error);
      matriculeError.value = true;
      matriculeErrorMessage.value = "Erreur lors de la vérification du matricule. Veuillez réessayer.";
    }
  }
};
watchEffect(() => {
  if (inputForm.dateNaiss) {
    inputForm.dateNaiss = formatDateForInput(inputForm.dateNaiss);
  }
});
function formatDateForInput(date) {
  const formattedDate = format(new Date(date), 'yyyy-MM-dd', { locale: fr });
  return formattedDate;
};
const handleSave = () => {
  console.log("isSubmitDisabled:", isSubmitDisabled.value);
  if(instance.refs.userForm.validate() && !isSubmitDisabled.value){
    actionSubmit(inputForm);
  }
};

</script>
<style>
.error-message {
  color: red; /* ou toute autre couleur de votre choix */
  margin-top: 5px; /* Ajustez la marge en fonction de vos besoins */
}
</style>