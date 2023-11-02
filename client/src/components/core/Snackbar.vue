<template>
  <div class="text-center">
    <v-snackbar 
      v-for="(snackbar, index) in notifications "
      :key="index"
      :timeout="-1"
      :value="true"
      relative
      middle
      top
      :color="snackbar.color"
      elevation="24"
      v-model="snackbar.show"
    >
    {{snackbar.text}}
    <template v-slot:actions>
      <v-btn
        color="white"
        text
        @click="removeNotification(index)"
        style="color: azure;"
      >
        {{ $t('close') }}
      </v-btn>
    </template>
    </v-snackbar>
  </div>
</template>
  
<script setup>
import { storeToRefs } from "pinia";
import { useLangStore } from "@/store/lang";
import { useI18n } from "vue-i18n";

const langStore = useLangStore();
const { locales } = storeToRefs(langStore);
const { changeLang } = useLangStore();

const i18n = useI18n();

defineProps({
  notifications: [],
  removeNotification: {
    type: Function,
  }
});


</script>

<style scoped>
  .remove-top-espace{
    margin-top: -15px;
  }
</style>