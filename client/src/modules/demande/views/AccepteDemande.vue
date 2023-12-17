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
  import { useDemandeStore } from "../store";
  const i18n = useI18n();
  
  const notificationStore = useNotificationStore();
  const { addNotification } = notificationStore;
  
  const instance = getCurrentInstance();
  const router = useRouter();
  const route = useRoute();
  
  const demandeStore = useDemandeStore();
  const { dataDetails, loading } = storeToRefs(demandeStore);
  const { one, modify,accepterDemande } = demandeStore;
  
  const inputForm = reactive({
    session:null,
    ville:null,
    academie:null,
    centre:null,
  });
  
  const handleSave = (payload) => {
    accepterDemande(route.params.id, payload).then( () => {
      addNotification({
          show: true,
          text:  i18n.t('updated'),
          color: 'blue'
        });
      router.push( { name: 'demande-liste'});
    });
  }
  onMounted(()=>{
    one(route.params.id ).then( () => {
      inputForm.session=dataDetails.value.session?dataDetails.value.session.id:null,
      inputForm.academie = dataDetails.value.academie?dataDetails.value.academie.id:null,
      inputForm.ville = dataDetails.value.ville?dataDetails.value.ville.id:null
    });
  });
  
  
  </script>
  