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
import { useSessionStore } from "../store";
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();

const sessionStore = useSessionStore();
const { add } = sessionStore;

const inputForm= reactive({
  libelleLong:'',
  dateDebut:'',
  dateFin:'',
  nombreDemandeAutorise:'',
  delaisValidation:'',
  dateOuvertureDepotCandidature:'',
  dateClotureDepotCandidature:'',
  annee: null
});

const handleSave = (payload) => {
  add(payload).then( () => {
    addNotification({
        show: true,
        text:  i18n.t('added'),
        color: 'blue'
      });
    router.push( { name: 'session-liste'});
  });
}

</script>
