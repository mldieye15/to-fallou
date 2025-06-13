<template>
  <p class="text-h6">{{ $t('apps.forms.demande.demande') }}</p>
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
          <v-tabs bg-color="blue">
            <v-tab @click="redirectToVilles">Villes à Planifier</v-tab>
            <v-tab @click="redirectToVillesDejaProposer">Villes dèjà Planifiées</v-tab>
            <v-tab @click="redirectToAllDemandeProposer">Demandes dèjà proposées</v-tab>
            <v-tab @click="redirectToAllDemandeAccepter">Demandes dèjà acceptées</v-tab>
            <v-tab @click="redirectToCentres">Demandes par centre</v-tab>
            <v-tab @click="redirectToAllDemandes">Demandes archivées</v-tab>
          </v-tabs>
        </v-col>
        </v-row>
        </v-card>

        <div v-if="loading">Chargement en cours...</div>
        <div v-for="userEntry in paginatedData" :key="userEntry.userId">
      <div class="mb-2 mt-2">
        <v-row>
          <v-col cols="6" class="pr-0">
            <h2>{{ userEntry.user }}:{{ userEntry.code }}</h2>
          </v-col>
          <v-col cols="6" class="pl-0">

          </v-col>
        </v-row>
</div>
      <vue-good-table
        :columns="columns"
        :rows="userEntry.demandes"
        :resizable="true"
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
        <router-link :to="{ name: 'demande-edit', params: { id: props.row.id } }" class="ml-4"> <v-icon small flat color="blue dark">mdi-pencil</v-icon> </router-link>
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
         <div class="actions-wrapper"
         v-else-if="props.row.etatDemande === 'en attente' &&
          props.row.affectable === 'NON' ">
          <v-dialog transition="dialog-top-transition" width="50%" height="auto">
              <template v-slot:activator="{ props }">
                <v-btn variant="outlined" color="red" class="text" v-bind="props" size="small">
              <!-- <v-icon small flat color="red dark">mdi-delete</v-icon> -->
                  rejetée
              </v-btn>
              </template>
              <template v-slot:default="{ isActive }">
                <v-card>
                  <v-toolbar color="primary" :title="$t('apps.forms.demande.demande')"></v-toolbar>
                  <v-card-text>

                    <div class="text-h6">{{ $t('apps.forms.annulerMessage') }}</div>
                  </v-card-text>
                  <v-card-actions class="justify-end">
                    <v-btn variant="text" color="primary" @click="isActive.value = false">{{ $t('apps.forms.annuler') }}</v-btn>
                    <v-btn variant="outlined" color="black"  @click="rejete(props.row.id)">{{ $t('apps.forms.oui') }}</v-btn>
                  </v-card-actions>
                </v-card>
              </template>
            </v-dialog>
         </div>
        <div v-else>
        <div class="actions-wrapper" v-if="props.row.hasAcceptedDemande === 'OUI'">
          <v-chip  variant="flat" color="grey" size="small">
            DÉJÀ ACCEPTÉE
            </v-chip>
        </div>
        <div class="actions-wrapper" v-else-if="props.row.etatDemande === 'validée'">
          <v-chip  variant="flat" color="green-darken-4" size="small">
            DÉJÀ AFFECTÉ
            </v-chip>
        </div>
        <div class="actions-wrapper" v-else-if="props.row.etatDemande === 'rejetée'">
          <v-chip  variant="flat" color="red-darken-3" size="small">
            DÉJÀ REJETÉ
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
import { useToast } from 'vue-toastification';

const toast= useToast();

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
const { all, destroy,validerDemande,allGroupedByUser,rejeter } = demandeStore;

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
const redirectToAllDemandeProposer = () => {
  router.push({ name: 'allDemandeProposer' });
};
const redirectToAllDemandeAccepter = () => {
  router.push({ name: 'allDemandeAccepter' });
};
const redirectToVillesDejaProposer = () => {
  router.push({ name: 'demandeByVille-liste-proposer' });
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
  console.log(dataListeSession.value[0].id);
};
const rejete = (id) => {
  rejeter(id).then( () => {
    // addNotification({
    //     show: true,
    //     text:  i18n.t('deleted'),
    //     color: 'blue'
    //   });
    toast.success(i18n.t('rejeter'));
      dialog.value=false;
      allGroupedByUser();
  });
  }
const onSearchInputChange = () => {
  filterData();
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

