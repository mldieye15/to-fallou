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
          <v-btn @click.prevent="redirectToSupervisseurs()" class="ma-0" variant="outlined" color="cyan-darken-1">Superviseurs </v-btn>
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
        rows-per-page="20"
      >
      <!-- Template pour personnaliser le contenu de la colonne 'Etablissement de Provenance' -->
        <template #item-etablissement=" item">
          <!-- Utilisation  d'une classe spécifique pour appliquer des styles à cette colonne -->
          <div class="etablissement-wrapper">
            {{ item.etablissement }}
          </div>
        </template>
        <template #item-etat="item">
          <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" @click="showConfirmDialog(item)"
                 :color="item.etat === 'NON' ? 'green' : 'red'" text variant="tonal">
              {{ item.etat}}
          </v-chip>
      </template>
      <template #item-listeNoire="item">
          <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" @click="showConfirm(item)"
                 :color="item.listeNoire === 'NON' ? 'green' : 'red'" text variant="tonal">
              {{ item.listeNoire}}
          </v-chip>
      </template>
        <template #item-actions="item">
          <div class="actions-wrapper">
            <router-link :to="{ name: 'user-details', params: { id: item.id } }"> <v-icon small flat color="green dark">mdi-eye</v-icon> </router-link>
            <router-link :to="{ name: 'user-edit', params: { id: item.id } }" class="ml-4"> <v-icon small flat color="blue dark">mdi-pencil</v-icon> </router-link>
            <v-dialog  transition="dialog-top-transition" width="50%" height="auto">
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
                    <v-btn variant="outlined" color="black"  @click="del(item.id);isActive.value = false">{{ $t('apps.forms.oui') }}</v-btn>
                  </v-card-actions>
                </v-card>
              </template>
            </v-dialog>
          </div>

        </template>
      </EasyDataTable>
    </v-container>
    <v-dialog v-model="confirmDialog" max-width="500">
    <v-card>
      <v-card-title>{{ confirmDialogTitle }}</v-card-title>
      <v-card-text>{{ confirmDialogMessage }}</v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="confirmAction">Oui</v-btn>
        <v-btn color="error" text @click="cancelAction">Non</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  <v-dialog v-model="confirm" max-width="500">
    <v-card>
      <v-card-title>{{ confirmTitle }}</v-card-title>
      <v-card-text>{{ confirmMessage }}</v-card-text>
      <v-card-actions>
        <v-btn color="primary" @click="confirmAction2">Oui</v-btn>
        <v-btn color="error" text @click="cancel">Non</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  </div>
</template>

<script setup>
import { storeToRefs } from "pinia";
import { useUtilisateurStore } from "../store";
import { onMounted, reactive, ref } from "vue"
import { useNotificationStore } from "@/store/notification";
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";
import { useToast } from 'vue-toastification';


const toast= useToast();
const router = useRouter();
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const userStore = useUtilisateurStore();
const { dataListeUtilisateur, headerTable, loading } = storeToRefs(userStore);
const { all, destroy,user,bloquer,listeNoire } = userStore;

const liste = reactive({ items: [] });
const headers = reactive({ items: [] });
const searchValue = ref("");
const dialog = ref(false);

onMounted(()=>{
  user();
});
const confirm = ref(false);
const confirmTitle = ref('');
const confirmMessage = ref('');
const current = ref(null);

const showConfirm = (item) => {
  console.log("Valeur de item.affectable :", item.etat);
  confirmTitle.value = item.listeNoire === 'NON' ? "Confirmer l'ajout à la liste noire" :"Confirmer la supression à la liste noire";
  confirmMessage.value = item.listeNoire === 'NON' ? "Voulez-vous ajouter ce compte sur liste noire ?" :"Voulez-vous suprimer ce compte de la liste noire?";
  current.value = item.id;
  confirm.value = true;
};

const confirmAction2 = () => {
  if (current.value) {
    listeNoire(current.value);
    user();
    confirmDialog.value = false;
  }
  closeConfirm();
};
const cancel = () => {
  closeConfirm();
};
const closeConfirm = () => {
  confirm.value = false;
  confirmTitle.value = '';
  confirmMessage.value = '';
  current.value = null;
  };
const confirmDialog = ref(false);
const confirmDialogTitle = ref('');
const confirmDialogMessage = ref('');
const currentItem = ref(null);

const showConfirmDialog = (item) => {
  console.log("Valeur de item.affectable :", item.etat);
  confirmDialogTitle.value = item.etat === 'NON' ? "Confirmer l'ajout à la liste rouge" :"Confirmer la supression à la liste rouge";
  confirmDialogMessage.value = item.etat === 'NON' ? "Voulez-vous ajouter ce compte sur liste rouge ?" :"Voulez-vous suprimer ce compte de la liste rouge?";
  currentItem.value = item.id;
  confirmDialog.value = true;
};

const confirmAction = () => {
  if (currentItem.value) {
    bloquer(currentItem.value);
    user();
    confirmDialog.value = false;
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
  currentItem.value = null;
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
      user();
  });
}
const redirectToPlanificateurs = () => {
  router.push({ name: 'planif-liste' });
};
const redirectToSupervisseurs = () => {
  router.push({ name: 'sup-liste'});
};
const redirectToAdmins = () => {
  router.push({ name: 'admin-liste'});
};
const redirectToUsers = () => {
  router.push({ name: 'user-liste'});
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
