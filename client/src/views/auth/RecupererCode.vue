<template>
    <div>
      <v-card
        class="mx-auto pa-12 pb-8 mt-5 mb-5" 
        elevation="8"
        max-width="500"
        rounded="lg"
      >
  
        <v-text-field
          density="compact"
          prepend-inner-icon="mdi-email-outline"
          variant="outlined"
          label="Votre Email"
          v-model="email"
        ></v-text-field>
        <v-row v-if="codeStore.code">
          <v-text-field
          density="compact"
          placeholder="Ici votre code"
          icon:="mdi:identifier"
          variant="outlined"
          label="votre code"
          v-model="codeStore.code"
          readonly
        ></v-text-field>
        </v-row>
        <v-row v-if="codeStore.error">
      <v-col>
        <v-alert type="error">
          {{ codeStore.error }}
        </v-alert>
      </v-col>
    </v-row>

    <v-row v-if="codeStore.successMessage">
      <v-col>
        <v-alert type="success">
          {{ codeStore.successMessage }}
        </v-alert>
      </v-col>
    </v-row>
        
  
        <v-card
          class="mb-12"
          color="surface-variant"
          variant="tonal"
        >
          <v-card-text class="text-medium-emphasis text-caption">
            Ce code vous permet de finaliser votre inscription 
          </v-card-text>
        </v-card>
  
        <v-btn @click="getCode"
          block
          class="mb-3"
          color="blue"
          size="large"
          variant="tonal"
        >
         Obtenir le Code
        </v-btn>
        <v-btn @click="sendCode"
          block
          class="mb-3"
          color="blue"
          size="large"
          variant="tonal"
        >
         Email le code dans votre email
        </v-btn>
  
        <v-card-text class="text-center">
        </v-card-text>
      </v-card>
    </div>
  </template>
  <script setup>
  import { useCodeStore } from '@/store/codification';
  import { ref } from 'vue';
  const email = ref('');
  const codeStore = useCodeStore();
  
  const getCode = () => {
    codeStore.getCode(email.value);
  };
  
  const sendCode = () => {
    codeStore.sendCode(email.value);
  };
  </script>
  