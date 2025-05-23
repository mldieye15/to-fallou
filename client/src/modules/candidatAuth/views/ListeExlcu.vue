<template>
  <div>
    <p class="text-h6">Liste des candidats non authorisés</p>

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
          <v-btn
            v-if="disable"
            color="red"
            @click="showDesactiveDialog"
            :loading="loading"
          >
            Désactiver les candidats non autorisés
          </v-btn>
           <p v-else>Aucun candidat non autorisé activé.</p>
        </v-col>
      </v-row>
      <EasyDataTable
        :headers="headerTable1"
        :items="dataListe"
        :loading="loading"
        buttons-pagination
        :search-value="searchValue"
        class="custom-datatable"
      >
      <template #item-etat="item">
          <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" @click="showConfirmDialog(item)"
                 :color="item.etat === 'activé' ? 'green' : 'red'" text variant="tonal">
              {{ item.etat}}
          </v-chip>
      </template>
        <template #item-actions="item">
          <div class="actions-wrapper">
            <router-link :to="{ name: 'candidatAuthoriser-details', params: { id: item.id } }"> <v-icon small flat color="green dark">mdi-eye</v-icon> </router-link>
            <router-link :to="{ name: 'candidatAuthoriser-edit', params: { id: item.id } }" class="ml-4"> <v-icon small flat color="blue dark">mdi-pencil</v-icon> </router-link>
            <v-dialog  transition="dialog-top-transition" width="50%" height="auto">
              <!-- <template v-slot:activator="{ props }">
                <v-btn variant="text"  class="text" v-bind="props">
                  <v-icon small flat color="red dark">mdi-delete</v-icon>
              </v-btn>
              </template> -->
              <template v-slot:default="{ isActive }">
                <v-card>
                  <v-toolbar color="primary" :title="$t('apps.forms.candidatAuthoriser.candidatAuthoriser')"></v-toolbar>
                  <v-card-text>

                    <div class="text-h6">Etes vous sûre de vouloir suprimer le candidat</div>
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
  </div>
  <VDialog
    v-model="desactiveDialog"
    max-width="500px"
  >
    <VCard title="Etes vous sure de bien vouloir désactiver ces comptes?">
      <VCardText>
        <div class="d-flex justify-center gap-4">
          <VBtn
            color="error"
            variant="outlined"
            @click="closeDesactive"
          >
            Cancel
          </VBtn>
          <VBtn
            color="success"
            variant="elevated"
            @click="desactiveConfirm"
          >
            OK
          </VBtn>
        </div>
      </VCardText>
    </VCard>
  </VDialog>
</template>

<script setup>
import { storeToRefs } from "pinia";
import { useCandidatAuthoriserStore } from "../store";
import { onMounted, reactive, ref } from "vue"
import { useNotificationStore } from "@/store/notification";
import { useI18n } from "vue-i18n";
import { useToast } from 'vue-toastification';
import { useRouter } from "vue-router";

const router = useRouter();
const redirectToAdd= () => {
  router.push({ name: 'candidatAuthoriser-add'});
};


const toast= useToast();

const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const candidatAuthoriserStore = useCandidatAuthoriserStore();
const { dataListe, headerTable1, loading,disable } = storeToRefs(candidatAuthoriserStore);
const { allNotAutoriser, destroy,autorisation,hasEnabledNonAutoriserUsers,disableNonAutoriserUsers } = candidatAuthoriserStore;

const liste = reactive({ items: [] });
const headers = reactive({ items: [] });
const searchValue = ref("");
const dialog = ref(false);

onMounted(()=>{
  allNotAutoriser();
  hasEnabledNonAutoriserUsers()
});

const confirmDialog = ref(false);
const confirmDialogTitle = ref('');
const confirmDialogMessage = ref('');
const currentItem = ref(null);

const showConfirmDialog = (item) => {
  console.log("Valeur de item.affectable :", item.etat);
  confirmDialogTitle.value = item.etat === 'activé' ? "Confirmer la désactivation du compte" :"Confirmer l'activation du compte";
  confirmDialogMessage.value = item.etat === 'activé' ? "Voulez-vous désactiver  le compte ?" :"Voulez-vous activer le compte?";
  currentItem.value = item.id;
  confirmDialog.value = true;
};

const confirmAction = () => {
  if (currentItem.value) {
    autorisation(currentItem.value);
    confirmDialog.value = false;
    allNotAutoriser();
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
const desactiveDialog = ref(false)

const showDesactiveDialog = () => {
  desactiveDialog.value = true; // Ouvre la boîte de dialogue
};
const desactiveConfirm = async () => {
  try {
    await disableNonAutoriserUsers(); // Appel à la méthode de suppression
    desactiveDialog.value = false; // Ferme la boîte de dialogue
    toast.success("compte désactivés avec succès !", {
      position: toast.POSITION.TOP_CENTER,
    });
    allNotAutoriser();
  } catch (error) {
    toast.error("Erreur lors de la suppression : " + error.message, {
      position: toast.POSITION.TOP_CENTER,
    });
  }
};
const closeDesactive = () => {
  desactiveDialog.value = false
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
