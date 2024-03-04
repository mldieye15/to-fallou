<template>
    <v-app-bar app flat color="white" class="px-12 bg-blue-navbar">
      <div class="d-flex align-center">
        <v-img
          alt="Logo"
          class="shrink mr-2 logo"
          rounded
          lazy-src=""
          :src="imageUrl"
          transition="scale-transition"
          width="40"
        />
        <h1 class="text-body-1 mb-0 black--text">
            <router-link :to="{name:'home'}"><span class="text hidden-sm-and-down text-white"> {{ appName }}</span></router-link>
            </h1>
            <!-- <router-link :to="{name:'about'}"><span class="text black--text "> A propos</span></router-link>-->
      </div>
      <v-spacer></v-spacer>
      <!-- <v-btn class="text-button">
      <router-link :to="{name:'aide'}"><span class="text-white">{{ $t('public.nav.top.aide') }}</span></router-link>
      </v-btn> -->
      <v-btn class="text-caption" style="background-color: white; color: blue;">
    <router-link :to="{name:'login'}"><span class="text-black">{{ $t('public.nav.top.monespace') }}</span></router-link>
    </v-btn>

      <!-- <Localisation/> -->

    </v-app-bar>
</template>
  
<script setup>
import imageUrl from '@/assets/imgs/logo_blanc.jpg'
import Localisation from '@/components/core/Localisation.vue';

defineProps({
  appName: String
})
;
</script>

<style scoped>
router-link, a{
    text-decoration: dashed !important;
 }
</style>