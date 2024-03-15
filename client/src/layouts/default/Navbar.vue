<template>
  <v-app-bar app flat color="white" class="px-12">
    <div class="d-flex align-center">
      <v-img
        alt="Logo"
        class="shrink mr-2 logo"
        rounded
        lazy-src=""
        :src="imageUrl"
        transition="scale-transition"
        width="50"
      />
      <h1 class="text-body-1 mb-0 black--text">
          <router-link :to="{name:'home'}"><h3 class="text hidden-sm-and-down text-blue-darken-4"> {{ appName }}</h3></router-link>
          </h1>
    </div>
    <v-spacer></v-spacer>
    <!-- <v-btn class="text-button">
    <router-link :to="{name:'aide'}"><span class="text-white">{{ $t('public.nav.top.aide') }}</span></router-link>
    </v-btn> -->
    <!-- <v-btn class="text-caption" style="background-color: white; color: blue;">
    <router-link :to="{name:'login'}"><span class="text-black">{{ $t('public.nav.top.monespace') }}</span></router-link>
    </v-btn> -->
    <!-- <Localisation/> -->
  </v-app-bar>
</template>
  
<script setup>
import imageUrl from '@/assets/imgs/logo_ucad.png'
import Localisation from '@/components/core/Localisation.vue';

defineProps({
  appName: String
});
const drawer = false;
</script>

<style scoped>
router-link, a{
    text-decoration: dashed !important;
 }
</style>