<template>
  <div>
    <FormVue :inputForm="inputForm" :actionSubmit="handleSave" />
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance, onMounted, computed, ref } from "vue";
import { storeToRefs } from "pinia";
import { useRouter, useRoute } from 'vue-router';
import { useNotificationStore } from "@/store/notification";
import { useI18n } from "vue-i18n";

//  
import FormVue from "./Form.vue";
import { useSessionStore } from "../store";
import { useToast } from 'vue-toastification';


const toast= useToast();
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const sessionStore = useSessionStore();
const { dataDetails, loading } = storeToRefs(sessionStore);
const { one, modify } = sessionStore;

const inputForm = reactive({
  libelleLong:'',
  dateDebut:null,
  dateFin:null,
  nombreDemandeAutorise:'',
  delaisValidation:'',
  dateOuvertureDepotCandidature:null,
  dateClotureDepotCandidature:null,
  annee:null,
  typeSession:null,
});

const handleSave = (payload) => {
  modify(route.params.id, payload).then( () => {
    // addNotification({
    //     show: true,
    //     text:  i18n.t('updated'),
    //     color: 'blue'
    //   });
    toast.success(i18n.t('updated'));
    router.push( { name: 'session-liste'});
  });
}

onMounted(()=>{
  one(route.params.id ).then( () => {
    inputForm.libelleLong = dataDetails.value.libelleLong,
    inputForm.dateDebut = dataDetails.value.dateDebut,
    inputForm.dateFin = dataDetails.value.dateFin,
    inputForm.nombreDemandeAutorise = dataDetails.value.nombreDemandeAutorise,
    inputForm.delaisValidation = dataDetails.value.delaisValidation,
    inputForm.dateOuvertureDepotCandidature = dataDetails.value.dateOuvertureDepotCandidature,
    inputForm.dateClotureDepotCandidature = dataDetails.value.dateClotureDepotCandidature
    inputForm.annee = dataDetails.value.annee ? dataDetails.value.annee.id : null;
    inputForm.typeSession = dataDetails.value.typeSession ? dataDetails.value.typeSession.id : null;
    


  });
});


</script>
