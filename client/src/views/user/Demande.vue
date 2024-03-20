<template>
  <div> 
      <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="700"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.demande.demande') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="handleSave" ref="demandeForm" v-model="formValid">
      <div v-for="(inputForm,index) in requests" :key="index">
       <v-chip color="green" class="mt-3 mb-2"><h3>Choix N°{{ index+1 }}</h3></v-chip> 
      <v-text-field
        :id="'session'+index"
        prepend-inner-icon="mdi-calendar"
        :name="'session'+index"
        density="compact"
        :label="$t('apps.forms.session.nom')"
        color="balck"
        v-model="inputForm.libelleLong"
        variant="outlined"
        readonly
      >
      <input type="hidden" :v-model="inputForm.session">
    </v-text-field>
    <v-autocomplete
        prepend-inner-icon="mdi-school"
        :name="'academie'+index"
        v-model="inputForm.academie"
        :items="dataListe"
        item-title="libelleLong"
        item-value="id"
        :label="$t('apps.forms.academie.nom')"
        dense
        outlined
        variant="outlined"
        density="compact"
        clearable
        @input="onAcademieChange(index)"
        :error-messages="errors.academie ? [errors.academie] : []"
        @focus="clearErrors"
      >
      <template v-if="errors.academie"  v-slot:append>
            <v-icon color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
    </v-autocomplete>
      <v-autocomplete
      :name="'ville'+index"
       v-model="inputForm.ville"
        :items="inputForm.villes"
        item-title="libelleLong"
        item-value="id"
        :label="$t('apps.forms.ville.nom')"
        dense
        outlined
        variant="outlined"
        prepend-inner-icon="mdi-city"
        density="compact"
        clearable
        :error-messages="errors.ville ? [errors.ville] : []"
        @focus="clearErrors"
      >
      <template v-if="errors.ville"  v-slot:append>
            <v-icon color="red">
               mdi-alert-circle-outline
            </v-icon>
          </template>
    </v-autocomplete>


    </div>  
    </v-form>
    <div class="d-flex justify-end">
        <v-btn class="mt-8 mb-8 mr-2" color="red" @click.prevent="redirectToListe()">{{ $t('apps.forms.annuler') }}</v-btn>
        <v-btn class="mt-8 mb-8" color="blue" @click="handleSave">{{ $t('apps.forms.valider') }}</v-btn>
      </div>
    </v-card>
    
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance,watchEffect } from "vue";
import { onMounted,ref,onBeforeMount } from "vue"
import { storeToRefs } from "pinia";
import { useVilleStore } from "@/modules/ville/store";
import { useAcademieStore } from "@/modules/academie/store";
import { useSessionStore } from "@/modules/session/store";
import { useDemandeStore } from "@/modules/demande/store";
import { useNotificationStore } from "@/store/notification";
import { useI18n } from "vue-i18n";
import { useRouter, useRoute } from 'vue-router';
import { useToast } from 'vue-toastification';
import * as yup from 'yup';

const schema = yup.object().shape({
  ville: yup.string().required('Veuillez selectionner une ville'),
  academie: yup.string().required('Veuillez selectionner une académie'),
});
const errors = reactive({
  ville:null,
  academie: null,
  error: false,
});
const clearErrors = () => {
  errors.ville = null;
  errors.academie= null;
};
const toast= useToast();
const instance = getCurrentInstance();

const villeStore = useVilleStore();
const {dataListeVille,dataListeByAcademie} = storeToRefs(villeStore);
const {villesByAcademie}=villeStore;
const academieStore = useAcademieStore();
const { dataListe } = storeToRefs(academieStore);
const sessionStore=useSessionStore();
const { dataListeSession,dataDetails} = storeToRefs(sessionStore);
const route = useRoute();
const router=useRouter();
const {one}=sessionStore;
const redirectToListe = () => {
  router.push({ name: 'accueil'});
};

const rules = reactive({
  required: value => !!value || 'Champ obligatoire.',
});
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;
const demandeStore = useDemandeStore();
const { add } = demandeStore;
const formValid = ref(false);
const handleSave = async() => {
  try{
    const selectedAcademies = new Map();
    const selectedVilles = new Set();
   for (const inputForm of requests.value) {
     await schema.validate(inputForm, { abortEarly: false });
    // Vérifier les académies
    if (selectedAcademies.has(inputForm.academie)) {
      if (selectedAcademies.get(inputForm.academie) >= 2) {
        toast.error("Vous ne pouvez sélectionner la même académie plus de deux fois.");
        return; // Bloquer l'envoi du formulaire
      }
      selectedAcademies.set(inputForm.academie, selectedAcademies.get(inputForm.academie) + 1);
    } else {
      selectedAcademies.set(inputForm.academie, 1);
    }
    // Vérifier les villes
    if (selectedVilles.has(inputForm.ville)) {
      toast.error("Vous ne pouvez sélectionner la même ville plus d'une fois.");
      return; // Bloquer l'envoi du formulaire
    }
    selectedVilles.add(inputForm.ville);
  }
  console.log("Payload avant envoi:", requests.value);
    const payload = requests.value.map((inputForm) => ({
    ville: inputForm.ville,
    academie: inputForm.academie,
    session: inputForm.session,
  }));
  console.log("Payload transformé:", payload);
    add(payload)
      .then(() => {
        // addNotification({
        //   show: true,
        //   text: i18n.t('added'),
        //   color: 'blue'
        // });
        toast.success(i18n.t('added'));
        router.push({ name: 'accueil' });
      })
  }catch (error) {
    if (error instanceof yup.ValidationError) {
    error.inner.forEach(err => {
      if (err.path === 'ville') {
        errors.ville = err.message;
      } else if (err.path === 'academie') {
        errors.academie = err.message;
      } 
      });
    } else {
      // Gérer d'autres erreurs ici, si nécessaire
      console.error(error);
    }
  }
  };
const selected = new Map();
const requests = ref([]);
const onAcademieChange = async (index) => {
  console.log(`onAcademieChange appelée avec l'index : ${index}`);
  if (requests.value[index].academie !== null) {
    console.log(`Academie avant mise à jour des villes : ${requests.value[index].academie}`);
    const selectedAcademie = requests.value[index].academie;
    const previousAcademie = selected.get(index);
    if (selectedAcademie !== previousAcademie) {
      try {
        // Réinitialiser la valeur de la ville à null
        requests.value[index].ville = null;

        // Appeler la fonction pour récupérer les villes disponibles pour cette académie
        await villesByAcademie(selectedAcademie);
         requests.value[index].villes = dataListeByAcademie.value;
        // Mettre à jour la map avec la nouvelle académie sélectionnée
        selected.set(index, selectedAcademie);
      } catch (error) {
        console.error("Erreur lors de la récupération des villes :", error);
      }
    }
    console.log(`Academie après mise à jour des villes : ${requests.value[index].academie}`);
  }
};
const updateVilles = async (index) => {
  console.log(`updateVilles appelée avec l'index : ${index}`);
  console.log(`Academie avant mise à jour des villes : ${requests.value[index].academie}`);

  let academieId = requests.value[index].academie;
  console.log(`Academie après récupération : ${academieId}`);

  await villesByAcademie(academieId);
  // console.log(`Academie après mise à jour des villes : ${academieId}`);

  requests.value[index].villes = dataListeByAcademie.value;

  //console.log(`Villes mises à jour pour l'index ${index} :`, dataListeByAcademie);
};
onMounted(async () => {
  villeStore.all();
  academieStore.all();
  await one(route.params.id);
  
  if (dataDetails.value) {
    requests.value = Array.from({ length: dataDetails.value.nombreDemandeAutorise }, () => ({
      ville: null,
      academie:null,
      session: dataDetails.value.id,
      libelleLong: dataDetails.value.libelleLong,
    }));
  }
  watchEffect(() => {
  requests.value.forEach((inputForm, index) => {
    onAcademieChange(index);
  });
});
  console.log("Données initialisées :", dataListeVille.value, dataListe.value, dataDetails.value);
  console.log("Requests après initialisation :", requests.value);
});
</script>