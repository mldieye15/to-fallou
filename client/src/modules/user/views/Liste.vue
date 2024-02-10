<template>
  <div>
    <p class="text-h6">{{ $t('apps.forms.user.user') }}</p>
    
    <v-container class="my-5" grid-list-xl>
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
          <v-btn  @click.prevent="redirectToPlanificateurs()" class="ma-0" variant="outlined" color="cyan-darken-1">Planificateurs</v-btn>
          <v-btn @click.prevent="redirectToSupervisseurs()" class="ma-0" variant="outlined" color="cyan-darken-1">Supervisseurs </v-btn>
          <v-btn  @click.prevent="redirectToAdmins()" class="ma-0" variant="outlined" color="cyan-darken-1"> Administrateurs</v-btn>
          <v-btn @click.prevent="redirectToUsers()" class="ma-0" variant="outlined" color="cyan-darken-1">Utilisateurs </v-btn>
        </v-col>
        
      </v-row>
      <EasyDataTable
        :headers="headerTable"
        :items="dataListeUtilisateur"
        :loading="loading"
        buttons-pagination
        :search-value="searchValue"
        rows-per-page="5"
      >
      <!-- Template pour personnaliser le contenu de la colonne 'Etablissement de Provenance' -->
        <template #item-etablissement=" item">
          <!-- Utilisation  d'une classe spécifique pour appliquer des styles à cette colonne -->
          <div class="etablissement-wrapper">
            {{ item.etablissement }}
          </div>
        </template>
        <template #item-actions="item">
          <div class="actions-wrapper">
            <router-link :to="{ name: 'user-details', params: { id: item.id } }"> <v-icon small flat color="green dark">mdi-eye</v-icon> </router-link>
            <router-link :to="{ name: 'user-edit', params: { id: item.id } }" class="ml-4"> <v-icon small flat color="blue dark">mdi-pencil</v-icon> </router-link>
            <v-dialog transition="dialog-top-transition" width="50%" height="auto">
              <template v-slot:activator="{ props }">
                <v-btn variant="text"  class="text" v-bind="props">
                  <v-icon small flat color="red dark">mdi-delete</v-icon>
              </v-btn>
              </template>
              <template v-slot:default="{ isActive }">
                <v-card>
                  <v-toolbar color="primary" :title="$t('apps.forms.user.user')"></v-toolbar>
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
import { useUtilisateurStore } from "../store";
import { onMounted, reactive, ref } from "vue"
import { useNotificationStore } from "@/store/notification";
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";
const router = useRouter();
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const userStore = useUtilisateurStore();
const { dataListeUtilisateur, headerTable, loading } = storeToRefs(userStore);
const { all, destroy } = userStore;

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
const redirectToPlanificateurs = () => {
  router.push({ name: 'liste-planificateur' });
};
const redirectToSupervisseurs = () => {
  router.push({ name: 'liste-supervisseur'});
};
const redirectToAdmins = () => {
  router.push({ name: 'liste-admin'});
};
const redirectToUsers = () => {
  router.push({ name: 'liste-user'});
};
</script>
<style scoped>
.v-text-field {
  background-color: white;
}
.v-text-field:hover {
  background-color: white;
}
.actions-wrapper {
  width: 120px;
}
.etablissement-wrapper{
 width: 110px; 
}
</style>
