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
import { useUtilisateurStore }from "@/modules/user/store";
import { useToast } from 'vue-toastification';


const toast= useToast();
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();

const userStore = useUtilisateurStore();
const { inscription } = userStore;

const inputForm= reactive({
  prenoms: "",
  nom: "",
  matricule: "",
  // dateNaiss:null,
  email: "",
  username: "",
  mdpasse: "",
  sexe: "",
  code: "",
  telephone: "",
  anciennete: "",
  fonction: null,
  etablissement: null,
  banque: "",
  codeBanque: "",
  codeAgence:"",
  numeroCompte: "",
  cleRib: "",

});

const handleSave = (payload) => {
  inscription(payload).then( () => {
    // addNotification({
    //     show: true,
    //     text:  i18n.t('added'),
    //     color: 'blue'
    //   });
    toast.success(i18n.t('added'));
    router.push( { name: 'user-liste'});
  });
}

</script>
