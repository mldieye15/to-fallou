<template>
  <div>
    <p class="text-h6">{{ $t('apps.forms.academie.academie') }}</p>
    
    <v-container class="my-5 container" grid-list-xl>
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
            <router-link :to="{ name: 'academie-add' }" class="">
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
        class="custom-datatable"
      >
        <template #item-actions="item">
          <div class="actions-wrapper">
            <router-link :to="{ name: 'academie-details', params: { id: item.id } }"> <v-icon small flat color="green dark">mdi-eye</v-icon> </router-link>
            <router-link :to="{ name: 'academie-edit', params: { id: item.id } }" class="ml-4"> <v-icon small flat color="blue dark">mdi-pencil</v-icon> </router-link>
            <v-dialog  v-model="dialog" transition="dialog-top-transition" width="50%" height="auto">
              <template v-slot:activator="{ props }">
                <v-btn variant="text"  class="text" v-bind="props">
                  <v-icon small flat color="red dark">mdi-delete</v-icon>
              </v-btn>
              </template>
              <template v-slot:default="{ isActive }">
                <v-card>
                  <v-toolbar color="primary" :title="$t('apps.forms.academie.academie')"></v-toolbar>
                  <v-card-text>
                    
                    <div class="text-h6">{{ $t('apps.forms.delteMessage') }}</div>
                  </v-card-text>
                  <v-card-actions class="justify-end">
                    <v-btn variant="text" color="primary" @click="isActive.value = false">{{ $t('apps.forms.annuler') }}</v-btn>
                    <v-btn variant="outlined" color="black"  @click="del(item.id)">{{ $t('apps.forms.oui') }}</v-btn>
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
import { useAcademieStore } from "../store";
import { onMounted, reactive, ref } from "vue"
import { useNotificationStore } from "@/store/notification";
import { useI18n } from "vue-i18n";

const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const academieStore = useAcademieStore();
const { dataListe, headerTable, loading } = storeToRefs(academieStore);
const { all, destroy } = academieStore;

const liste = reactive({ items: [] });
const headers = reactive({ items: [] });
const searchValue = ref("");
const dialog = ref(false);

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
.container {
  width: 50%; /* Ajustez la largeur en pourcentage ou en pixels selon vos besoins */
}
</style>
