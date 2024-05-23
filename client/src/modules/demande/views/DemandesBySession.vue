<template>
  <v-container class="my-1" grid-list-xl>
    <v-card class="custom-shadow mb-3">
          <v-row class="mb-0 mx-auto pa-0"  align="center">
        <v-col cols="12" sm="4" md="3" >
          <v-text-field
            v-model="searchValue"
            :loading="loading"
            append-inner-icon="mdi-magnify"
            placeholder="Entrez votre recherche"
            density="compact"
            variant="solo"
            hide-details
            single-line
            @input="onSearchInputChange"
          ></v-text-field>
        </v-col>
        <v-spacer></v-spacer>
        <v-col class="text-right" md="9" cols="auto">
          <v-tabs v-model="selectedTab" 
          bg-color="blue"
          next-icon="mdi-arrow-right-bold-box-outline"
          prev-icon="mdi-arrow-left-bold-box-outline"
          show-arrows
          >
          <v-tab v-for="(session, index) in dataListeSession" :key="session.id" @click="redirectToSessions(session.id, index)">
            {{ session.libelleLong }}
          </v-tab>
          </v-tabs>
        </v-col>
        </v-row>
        </v-card>
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
            <div class="actions-wrapper">
              <v-chip  size="x-small" variant="flat">archivée</v-chip>
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
import { onMounted, reactive, ref, computed } from "vue";
import { useNotificationStore } from "@/store/notification";
import { useCentreStore } from "@/modules/centre/store";
import { useI18n } from "vue-i18n";
import { watchEffect, watch } from "vue";
import { useRouter, useRoute } from "vue-router";

const route = useRoute();
const sessionStore = useSessionStore();
const router = useRouter();
const centreStore = useCentreStore();
const { dataListeSession } = storeToRefs(sessionStore);
const { dataListeByVille } = storeToRefs(centreStore);

const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const demandeStore = useDemandeStore();
const { dataListeGroupedByUser, columns, headerTable, loading, etatCouleurs, dataListe } = storeToRefs(demandeStore);
const { all, demandeBySession } = demandeStore;

const liste = reactive({ items: [] });
const headers = reactive({ items: [] });
const searchValue = ref("");
const dialog = ref(false);

onMounted(() => {
  const sessionId = route.params.id;
  demandeBySession(sessionId);
  sessionStore.sessionsArchive();
  console.log(dataListe); // ajustez le nombre d'éléments par page selon vos besoins
});

const currentPage = ref(1);

const onItemsPerPageChange = () => {
  currentPage.value = 1; // Réinitialisez la page actuelle lorsque le nombre d'éléments par page change
};

const itemsPerPageOptions = ref([1, 2, 3, 5, 10, 20, 30]); // Ajoutez d'autres valeurs au besoin
const selectedItemsPerPage = ref(5);

// const paginatedData = computed(() => {
//   const start = (currentPage.value - 1) * selectedItemsPerPage.value;
//   const end = start + selectedItemsPerPage.value;
//   const dataList = dataListeGroupedByUser.value || [];
//   return dataList.slice(start, end);
// });

const paginatedData = computed(() => {
  const start = (currentPage.value - 1) * selectedItemsPerPage.value;
  const end = start + selectedItemsPerPage.value;
  let filteredData = dataListeGroupedByUser.value || [];
  // Appliquer le filtre si une valeur de recherche est présente
  if (searchValue.value.trim() !== "") {
    const searchRegex = new RegExp(searchValue.value.trim(), "i"); // Expression régulière pour rechercher sans tenir compte de la casse
    filteredData = filteredData.filter(entry => {
      // Recherche dans toutes les propriétés de l'objet entry
      for (const prop in entry) {
        if (Object.prototype.hasOwnProperty.call(entry, prop)) {
          // Vérifie si la propriété est une chaîne et si elle contient la valeur de recherche
          if (typeof entry[prop] === "string" && searchRegex.test(entry[prop])) {
            return true; // Correspondance trouvée, inclure cet objet dans les résultats filtrés
          }
        }
      }
      return false; // Aucune correspondance trouvée pour cet objet
    });
  }
  return filteredData.slice(start, end);
});


const totalPages = computed(() => Math.ceil((dataListeGroupedByUser.value || []).length / selectedItemsPerPage.value));

watch(() => selectedItemsPerPage.value, () => {
  currentPage.value = 1;
  demandeBySession(route.params.id);
});

watch(() => currentPage.value, () => {});

const getPageClass = (pageNumber) => {
  return pageNumber === currentPage.value ? 'active-page' : '';
};
const onSearchInputChange = () => {
  filterData();
};
const selectedSessionId = ref(null);
const selectedTab = ref(0); // Nouvelle variable pour stocker l'index de l'onglet sélectionné

const redirectToSessions = (id, tabIndex) => {
  router.push({ name: 'demandeBySession-demandes', params: { id } });
  demandeBySession(id);
  selectedSessionId.value = id;
  selectedTab.value = tabIndex; // Mettre à jour l'index de l'onglet sélectionné
};
</script>

<style>
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
.vgt-table td,
.vgt-table th {
  font-size: 12px;
  padding: 4px;
  width: auto;
}
.custom-shadow {
  box-shadow: rgba(46, 130, 240, 0.4) 5px 5px, rgba(46, 101, 240, 0.3) 10px 10px, rgba(46, 124, 240, 0.2) 15px 15px, rgba(46, 137, 240, 0.1) 20px 20px, rgba(46, 88, 240, 0.05) 25px 25px;
}
</style>
