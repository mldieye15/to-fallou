<template>
  <div>
    <p class="text-h6">{{ $t('apps.forms.user.user') }}</p>
    
    <v-container class="my-5">
      <v-col md="12" cols="auto">
        <v-btn  @click.prevent="redirectToPlanificateurs()" class="ma-0" variant="outlined" color="cyan-darken-1">Planificateurs</v-btn>
          <v-btn @click.prevent="redirectToSupervisseurs()" class="ma-0" variant="outlined" color="cyan-darken-1">Supervisseurs </v-btn>
          <v-btn  @click.prevent="redirectToAdmins()" class="ma-0" variant="outlined" color="cyan-darken-1"> Administrateurs</v-btn>
          <v-btn @click.prevent="redirectToUsers()" class="ma-0" variant="outlined" color="cyan-darken-1">Utilisateurs </v-btn>
        </v-col>
      <vue-good-table
        :columns="columns"
        :rows="dataListeUtilisateur"
        :pagination-options="{
          enabled: true,
          mode: 'pages',
          perPageDropdown: [5, 10, 15,20, 30, 40, 50]
          }"
         :search-options="{
            enabled: true
          }" 
           
        > 
        <template >
          <div v-if="props.column.field === 'actions'">
            <div class="actions-wrapper">
              <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" color="green" variant="tonal">
                <router-link :to="{ name: 'accepte-Demande', params: { id: props.row.id } }">
                  <v-icon small flat color="green">mdi-check</v-icon> Accepte
                </router-link>
              </v-chip>
            </div>
          </div>
        </template>
        <template #table-row="props">
          <div v-if="props.column.field === 'actions'">
            <div class="actions-wrapper">
            <router-link :to="{ name: 'user-details', params: { id: props.row.id } }"> <v-icon small flat color="green dark">mdi-eye</v-icon> </router-link>
            <router-link :to="{ name: 'user-edit', params: { id: props.row.id } }" class="ml-4"> <v-icon small flat color="blue dark">mdi-pencil</v-icon> </router-link>
            <v-dialog  v-model="dialog" transition="dialog-top-transition" width="50%" height="auto">
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
                    <v-btn variant="outlined" color="black"  @click="del(props.row.id)">{{ $t('apps.forms.oui') }}</v-btn>
                  </v-card-actions>
                </v-card>
              </template>
            </v-dialog>
          </div>
          </div> 
        </template>
      </vue-good-table>
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
const { dataListeUtilisateur, columns, loading } = storeToRefs(userStore);
const { user, destroy } = userStore;

const liste = reactive({ items: [] });
const headers = reactive({ items: [] });
const searchValue = ref("");
const dialog = ref(false);

onMounted(()=>{
  user();
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
.etablissement-wrapper{
 width: 110px; 
}
</style>
