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
      </v-row>
      <EasyDataTable
        :headers="headerTable"
        :items="dataListe"
        :loading="loading"
        buttons-pagination
        :search-value="searchValue"
      >
      <template #item-etatDemande="item">
          <v-chip :style="{ 'font-size': '15px', 'height': '20px' }"  :color="etatCouleurs[item.etatDemande]" text variant="flat" size="small">
              {{ item.etatDemande}}
          </v-chip>
      </template>
        <template #item-actions="item">
          <div class="actions-wrapper">
            <v-dialog transition="dialog-top-transition" width="50%" height="auto">
              <template v-slot:activator="{ props }">
                <v-btn variant="outlined" color="red" class="text" v-bind="props" size="small">
                  <!-- <v-icon small flat color="red dark">mdi-delete</v-icon> -->
                  annuler
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
                    <v-btn variant="outlined" color="black"  @click="reinitialiser(item.id)">{{ $t('apps.forms.oui') }}</v-btn>
                  </v-card-actions>
                </v-card>
              </template>
            </v-dialog>
          </div>
        </template>
      </EasyDataTable>
    </v-container>
    
  </div>
</template>

<script setup>
import { storeToRefs } from "pinia";
import { useCasStore } from "../store";
import { onMounted, reactive, ref } from "vue"
import { useI18n } from "vue-i18n";
import { useToast } from 'vue-toastification';

const toast= useToast();

const i18n = useI18n();
const casStore = useCasStore();
const { dataListe, headerTable, loading,etatCouleurs } = storeToRefs(casStore);
const { all, annuler } = casStore;

const liste = reactive({ items: [] });
const headers = reactive({ items: [] });
const searchValue = ref("");
const dialog = ref(false);

onMounted(()=>{
  all();
});

const reinitialiser = (id) => {
  annuler(id).then( () => {
    // addNotification({
    //     show: true,
    //     text:  i18n.t('deleted'),
    //     color: 'blue'
    //   });
    toast.success(i18n.t('rejeter'));
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
