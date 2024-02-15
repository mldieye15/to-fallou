<template>
  <p class="text-h6">{{ $t('apps.forms.demande.demande') }}</p>
      <v-container class="my-1" grid-list-xl>
        <v-row class="mb-0 mx-auto pa-0"  align="center">
        <v-col cols="12" sm="4" md="3" >
          <v-text-field
            label="Underlined"
            placeholder="Placeholder"
            variant="underlined"
            append-inner-icon="mdi-magnify"
            v-model="searchValue"
          ></v-text-field>
        </v-col>
        <v-spacer></v-spacer>
        <v-col class="text-right" md="9" cols="auto">
          <v-btn  @click.prevent="redirectToVilles()" class="ma-0" variant="outlined" color="cyan-darken-1">Demandes par ville</v-btn>
          <v-btn @click.prevent="redirectToCentres()" class="ma-0" variant="outlined" color="cyan-darken-1">Demandes par centre </v-btn>
          <v-btn  @click.prevent="redirectToAllDemandes()" class="ma-0" variant="outlined" color="cyan-darken-1"> Demandes archivées</v-btn>
        </v-col>
        </v-row>
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
         v-if="props.row.etatDemande === 'en attente' &&
          props.row.quota === 'OUI' && 
          props.row.hasAcceptedDemande === 'NON'&&
          props.row.affectable === 'OUI'">
            <v-btn  variant="flat" color="teal" size="small" @click.prevent="redirectToDemandes(props.row.id)" class="">
              Affecté
            </v-btn>
         </div>
         <div class="actions-wrapper"
         v-else-if="props.row.etatDemande === 'déclinée' &&
          props.row.quota === 'OUI' && 
          props.row.hasAcceptedDemande === 'NON'&&
          props.row.affectable === 'OUI'">
            <v-btn  variant="flat" color="pink" size="small" @click.prevent="redirectToDemandes(props.row.id)" class="">
              réaffecté
            </v-btn>
         </div>
        <div v-else>
          <div class="actions-wrapper" v-if="props.row.affectable === 'NON'">
          <v-chip  variant="flat" color="red-darken-4" size="small">
              NON AFFECTABLE
            </v-chip>
        </div>
        <div class="actions-wrapper" v-else-if="props.row.hasAcceptedDemande === 'OUI'">
          <v-chip  variant="flat" color="grey" size="small">
            DÉJÀ ACCEPTÉE
            </v-chip>
        </div>
        <div class="actions-wrapper" v-else-if="props.row.etatDemande === 'validée' || props.row.etatDemande === 'rejetée'">
          <v-chip  variant="flat" color="green-darken-4" size="small">
            DÉJÀ AFFECTÉ
            </v-chip>
        </div>
        <div class="actions-wrapper" v-else>
          <v-chip  variant="flat" color="red" size="small">
             QUOTA ATTEINT
            </v-chip>
        </div>
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
              <v-chip color="teal" class="mt-2" size="small" variant="flat" :class="getPageClass(page)">
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
import { useSessionStore } from "@/modules/session/store";
import { onMounted, reactive, ref,computed } from "vue"
import { useNotificationStore } from "@/store/notification";
import { useCentreStore } from "@/modules/centre/store";
import { useI18n } from "vue-i18n";
import { watchEffect,watch } from "vue";
import { useRouter } from "vue-router";

const sessionStore= useSessionStore();
const router = useRouter();
const centreStore=useCentreStore();
const {dataListeSession}=storeToRefs(sessionStore);
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
  sessionStore.sessionsArchive();
  console.log(dataListeGroupedByUser) // ajustez le nombre d'éléments par page selon vos besoins
});
const currentPage = ref(1);
const onItemsPerPageChange = () => {
  currentPage.value = 1; // Réinitialisez la page actuelle lorsque le nombre d'éléments par page change
};

const itemsPerPageOptions = ref([1,2,3,5, 10, 20, 30]); // Ajoutez d'autres valeurs au besoin
const selectedItemsPerPage = ref(5);
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

const redirectToVilles = () => {
  router.push({ name: 'demandeByVille-liste' });
};
const redirectToCentres = () => {
  router.push({ name: 'demandeByCentre-liste' });
};
const redirectToDemandes = (id) => {
  router.push({ name: 'accepte-Demande', params: { id } });
};
const redirectToAllDemandes = () => {
  const defaultSessionId = dataListeSession.value[0].id;
  router.push({ name: 'demandeBySession-demandes',params: { id: defaultSessionId}});
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
