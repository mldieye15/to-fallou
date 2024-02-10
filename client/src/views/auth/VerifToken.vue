<template>
  <v-container>
    <v-alert
      v-if="successMessage || errorMessage"
      :type="successMessage ? 'success' : 'error'"
    >
      {{ successMessage || errorMessage }}
    </v-alert>
  </v-container>
</template>

<script setup>
// Importez votre store
import { useUserStore } from '@/store/user';
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
const userStore = useUserStore();
const {verifyToken}=useUserStore();
// Initialisez les variables réactives
const successMessage = ref('');
const errorMessage = ref('');
const router = useRouter();

// Appelez la méthode de vérification du token après le montage du composant
onMounted(async () => {
  try {
    const token = router.currentRoute.value.params.token;
    await verifyToken(token);
    successMessage.value = 'Activation du compte effectuée avec succès.';
  } catch (error) {
    errorMessage.value = 'Erreur lors de la vérification du token.';
    console.error(error);
  }
});
</script>
<style>
</style>
