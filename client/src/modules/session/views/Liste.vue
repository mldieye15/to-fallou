<template>
  <div>
    <p class="text-h6">{{ $t('apps.forms.session.session') }}</p>
    
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
          <!-- <v-btn variant="outlined" color="black" >
            <router-link :to="{ name: 'session-add' }" class="">
              {{ $t('apps.forms.ajouter') }}
            </router-link>
          </v-btn> -->
          <v-btn @click.prevent="redirectToAdd()" class="ma-0" variant="outlined" color="cyan-darken-1"> {{ $t('apps.forms.ajouter') }} </v-btn>
        </v-col>
      </v-row>
      <EasyDataTable
        :headers="headerTable"
        :items="dataListeSession"
        :loading="loading"
        buttons-pagination
        :search-value="searchValue"
        rows-per-page="5"
      >
      <template #item-ouvert="item">
          <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" @click="showConfirmDialog('toggleSessionState', item)" :color="item.ouvert === 'ouverte' ? 'green' : 'red'" text variant="flat" size="small">
              {{ item.ouvert}}
          </v-chip>
      </template>
      <template #item-candidature="item">
        <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" @click="showConfirmDialog('toggleCandidatureState', item)" :color="item.candidature === 'ouverte' ? 'green' : 'red'" text  variant="flat"  size="small">
        {{ item.candidature }}
      </v-chip>
      </template>
      <template #item-modification="item">
        <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" @click="showConfirmDialog('toggleModificationState', item)" :color="item.modification === 'ouverte' ? 'green' : 'red'" text  variant="flat"  size="small">
        {{ item.modification }}
      </v-chip>
      </template>
        <template #item-actions="item">
          <div class="actions-wrapper">
            <router-link :to="{ name: 'session-details', params: { id: item.id } }"> <v-icon small flat color="green dark">mdi-eye</v-icon> </router-link>
            <router-link :to="{ name: 'session-edit', params: { id: item.id } }" class="ml-4"> <v-icon small flat color="blue dark">mdi-pencil</v-icon> </router-link>
            <v-dialog   transition="dialog-top-transition" width="50%" height="auto">
              <template v-slot:activator="{ props }">
                <v-btn variant="text"  class="text" v-bind="props">
                  <v-icon small flat color="red dark">mdi-delete</v-icon>
              </v-btn>
              </template>
              <template v-slot:default="{ isActive }">
                <v-card>
                  <v-toolbar color="primary" :title="$t('apps.forms.session.session')"></v-toolbar>
                  <v-card-text>
                    
                    <div class="text-h6">{{ $t('apps.forms.delteMessage') }}</div>
                  </v-card-text>
                  <v-card-actions class="justify-end">
                    <v-btn variant="text" color="primary" @click="isActive.value = false">{{ $t('apps.forms.annuler') }}</v-btn>
                    <v-btn variant="outlined" color="black"  @click="del(item.id);isActive.value = false">{{ $t('apps.forms.oui') }}</v-btn>
                  </v-card-actions>
                </v-card>
              </template>
            </v-dialog>
          </div>
        </template>
      </EasyDataTable>
    </v-container>
    
  </div>
  <v-dialog v-model="confirmDialog" max-width="500">
    <v-card>
      <v-card-title>{{ confirmDialogTitle }}</v-card-title>
      <v-card-text>{{ confirmDialogMessage }}</v-card-text>
      <v-card-actions>
        <v-btn color="green" @click="confirmAction">Oui</v-btn>
        <v-btn color="error" text @click="cancelAction">Non</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
<script setup>
import { storeToRefs } from "pinia";
import { useSessionStore } from "../store";
import { onMounted, reactive, ref } from "vue"
import { useNotificationStore } from "@/store/notification";
import { useI18n } from "vue-i18n";
import { useToast } from 'vue-toastification';
import { useRouter } from "vue-router";
const router = useRouter();
const redirectToAdd= () => {
  router.push({ name: 'session-add'});
};

const toast= useToast();



const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const sessionStore = useSessionStore();
const { dataListeSession, headerTable, loading } = storeToRefs(sessionStore);
const { all, destroy } = sessionStore;

const liste = reactive({ items: [] });
const headers = reactive({ items: [] });
const searchValue = ref("");
const dialog = ref(false);

onMounted(()=>{
  all();
});
const toggleSessionState = (item) => {
  // Inverser l'état ouvert
  sessionStore.toggleSessionState(item.id);
  all();
};
const toggleCandidatureState = (item) => {
  // Inverser l'état candidature
  if (item.ouvert=== 'ouverte'){
    sessionStore.toggleCandidatureState(item.id);
    all();
  }else{
    addNotification({
      show: true,
      text: "Impossible d'ouvrir les candidatures, la session est fermée.",
      color: 'red'
    });
  }
  
};
const toggleModificationState = (item) => {
  // Inverser l'état candidature
  if (item.ouvert=== 'ouverte'){
    sessionStore.toggleModificationState(item.id);
    all();
  }else{
    addNotification({
      show: true,
      text: "Impossible d'ouvrir les candidatures, la session est fermée.",
      color: 'red'
    });
  }
  
};
const del = (id) => {
  destroy(id).then( () => {
    // addNotification({
    //     show: true,
    //     text:  i18n.t('deleted'),
    //     color: 'blue'
    //   });
    toast.success(i18n.t('deleted'));
      dialog.value=false;
      all();
  });
}
const confirmDialog = ref(false);
const confirmDialogTitle = ref('');
const confirmDialogMessage = ref('');
const confirmActionCallback = ref(null);

const showConfirmDialog = (action, item) => {
  switch(action) {
    case 'toggleSessionState':
      confirmDialogTitle.value = item.ouvert === 'ouverte' ? "Confirmer la fermeture de session" : "Confirmer l'ouverture de session";
      confirmDialogMessage.value = item.ouvert === 'ouverte' ? "Voulez-vous vraiment fermer cette session ?" : "Voulez-vous vraiment ouvrir cette session ?";
      confirmActionCallback.value = () => toggleSessionState(item);
      break;
    case 'toggleCandidatureState':
      confirmDialogTitle.value = item.candidature === 'ouverte' ? "Confirmer la fermeture de candidature" : "Confirmer l'ouverture de candidature";
      confirmDialogMessage.value = item.candidature === 'ouverte' ? "Voulez-vous vraiment fermer la candidature ?" : "Voulez-vous vraiment ouvrir la candidature ?";
      confirmActionCallback.value = () => toggleCandidatureState(item);
      break;
    case 'toggleModificationState':
    confirmDialogTitle.value = item.modification === 'ouverte' ? "Confirmer la fermeture les modifications" : "Confirmer l'ouverture des modifications";
      confirmDialogMessage.value = item.modification === 'ouverte' ? "Voulez-vous vraiment fermer les modifications ?" : "Voulez-vous vraiment ouvrir les modifications ?";
      confirmActionCallback.value = () => toggleModificationState(item);
      break;
    default:
      break;
  }
  confirmDialog.value = true;
};

const confirmAction = () => {
  if (confirmActionCallback.value) {
    confirmActionCallback.value();
    all();
  }
  closeConfirmDialog();
};

const cancelAction = () => {
  closeConfirmDialog();
};

const closeConfirmDialog = () => {
  confirmDialog.value = false;
  confirmDialogTitle.value = '';
  confirmDialogMessage.value = '';
  confirmActionCallback.value = null;
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
</style>
