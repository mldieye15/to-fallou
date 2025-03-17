<template>
  <div>
    <p class="text-h6">{{ $t('apps.forms.academie.academie') }}</p>
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
import { useCandidatAuthoriserStore } from "../store";
import { useToast } from 'vue-toastification';


const toast= useToast();
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const academietore = useCandidatAuthoriserStore();
const { dataDetails, loading } = storeToRefs(academietore);
const { one, modify } = academietore;

const inputForm = reactive({
  id:"",
  libelleLong: '',
  libelleCourt: '',
});

const handleSave = (payload) => {
  modify(route.params.id, payload).then( () => {
    // addNotification({
    //     show: true,
    //     text:  i18n.t('updated'),
    //     color: 'blue'
    //   });
    toast.success(i18n.t('updated'));
    router.push( { name: 'academie-liste'});
  });
}

onMounted(()=>{
  one(route.params.id ).then( () => {
    inputForm.id = dataDetails.value.id
    inputForm.libelleLong = dataDetails.value.libelleLong
    inputForm.libelleCourt = dataDetails.value.libelleCourt
  });
});


</script>
