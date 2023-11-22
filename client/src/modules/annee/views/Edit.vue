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
import { useAnneeStore } from "../store";
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const anneeStore = useAnneeStore();
const { dataDetails, loading } = storeToRefs(anneeStore);
const { one, modify } = anneeStore;

const inputForm = reactive({
  libelleLong: ''
});

const handleSave = (payload) => {
  modify(route.params.id, payload).then( () => {
    addNotification({
        show: true,
        text:  i18n.t('updated'),
        color: 'blue'
      });
    router.push( { name: 'annee-liste'});
  });
}

onMounted(()=>{
  one(route.params.id ).then( () => {
    inputForm.libelleLong = dataDetails.value.libelleLong
  });
});

</script>
