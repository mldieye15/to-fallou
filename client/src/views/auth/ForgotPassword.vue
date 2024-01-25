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
        :rules="emailRules"
      ></v-text-field>

  <v-row >
    <v-col v-if="userStore.successMessage">
      <v-alert type="success">
        {{ userStore.successMessage }}
      </v-alert>
    </v-col>
    <v-col v-if="userStore.errorMessage">
        <v-alert type="error">
            {{ userStore.errorMessage }}
        </v-alert>
      </v-col>
    <v-col>
      <v-btn @click="reset"
        block
        class="mb-3"
        color="blue"
        size="large"
        variant="tonal"
      >
      Reinitialiser le mot de passe ici
      </v-btn>
    </v-col>
  </v-row>
     
      <v-card-text class="text-center">
      </v-card-text>
    </v-card>
  </div>
</template>
<script setup>
import { useCodeStore } from '@/store/codification';
import { useUserStore } from '@/store/user';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
const router=useRouter();
const email = ref('');
const userStore = useUserStore();
const {resetPassword}=userStore;
const emailRules = [
  (v) => !!v || "L'adresse e-mail est requise",
  (v) => /.+@.+\..+/.test(v) || "Veuillez entrer une adresse e-mail valide",
];
const reset = async () => {
  try {
    if (emailRules.every(rule => rule(email.value) === true)) {
      await resetPassword(email.value);
      console.log('Réinitialisation de mot de passe réussie');
      userStore.successMessage = 'Email de réinitialisation de mot de passe envoyé avec succes. Veuillez vérifier votre e-mail.';
      userStore.errorMessage = '';
  } else {
    userStore.errorMessage = 'Veuillez entrer une adresse e-mail valide.';
    userStore.successMessage = '';
    console.error('Adresse e-mail non valide');
  } 
    // router.push({ name: 'login' });
  } catch (error) {
    console.error('Erreur lors de la réinitialisation de mot de passe', error);
  }
};
</script>
