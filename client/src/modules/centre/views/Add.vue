<template>
  <div>
    <FormVue :inputForm="inputForm" :actionSubmit="handleSave"  :isEdit="false"/>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance } from "vue";
import { useRouter } from 'vue-router';
import { useNotificationStore } from "@/store/notification";
import { useI18n } from "vue-i18n";

//  
import FormVue from "./Form.vue";
import { useCentreStore } from "../store";
import { useToast } from 'vue-toastification';


const toast= useToast();
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();

const centreStore = useCentreStore();
const { add } = centreStore;

const inputForm= reactive({
  libelleLong:'',
  libelleCourt:'',
  ville: null,
  typeCentre: null,
  error: false
});
const updateLibelleLong = () => {
  if (inputForm.typeSession && inputForm.annee) {
    const typeSessionLibelleLong = getTypeSessionLibelleLong(inputForm.typeSession);
    const anneeLibelleLong = getAnneeLibelleLong(inputForm.annee);
    const libelleLong = `Session ${typeSessionLibelleLong} ${anneeLibelleLong}`;
    inputForm.libelleLong = libelleLong;
  }
};

const handleSave = (payload) => {
  add(payload).then( () => {
    // addNotification({
    //     show: true,
    //     text:  i18n.t('added'),
    //     color: 'blue'
    //   });
    toast.success(i18n.t('added'));
    router.push( { name: 'centre-liste'});
  });
}

</script>
