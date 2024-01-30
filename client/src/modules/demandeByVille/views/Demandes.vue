<template>
  <v-container class="my-1" grid-list-xl>
    <v-row class="mb-0 mx-auto pa-0"  align="center">
    <v-spacer></v-spacer>
    <v-col class="text-right" md="8" cols="auto">
      <v-chip  @click.prevent="redirectToVilles()" class="ma-1" variant="outlined" color="blue">Traitement des demandes par ville</v-chip>
      <v-chip  @click.prevent="redirectToCentres()" class="ma-1" variant="outlined" color="blue">Traitement des demandes par centre</v-chip>
      <!-- <v-chip  @click.prevent="redirectToAdmins()" class="ma-0" variant="outlined" color="blue"> Administrateurs</v-chip>
      <v-chip @click.prevent="redirectToUsers()" class="ma-0" variant="outlined" color="blue">Utilisateurs </v-chip> -->
    </v-col>
    </v-row>
  <vue-good-table
    :columns="columns"
    :rows="dataListe"
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
      <span v-if="props.column.field == 'ordreArrivee'">
        <v-chip color="pink"  size="small" variant="flat" >
          {{ props.row.ordreArrivee}}
      </v-chip>
      </span>
      <span v-if="props.column.field == 'rang'">
        <v-chip color="cyan"  size="small" variant="flat" >
          {{ props.row.rang}}
       </v-chip>
      </span>
      <div v-if="props.column.field === 'actions'">
        <div class="actions-wrapper"
         v-if="props.row.etatDemande === 'EN ATTENTE' &&
          props.row.quota === 'OUI' && 
          props.row.hasAcceptedDemande === 'NON'&&
          props.row.affectable === 'OUI'">
            <v-btn  variant="flat" color="teal" size="small" @click.prevent="redirectToDemandes(props.row.id)" class="">
              Affecté
            </v-btn>
        </div>
        <div v-else>
          <div class="actions-wrapper" v-if="props.row.affectable === 'NON'">
          <v-btn  variant="flat" color="red-darken-4" size="small">
              NON AFFECTABLE
            </v-btn>
        </div>
        <div class="actions-wrapper" v-else-if="props.row.hasAcceptedDemande === 'OUI'">
          <v-btn   variant="flat" color="green" size="small">
              dèja Affecté
            </v-btn>
        </div>
        <div class="actions-wrapper" v-else>
          <v-btn  variant="flat" color="red-accent-4" size="small">
             quota atteint
            </v-btn>
          </div>
        </div> 
      </div>
    </template>
  </vue-good-table>
  </v-container>
</template>
<script setup>
import { storeToRefs } from "pinia";
import { useDemandeByVilleStore } from "../store";
import { onMounted, reactive, ref,computed } from "vue"
import { useNotificationStore } from "@/store/notification";
import { useCentreStore } from "@/modules/centre/store";
import { useI18n } from "vue-i18n";
import { watchEffect,watch } from "vue";
import { useRouter,useRoute } from "vue-router";


const router = useRouter();
const route = useRoute();
const centreStore=useCentreStore();
const { dataListeByVille} = storeToRefs(centreStore);

const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const demandeByVilleStore = useDemandeByVilleStore();
const {columns,loading,etatCouleurs,dataListe } = storeToRefs(demandeByVilleStore);
const { demandeByVille } = demandeByVilleStore;

const dialog = ref(false);
onMounted(()=>{
const villeId=route.params.id;
demandeByVille(villeId)
console.log(dataListe) // ajustez le nombre d'éléments par page selon vos besoins
});
// const currentPage = ref(1);
// const onItemsPerPageChange = () => {
// currentPage.value = 1; // Réinitialisez la page actuelle lorsque le nombre d'éléments par page change
// };

// const itemsPerPageOptions = ref([1,2,3,5, 10, 20, 30]); // Ajoutez d'autres valeurs au besoin
// const selectedItemsPerPage = ref(5);
// const paginatedData = computed(() => {
// const start = (currentPage.value - 1) * selectedItemsPerPage.value;
// const end = start + selectedItemsPerPage.value;
// const dataList = dataListeGroupedByUser.value || [];
// return dataList.slice(start, end);
// });
// const totalPages = computed(() => Math.ceil(
// (dataListeGroupedByUser.value || []).length / selectedItemsPerPage.value));
// watch(() => selectedItemsPerPage.value, () => {
// currentPage.value = 1; 
// allGroupedByUser();
// });
// watch(() => currentPage.value, () => {
// });
// const getPageClass = (pageNumber) => {
// return pageNumber === currentPage.value ? 'active-page' : '';
// }; 

const redirectToVilles = () => {
router.push({ name: 'demandeByVille-liste' });
};
const redirectToCentres = () => {
router.push({ name: 'demandeByCentre-liste' });
};
const redirectToDemandes = (id) => {
  router.push({ name: 'accepte-DemandeByVille', params: { id } });
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
