<template>
  <div>
    <FormVue :inputForm="inputForm" :actionSubmit="handleSave" :isEdit="true" />
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
import { useJuryStore } from "../store";
import { useToast } from 'vue-toastification';


const toast= useToast();
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const juryStore = useJuryStore();
const { dataDetails, loading } = storeToRefs(juryStore);
const { one, modify } = juryStore;

const inputForm = reactive({
  id: "",
  centre: null,
  technique :"",
  numero:'',
});
const handleSave = (payload) => {
  modify(route.params.id, payload).then( () => {
    // addNotification({
    //     show: true,
    //     text:  i18n.t('updated'),
    //     color: 'blue'
    //   });
    toast.success(i18n.t('updated'));
    router.push( { name: 'jury-liste'});
  });
}

onMounted(()=>{
  one(route.params.id ).then( () => {
    inputForm.id= dataDetails.value.id,
    inputForm.centre= dataDetails.value.centre?dataDetails.value.centre.id:null,
    inputForm.technique= dataDetails.value.technique,
    inputForm.numero= dataDetails.value.numero
  });
});


</script>
