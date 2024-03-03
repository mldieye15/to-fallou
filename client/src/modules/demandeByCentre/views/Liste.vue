<template>
  <div>
    <p class="text-h6">{{ $t('apps.forms.centre.centre') }}</p>
    
    <v-container class="my-5">
      <v-col class="text-right" md="8" cols="auto">
      <v-btn  @click.prevent="redirectToRecap()" class="ma-1" variant="flat" color="cyan-darken-1">Récapitulatif</v-btn>
      <v-btn  @click.prevent="redirectToCentres()" class="ma-1" variant="flat" color="cyan-darken-1">Planification des centres secondaires</v-btn>
      <!-- <v-chip  @click.prevent="redirectToAdmins()" class="ma-0" variant="outlined" color="blue"> Administrateurs</v-chip>
      <v-chip @click.prevent="redirectToUsers()" class="ma-0" variant="outlined" color="blue">Utilisateurs </v-chip> -->
    </v-col>
      <vue-good-table
        :columns="columns"
        :rows="dataListeCentre"
        :pagination-options="{
          enabled: true,
          mode: 'pages',
          perPageDropdown: [10, 15,20, 30, 40, 50]
          }"
         :search-options="{
            enabled: true
          }" 
           
        >    
        <template #table-row="props">
          <div v-if="props.column.field === 'totalDemandes'">
            <div v-if="props.row.planification==='OUI'">
              <v-icon class="mr-1" color="teal-darken-4" style="font-size: 25px;">mdi-checkbox-marked-circle-outline</v-icon> Terminée
            </div>
            <div v-else>
              <v-btn  variant="flat" color="light-blue-darken-4" size="small" @click.prevent="redirectToDemandes(props.row.id)" class="">
              Planifié
              <div class="ml-3">
                <p color="light-blue-darken-4"  size="small" variant="outlined" >
              ( {{ props.row.totalDemandes}})
              </p>
              </div>
            </v-btn>
            </div>
          </div> 
          <div v-if="props.column.field === 'nombreJury'">
              <v-chip color="light-blue-darken-4"  size="small" variant="outlined" >
              {{ props.row.nombreJury}}
            </v-chip>
          </div>
        </template>
      </vue-good-table>
    </v-container>
    
  </div>
  <div class="d-flex justify-end">
  <v-btn class="mt-16 mb-8 mr-2" color="blue" @click.prevent="redirectToListe()"><v-icon dark left> mdi-arrow-left </v-icon>{{ $t('apps.forms.retour') }}</v-btn>
 </div> 
</template>

<script setup>
import { storeToRefs } from "pinia";
import { onMounted, reactive, ref } from "vue"
import { useNotificationStore } from "@/store/notification";
import { useI18n } from "vue-i18n";
import { useCentreStore } from "@/modules/centre/store";
import { useRouter } from "vue-router";


const router = useRouter();

const i18n = useI18n();
const redirectToListe = () => {
    router.push({ name: 'demande-liste'});
  };

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const centreStore = useCentreStore();
const { dataListeCentre, headerTable, loading,columns } = storeToRefs(centreStore);
const { all, destroy } = centreStore;

const liste = reactive({ items: [] });
const headers = reactive({ items: [] });
const searchValue = ref("");
const dialog = ref(false);

onMounted(()=>{
  all();
});

const redirectToDemandes = (id) => {
  router.push({ name: 'demandeByCentre-demandes', params: { id } });
};
const redirectToRecap = () => {
  router.push({ name: 'recap-demandes'});
};
</script>
<style>
.v-text-field {
  background-color: white;
}
.v-text-field:hover {
  background-color: white;
}
.vgt-table td,
  .vgt-table th {
  font-size: 13px;
  width: auto;
  }

</style>
