<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.codification.codification') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="submit" ref="codificationForm" :value="formValid">
      <v-text-field
        id="email"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="email"
        density="compact"
        :label="$t('apps.forms.codification.email')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.email"
        variant="underlined"
      ></v-text-field>
      <v-text-field
        id="code"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="code"
        density="compact"
        :label="$t('apps.forms.codification.code')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.code"
        variant="underlined"
      ></v-text-field>

      <v-btn block class="mt-2 mb-8" size="large" color="primary" @click="handleSave">{{ $t('apps.forms.enregistrer') }}</v-btn>
    </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance } from "vue";

const instance = getCurrentInstance();

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
  if(instance.refs.codificationForm.validate){
    actionSubmit(inputForm);
  }
}

</script>
