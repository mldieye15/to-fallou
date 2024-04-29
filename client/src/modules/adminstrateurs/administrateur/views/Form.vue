<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-1"
      elevation="8"
      max-width="900"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.user.admin') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="handleSave" ref="userForm">
      <v-row >
        <v-col>
      <v-text-field 
        id="prenoms"
        prepend-inner-icon="mdi-account"
        name="prenoms"
        density="compact"
        :label="$t('apps.forms.user.prenoms')"
        color="balck"
        v-model="inputForm.prenoms"
        variant="outlined" 
      ></v-text-field >
      <div v-if="formSubmitted && !inputForm.prenoms" class="required-message mb-0">
          Champ obligatoire
          <span class="required-icon">
            <i class="mdi mdi-alert"></i>
          </span>
        </div>
      </v-col>
      <v-col sm="6" cols="12">
        <v-text-field
        id="nom"
        prepend-inner-icon="mdi-account"
        name="nom"
        density="compact"
        :label="$t('apps.forms.user.nom')"
        color="balck"
        v-model="inputForm.nom"
        variant="outlined"
      ></v-text-field>
      <div v-if="formSubmitted && !inputForm.nom" class="required-message mb-0">
          Champ obligatoire
          <span class="required-icon">
            <i class="mdi mdi-alert"></i>
          </span>
        </div>
      </v-col>
       
    </v-row>
    <v-row >
        <v-col>
      <v-text-field 
        id="matricule"
        prepend-inner-icon="mdi-card-account-details"
        name="matricule"
        density="compact"
        :label="$t('apps.forms.user.matricule')"
        color="balck"
        v-model="inputForm.matricule"
        variant="outlined" 
        @blur="onMatriculeInput"
    >
    </v-text-field >
    <div v-if="matriculeError" class="error-message">{{ matriculeErrorMessage }}</div>
    <div v-if="formSubmitted && !inputForm.matricule" class="required-message mb-0">
          Champ obligatoire
          <span class="required-icon">
            <i class="mdi mdi-alert"></i>
          </span>
        </div>
      </v-col>
      <v-col>
        <v-select
        id="sexe"
        prepend-inner-icon="mdi-gender-male-female"
        name="sexe"
        density="compact"
        :label="$t('apps.forms.user.sexe')"
        color="balck"
        v-model="inputForm.sexe"
        variant="outlined"
        :items="['Homme', 'Femme']"
        autocomplete="off"
      >
    </v-select>
    <div v-if="formSubmitted && !inputForm.sexe" class="required-message mb-0">
          Champ obligatoire
          <span class="required-icon">
            <i class="mdi mdi-alert"></i>
          </span>
        </div>
      </v-col> 
    </v-row>
    <v-row >
      <v-col>
        <v-text-field
        id="mdpasse"
        prepend-inner-icon="mdi-lock"
        name="mdpasse"
        density="compact"
        :label="$t('apps.forms.user.mdpasse')"
        color="balck"
        v-model="inputForm.mdpasse"
        variant="outlined"
      ></v-text-field>
      <div v-if="formSubmitted && !inputForm.mdpasse" class="required-message mb-0">
          Champ obligatoire
          <span class="required-icon">
            <i class="mdi mdi-alert"></i>
          </span>
        </div>
      </v-col>
    </v-row >
      <v-row >
        <v-col>
          <v-text-field
        id="telephone"
        prepend-inner-icon="mdi-phone"
        name="telephone"
        density="compact"
        :label="$t('apps.forms.user.telephone')"
        color="balck"
        :rules="[rules.exactlynumeroTelephone]"
        v-model="inputForm.telephone"
        variant="outlined"
        maxlength="9"
      ></v-text-field>
      <div v-if="formSubmitted && !inputForm.telephone" class="required-message mb-0">
          Champ obligatoire
          <span class="required-icon">
            <i class="mdi mdi-alert"></i>
          </span>
        </div>
        </v-col>
        <v-col>
        <v-text-field
        id="email"
        prepend-inner-icon="mdi-email"
        name="email"
        density="compact"
        :label="$t('apps.forms.user.email')"
        color="balck"
        v-model="inputForm.email"
        variant="outlined"
        @blur="onEmailInput"
      >
    </v-text-field>
    <div v-if="emailError" class="error-message ma-1">{{ emailErrorMessage }}</div>
    <div v-if="formSubmitted && !inputForm.email" class="required-message mb-0">
          Champ obligatoire
          <span class="required-icon">
            <i class="mdi mdi-alert"></i>
          </span>
        </div>
      </v-col> 
      </v-row>
      <div class="d-flex justify-end">
        <v-btn class="mt-8 mb-8 mr-2" color="red" @click.prevent="redirectToUsers()">{{ $t('apps.forms.annuler') }}</v-btn>
        <v-btn class="mt-8 mb-8" color="blue" @click="handleSave">{{ $t('apps.forms.ajouter') }}</v-btn>
      </div>
    </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance,watchEffect,ref} from "vue";
import { useAdminStore } from "../../store";
import { onMounted } from "vue"
import { storeToRefs } from "pinia";
import { useFonctionStore } from "@/modules/fonction/store";
import { useEtablissementStore } from "@/modules/etablissement/store";
import { useCodeStore } from "@/store/codification";
import { format } from 'date-fns';
import { fr } from "date-fns/locale";
import { useRouter } from "vue-router";
import * as yup from 'yup';

const router = useRouter();
const  formSubmitted=ref(false);
const instance = getCurrentInstance();
const adminStore= useAdminStore();
const fonctionStore = useFonctionStore();
const etablissementStore= useEtablissementStore();
const codeStore = useCodeStore();
const { dataListe } = storeToRefs(fonctionStore);
const { dataListeEtab } = storeToRefs(etablissementStore);
const rules = reactive({
  min: v => v.length >= 5 || '5 cractére au moins',
  exactlynumeroTelephone: value => {
    return (
      value &&
      value.length === 9 &&
      /^(77|76|75|78|70)\d+$/.test(value) && // Vérifie le préfixe
      /^\d+$/.test(value) // Vérifie que tous les caractères sont des chiffres
    ) || 'Le numéro de téléphone invalide';
  },
  
});
const schema = yup.object().shape({
  prenoms: yup.string().required('Le prénom est requis'),
  nom: yup.string().required('Le nom est requis'),
  email: yup.string().email('Adresse email invalide').required('L\'adresse email est requise'),
  mdpasse: yup.string().required('Le mot de passe est requis'),
  telephone: yup.string().matches(/^(77|76|75|70|78)\d{7}$/, 'Numéro de téléphone invalide').required('Le numéro de téléphone est requis'),
  matricule: yup.string().required('Le matricule est requis'),
  sexe: yup.string().required('Le sexe est requis'),
 
});

const { inputForm, actionSubmit } = defineProps({
  inputForm: Object,
  actionSubmit: {
    type: Function,
  }
});

const emailError = ref(false);
const emailErrorMessage = ref("");
const matriculeError = ref("");
const matriculeErrorMessage = ref("");
const isSubmitDisabled = ref(false);
watchEffect(() => {
  isSubmitDisabled.value = emailError.value ||matriculeError.value
});

const checkEmailExistence = async () => {
  emailError.value = false;
  emailErrorMessage.value = "";
  if (inputForm.email) {
    try {
      const isAvailable = await adminStore.checkEmailExistence(inputForm.email);
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
const checkMatriculeExistence = async () => {
  matriculeError.value = false;
  matriculeErrorMessage.value = "";
  if (inputForm.matricule) {
    try {
      const isAvailable = await adminStore.checkMatriculeExistence(inputForm.matricule);
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
const onEmailInput = () => {
  // Vérifie s'il y a des espaces dans l'email
  if (/\s/.test(inputForm.email)) {
    // Si des espaces sont trouvés, affiche un message d'erreur
    emailError.value = true;
    emailErrorMessage.value = "L'adresse e-mail ne doit pas contenir d'espaces.";
  } else {
    // Sinon, effectue la vérification normale de l'existence de l'email
    checkEmailExistence(); 
  }
};
const onMatriculeInput = () => {
  // Vérifie s'il y a des espaces dans le matricule
  if (/\s/.test(inputForm.matricule)) {
    // Si des espaces sont trouvés, affiche un message d'erreur
    matriculeError.value = true;
    matriculeErrorMessage.value = "Le matricule ne doit pas contenir d'espaces.";
  } else {
    // Sinon, effectue la vérification normale de l'existence du matricule
    checkMatriculeExistence();
  }
};
const redirectToUsers = () => {
  router.push({ name: 'admin-liste'});
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