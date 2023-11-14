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
import { useUtilisateurStore } from "../store";
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const userStore = useUtilisateurStore();
const { dataDetails, loading } = storeToRefs(userStore);
const { one, modify } = userStore;

const inputForm = reactive({
  prenoms: "",
  nom: "",
  matricule: "",
  dateNaiss:"",
  email: "",
  username: "",
  mdpasse: "",
  sexe: "",
  code: "",
  telephone: "",
  anciennete: "",
  fonction: null,
  etablissement: null,
});

const handleSave = (payload) => {
  modify(route.params.id, payload).then( () => {
    addNotification({
        show: true,
        text:  i18n.t('updated'),
        color: 'blue'
      });
    router.push( { name: 'user-liste'});
  });
}

onMounted(()=>{
  one(route.params.id ).then( () => {
    inputForm.prenoms = dataDetails.value.prenoms,
    inputForm.nom = dataDetails.value.nom,
    inputForm.matricule = dataDetails.value.matricule,
    inputForm.dateNaiss = dataDetails.value.dateNaiss,
    inputForm.email = dataDetails.value.email,
    inputForm.username = dataDetails.value.username,
    inputForm.mdpasse = dataDetails.value.mdpasse
    inputForm.sexe = dataDetails.value.sexe,
    inputForm.code = dataDetails.value.code,
    inputForm.telephone = dataDetails.value.telephone,
    inputForm.anciennete = dataDetails.value.anciennete

    inputForm.fonction=dataDetails.value.fonction.id,
    inputForm.etablissement=dataDetails.value.etablissement.id
    


  });
});


</script>
