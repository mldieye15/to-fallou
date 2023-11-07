<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.jury.jury') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="submit" ref="juryForm" :value="formValid">
      <v-text-field
        id="numero"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="numero"
        density="compact"
        :label="$t('apps.forms.jury.numero')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.numero"
        variant="underlined"
      ></v-text-field>

      <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        name="centre"
        density="compact"
        :label="$t('apps.forms.centre.nom')"
        color="balck"
        v-model="inputForm.centre"
        variant="underlined"
        :items="dataListe"
        persistent-hint
        
        single-line
        item-title="libelleLong"
        item-value="id"
      ></v-select>

      <v-btn block class="mt-2 mb-8" size="large" color="primary" @click="handleSave">{{ $t('apps.forms.enregistrer') }}</v-btn>
    </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance } from "vue";
import { onMounted } from "vue"
import { storeToRefs } from "pinia";
import { useCentreStore } from "@/modules/centre/store";

const instance = getCurrentInstance();

const centreStore = useCentreStore();
const { dataListe } = storeToRefs(centreStore);

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
  if(instance.refs.juryForm.validate){
    actionSubmit(inputForm);
  }
}

onMounted(()=>{
  centreStore.all();
});

</script>
