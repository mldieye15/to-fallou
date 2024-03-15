<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-1"
      elevation="8"
      max-width="900"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">Mon profil</h2>
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
        class="input-with-asterisk"
      ></v-text-field >
      <div v-if="formSubmitted && !inputForm.prenoms" class="required-message mb-0">
          Champ obligatoire
          <span class="required-icon">
            <i class="mdi mdi-alert"></i>
          </span>
        </div>
      </v-col>
      <v-col sm="4" cols="12">
        <v-text-field
        id="nom"
        prepend-inner-icon="mdi-account"
        name="nom"
        density="compact"
        :label="$t('apps.forms.user.nom')"
        color="balck"
        v-model="inputForm.nom"
        variant="outlined"
        class="input-with-asterisk"
        
      ></v-text-field >
      <div v-if="formSubmitted && !inputForm.nom" class="required-message mb-0">
          Champ obligatoire
          <span class="required-icon">
            <i class="mdi mdi-alert"></i>
          </span>
        </div>
      </v-col>
    </v-row>
    <v-row>
      <v-col >
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
        class="input-with-asterisk"
        
      >
    </v-select>
    <div v-if="formSubmitted && !inputForm.sexe" class="required-message mb-0">
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
        class="input-with-asterisk"
        readonly
      >
    </v-text-field>
    <div v-if="formSubmitted && !inputForm.email" class="required-message mb-0">
          Champ obligatoire
          <span class="required-icon">
            <i class="mdi mdi-alert"></i>
          </span>
        </div>
    <div v-if="emailError" class="error-message">{{ emailErrorMessage }}</div>
      </v-col>
    </v-row>
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
        class="input-with-asterisk"
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
        id="matricule"
        prepend-inner-icon="mdi-card-account-details"
        name="matricule"
        density="compact"
        :label="$t('apps.forms.user.matricule')"
        color="balck"
        v-model="inputForm.matricule"
        variant="outlined" 
        @blur="onMatriculeInput"
        class="input-with-asterisk"
        
    >
    </v-text-field >
    <div v-if="formSubmitted && !inputForm.matricule" class="required-message mb-0">
          Champ obligatoire
          <span class="required-icon">
            <i class="mdi mdi-alert"></i>
          </span>
        </div>
    <div v-if="matriculeError" class="error-message">{{ matriculeErrorMessage }}</div>
      </v-col>
      </v-row>      
       <!-- <v-btn v-if="!isEditing" block class="mt-2" size="large" color="orange" @click="startEditing">
    Mise à jour de mes informations
    </v-btn> -->

    <!-- Bouton pour enregistrer les modifications -->
    <!-- <v-btn  block class="mt-2" size="large" color="blue" @click="handleSave">
      {{ $t('apps.forms.enregistrer') }}
    </v-btn> -->
     <div class="d-flex justify-end">
        <v-btn class="mt-8 mb-8 mr-2" color="red" @click.prevent="redirectToAccueil()">{{ $t('apps.forms.annuler') }}</v-btn>
        <v-btn class="mt-8 mb-8" color="blue" @click="handleSave">{{ $t('apps.forms.valider') }}</v-btn>
      </div>
    </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance,watchEffect,ref} from "vue";
import { useAdminStore } from "@/modules/adminstrateurs/store";
import { useUtilisateurStore } from "@/modules/user/store";
import { onMounted } from "vue"
import { storeToRefs } from "pinia";
import { useFonctionStore } from "@/modules/fonction/store";
import { useEtablissementStore } from "@/modules/etablissement/store";
import { useCodeStore } from "@/store/codification";
import { format } from 'date-fns';
import { fr } from "date-fns/locale";
import { useRouter } from "vue-router";
import * as yup from 'yup';
import { useUserStore } from "@/store/user";
const currentUser = useUserStore();
const {current} = currentUser;
const router = useRouter();
const { dataDetails, loading } = storeToRefs(currentUser);
const instance = getCurrentInstance();
const adminStore= useAdminStore();
const {upProfile}=adminStore;
const fonctionStore = useFonctionStore();
const etablissementStore= useEtablissementStore();
const utilisateurStore= useUtilisateurStore();
const codeStore = useCodeStore();
const { dataListe } = storeToRefs(fonctionStore);
const { dataListeEtab } = storeToRefs(etablissementStore);
  const rules = reactive({
  required: value => !!value || 'Champ obligatoire.',
  min: v => v.length >= 5 || '5 cractére au moins',
  exactlynumeroCompte: value => value && value.length === 12 && /^\d+$/.test(value) || 'Le numéro de compte doit comporter exactement 12 chiffres',
  exactlycodeBanque: value => value && value.length === 5 && /^[a-zA-Z0-9]+$/.test(value) || 'Le code banque doit comporter exactement 5 caractères',
  exactlycodeAgence: value => value && value.length === 5 && /^\d+$/.test(value) || 'Le code agence doit comporter exactement 5 chiffres',
  exactlycleRib: value => value && value.length === 2 && /^\d+$/.test(value) || 'Le clé rip doit comporter exactement 2 chiffres',
  exactlynumeroTelephone: value => value && value.length === 9 && /^\d+$/.test(value) || 'Le numéro de téléphone doit comporter exactement 9 chiffres',
  
});
const schema = yup.object().shape({
  prenoms: yup.string().required('Le prénom est requis'),
  nom: yup.string().required('Le nom est requis'),
  email: yup.string().email('Adresse email invalide').required('L\'adresse email est requise'),
  telephone: yup.string().matches(/^\d{9}$/, 'Numéro de téléphone invalide').required('Le numéro de téléphone est requis'),
  matricule: yup.string().required('Le matricule est requis'),
  sexe: yup.string().required('Le sexe est requis'),
});
const inputForm = reactive({
  id:"",
  prenoms: "",
  nom: "",
  matricule: "",
  email: "",
  sexe: "",
  telephone: "",
});
const  formSubmitted=ref(false);
const redirectToAccueil = () => {
  router.push({ name: 'dashboard'});
};
const isEditing = ref(false);
const startEditing = () => {
  isEditing.value = true;
};
const emailError = ref(false);
const emailErrorMessage = ref("");
const matriculeError = ref("");
const matriculeErrorMessage = ref("");
const isSubmitDisabled = ref(false);
watchEffect(() => {
  isSubmitDisabled.value = emailError.value ||matriculeError.value
});

const checkEmailExistenceUp = async () => {
  emailError.value = false;
  emailErrorMessage.value = "";
  if (inputForm.email) {
    const userId = inputForm.id;
    const email = inputForm.email;
    try {
      const isAvailable = await utilisateurStore.checkEmailExistenceUp({ userId, email });
      // console.log("Résultat de la vérification du email (isAvailable) :", isAvailable);
      console.log("email, userId :", email, userId);
      if (!isAvailable) {
        emailError.value = true;
        emailErrorMessage.value = "Cet email  est déjà utilisé.";
        // console.log('emailErrorMessage:', emailErrorMessage);
      }
    } catch (error) {
      // console.error("Erreur lors de la vérification de l'email :", error);
      emailError.value = true;
      emailErrorMessage.value = "Erreur lors de la vérification de l'email. Veuillez réessayer.";
    }
  }
};
const checkMatriculeExistenceUp = async () => {
  matriculeError.value = false;
  matriculeErrorMessage.value = "";
  if (inputForm.matricule) {
    const userId = inputForm.id;
    const matricule = inputForm.matricule;
    try {
      const isAvailable = await utilisateurStore.checkMatriculeExistenceUp({userId,matricule});
      console.log("userId et :matricule) :", userId, matricule);
      // console.log("Résultat de la vérification du matricule (isAvailable) :", isAvailable);
      if (!isAvailable) {
        matriculeError.value = true;
        matriculeErrorMessage.value = "Ce matricule  est déjà utilisé.";
        // console.log('matriculeErrorMessage:', matriculeErrorMessage);
      }
    } catch (error) {
      // console.error("Erreur lors de la vérification du matricule :", error);
      matriculeError.value = true;
      matriculeErrorMessage.value = "Erreur lors de la vérification du matricule. Veuillez réessayer.";
    }
  }
};
const onEmailInput = () => {
  // Vérifie s'il y a des espaces dans l'email
  if (/\s/.test(inputForm.email)) {
    // Si des espaces sont trouvés, affiche un message d'erreur
    emailError.value = true;
    emailErrorMessage.value = "L'adresse e-mail ne doit pas contenir d'espaces.";
  } else {
    // Sinon, effectue la vérification normale de l'existence de l'email
    checkEmailExistenceUp();
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
    checkMatriculeExistenceUp();
  }
};
const handleSave = async () => {
  try {
    if (!isSubmitDisabled.value){
      await schema.validate(inputForm, { abortEarly: false });
      console.log('Formulaire valide. Soumission en cours...');
        const payload = {
          prenoms:inputForm.prenoms, 
          nom:inputForm.nom,
          matricule:inputForm.matricule, 
          email:inputForm.email,
          sexe:inputForm.sexe ,
          telephone:inputForm.telephone ,
    };
      upProfile(payload); 
      router.push({ name: 'dashboard'});
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
onMounted(()=>{
  current().then( () => {
    inputForm.id= dataDetails.value.userId,
    inputForm.prenoms = dataDetails.value.prenoms,
    inputForm.nom = dataDetails.value.nom,
    inputForm.matricule = dataDetails.value.matricule,
    inputForm.email = dataDetails.value.email,
    inputForm.sexe = dataDetails.value.sexe,
    inputForm.telephone = dataDetails.value.telephone 
  });

});
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