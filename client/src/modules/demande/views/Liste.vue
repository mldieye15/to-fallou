<template>
      <v-container class="my-6" grid-list-xl>
        <div v-if="loading">Chargement en cours...</div>
        <div v-for="userEntry in paginatedData" :key="userEntry.userId">
      <div class="mb-2 mt-2"><h2>{{ userEntry.user }}</h2></div>
      <vue-good-table
        :columns="columns"
        :rows="userEntry.demandes"
        > 
        <template #table-row="props">
          <span v-if="props.column.field == 'affectable'">
            <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" 
                 :color="props.row.affectable=== 'OUI' ? 'green' : 'red'" text variant="tonal">
              {{ props.row.affectable}}
          </v-chip>
          </span>
          <span v-if="props.column.field == 'etatDemande'">
             <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" :color="etatCouleurs[props.row.etatDemande]" variant="tonal">
            <span>{{ props.row.etatDemande }}</span>
          </v-chip>
          </span>
          <div v-if="props.column.field === 'actions'">
            <div class="actions-wrapper"
             v-if="props.row.etatDemande === 'EN ATTENTE' &&
              props.row.quota === 'OUI' && 
              props.row.hasAcceptedDemande === 'NON'&&
              props.row.affectable === 'OUI'">
              <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" color="green" variant="tonal">
                <router-link :to="{ name: 'accepte-Demande', params: { id: props.row.id } }">
                  <v-icon small flat color="green">mdi-check</v-icon> Accepte
                </router-link>
              </v-chip>
            </div>
          </div>
        </template>
      </vue-good-table>
    </div>
    <div class="mt-2">
      <v-row align="center">
      <v-col>
         <div style="display: flex;">
          <div class="mt-4">
            <p >Utilisateur par page:</p>
          </div>
            
            <v-select
              v-model="selectedItemsPerPage"
              :items="itemsPerPageOptions"
              outlined
              dense
              @change="onItemsPerPageChange"
              class="select-width"
              variant="sollo"
            ></v-select>
          </div>
      </v-col>
      <v-col>
        <v-pagination
          v-model="currentPage"
          :length="totalPages"
          color="green"
        >
          <template v-slot:item="{ page }">
              <v-chip color="blue" class="mt-2" size="small" :class="getPageClass(page)">
                {{ page }}
              </v-chip>
        </template>
      </v-pagination>
      </v-col>
    </v-row>
    </div>
      </v-container>
</template>
<script setup>
import { storeToRefs } from "pinia";
import { useDemandeStore } from "../store";
import { onMounted, reactive, ref,computed } from "vue"
import { useNotificationStore } from "@/store/notification";
import { useCentreStore } from "@/modules/centre/store";
import { useI18n } from "vue-i18n";
import { watchEffect,watch } from "vue";
const centreStore=useCentreStore();
const { dataListeByVille} = storeToRefs(centreStore);

const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const demandeStore = useDemandeStore();
const { dataListeGroupedByUser,columns, headerTable, loading,etatCouleurs,dataListe } = storeToRefs(demandeStore);
const { all, destroy,validerDemande,allGroupedByUser } = demandeStore;

const liste = reactive({ items: [] });
const headers = reactive({ items: [] });
const searchValue = ref("");
const dialog = ref(false);
onMounted(()=>{
  all();
  allGroupedByUser();
  console.log(dataListeGroupedByUser) // ajustez le nombre d'éléments par page selon vos besoins
});
const currentPage = ref(1);
const onItemsPerPageChange = () => {
  currentPage.value = 1; // Réinitialisez la page actuelle lorsque le nombre d'éléments par page change
};

const itemsPerPageOptions = ref([1,2,5, 10, 20, 30]); // Ajoutez d'autres valeurs au besoin
const selectedItemsPerPage = ref(2);
const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * selectedItemsPerPage.value;
  const end = start + selectedItemsPerPage.value;
  const dataList = dataListeGroupedByUser.value || [];
  return dataList.slice(start, end);
});
const totalPages = computed(() => Math.ceil(
  (dataListeGroupedByUser.value || []).length / selectedItemsPerPage.value));
  watch(() => selectedItemsPerPage.value, () => {
  currentPage.value = 1; 
  allGroupedByUser();
});
watch(() => currentPage.value, () => {
});
const getPageClass = (pageNumber) => {
  return pageNumber === currentPage.value ? 'active-page' : '';
}; 
</script>
<style scoped>
.v-text-field {
  background-color: white;
}
.v-text-field:hover {
  background-color: white;
}
.select-width {
  max-width: 85px;
  }
  .active-page {
  background-color: green;
  }
</style>
