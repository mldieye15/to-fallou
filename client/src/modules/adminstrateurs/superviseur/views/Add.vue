<template>
  <div>
    <FormVue :inputForm="inputForm" :actionSubmit="handleSave"/>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance } from "vue";
import { useRouter } from 'vue-router';
import { useNotificationStore } from "@/store/notification";
import { useI18n } from "vue-i18n";

//  
import FormVue from "./Form.vue";
import { useAdminStore } from "../../store";
import { useToast } from 'vue-toastification';


const toast= useToast();
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();

const adminStore = useAdminStore();
const { addSupervisseur } = adminStore;

const inputForm= reactive({
  prenoms: "",
  nom: "",
  matricule: "",
  // dateNaiss:null,
  email: "",
  username: "",
  mdpasse: "",
  sexe: "",
  telephone: "",
});

const handleSave = (payload) => {
  addSupervisseur(payload).then( () => {
    // addNotification({
    //     show: true,
    //     text:  i18n.t('added'),
    //     color: 'blue'
    //   });
    toast.success(i18n.t('added'));
    router.push( { name: 'sup-liste'});
  });
}

</script>
