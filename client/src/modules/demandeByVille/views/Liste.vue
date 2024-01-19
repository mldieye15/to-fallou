<template>
  <div>
    <p class="text-h6">{{ $t('apps.forms.user.user') }}</p>
    
    <v-container class="my-5">
      <vue-good-table
        :columns="columns"
        :rows="dataListeVille"
        :pagination-options="{
          enabled: true,
          mode: 'pages',
          perPageDropdown: [5, 10, 15,20, 30, 40, 50]
          }"
         :search-options="{
            enabled: true
          }" 
           
        >    
        <template #table-row="props">
          <div v-if="props.column.field === 'totalDemandes'">
            <router-link :to="{ name: 'admin-details', params: { id: props.row.id } }"> 
            </router-link>
            <v-btn  variant="flat" color="teal" size="small" @click.prevent="redirectToDemandes(props.row.id)" class="">
              Voir demandes
              <div class="ml-3">
                <p color="teal"  size="small" variant="outlined" >
              ( {{ props.row.totalDemandes}})
              </p>
              </div>
            </v-btn>
          </div> 
          <div v-if="props.column.field === 'totalJury'">
              <v-chip color="light-blue-darken-4"  size="small" variant="outlined" >
              {{ props.row.totalJury}}
            </v-chip>
          </div>
        </template>
      </vue-good-table>
    </v-container>
    
  </div>
</template>

<script setup>
import { storeToRefs } from "pinia";
import { onMounted, reactive, ref } from "vue"
import { useNotificationStore } from "@/store/notification";
import { useI18n } from "vue-i18n";
import { useVilleStore } from "@/modules/ville/store";
import { useRouter } from "vue-router";


const router = useRouter();

const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const villeStore = useVilleStore();
const { dataListeVille, headerTable, loading,columns } = storeToRefs(villeStore);
const { all, destroy } = villeStore;

const liste = reactive({ items: [] });
const headers = reactive({ items: [] });
const searchValue = ref("");
const dialog = ref(false);

onMounted(()=>{
  all();
});

const redirectToDemandes = (id) => {
  router.push({ name: 'demandeByVille-demandes', params: { id } });
};
</script>
<style scoped>
.v-text-field {
  background-color: white;
}
.v-text-field:hover {
  background-color: white;
}
.actions-wrapper {
  width: 120px;
}
</style>
