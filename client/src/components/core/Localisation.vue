<template>
    <div class="text-center">
      <v-menu  open-on-hover>
        <template v-slot:activator="{ props }">
          <v-btn  v-bind="props" >
            <v-icon left size="20" :class="iconColorClass">mdi-translate</v-icon>
          </v-btn>
        </template>
        
        <v-list v-model="$i18n.locale">
          <v-list-item v-for="(item, id) in locales" :key="id" @click="switchLocale(item.id)" active-class="border">
            <v-list-item-title>{{ item.locale }}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
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
  iconColorClass: {
    type: String,
    default: "mi-white-texte"
  }
});

const switchLocale = (locale) =>{
  if (i18n.locale.value !== locale){
    i18n.locale.value = locale;
    localStorage.setItem('lang', locale);
    changeLang(locale);
    window.location.reload();
  }
}

</script>