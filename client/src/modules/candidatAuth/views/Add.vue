<template>
  <div>
    <p class="text-h6">{{ $t('apps.forms.academie.academie') }}</p>
    <FormVue :inputForm="inputForm" :actionSubmit="handleSave" :isEdit="false"/>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance } from "vue";
import { useRouter } from 'vue-router';
import { useNotificationStore } from "@/store/notification";
import { useI18n } from "vue-i18n";
import { useToast } from 'vue-toastification';


const toast= useToast();

import FormVue from "./Form.vue";
import { useCandidatAuthoriserStore } from "../store";
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();

const academieStore = useCandidatAuthoriserStore();
const { add } = academieStore;

const inputForm= reactive({
  libelleLong:'',
  libelleCourt: '',
});

const handleSave = (payload) => {
  add(payload).then( () => {
    // addNotification({
    //     show: true,
    //     text:  i18n.t('added'),
    //     color: 'blue'
    //   });
    toast.success(i18n.t('added'));
    router.push( { name: 'academie-liste'});
  });
}

</script>
