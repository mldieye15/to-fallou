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
        v-model="userForm.username"
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
        :rules="[rules.required, rules.min]"
        :type="showPwd ? 'text' : 'password'"
        @click:append-inner="showPwd = !showPwd"
        v-model="userForm.password"
        variant="underlined"
      ></v-text-field>

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
  username:'',
  password:''
});

const rules = reactive({
  required: value => !!value || 'Champ obligatoire.',
  min: v => v.length >= 6 || '6 cractére au moins',
  //emailMatch: () => (`The email and password you entered don't match`),
});

//  traitement de la connexion
const handleLogin = () => {

  if(instance.refs.loginForm.validate){
     login(userForm).then( () => {
      console.log("Debug 2: ",userForm);
      const role = localStorage.getItem('role');
      const name = localStorage.getItem('username');
      if (role === 'ROLE_USER') {
        router.push({ name: 'accueil' });
      } else {
        router.push({ name: 'dashboard' });
      }
      // addNotification({
      //   show: true,
      //   text:  i18n.t('welcome')+' '+name,
      //   color: 'black'
      // });
      toast.success(i18n.t('welcome')+' '+name);

    });
  }
}
</script>
