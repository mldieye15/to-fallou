<template>
  <div>
    <p class="text-h6">{{ $t('apps.forms.demande.demande') }}</p>
    
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
        <v-spacer></v-spacer>
        <v-col cols="auto">
          <v-btn variant="outlined" color="black" >
            <router-link :to="{ name: 'demande-add' }" class="">
              {{ $t('apps.forms.ajouter') }}
            </router-link>
          </v-btn>
        </v-col>
      </v-row>
      <EasyDataTable
        :headers="headerTable"
        :items="dataListe"
        :loading="loading"
        buttons-pagination
        :search-value="searchValue"
      >
     
        <template #item-etatDemande="item">
            <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" :color="etatCouleurs[item.etatDemande]" variant="tonal">
              <span >{{ item.etatDemande }}</span>
            </v-chip>
        </template>
        <template #item-actions="item">
          <div class="actions-wrapper" v-if="item.etatDemande==='EN ATTENTE' && item.quotaDemandeAccepte==='NON'">
            <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" color="green" variant="tonal">
              <router-link  :to="{ name: 'accepte-Demande', params: { id: item.id } }" > <v-icon small flat color="green">mdi-check</v-icon> Accepte</router-link>
            </v-chip>
          </div>
          <div v-if="item.etatDemande==='ACCEPTE'">
            <v-dialog transition="dialog-top-transition" width="50%" height="auto">
              <template v-slot:activator="{ props }">
                <v-chip :style="{ 'font-size': '15px', 'height': '25px' }" color="green" variant="tonal">
                  <v-btn variant="text"  class="text" v-bind="props">
                 valider
                </v-btn>
                </v-chip>
              </template>
              <template v-slot:default="{ isActive }">
                <v-card>
                  <v-toolbar color="primary" :title="$t('apps.forms.demande.demande')"></v-toolbar>
                  <v-card-text>
                    <div class="text-h6">{{ $t('apps.forms.delteMessage') }}</div>
                  </v-card-text>
                  <v-card-actions class="justify-end">
                    <v-btn variant="text" color="primary" @click="isActive.value = false">{{ $t('apps.forms.annuler') }}</v-btn>
                    <v-btn variant="outlined" color="black"  @click="valider(item.id)">{{ $t('apps.forms.oui') }}</v-btn>
                  </v-card-actions>
                </v-card>
              </template>
            </v-dialog>
          </div>
        </template>
        {{ console.log(dataListeGroupedByUser) }}
      </EasyDataTable>
    </v-container>
    
  </div>
</template>

<script setup>
import { storeToRefs } from "pinia";
import { useDemandeStore } from "../store";
import { onMounted, reactive, ref } from "vue"
import { useNotificationStore } from "@/store/notification";
import { useCentreStore } from "@/modules/centre/store";
import { useI18n } from "vue-i18n";
const centreStore=useCentreStore();
const { dataListeByVille} = storeToRefs(centreStore);

const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const demandeStore = useDemandeStore();
const { dataListeGroupedByUser, headerTable, loading,etatCouleurs,dataListe } = storeToRefs(demandeStore);
const { all, destroy,validerDemande,allGroupedByUser } = demandeStore;

const liste = reactive({ items: [] });
const headers = reactive({ items: [] });
const searchValue = ref("");
const dialog = ref(false);

onMounted(()=>{
  all();
});

const valider = (id) => {
  validerDemande(id).then( () => {
    addNotification({
        show: true,
        text:  i18n.t('valider'),
        color: 'blue'
      });
      dialog.value=false;
      all();
  });
}
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
</style>
