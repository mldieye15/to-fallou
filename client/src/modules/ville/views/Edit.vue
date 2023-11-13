<template>
  <div>
    <p class="text-h6">{{ $t('apps.forms.ville.ville') }}</p>
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
import { useVilleStore } from "../store";
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const villeStore = useVilleStore();
const { dataDetails, loading } = storeToRefs(villeStore);
const { one, modify } = villeStore;

const inputForm = reactive({
  libelleLong:'',
  libelleCourt: '',
  academie:null
});

const handleSave = (payload) => {
  modify(route.params.id, payload).then( () => {
    addNotification({
        show: true,
        text:  i18n.t('updated'),
        color: 'blue'
      });
    router.push( { name: 'ville-liste'});
  });
}

onMounted(()=>{
  one(route.params.id ).then( () => {
    inputForm.libelleLong = dataDetails.value.libelleLong
    inputForm.libelleCourt = dataDetails.value.libelleCourt
    inputForm.academie = dataDetails.value.academie.id
  });
});


</script>
