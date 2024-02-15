<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.etablissement.etablissement') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="submit" ref="etablissementForm" :value="formValid">
      <v-text-field
        id="libelleLong"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="libelleLong"
        density="compact"
        :label="$t('apps.forms.etablissement.nom')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.libelleLong"
        variant="underlined"
      ></v-text-field>
      <v-text-field
        id="libelleCourt"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="libelleCourt"
        density="compact"
        :label="$t('apps.forms.etablissement.abreviation')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.libelleCourt"
        variant="underlined"
      ></v-text-field>

      <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        name="typeEtablissement"
        density="compact"
        :label="$t('apps.forms.typeEtablissement.nom')"
        color="balck"
        v-model="inputForm.typeEtablissement"
        variant="underlined"
        :items="dataListeTypeEtablissement"
        persistent-hint
        single-line
        item-title="libelleLong"
        item-value="id"
      ></v-select>
      <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        name="ville"
        density="compact"
        :label="$t('apps.forms.ville.nom')"
        color="balck"
        v-model="inputForm.ville"
        variant="underlined"
        :items="dataListeVille"
        persistent-hint
        
        single-line
        item-title="libelleLong"
        item-value="id"
      ></v-select>

      <v-btn block class="mt-2 mb-8" size="large" color="primary" @click="handleSave">{{ $t('apps.forms.valider') }}</v-btn>
    </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance } from "vue";
import { onMounted } from "vue"
import { storeToRefs } from "pinia";
import { useTypeEtablissementStore } from "@/modules/typeEtablissement/store";
import { useVilleStore } from "@/modules/ville/store";

const instance = getCurrentInstance();
const typeEtablissementStore = useTypeEtablissementStore();
const { dataListeTypeEtablissement } = storeToRefs(typeEtablissementStore);
const villeStore = useVilleStore();
const { dataListeVille } = storeToRefs(villeStore);

const rules = reactive({
  required: value => !!value || 'Champ obligatoire.',
  min: v => v.length >= 2 || '2 cractére au moins',
});

const { inputForm, actionSubmit } = defineProps({
  inputForm: Object,
  actionSubmit: {
    type: Function,
  }
});

const handleSave = () => {
  if(instance.refs.etablissementForm.validate){
    actionSubmit(inputForm);
  }
}

onMounted(()=>{
  typeEtablissementStore.all();
  villeStore.all();
});

</script>
