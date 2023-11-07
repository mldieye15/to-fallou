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
    <v-form @submit.prevent="submit" ref="loginForm" :value="formValid">
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
        <router-link :to="{name:'home'}" class="text-caption text-decoration-none text-blue font-italic">{{ $t('auth.forms.authentification.mdpoublie') }}</router-link>
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
    <!--<v-container fluid  class="d-flex flex-column align-center" style="height: 80vh">
      <v-layout>
        <v-flex xs12 sm8 md4>
          <v-card class="elevation-3" min-width="500">
            <v-toolbar class="px-2 bg-blue">
              <v-toolbar-title >{{ $t('auth.forms.authentification.titre') }}</v-toolbar-title>
            </v-toolbar>
            <v-form @submit.prevent="submit" ref="loginForm" :value="formValid">
              <v-card-text>
                {{userForm}}
                <v-text-field
                  prepend-inner-icon="mdi-account"
                  name="username"
                  :label="$t('auth.forms.authentification.username')"
                  type="text"
                  variant="underlined"
                  v-model="userForm.email"
                ></v-text-field>
                <v-text-field
                  id="password"
                  prepend-inner-icon="mdi-lock"
                  name="password"
                  :label="$t('auth.forms.authentification.mdp')"
                  :append-icon="showPwd ? 'mdi-eye' : 'mdi-eye-off'"
                  :rules="[rules.required, rules.min]"
                  :type="showPwd ? 'text' : 'password'"
                  @click:append="showPwd = !showPwd"
                  v-model="userForm.password"
                  variant="underlined"
                ></v-text-field>
                <p class="subheading font-weight-regular text-white-50">
                  <router-link :to="{name:'home'}" class="text">{{ $t('auth.forms.authentification.mdpoublie') }}</router-link>
                </p>
            </v-card-text>
            <v-card-actions class="d-flex flex-column px-2">
              <v-btn type="submit" variant="outlined" color="primary" block class="mt-2" @click="handleLogin">{{ $t('auth.forms.authentification.btnconn') }}</v-btn>
              
            </v-card-actions>
          </v-form>
          <div class=" mb-3 px-2">
            <p class="subheading font-weight-regular text-white-50" style="align-items: flex-start;">
              {{ $t('auth.forms.authentification.pasdecompte') }} 
              </p>
                <p class="text-body-2 ">
                  {{ $t('auth.forms.authentification.pasdecompte') }} <router-link :to="{name:'register'}" class="text">{{ $t('public.nav.top.inscription') }}</router-link>
                </p>
            </div>
          </v-card>
        </v-flex>
      </v-layout>
    </v-container>-->
  </div>
</template>

<script setup>
import { storeToRefs } from "pinia";
import { ref, reactive, getCurrentInstance } from "vue";
import { useRouter } from 'vue-router'
//  recupération des states et des actions définies dans la store
import { useUserStore } from "@/store/user";
import axios from '@/plugins/axios.js'

//
const instance = getCurrentInstance();
const router = useRouter();

const userStore = useUserStore();
const { isLoggedIn, userDetails, refreshToken, username, users, loading, error } = storeToRefs(userStore);
const { login } = useUserStore();

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
const handleLogin = () => {
  //  instance.refs.file.value = null
  /*console.log("Axios importe: ", axios);
  console.log("Axios from instance: ", instance.appContext.config.globalProperties.http);
  console.log("Debug 3: ", instance);
  try {
    const response = axios.get(`http://localhost:5500/annees`)
    console.log("With axios importé",response.data);
  } catch (e) {
    this.errors.push(e)
  }*/
  //console.log(instance.refs.loginForm.validate);
  
  if(instance.refs.loginForm.validate){
     login(userForm).then( () => {
      console.log("Debug 2: ",userForm);
      router.push( { name: 'dashboard'});
      this.addNotification({
        show: true,
        text:  this.$i18n.t('welcome')+' '+this.user.username,
        color: 'black'
      });
    });
  }
}
</script>
