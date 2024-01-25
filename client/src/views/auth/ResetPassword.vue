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
        prepend-inner-icon="mdi-lock"
        variant="outlined"
        label="Nouveau mot de passe"
        v-model="newPassword"
        :append-inner-icon="showPwd ? 'mdi-eye' : 'mdi-eye-off'"
        :type="showPwd ? 'text' : 'password'"
        @click:append-inner="showPwd = !showPwd"
      ></v-text-field>
        <v-text-field
        prepend-inner-icon="mdi-lock"
        density="compact"
        placeholder="Confirmer"
        :append-inner-icon="showPwd ? 'mdi-eye' : 'mdi-eye-off'"
        :type="showPwd ? 'text' : 'password'"
        @click:append-inner="showPwd = !showPwd"
        variant="outlined"
        label="Confirmer"
        v-model="confirmPassword"
      ></v-text-field>
  
      <v-card
        class="mb-12"
        color="surface-variant"
        variant="tonal"
      >
      </v-card>

      <v-btn
        block
        class="mb-3"
        color="blue"
        size="large"
        variant="tonal"
        @click="resetPassword"
      >
       Reinitialiser le mot de passe
      </v-btn>
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>
      <v-card-text class="text-center">
      </v-card-text>
    </v-card>
  </div>
</template>
<script setup>
import { useUserStore } from '@/store/user';
import { useRouter } from 'vue-router';
import { ref } from 'vue';
const newPassword = ref('');
const confirmPassword = ref('');
const userStore = useUserStore();
const router = useRouter();
const errorMessage = ref('');
const showPwd = ref(false);

const resetPassword = async () => {
  try {
    const token = router.currentRoute.value.params.token;
    if (newPassword.value === confirmPassword.value) {
      await userStore.newPassword(token, newPassword.value);
      errorMessage.value = '';
      // Gérez le succès de la réinitialisation du mot de passe ici
    router.push({ name: 'login' });
    } else {
      // Gérez le cas où les mots de passe ne correspondent pas
      errorMessage.value = "Les mots de passe ne correspondent pas";
      console.error("Les mots de passe ne correspondent pas");
    }
  } catch (error) {
    // Gérez les erreurs de réinitialisation du mot de passe ici
    console.error("Erreur lors de la réinitialisation du mot de passe", error);
    errorMessage.value = "Erreur lors de la réinitialisation du mot de passe";
  }
};

</script>
<style>
.error-message {
  color: red; /* ou toute autre couleur de votre choix */
  margin-top: 5px; /* Ajustez la marge en fonction de vos besoins */
}
</style>
