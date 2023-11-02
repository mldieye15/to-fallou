<template>
  <div>
    <p class="text-h6">{{ $t('apps.forms.academie.academie') }}</p>
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
import { useAcademieStore } from "../store";
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();

const academieStore = useAcademieStore();
const { add } = academieStore;

const inputForm= reactive({
  nom:''
});

const handleSave = (payload) => {
  add(payload).then( () => {
    addNotification({
        show: true,
        text:  i18n.t('added'),
        color: 'blue'
      });
    router.push( { name: 'academie-liste'});
  });
}

</script>
