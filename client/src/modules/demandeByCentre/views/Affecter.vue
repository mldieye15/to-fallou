<template>
  <div v-if="inputForm.etatDemande==='validée' && juryForm.juryId===null">
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.demande.demande') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="handleSave" ref="demandeForm" :value="formValid">
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
        prepend-inner-icon="mdi-office-building"
        name="centre"
        density="compact"
        :label="$t('apps.forms.centre.nom')"
        color="balck"
        v-model="inputForm.centreName"
        variant="outlined"
        readonly
      >
      <input type="hidden" :v-model="inputForm.centre">
    </v-text-field>
    <v-autocomplete
        prepend-inner-icon="mdi-gavel"
        name="jury"
        density="compact"
        color="balck"
        v-model="inputForm.jury"
        variant="outlined"
        :items="dataListeJury"
        persistent-hint
        item-title="numero"
        item-value="id"
        :error-messages="errors.jury ? [errors.jury] : []"
        @focus="clearErrors"
        clearable 
        autocomplete="off"
      >
         <template v-if="errors.jury"  v-slot:append>
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
  import { useJuryStore } from "@/modules/jury/store";
  import { useI18n } from "vue-i18n";
  
  import { useDemandeByCentreStore } from "../store";
  import { useToast } from 'vue-toastification';
  import * as yup from 'yup';
  
  const schema = yup.object().shape({
   jury: yup.string().required('Veuillez selectionner un centre'),
});

  const redirectToListe = () => {
    router.push({ name: 'demandeByCentre-demandes'});
  };


  const toast= useToast();
  const i18n = useI18n();
  
  const notificationStore = useNotificationStore();
  const { addNotification } = notificationStore;
  const clearErrors = () => {
  errors.jury= null;
};
  const errors = reactive({
  jury: null,
  error: false,
});
  const villeStore = useVilleStore();
const juryStore= useJuryStore();
const {dataListeJury}=storeToRefs(juryStore);
const academieStore = useAcademieStore();
const sesssionStore=useSessionStore();
const centreStore=useCentreStore();
const { dataListeCentre,dataListeByVille} = storeToRefs(centreStore);
  
  const instance = getCurrentInstance();
  const router = useRouter();
  const route = useRoute();
  
  const demandeStore = useDemandeByCentreStore();
  const { dataDetails, loading } = storeToRefs(demandeStore);
  const { one, modify,affecterJury } = demandeStore;
  const juryForm = reactive({
  juryId: null
});
  const inputForm = reactive({
    villeName:"",
    sessionName:"",
    academieName:"",
    centreName:"",
    session:null,
    ville:null,
    academie:null,
    centre:null,
    jury:null,
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
    jury: inputForm.jury,
  };
  const redirectToCentreAfterAffected = (centreId) => {
    router.push({ name: 'demandeByCentre-demandes', params: { id: centreId } });
};
  affecterJury(route.params.id, payload).then(() => {
    // addNotification({
    //   show: true,
    //   text:  i18n.t('updated'),
    //   color: 'blue',
    // });
    toast.success(i18n.t('valided'));
    redirectToCentreAfterAffected(inputForm.centre)
    // router.push({ name: 'demandeByCentre-demandes' });
  });
 }catch (error) {
    // Si la validation échoue, afficher les messages d'erreur
    if (error instanceof yup.ValidationError) {
      error.errors.forEach(errorMessage => {
        // Afficher les messages d'erreur associés à chaque champ du formulaire
        errors.jury = errorMessage;
        // toast.error(errorMessage);
      });
    } else {
      // Gérer d'autres erreurs ici, si nécessaire
      console.error(error);
    }
};
}
  onMounted(()=>{
    one(route.params.id ).then( () => {
      inputForm.session=dataDetails.value.session?dataDetails.value.session.id:null,
      inputForm.academie = dataDetails.value.academie?dataDetails.value.academie.id:null,
      inputForm.ville = dataDetails.value.ville?dataDetails.value.ville.id:null,
      inputForm.centre = dataDetails.value.centre?dataDetails.value.centre.id:null,
      inputForm.etatDemande=dataDetails.value.etatDemande?dataDetails.value.etatDemande.libelleLong:null,
      inputForm.villeName=dataDetails.value.ville.libelleLong,
      inputForm.academieName=dataDetails.value.academie.libelleLong,
      inputForm.sessionName=dataDetails.value.session.libelleLong
      inputForm.centreName=dataDetails.value.centre.libelleLong
      juryForm.juryId = dataDetails.value.jury?dataDetails.value.jury.id:null
      villeStore.all();
      academieStore.all();
      sesssionStore.all();
      centreStore.all();
      const centreId=dataDetails.value.centre.id;
      console.log("Centre:",centreId)
      juryStore.jurysBycentre(centreId);
      console.log("jurys:",jury)
    });
  });
  
  
  </script>
  