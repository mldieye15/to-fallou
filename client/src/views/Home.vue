<template>
  <div class="d-flex flex-column align-center mt-0 " >
      <v-row no-gutters class="bg-blue w-100 px-12 pt-1 mb-0 pb-10 flex-sm-row">
        <v-col cols="10">
          <v-flex xs12 sm8 md4>
            <h1 class=" font-weight-bold mt-0 text-center">
              {{ $t('public.hero.titre') }}
            </h1>
            <h3 class="subheading font-weight-bold text-white">
              {{ $t('public.hero.soustitre') }}
              <br>{{ $t('public.hero.pasdecompte') }}
              <v-btn @click.prevent="redirectToRegistre ()" class="text-black text-caption ml-2">{{ $t('public.nav.top.inscription') }}</v-btn>
              <!-- <v-btn class="text-caption large" large>
                <router-link :to="{name:'register'}"><span class="text hidden-sm-and-down text-black"> </span></router-link>
              </v-btn> -->
              <!-- <v-btn  class="text-caption ml-2" style="background-color: white; color: blue;">
                <router-link :to="{name:'login'}"><span class="text-black">Se connecter</span></router-link>
              </v-btn> -->
              <v-btn @click.prevent="redirectToLogin ()" class="text-black text-caption ml-2">Se connecter</v-btn>
            </h3>
          </v-flex>
        </v-col>
        <!-- <v-col cols="2">
          <v-flex xs12 sm8 md4>
            <v-card>
              <v-img
                :src="heroImage2"
                :lazy-src="heroImage"
                cover
                class="mx-auto"
                max-height="150"
                max-width="200"
              ></v-img>
            </v-card>
          </v-flex>
        </v-col> -->
      </v-row>
      <v-row no-gutters class="custom-bg-blue  mb-3">
        <!-- <v-col cols="3">
            <v-card elevation="10">
              <v-img
                :src="dir"
                :lazy-src="heroImage"
                cover
                class="mx-auto"
                max-height="500"
                max-width="500"
              ></v-img>
            </v-card>
        </v-col> -->
        <v-col cols="6" justify-end class="mt-1" style="font-size: 18px;">
          <div class="ml-10">
          <p class="mb-2 mt-3"><strong>Responsabilités du Président de Jury</strong></p>
          <ul>
            <li>Disposer d’un ordinateur portable ayant un système d’exploitation compatible avec le logiciel d’organisation de l’examen du baccalauréat</li>
            <li>Avoir des aptitudes liées à l’utilisation d’un ordinateur portable, d’un logiciel de traitement de texte, d’un tableur, l’impression de documents, et la transmission de documents par Internet</li>
            <li>Connaître le logiciel d’organisation de l’examen du baccalauréat</li>
            <li>Coopérer avec le superviseur des présidents de jury</li>
            <li>Respecter l’éthique et la déontologie de l’office du baccalauréat</li>
          </ul>
        </div>
        </v-col>
        <v-col cols="6" justify-end class="mt-2 text-wthite" style="font-size: 18px;">
          <div class="ml-10">
          <p class="mb-2 mt-3"><strong>Mission Pédagogique et Administrative</strong></p>
          <ul>
            <li>Garantir l’impartialité des débats et l’égalité de traitement des candidats</li>
            <li>Réguler harmonieusement l’ensemble des documents pédagogique et administratif du jury</li>
            <li>Garantir qu’aucune erreur matérielle n’est commise dans les différentes opérations qui incombent au jury</li>
            <li>Garantir la souveraineté </li>
            <li>Garantir le secret des délibérations</li>
          </ul>
        </div>
        </v-col>
      </v-row>
  </div>
</template>

<script setup>
import heroImage from '@/assets/imgs/off1.jpeg'
import heroImage2 from '@/assets/imgs/off2.jpg'
import dir from '@/assets/imgs/dir3.jpg'
import { useRouter } from 'vue-router';
const router = useRouter();
const redirectToLogin = () => {
  router.push({ name: 'login'});
};
const redirectToRegistre = () => {
  router.push({ name: 'register'});
};
</script>
<style scoped>
/* .custom-bg-blue {
  background-image: url('@/assets/imgs/OFFICEDUBAC.jpg');
  background-size: 2000px 800px; 
  background-position: center; 
  height: 700px;
} */
</style>
