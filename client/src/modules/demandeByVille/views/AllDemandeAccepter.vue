<template>
  <div>
    <p class="text-h6">Demandes déjà acceptées</p>
    <div v-if="selectedItems.length > 0" class="mt-2">
      <VBtn @click="showConfirmDialog"><v-icon class="mr-1">mdi-cursor-move</v-icon>Accepter les demandes selectionnées</VBtn>
    </div>

    <v-container class="my-5" grid-list-xl>
      <v-row class="mb-0 mx-auto pa-1"  align="center">
        <VCol
          cols="12"
          offset-md="8"
          md="4"
        >
          <v-text-field
            v-model="search"
            placeholder="Search ..."
            append-inner-icon="bx-search"
            single-line
            hide-details
            dense
            variant="outlined"
            density="compact"
          >
        </v-text-field>
        </VCol>
        <v-spacer></v-spacer>
        <v-col cols="auto">
          <v-btn class="text-right" color="green">
              <download-excel
              class="btn"
              :data="dataListe"
              :fields="json_fields"
              worksheet="My Worksheet"
              type="xlsx"
              name="recapPresident.xlsx"
              >
            Exporté
            <i class="mdi mdi-cloud-download"></i>
          </download-excel>
          </v-btn>
        </v-col>
      </v-row>
      <VDataTable
      :headers="headerTable"
      :items="dataListe"
      :search="search"
       item-value="id"
       density="compact"
      :items-per-page="50"
       class="text-no-wrap"
       v-model="selectedItems"
       :loading="loading"
       :sort-by="[{ key: 'ville', order: 'asc' },{ key: 'nom', order: 'asc' }, { key: 'prenoms', order: 'asc' }]"
       multi-sort
    >
  </VDataTable>
    </v-container>
  </div>
  <VDialog
    v-model="confirmDialog"
    max-width="700px"
  >
    <VCard title="Voulez vous accepter les demandes selectionnées?">
      <VCardText>
        <div class="d-flex justify-center gap-4">
          <VBtn
            class="ma-1"
            color="error"
            variant="outlined"
            @click="cancelAction"
          >
            Cancel
          </VBtn>
          <VBtn
            color="success"
            variant="elevated"
            class="ma-1"
            @click="confirmAction"
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
import { useDemandeByVilleStore } from "../store";
import { onMounted, reactive, ref } from "vue"
import { useI18n } from "vue-i18n";
import  { useVilleStore } from "../../ville/store"
import { useToast } from 'vue-toastification';


const toast= useToast();

const i18n = useI18n();


const demandeStore = useDemandeByVilleStore (); //;
const { dataListe, headerTable, loading } = storeToRefs(demandeStore);
const { allDemandeAccepter, destroy,accepterAll } = demandeStore;
let json_fields = {
"Centre": "centre",
"Prenom": "prenoms",
"Nom": "nom",
"Etablissement de provenance": "etablissement",
"Telephone": "telephone",
"Matricule": "matricule",
"Code": "code",
"Ville": "ville",
};
const search = ref("");
const selectedItems = ref([]);
const confirmDialog = ref(false);
const showConfirmDialog = () => {
  confirmDialog.value = true;
};
const cancelAction = () => {
  closeConfirmDialog();
};
const closeConfirmDialog = () => {
  confirmDialog.value = false;
  };
  const confirmAction = async () => {
  try {
    console.log('Selected Rows:', selectedItems.value);

    await accepterAll(selectedItems.value); // Envoyer uniquement les IDs
    toast.success("Demandes acceptées avec succès !");
    selectedItems.value = [];
  } catch (error) {
    alert('Une erreur est survenue : ' + error.message);
  } finally {
    confirmDialog.value = false;
  }
};
onMounted(()=>{
  allDemandeAccepter();
});
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
.v-dialog__content {
  position: fixed; /* Ou relative ou absolue, selon le contexte */
  top: 50%; /* Centrez verticalement */
  left: 50%; /* Centrez horizontalement */
  transform: translate(-50%, -50%);
  z-index: 9999; /* Assurez-vous que le modal s'affiche au-dessus du reste du contenu */
}
</style>
