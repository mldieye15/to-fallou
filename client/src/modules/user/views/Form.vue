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
      <v-row style="height: 12vh">
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
    <v-row style="height: 12vh">
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
        @input="checkMatriculeExistence">
    </v-text-field >
    <div v-if="matriculeError" class="error-message">{{ matriculeErrorMessage }}</div>
      </v-col>
      <v-col>
        <!-- <v-text-field
        id="dateNaiss"
        prepend-inner-icon="mdi-calendar"
        name="dateNaiss"
        density="compact"
        :label="$t('apps.forms.user.dateNaiss')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.dateNaiss"
        variant="solo"
      ></v-text-field> -->
       <v-menu
          v-model="menu"
          :close-on-content-click="false"
          transition="scale-transition"
          offset-y
          max-width="100px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              v-model="computedDateFormatted"
              label="Date"
              hint="MM/DD/YYYY format"
              persistent-hint
              prepend-icon="mdi-calendar"
              readonly
              v-bind="attrs"
              v-on="on"
              variant="solo"
              density="compact"
            ></v-text-field>
          </template>
          <v-date-picker
          v-model="date"
          no-title
          scrollable
        >
          <v-spacer></v-spacer>
          <v-btn
            variant="text"
           color="primary"
            @click="menu = false"
          >
            Cancel
          </v-btn>
          <v-btn
            variant="text"
            color="primary"
            @click="menu.save(date)"
          >
            OK
          </v-btn>
        </v-date-picker>
      </v-menu>
      </v-col>     
    </v-row>
    <v-row style="height: 12vh">
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
        @input="checkUsernameExistence"    
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
    </v-row >
      <v-row style="height: 12vh">
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
        @input="checkEmailExistence"
      >
    </v-text-field>
    <div v-if="emailError" class="error-message">{{ emailErrorMessage }}</div>
      </v-col> 
      </v-row>
      <v-row style="height: 12vh">
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
      <v-col>
      <v-text-field
        id="code"
        prepend-inner-icon="mdi-key"
        name="code"
        density="compact"
        :label="$t('apps.forms.user.code')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.code"
        variant="solo"
        @input="checkCodeValidity"
      ></v-text-field>
        </v-col>

      </v-row>
      <v-row class="reduce-margin">
        <v-col>
          <v-text-field
        id="anciennete"
        prepend-inner-icon="mdi-timer"
        name="anciennete"
        density="compact"
        :label="$t('apps.forms.user.anciennete')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.anciennete"
        variant="solo"
      ></v-text-field>
        </v-col>
        <v-col>
          <v-select
        prepend-inner-icon="mdi-briefcase"
        name="fonction"
        density="compact"
        :label="$t('apps.forms.fonction.nom')"
        color="balck"
        v-model="inputForm.fonction"
        variant="solo"
        :items="dataListe"
        persistent-hint
        
        single-line
        item-title="libelleLong"
        item-value="id"
      ></v-select>
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
      ></v-select>
        </v-col>
      </v-row>
      <router-link :to="{ name: 'code' }"> <p>Recuperer votre code ici</p> 
       </router-link>
       <p>
        <v-alert v-if="codeError" type="error">{{ codeErrorMessage }}
        <router-link :to="{ name: 'code' }"> Recuperer votre code ici
       </router-link>
      </v-alert>
       </p>
       

      <v-btn block class="mt-2 mb-8" size="large" color="blue" @click="handleSave">{{ $t('apps.forms.enregistrer') }}</v-btn>
    </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance,watchEffect,ref,watch,computed} from "vue";
import { onMounted } from "vue"
import { storeToRefs } from "pinia";
import { useFonctionStore } from "@/modules/fonction/store";
import { useEtablissementStore } from "@/modules/etablissement/store";
import { useCodeStore } from "@/store/codification";
import { useValidationStore } from "@/store/validationstore";

const instance = getCurrentInstance();

const fonctionStore = useFonctionStore();
const etablissementStore= useEtablissementStore();
const codeStore = useCodeStore();
const validationstore=useValidationStore();
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
      const isAvailable = await validationstore.emailAvailability(inputForm.email);
      console.log("Résultat de la vérification de l'e-mail (isAvailable) :", isAvailable);

      if (typeof isAvailable === 'undefined' || isAvailable === null) {
        console.error('La fonction emailAvailability n\'a pas retourné de valeur valide.');
        return;
      }

      if (!isAvailable) {
        emailError.value = true;
        emailErrorMessage.value = "Cet e-mail est déjà utilisé.";
        console.log('emailErrorMessage:', emailErrorMessage);
      }
    } catch (error) {
      console.error("Erreur lors de la vérification de l'e-mail :", error);
      emailError.value = true;
      emailErrorMessage.value = "Erreur lors de la vérification de l'e-mail. Veuillez réessayer.";
    }
  }
};
const checkMatriculeExistence = async () => {
  matriculeError.value = false;
  matriculeErrorMessage.value = "";
  if (inputForm.matricule) {
    try {
      const isAvailable = await validationstore.matriculeAvailability(inputForm.matricule);
      console.log("Résultat de la vérification du matricule (isAvailable) :", isAvailable);

      if (typeof isAvailable === 'undefined' || isAvailable === null) {
        console.error('La fonction matriculeAvailability n\'a pas retourné de valeur valide.');
        return;
      }

      if (!isAvailable) {
        matriculeError.value = true;
        matriculeErrorMessage.value = "Cet matricule est déjà utilisé.";
        console.log('matriculeErrorMessage:', matriculeErrorMessage);
      }
    } catch (error) {
      console.error("Erreur lors de la vérification du matricule :", error);
      matriculeError.value = true;
      matriculeErrorMessage.value = "Erreur lors de la vérification du matricule. Veuillez réessayer.";
    }
  }
};
const checkUsernameExistence = async () => {
  usernameError.value = false;
  usernameErrorMessage.value = "";
  if (inputForm.username) {
    try {
      const isAvailable = await validationstore.usernameAvailability(inputForm.username);
      console.log("Résultat de la vérification du nom d'utilisateur (isAvailable) :", isAvailable);

      if (typeof isAvailable === 'undefined' || isAvailable === null) {
        console.error('La fonction usernameAvailability n\'a pas retourné de valeur valide.');
        return ;
      }

      if (!isAvailable) {
        usernameError.value = true;
        usernameErrorMessage.value = "Cet nom d'utilisateur est déjà utilisé.";
        console.log('usernameErrorMessage:', usernameErrorMessage);
      }
    } catch (error) {
      console.error("Erreur lors de la vérification nom d'utilisateur :", error);
      usernameError.value = true;
      usernameErrorMessage.value = "Erreur lors de la vérification nom d'utilisateur. Veuillez réessayer.";
    }
  }
};
const date = ref((new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10));
const menu = ref(true);

const computedDateFormatted = computed(() => {
  return formatDate(date.value)
});

watch(date, val => {
  dateFormatted.value = formatDate(val)
});
function formatDate(date) {
  if (!date) return null;
  const [year, month, day] = date.split('-');
  return `${month}/${day}/${year}`;
}
const dateFormatted = ref(formatDate(date.value));
const handleSave = () => {
  console.log("isSubmitDisabled:", isSubmitDisabled.value);
  if(instance.refs.userForm.validate() && !isSubmitDisabled.value){
    actionSubmit(inputForm);
  }
}

onMounted(()=>{
  fonctionStore.all();
  etablissementStore.all();

});

</script>
<style>
.error-message {
  color: red; /* ou toute autre couleur de votre choix */
  margin-top: 5px; /* Ajustez la marge en fonction de vos besoins */
}
</style>