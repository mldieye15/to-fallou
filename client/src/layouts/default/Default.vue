<template>
  <v-main id="inspire" class="d-flex flex-column" justify="space-between"  height="100vh">
    <Navbar :appName="appName"/>
    <router-view/>
    <Footer :copyrightName="copyrightName" align-self="end"/>
  </v-main>
  
  
</template>

<script setup>
import Navbar from './Navbar.vue';
import Footer from './Footer.vue';
//  nom de l'application défin au niveau du fihcier .env
const appName = import.meta.env.VITE_APP_NAME;
const copyrightName = import.meta.env.VITE_APP_COPYRIGHT;

</script>
