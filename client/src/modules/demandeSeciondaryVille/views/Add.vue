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
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();

const demandeStore = useDemandeStore();
const { add } = demandeStore;

const inputForm= reactive({
  session: null,
  ville: null,
  academie: null,
  

});

const handleSave = (payload) => {
  add(payload).then( () => {
    addNotification({
        show: true,
        text:  i18n.t('added'),
        color: 'blue'
      });
    router.push( { name: 'demande-liste'});
  });
}

</script>
