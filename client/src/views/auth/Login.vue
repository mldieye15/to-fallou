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
    <v-form @keyup.enter="handleLogin" ref="loginForm">
      <v-text-field
        density="compact"
        prepend-inner-icon="mdi-account"
        name="username"
        :label="$t('auth.forms.authentification.username')"
        type="text"
        variant="underlined"
        v-model="userForm.email"
        color="balck"
      ></v-text-field>

      <div  class="text-subtitle-1 text-medium-emphasis d-flex align-center justify-space-between">
        &nbsp;
        <router-link :to="{name:'forgotPassword'}" class="text-caption text-decoration-none text-blue font-italic">{{ $t('auth.forms.authentification.mdpoublie') }}</router-link>
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
        v-model="userForm.password"
        variant="underlined"
      ></v-text-field>
      <div
      v-if="errorMessage"
      class="mb-3 responsive-text"
      v-html="errorMessage"
      style="white-space: pre-line; max-width: 100%;">
    </div>

      <v-btn variant="tonal" block class="mt-2 mb-8" size="large" color="primary" @click="handleLogin">{{ $t('auth.forms.authentification.btnconn') }}</v-btn>
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

<script setup>
import { storeToRefs } from "pinia";
import { ref, reactive, getCurrentInstance } from "vue";
import { useRouter } from 'vue-router'
import { useNotificationStore } from "@/store/notification";
//  recupération des states et des actions définies dans la store
import { useUserStore } from "@/store/user";
import axios from '@/plugins/axios.js'
import { useI18n } from "vue-i18n";
import { useToast } from 'vue-toastification';


const toast= useToast();
const errorMessage = ref("");
//
const instance = getCurrentInstance();
const router = useRouter();
const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;
const userStore = useUserStore();
const { isLoggedIn, userDetails, refreshToken, username, users, loading, error } = storeToRefs(userStore);
const { login } = useUserStore();
const i18n = useI18n();

//  définition de quelques variables utilisées dans le formulaire
const formValid = ref(false);
const showPwd = ref(false);

const userForm = reactive({
  //username:'',
  email:'',
  password:''
});

const rules = reactive({
  required: value => !!value || 'Champ obligatoire.',
  min: v => v.length >= 6 || '6 cractére au moins',
  //emailMatch: () => (`The email and password you entered don't match`),
});
//  traitement de la connexion
const handleLogin = async () => {
  errorMessage.value = ""; // Réinitialisation du message d'erreur
  try {
    await userStore.login(userForm);

    if (!userStore.error) {
      const role = localStorage.getItem('role');
      const name = localStorage.getItem('fullname');
      if (role === 'ROLE_USER') {
        router.push({ name: 'accueil' });
      } else {
        router.push({ name: 'dashboard' });
      }
      toast.success(i18n.t('welcome') + ' ' + name);
    }
  } catch (error) {
    console.error('Erreur lors de la connexion:', error);

    if (error.response) {
      console.log("Réponse du backend:", error.response);
      if (error.response.data?.errorMessage === "User is disabled") {
        errorMessage.value =errorMessage.value = `Vous n'etes pas inscrit (e) dans votre établissement pour la session du Baccalauréat ${new Date().getFullYear()} dans le cadre de la formation des présidents de jury.`;
        // toast.error("Votre compte est désactivé. Contactez l'administrateur.");
      }else if (error.response.data?.errorMessage === "User account is locked") {
        errorMessage.value =errorMessage.value = ` <p>A partir des conclusions issues de l'évaluation de la session du Baccalauréat ${new Date().getFullYear() -1}, vous n'êtes pas autorisé(e) à candidater en tant que président de jury pour cette année.<br> Veuillez vous rapprocher du coordonateur des supervisseurs.</p>`;
        ;
        // toast.error("Votre compte est désactivé. Contactez l'administrateur.");
      }else if (error.response.data?.errorMessage === "User account has expired") {
        errorMessage.value =errorMessage.value = ` <p>A partir des conclusions issues de l'évaluation de la session du Baccalauréat ${new Date().getFullYear() -1}, vous n'êtes plus autorisé(e) à candidater en tant que président de jury du Baccalauréat.<br> Voudriez-vous bien vous rapprocher du coordonateur des supervisseurs.</p>`;
        ;
        // toast.error("Votre compte est désactivé. Contactez l'administrateur.");
      }
      else if (error.response.data?.errorMessage === "Bad credentials") {
        errorMessage.value = "Email ou mot de passe incorrect. Veuillez réessayer.";
        // toast.error("Votre compte est désactivé. Contactez l'administrateur.");
      }
       else if (error.response.data?.message) {
        toast.error(error.response.data.message);
      } else {
        toast.error("Une erreur est survenue. Veuillez réessayer.");
      }
    } else {
      toast.error("Erreur de connexion au serveur.");
    }
  }
};
</script>
<style>
.responsive-text {
  font-size: 0.9rem;
  color: brown; /* Taille normale */
}

@media (max-width: 600px) {
  .responsive-text {
    font-size: 0.7rem;
    color:brown; /* Réduction sur petits écrans */
  }
}
</style>
