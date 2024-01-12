<template>
  <div>
    <p class="text-h6">{{ $t('apps.forms.candidat.candidat') }}</p>
    
    <v-container class="my-5" grid-list-xl>
      <v-row class="mb-0 mx-auto pa-1"  align="center">
        <v-col cols="12" sm="6" md="4" >
          <v-text-field
            label="Underlined"
            placeholder="Placeholder"
            variant="underlined"
            append-inner-icon="mdi-magnify"
            v-model="searchValue"
          ></v-text-field>
        </v-col>
      </v-row>
      <EasyDataTable
        :headers="headerTable"
        :items="dataListeCandidat"
        :loading="loading"
        buttons-pagination
        :search-value="searchValue"
        rows-per-page="5"
      >affectableLabel<template #item-affectable="item">
          <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" 
                 :color="item.affectable === 'OUI' ? 'green' : 'red'" text variant="tonal">
              {{ item.affectable}}
          </v-chip>
      </template>
      <!-- Template pour personnaliser le contenu de la colonne 'Etablissement de Provenance' -->
        <template #item-etablissement=" item">
          <!-- Utilisation  d'une classe spécifique pour appliquer des styles à cette colonne -->
          <div class="etablissement-wrapper">
            {{ item.etablissement }}
          </div>
        </template>
        <template #item-actions="item">
          <div class="actions-wrapper">
            <v-btn  size="small" @click.prevent="redirectToDetails(item.id)">
              Details
            </v-btn>
            <v-btn size="small" @click.prevent="redirectToAppreciation(item.id)" class="ml-4">
              Appreciation
            </v-btn>
          </div>
          
        </template>
      </EasyDataTable>
    </v-container>
    
  </div>
</template>

<script setup>
import { storeToRefs } from "pinia";
import { useCandidatStore } from "../store";
import { onMounted, reactive, ref } from "vue"
import { useNotificationStore } from "@/store/notification";
import { useI18n } from "vue-i18n";
import { useRouter } from 'vue-router';
const router = useRouter();
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const candidatStore = useCandidatStore();
const { dataListeCandidat, headerTable, loading } = storeToRefs(candidatStore);
const { all, destroy } = candidatStore;

const liste = reactive({ items: [] });
const headers = reactive({ items: [] });
const searchValue = ref("");
const dialog = ref(false);
const redirectToDetails = (id) => {
  router.push({ name: 'candidat-details', params: { id } });
};
const redirectToAppreciation = (id) => {
  router.push({ name: 'candidat-appreciation', params: { id } });
};

onMounted(()=>{
  all();
});

const del = (id) => {
  destroy(id).then( () => {
    addNotification({
        show: true,
        text:  i18n.t('deleted'),
        color: 'blue'
      });
      dialog.value=false;
      all();
  });
}
</script>
<style scoped>
.v-text-field {
  background-color: white;
}
.v-text-field:hover {
  background-color: white;
}
.actions-wrapper {
  width: 300px;
}
.etablissement-wrapper{
 width: 110px; 
}
</style>
