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
import { useEtablissementStore } from "../store";
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const etablissementStore = useEtablissementStore();
const { dataDetails, loading } = storeToRefs(etablissementStore);
const { one, modify } = etablissementStore;

const inputForm = reactive({
  libelleLong:'',
  libelleCourt:'',
  typeEtablissement: null,
  ville: null,
}); 

const handleSave = (payload) => {
  modify(route.params.id, payload).then( () => {
    addNotification({
        show: true,
        text:  i18n.t('updated'),
        color: 'blue'
      });
    router.push( { name: 'etablissement-liste'});
  });
}
 
onMounted(()=>{
  one(route.params.id ).then( () => {
    inputForm.libelleLong = dataDetails.value.libelleLong,
    inputForm.libelleCourt = dataDetails.value.libelleCourt,
    inputForm.typeEtablissement=dataDetails.value.typeEtablissement?dataDetails.value.typeEtablissement.id:null,
    inputForm.ville = dataDetails.value.ville?dataDetails.value.ville.id:null

  });
});


</script>
