<template>
  <div>
    <p class="text-h6">{{ $t('apps.forms.ville.ville') }}</p>
    
    <v-container class="my-5">
      <vue-good-table
        :columns="columns"
        :rows="dataListeVille"
        :pagination-options="{
          enabled: true,
          mode: 'records',
          perPageDropdown: [10, 15,20, 30, 40, 50]
          }"
         :search-options="{
            enabled: true
          }" 
           
        >    
        <template #table-row="props">
          <div v-if="props.column.field === 'totalDemandes'">
            <div v-if="props.row.quota===0">
              <v-icon class="mr-1" color="teal-darken-4" style="font-size: 25px;">mdi-checkbox-marked-circle-outline</v-icon> Terminée
              <router-link :to="{ name: 'demandeByVille-demandes', params: { id: props.row.id } }"> <v-icon class="ml-3" small flat color="green dark">mdi-eye</v-icon> </router-link>
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
          <div v-if="props.column.field === 'totalJury'">
              <v-chip color="light-blue-darken-4"  size="small" variant="flat" >
              {{ props.row.totalJury}}
            </v-chip>
          </div>
          <div v-if="props.column.field === 'rapport'">
            <div :class="{ 'square-chip': true, 'red-chip': props.row.rapport === 0, 'green-chip': props.row.rapport > 0 }">
                <span :class="{ 'red-text': props.row.rapport === 0, 'green-text': props.row.rapport > 0 }">{{ props.row.rapport }}</span>
            </div>

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
import { useVilleStore } from "@/modules/ville/store";
import { useRouter } from "vue-router";


const router = useRouter();

const i18n = useI18n();
const redirectToListe = () => {
    router.push({ name: 'demande-liste'});
  };

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const villeStore = useVilleStore();
const { dataListeVille, headerTable, loading,columns } = storeToRefs(villeStore);
const { all, destroy,allWithJury } = villeStore;

const liste = reactive({ items: [] });
const headers = reactive({ items: [] });
const searchValue = ref("");
const dialog = ref(false);

onMounted(()=>{
  allWithJury();
});

const redirectToDemandes = (id) => {
  router.push({ name: 'demandeByVille-demandes', params: { id } });
};
</script>
<style>
.v-text-field {
  background-color: white;
}
.v-text-field:hover {
  background-color: white;
}
.actions-wrapper {
  width: 120px;
}
.vgt-table td,
  .vgt-table th {
  font-size: 14px;
  width: auto;
  }
  .square-chip {
    display: inline-block;
    width: 30px;
    height: 24px;
    line-height: 24px;
    text-align: center;
    border-radius: 4px;
}

.red-chip {
    background-color: red;
}

.green-chip {
    background-color: #00C853;
}
.red-text {
    color: white; /* Couleur du texte pour le rapport 0 */
}
.green-text {
    color: black; /* Couleur du texte pour le rapport supérieur à 0 */
}

</style>
