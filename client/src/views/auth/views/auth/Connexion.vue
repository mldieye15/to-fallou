<script setup >
import { ref } from 'vue';
import { useAuthStore } from '@/store/authStore';
import api from '@/api/apiAuth';
import router from "@/router/index";
const authStore = useAuthStore();
const formData=ref({
  username: "",
  password: "",

})
const login = async () => {
  try {
    // Effectuez la requête de connexion avec Axios
    console.log("formData :"+formData.value)
    const response = await api.post('/auth/v1/connexion', formData.value);
    

    // Si la connexion réussit, mettez à jour l'état d'authentification
    authStore.login(response.data.accessToken, response.data.refreshToken);

    // En cas de succès :
    // toast.success('Connexion réussie !');

    // Redirigez l'utilisateur vers la page de tableau de bord ou toute autre page souhaitée
    router.push('/app/dashboard')
  } catch (error) {
    // En cas d'échec :
    // toast.error('La connexion a échoué. Veuillez vérifier vos identifiants.');
  }
};

</script>
<template>
    <div class="space-header-to-footer">
      <v-card
        class="mx-auto pa-12 pb-8 mt-5"
        elevation="8"
        max-width="500"
        rounded="lg"
      >
      <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('auth.forms.authentification.titre') }}</h2>
      <v-divider class="my-3" color="white"></v-divider>
      <v-form @submit.prevent="login" :value="formValid">
        <v-text-field
          density="compact"
          prepend-inner-icon="mdi-account"
          name="username"
          :label="$t('auth.forms.authentification.username')"
          type="text"
          variant="underlined"
          v-model="formData.username"
          color="balck"
        ></v-text-field>
  
        <div  class="text-subtitle-1 text-medium-emphasis d-flex align-center justify-space-between">
          &nbsp;
          <router-link :to="{name:'home'}" class="text-caption text-decoration-none text-blue font-italic">{{ $t('auth.forms.authentification.mdpoublie') }}</router-link>
        </div>
        <v-text-field
          id="password"
          prepend-inner-icon="mdi-lock"
          name="password"
          density="compact"
          :label="$t('auth.forms.authentification.mdp')"
          :append-inner-icon="showPwd ? 'mdi-eye' : 'mdi-eye-off'"
          :type="showPwd ? 'text' : 'password'"
          @click:append-inner="showPwd = !showPwd"
          v-model="formData.password"
          variant="underlined"
        ></v-text-field>
  
        <v-btn type="submit" variant="tonal" block class="mt-2 mb-8" size="large" color="primary">{{ $t('auth.forms.authentification.btnconn') }}</v-btn>
      </v-form>
      
  
      <v-card-text class="text-center">
        <div class=" mb-3 px-2">
          <p class="subheading font-weight-regular text-white-50" style="align-items: flex-start;">
            {{ $t('auth.forms.authentification.pasdecompte') }} 
          </p>
          <p class="text-body-2 ">
            {{ $t('auth.forms.authentification.pasdecompte') }} <router-link :to="{name:'register'}" class="text text-blue text-decoration-none font-italic">{{ $t('public.nav.top.inscription') }}  <v-icon icon="mdi-chevron-right"></v-icon></router-link>
          </p>
        </div>
      </v-card-text>
    </v-card>
    </div>
  </template>