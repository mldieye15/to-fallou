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
        variant="solo" 
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
        variant="solo"
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
      <v-col>
        <v-text-field
        id="email"
        prepend-inner-icon="mdi-email"
        name="email"
        density="compact"
        :label="$t('apps.forms.user.email')"
        color="balck"
        v-model="inputForm.email"
        variant="solo"
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
      <v-col>
      <v-text-field
        id="code"
        prepend-inner-icon="mdi-key"
        name="code"
        density="compact"
        :label="$t('apps.forms.user.code')"
        color="balck"
        v-model="inputForm.code"
        variant="solo"
        @blur="checkCodeValidity"
        readonly
        class="input-with-asterisk"
      ></v-text-field>
      <div v-if="formSubmitted && !inputForm.code" class="required-message mb-0">
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
        id="telephone"
        prepend-inner-icon="mdi-phone"
        name="telephone"
        density="compact"
        :label="$t('apps.forms.user.telephone')"
        color="balck"
        :rules="[rules.exactlynumeroTelephone]"
        v-model="inputForm.telephone"
        variant="solo"
        class="input-with-asterisk"
        
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
        variant="solo" 
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
      <v-row >
      <v-col >
        <v-select
        id="sexe"
        prepend-inner-icon="mdi-gender-male-female"
        name="sexe"
        density="compact"
        :label="$t('apps.forms.user.sexe')"
        color="balck"
        v-model="inputForm.sexe"
        variant="solo"
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
      <v-select
        prepend-inner-icon="mdi-domain"
        name="etablissement"
        density="compact"
        :label="$t('apps.forms.etablissement.nom')"
        color="balck"
        v-model="inputForm.etablissement"
        variant="solo"
        :items="dataListeEtab"
        persistent-hint
        
        single-line
        item-title="libelleLong"
        item-value="id"
        class="input-with-asterisk"
        
      ></v-select>
      <div v-if="formSubmitted && !inputForm.etablissement" class="required-message mb-0">
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
        id="banque"
        prepend-inner-icon="mdi-bank"
        name="banque"
        density="compact"
        :label="$t('apps.forms.user.banque')"
        color="balck"
        v-model="inputForm.banque"
        variant="solo" 
        @blur="onMatriculeInput"
        class="input-with-asterisk"
        
    >
    </v-text-field >
    <div v-if="formSubmitted && !inputForm.banque" class="required-message mb-0">
          Champ obligatoire
          <span class="required-icon">
            <i class="mdi mdi-alert"></i>
          </span>
        </div>
      </v-col>
      <v-col>
      <v-text-field 
        id="codeBanque"
        prepend-inner-icon="mdi-domain"
        name="codeBanque"
        density="compact"
        :label="$t('apps.forms.user.codeBanque')"
        color="balck"
        :rules="[rules.exactlycodeBanque]"
        v-model="inputForm.codeBanque"
        variant="solo" 
        @blur="onMatriculeInput"
        class="input-with-asterisk"
        
    >
    </v-text-field >
    <div v-if="formSubmitted && !inputForm.codeBanque" class="required-message mb-0">
          Champ obligatoire
          <span class="required-icon">
            <i class="mdi mdi-alert"></i>
          </span>
        </div>
      </v-col>
      <v-col>
      <v-text-field 
        id="codeAgence"
        prepend-inner-icon="mdi-bank"
        name="codeAgence"
        density="compact"
        :label="$t('apps.forms.user.codeAgence')"
        color="balck"
        :rules="[rules.exactlycodeAgence]"
        v-model="inputForm.codeAgence"
        variant="solo" 
        @blur="onMatriculeInput"
        class="input-with-asterisk"
        
    >
    </v-text-field >
    <div v-if="formSubmitted && !inputForm.codeAgence" class="required-message mb-0">
          Champ obligatoire
          <span class="required-icon">
            <i class="mdi mdi-alert"></i>
          </span>
        </div>
      </v-col>
      </v-row>
      <v-row>
        <v-col md="9">
      <v-text-field 
        id="numeroCompte"
        prepend-inner-icon="mdi-wallet"
        name="numeroCompte"
        density="compact"
        :label="$t('apps.forms.user.numeroCompte')"
        color="balck"
        :rules="[rules.exactlynumeroCompte]"
        v-model="inputForm.numeroCompte"
        variant="solo" 
        @blur="onMatriculeInput"
        class="input-with-asterisk"
    >
    </v-text-field >
    <div v-if="formSubmitted && !inputForm.numeroCompte" class="required-message mb-0">
          Champ obligatoire
          <span class="required-icon">
            <i class="mdi mdi-alert"></i>
          </span>
        </div>
      </v-col>
      <v-col >
      <v-text-field 
        id="cleRib"
        prepend-inner-icon="mdi-finance"
        name="cleRib"
        density="compact"
        :label="$t('apps.forms.user.cleRib')"
        color="balck"
        :rules="[rules.exactlycleRib]"
        v-model="inputForm.cleRib"
        variant="solo" 
        @blur="onMatriculeInput"
        class="input-with-asterisk"
    >
    </v-text-field >
    <div v-if="formSubmitted && !inputForm.cleRib" class="required-message mb-0">
          Champ obligatoire
          <span class="required-icon">
            <i class="mdi mdi-alert"></i>
          </span>
        </div>
      </v-col>
      </v-row>
       <p>
        <div v-if="codeError" class="error-message">{{ codeErrorMessage }}
        <router-link :to="{ name: 'code' }"> Recuperer votre code ici
       </router-link>
      </div>
       </p>
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
const utilisateurStore= useUtilisateurStore();
const {upProfileUser}=utilisateurStore;
const fonctionStore = useFonctionStore();
const etablissementStore= useEtablissementStore();
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
  code: yup.string().required('Le code est requis'),
  email: yup.string().email('Adresse email invalide').required('L\'adresse email est requise'),
  telephone: yup.string().matches(/^\d{9}$/, 'Numéro de téléphone invalide').required('Le numéro de téléphone est requis'),
  matricule: yup.string().required('Le matricule est requis'),
  sexe: yup.string().required('Le sexe est requis'),
  etablissement: yup.string().required('L\'établissement est requis'),
  banque: yup.string().required('La banque est requise'),
  codeBanque: yup.string().required('Le code banque est requis').matches(/^.{5}$/, 'Le code banque doit comporter exactement 5 caractères'),
  codeAgence: yup.string().required('Le code agence est requis').matches(/^\d{5}$/, 'Code agence invalide'),
  numeroCompte: yup.string().required('Le numéro de compte est requis').matches(/^\d{12}$/, 'Numéro de compte invalide'),
  cleRib: yup.string().required('La clé RIB est requise').matches(/^\d{2}$/, 'Clé RIB invalide'),
});
const inputForm = reactive({
  prenoms: "",
  nom: "",
  matricule: "",
  email: "",
  sexe: "",
  code: "",
  telephone: "",
  anciennete: "",
  banque: "",
  codeBanque: "",
  codeAgence:"",
  numeroCompte: "",
  cleRib: "",
  fonction: null,
  etablissement: null,
  libelleLong: "",
});
const  formSubmitted=ref(false);
// const { inputForm, actionSubmit } = defineProps({
//   inputForm: Object,
//   actionSubmit: {
//     type: Function,
//   }
// });
const redirectToAccueil = () => {
  router.push({ name: 'accueil'});
};
const isEditing = ref(false);
const startEditing = () => {
  isEditing.value = true;
};
const codeError = ref(false);
const codeErrorMessage = ref("");
const emailError = ref(false);
const emailErrorMessage = ref("");
const matriculeError = ref("");
const matriculeErrorMessage = ref("");
const isSubmitDisabled = ref(false);
watchEffect(() => {
  isSubmitDisabled.value = codeError.value||emailError.value ||matriculeError.value
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
        codeErrorMessage.value = "Code ou email invalide. Veuillez vérifier  le code ou l'email saisi.";
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
const onEmailInput = () => {
  // Vérifie s'il y a des espaces dans l'email
  if (/\s/.test(inputForm.email)) {
    // Si des espaces sont trouvés, affiche un message d'erreur
    emailError.value = true;
    emailErrorMessage.value = "L'adresse e-mail ne doit pas contenir d'espaces.";
  } else {
    // Sinon, effectue la vérification normale de l'existence de l'email
    checkEmailExistence();
    checkCodeValidity(); 
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
          code:inputForm.code ,
          telephone:inputForm.telephone ,
          etablissement:inputForm.etablissement, 
          banque:inputForm.banque,
          codeBanque:inputForm.codeBanque,
          codeAgence:inputForm.codeAgence,
          numeroCompte:inputForm.numeroCompte,
          cleRib:inputForm.cleRib 
    };
      upProfileUser(payload); 
      router.push({ name: 'accueil'});
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
  etablissementStore.all();
  current().then( () => {
    inputForm.prenoms = dataDetails.value.prenoms,
    inputForm.nom = dataDetails.value.nom,
    inputForm.matricule = dataDetails.value.matricule,
    inputForm.email = dataDetails.value.email,
    inputForm.sexe = dataDetails.value.sexe,
    inputForm.code = dataDetails.value.code,
    inputForm.telephone = dataDetails.value.telephone,
    inputForm.anciennete = dataDetails.value.anciennete,
    inputForm.etablissement=dataDetails.value.etablissement?dataDetails.value.etablissement.id:null,
    inputForm.libelleLong=dataDetails.value.etablissement?dataDetails.value.etablissement.libelleLong:null,
    inputForm.banque=dataDetails.value.banque,
    inputForm.codeBanque=dataDetails.value.codeBanque,
    inputForm.codeAgence=dataDetails.value.codeAgence,
    inputForm.numeroCompte=dataDetails.value.numeroCompte,
    inputForm.cleRib=dataDetails.value.cleRib   
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