<template>
  <div class="text-left">
    <v-list color="transparent" class="text-white" v-for="item in items" :key="item.id" >
      <!--<router-link :to="{name:item.route}" active-class="active-link" exact-active-class="exact-active-link" :class="{'active-link':isChild(`${item.route}`)}">-->
      <router-link :to="{name:item.route}" v-slot="{ isActive, isExactActive }" class="text-blue">
        <v-list-item  :class="[isActive && 'router-link-active', isExactActive && 'router-link-exact-active']" :prepend-icon="item.icon" :title="$t(`apps.side.${item.translate}`) "></v-list-item>
      </router-link>
    </v-list>
  </div>
</template>
  
<script setup>
import { computed } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();

defineProps({
  items: {
    type: Object,
    //default:() => defaultSideBarItems
  }
});

// pass it a string to check if the route has a parent with that name. its wrapped in a computed so that its reactive
const isChild = computed( () => (name) => {
    return route.matched.map((item) => {
        return item.name;
    }).includes(name);
});
//  route active: https://v3.router.vuejs.org/api/#v-slot-api-3-1-0
// https://www.webmound.com/custom-active-class-to-router-link-in-vue-3/
</script>

<style scoped>


.router-link-exact-active {
    color: #F0F0FF;
    font-weight: bold;
    border-left: 4px solid #e1d8d7;
    background-color: #3592d5 ;
}
</style>