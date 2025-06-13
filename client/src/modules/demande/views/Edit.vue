<template>
  <div >
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.demande.demande') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
      <v-form @submit.prevent="handleSave" ref="demandeForm" :value="formValid">
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
        readonly
        single-line
        item-title="libelleLong"
        item-value="id"
      ></v-select>
      <v-autocomplete
        prepend-inner-icon="mdi-school"
        name="academie"
        v-model="inputForm.academie"
        :items="dataListeForUser"
        item-title="libelleLong"
        item-value="id"
        :label="$t('apps.forms.academie.nom')"
        dense
        outlined
        variant="outlined"
        density="compact"
        clearable
        @change="handleAcademieChange"
        persistent-hint
        :error-messages="errors.academie ? [errors.academie] : []"
        @focus="clearErrors"
        autocomplete="off"
      >
      <template v-if="errors.academie"  v-slot:append>
            <v-icon color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
      </v-autocomplete>
      <!-- <v-select

        variant="outlined"

        persistent-hint
        single-line
      ></v-select> -->
      <v-autocomplete
        name="ville"
        v-model="inputForm.ville"
       :items="dataListeByAcademieAndUser"
        item-title="libelleLong"
        item-value="id"
        :label="$t('apps.forms.ville.nom')"
        dense
        outlined
        variant="outlined"
        prepend-inner-icon="mdi-city"
        density="compact"
        clearable
        persistent-hint
         @change="handleVilleChange"
        single-line
        :error-messages="errors.ville ? [errors.ville] : []"
        @focus="clearErrors"
        autocomplete="off"
      >
      <template v-if="errors.ville"  v-slot:append>
            <v-icon color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
      </v-autocomplete>
      <v-autocomplete
        prepend-inner-icon="mdi-office-building"
        name="centre"
        density="compact"
        color="balck"
        v-model="inputForm.centre"
        variant="outlined"
        :items="dataListeByVille"
        persistent-hint
        item-title="libelleLong"
        item-value="id"
        clearable
        autocomplete="off"
      >
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
  import { reactive, getCurrentInstance, onMounted,watchEffect,ref } from "vue";
  import { storeToRefs } from "pinia";
  import { useRouter, useRoute } from 'vue-router';
  import { useNotificationStore } from "@/store/notification";
  import { useCentreStore } from "@/modules/centre/store";
  import { useVilleStore } from "@/modules/ville/store";
  import { useAcademieStore } from "@/modules/academie/store";
  import { useSessionStore } from "@/modules/session/store";
  import { useI18n } from "vue-i18n";
  import { useDemandeStore } from '@/modules/demande/store';
  import { useToast } from 'vue-toastification';

  import * as yup from 'yup';

  const schema = yup.object().shape({
    ville: yup.string().required('Veuillez selectionner une ville'),
    academie: yup.string().required('Veuillez selectionner une académie'),
    user: yup.string().required('Veuillez selectionner un utilisateur'),
    etatDemande: yup.string().required('Veuillez selectionner le statut de la demande '),
  });
const toast= useToast();





const redirectToListe = () => {
  router.push({ name: 'accueil'});
};


  const i18n = useI18n();
  const notificationStore = useNotificationStore();
  const { addNotification } = notificationStore;

  const villeStore = useVilleStore();
const {dataListeVille,dataListeByAcademieAndUser} = storeToRefs(villeStore);
const {availableVillesForUserAndAcademy}=villeStore;

const academieStore = useAcademieStore();
const { dataListe,dataListeForUser } = storeToRefs(academieStore);
const sesssionStore=useSessionStore();
const { dataListeSession } = storeToRefs(sesssionStore);

  const instance = getCurrentInstance();
  const router = useRouter();
  const route = useRoute();

  const demandeStore = useDemandeStore();
  const { dataDetails, loading,error } = storeToRefs(demandeStore);
  const { one, modify2,accepterDemande } = demandeStore;
  const centreStore=useCentreStore();
  const { dataListeCentre,dataListeByVille} = storeToRefs(centreStore);
  const {centresByVille}=centreStore;
  const errors = reactive({
  ville:null,
  academie: null,
  error: false,
});
const clearErrors = () => {
  errors.ville = null;
  errors.academie= null;
};
  const inputForm = reactive({
    id:null,
    user:null,
    session:null,
    centre:null,
    ville:null,
    academie:null,
    jury:null,
    etatDemande:null,
  });

  const handleSave = async () => {
  try{
    await schema.validate(inputForm, { abortEarly: false });
    const payload = {
    user: inputForm.user,
    ville: inputForm.ville,
    academie: inputForm.academie,
    etatDemande: inputForm.etatDemande,
    centre: inputForm.centre,
    jury: inputForm.jury,
  };
  try {
   await modify2(route.params.id, payload).then(() => {
    if(!error.value){
      toast.success(i18n.t('updated'));
    router.push({ name: 'accueil' });
    }
  });
  } catch (error) {
    console.error('Erreur lors de la modification:', error);
    toast.error("Erreur lors de la modification. Veuillez vérifier les données saisies.");
  }
  }catch (validationError) {
    if (validationError instanceof yup.ValidationError) {
      validationError.inner.forEach(err => {
      if (err.path === 'ville') {
        errors.ville = err.message;
      } else if (err.path === 'academie') {
        errors.academie = err.message;
      }
      });
    } else {
      toast.error("Erreur lors de la validation. Veuillez vérifier les données saisies.");
      console.error(error);
    }
  }
};
const selectedAcademies = new Map();
const handleAcademieChange = async () => {
  if (inputForm.academie) {
    // Vérifier si l'académie sélectionnée a changé depuis la dernière fois
    const selectedAcademie = inputForm.academie;
    const previousAcademie = selectedAcademies.get("selectedAcademieKey");
    if (selectedAcademie !== previousAcademie) {
      try {
        // Réinitialiser la valeur de la ville uniquement si elle a été précédemment sélectionnée
        if (previousAcademie !== undefined) {
          inputForm.ville = null;
        }

        // Appeler la fonction pour récupérer les villes disponibles pour cette académie
        await availableVillesForUserAndAcademy(selectedAcademie);

        // Mettre à jour la map avec la nouvelle académie sélectionnée
        selectedAcademies.set("selectedAcademieKey", selectedAcademie);
      } catch (error) {
        console.error("Erreur lors de la récupération des villes:", error);
      }
    }
  }

};
const handleVilleChange = async () => {
  if (inputForm.ville) {
    // Vérifier si l'académie sélectionnée a changé depuis la dernière fois
    const selectedVille = inputForm.ville;
    const previousVille = selectedVilles.get("selectedVilleKey");
    if (selectedVille !== previousVille) {
      try {
        // Réinitialiser la valeur de la ville uniquement si elle a été précédemment sélectionnée
        if (previousVille !== undefined) {
          inputForm.ville = null;
        }

        // Appeler la fonction pour récupérer les villes disponibles pour cette académie
        await centresByVille(selectedVille);

        // Mettre à jour la map avec la nouvelle académie sélectionnée
        selectedVilles.set("selectedVilleKey", selectedVille);
      } catch (error) {
        console.error("Erreur lors de la récupération des villes:", error);
      }
    }
  }

};

watchEffect(() => {
  handleAcademieChange();
});
const demandeId = ref(route.params.id);
  onMounted(()=>{
    one(route.params.id ).then( () => {
      inputForm.session = dataDetails.value.session ? dataDetails.value.session.id : null;
      inputForm.academie = dataDetails.value.academie ? dataDetails.value.academie.id : null;
      inputForm.ville = dataDetails.value.ville ? dataDetails.value.ville.id : null;
      inputForm.etatDemande = dataDetails.value.etatDemande ? dataDetails.value.etatDemande.libelleLong : null;

      villeStore.all();
      academieStore.availableAcademiesForUser(demandeId.value);
      sesssionStore.all();
      centreStore.all();
    });
  });


  </script>
  <style>
  .error-message {
    color: red; /* ou toute autre couleur de votre choix */
    margin-top: 5px; /* Ajustez la marge en fonction de vos besoins */
  }
  </style>
