<template>
    <v-breadcrumbs :class="iconColorClass" :items="items"></v-breadcrumbs>
</template>
<script setup>


defineProps({
  items: [],
  iconColorClass: {
    type: String,
    default: "text-black"
  }
});


</script>

<style scoped>

</style>