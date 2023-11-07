<template>
    <v-footer height="auto" padless inset fixed class="bg-black-footer">
        <v-layout justify-center row wrap >
      <v-card class="flex bg-black-footer align-center w-100">
            <v-card-text class="py-4 text-white text-center">
                &copy; {{ new Date().getFullYear() }} — <strong>{{ copyrightName }}</strong>, {{ $t('allrightreserved') }}.
            </v-card-text>
        </v-card>
    </v-layout>
        
    </v-footer>
</template>

<script setup>
defineProps({
    copyrightName: String
})
</script>

<style scoped>

</style>