<template>
  <v-container class="text-center">
    <v-alert variant="flat"
      v-if="successMessage || errorMessage"
      :type="successMessage ? 'success' : 'error'"
    >
      {{ successMessage || errorMessage }}
    </v-alert>
    <!-- Utilisation de v-if pour conditionner l'affichage de la section -->
    <h2 v-if="successMessage">
     Veuillez cliquer
      <router-link :to="{name:'login'}"><span>ici</span></router-link> pour vous connecter.
    </h2>
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
