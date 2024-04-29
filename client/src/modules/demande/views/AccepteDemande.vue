<template>
  <div v-if="inputForm.etatDemande==='en attente'|| inputForm.etatDemande==='déclinée' && inputForm.etatUser===false ">
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.demande.demande') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="handleSave" ref="demandeForm">
      <v-text-field
        prepend-inner-icon="mdi-calendar"
        name="session"
        density="compact"
        :label="$t('apps.forms.session.nom')"
        color="balck"
        v-model="inputForm.sessionName"
        variant="outlined"
        readonly
      >
      <input type="hidden" :v-model="inputForm.session">
    </v-text-field>

      <v-text-field
        prepend-inner-icon="mdi-city"
        name="ville"
        density="compact"
        :label="$t('apps.forms.ville.nom')"
        color="balck"
        v-model="inputForm.villeName"
        variant="outlined"
        readonly
      >
      <input type="hidden" :v-model="inputForm.ville">
    </v-text-field>

      <v-text-field
        prepend-inner-icon="mdi-school"
        name="academie"
        density="compact"
        :label="$t('apps.forms.academie.nom')"
        color="balck"
        v-model="inputForm.academieName"
        variant="outlined"
        readonly
      >
      <input type="hidden" :v-model="inputForm.academie">
    </v-text-field>
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
        :error-messages="errors.centre ? [errors.centre] : []"
        @focus="clearErrors"
        clearable 
        autocomplete="off"
      >
         <template v-if="errors.centre"  v-slot:append>
            <v-icon color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
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
  import { reactive, getCurrentInstance, onMounted, computed, ref } from "vue";
  import { storeToRefs } from "pinia";
  import { useRouter, useRoute } from 'vue-router';
  import { useNotificationStore } from "@/store/notification";
  import { useCentreStore } from "@/modules/centre/store";
  import { useVilleStore } from "@/modules/ville/store";
  import { useAcademieStore } from "@/modules/academie/store";
  import { useSessionStore } from "@/modules/session/store";
  import { useI18n } from "vue-i18n";
 

  
  import { useDemandeStore } from "../store";
  import { useToast } from 'vue-toastification';
  import * as yup from 'yup';
  
  const schema = yup.object().shape({
   centre: yup.string().required('Veuillez selectionner un centre'),
});

const toast= useToast();
const redirectToListe = () => {
  router.push({ name: 'demande-liste'});
};
  const i18n = useI18n();
  
  const notificationStore = useNotificationStore();
  const { addNotification } = notificationStore;

  const villeStore = useVilleStore();
const {dataListeVille} = storeToRefs(villeStore);

const academieStore = useAcademieStore();
const { dataListe } = storeToRefs(academieStore);
const sesssionStore=useSessionStore();
const { dataListeSession } = storeToRefs(sesssionStore);
const centreStore=useCentreStore();
const { dataListeCentre,dataListeByVille} = storeToRefs(centreStore);
  
  const instance = getCurrentInstance();
  const router = useRouter();
  const route = useRoute();
  
  const demandeStore = useDemandeStore();
  const { dataDetails, loading } = storeToRefs(demandeStore);
  const { one, modify,accepterDemande,hasAcceptedDemande } = demandeStore;
  const clearErrors = () => {
  errors.centre= null;
};
  const errors = reactive({
  centre: null,
  error: false,
});
  const inputForm = reactive({
    villeName:"",
    sessionName:"",
    academieName:"",
    session:null,
    ville:null,
    academie:null,
    centre:null,
    etatDemande:null, 
  });
  const handleSave = async () => {
    try{
    await schema.validate(inputForm, { abortEarly: false });  
    const payload = {
    session: inputForm.session,
    ville: inputForm.ville,
    academie: inputForm.academie,
    centre: inputForm.centre,
  };
  accepterDemande(route.params.id, payload).then(() => {
    toast.success(i18n.t('accepted'));
    router.push({ name: 'demande-liste' });
  });
  } catch (error) {
    // Si la validation échoue, afficher les messages d'erreur
    if (error instanceof yup.ValidationError) {
      error.errors.forEach(errorMessage => {
        // Afficher les messages d'erreur associés à chaque champ du formulaire
        errors.centre = errorMessage;
        // toast.error(errorMessage);
      });
    } else {
      // Gérer d'autres erreurs ici, si nécessaire
      console.error(error);
    }
  }
 
  
};
onMounted(() => {
  one(route.params.id ).then(() => {
    inputForm.session = dataDetails.value.session ? dataDetails.value.session.id : null;
    inputForm.academie = dataDetails.value.academie ? dataDetails.value.academie.id : null;
    inputForm.ville = dataDetails.value.ville ? dataDetails.value.ville.id : null;
    inputForm.etatDemande = dataDetails.value.etatDemande ? dataDetails.value.etatDemande.libelleLong : null;
    inputForm.villeName = dataDetails.value.ville.libelleLong;
    inputForm.academieName = dataDetails.value.academie.libelleLong;
    inputForm.sessionName = dataDetails.value.session.libelleLong;
    
    villeStore.all();
    academieStore.all();
    sesssionStore.all();
    centreStore.all();

    const villeId = dataDetails.value.ville.id;
    const userId = dataDetails.value.user.id;
    
    console.log(villeId);
    console.log("user", userId);

    centreStore.centresByVille(villeId);

    hasAcceptedDemande(userId).then((etatUser) => {
      inputForm.etatUser = etatUser;
      console.log("etat des demande", inputForm.etatUser);
    });
  });
});
  </script>
  