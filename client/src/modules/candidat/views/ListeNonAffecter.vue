<template>
  <div>
    <p class="text-h6">{{ $t('apps.forms.candidat.candidat') }}</p>

    <v-container class="my-5" grid-list-xl>
      <v-row class="mb-0 mx-auto pa-1"  align="center">
        <v-col cols="12" sm="6" md="5" >
          <v-text-field
            label="Underlined"
            placeholder="Placeholder"
            variant="underlined"
            append-inner-icon="mdi-magnify"
            v-model="searchValue"
          ></v-text-field>
        </v-col>
        <v-col class="text-right" cols="auto">
          <v-btn @click.prevent="redirectToCandidat()" class="ma-0"  color="blue">candidats session en cours </v-btn>
        </v-col>
        <v-col class="text-right"  cols="auto">
          <v-btn @click.prevent="redirectToArchives()" class="ma-0" variant="outlined" color="cyan-darken-1">candidats Archivés </v-btn>
        </v-col>
        <v-col cols="auto">
            <v-btn class="text-right" color="green">
                <download-excel
                class="btn"
                :data="dataListeUtilisateur"
                :fields="json_fields"
                worksheet="My Worksheet"
                type="xlsx"
                name="recap.xlsx"
                >
              Exporté
              <i class="mdi mdi-cloud-download"></i>
            </download-excel>
            </v-btn>
          </v-col>
      </v-row>
      <EasyDataTable
        :headers="headerTable"
        :items="dataListeUtilisateur"
        :loading="loading"
        buttons-pagination
        :search-value="searchValue"
        rows-per-page="10"
      >
      <!-- <template #item-affectable="item">
          <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" @click="showConfirmDialog(item)"
                 :color="item.affectable === 'OUI' ? 'green' : 'red'" text variant="tonal">
              {{ item.affectable}}
          </v-chip>
      </template> -->
      <!-- Template pour personnaliser le contenu de la colonne 'Etablissement de Provenance' -->
        <template #item-etablissement=" item">
          <!-- Utilisation  d'une classe spécifique pour appliquer des styles à cette colonne -->
          <div class="etablissement-wrapper">
            {{ item.etablissement }}
          </div>
        </template>
        <template #item-actions="item">
          <div v-if="role=='ROLE_SUPERVISSEUR'" class="actions-wrapper">
            <!-- <v-btn  variant="flat" color="yellow" size="small" @click.prevent="redirectToAppreciation(item.id)" class="ma-1">
              Evaluation
            </v-btn> -->
            <v-btn  variant="flat" color="blue" size="small" @click.prevent="redirectToDetails(item.id)" class="ma-1">
              Details
            </v-btn>
          </div>
          <div v-else>
            <!-- <v-btn  variant="flat" color="blue" size="small" @click.prevent="redirectToDetails(item.id)" class="ma-1">
              Details
            </v-btn> -->
            <router-link :to="{ name: 'user-details', params: { id: item.id } }"> <v-icon small flat color="blue dark">mdi-eye</v-icon> </router-link>
            <v-dialog  transition="dialog-top-transition" width="50%" height="auto">
              <template v-slot:activator="{ props }">
                <!-- <v-btn  variant="text"  class="text" v-bind="props">
                  <v-icon small flat color="red dark">mdi-delete</v-icon>
              </v-btn> -->
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

  </div>
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
</template>

<script setup>
import { storeToRefs } from "pinia";
import { useUtilisateurStore } from "@/modules/user/store";
import { onMounted, reactive, ref } from "vue"
import { useNotificationStore } from "@/store/notification";
import { useI18n } from "vue-i18n";
import { useRouter } from 'vue-router';
import { useToast } from 'vue-toastification';


const toast= useToast();
const router = useRouter();
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const userStore = useUtilisateurStore();
const { dataListeUtilisateur, headerTable, loading } = storeToRefs(userStore);
const { allNonAffecter, destroy,user,bloquer,listeNoire,autorisation } = userStore;

const liste = reactive({ items: [] });
const headers = reactive({ items: [] });
const searchValue = ref("");
const dialog = ref(false);
const redirectToDetails = (id) => {
  router.push({ name: 'user-details', params: { id } });
};
const redirectToAppreciation = (id) => {
  router.push({ name: 'candidat-appreciation', params: { id } });
};
const redirectToCandidat = () => {
  router.push({ name: 'candidat-liste'});
};

onMounted(()=>{
  allNonAffecter();
});
let role= localStorage.getItem('role');
const confirmDialog = ref(false);
const confirmDialogTitle = ref('');
const confirmDialogMessage = ref('');
const currentItem = ref(null);

const showConfirmDialog = (item) => {
  console.log("Valeur de item.affectable :", item.affectable);
  confirmDialogTitle.value = item.affectable === 'OUI' ? "Confirmer la désactivation de l'affectabilté" :"Confirmer l'activation de l'affectabilté";
  confirmDialogMessage.value = item.affectable === 'OUI' ? "Voulez-vous désactiver  l'affectabilté ?" :"Voulez-vous activer l'affectabilté ?";
  currentItem.value = item.id;
  confirmDialog.value = true;
};

const confirmAction = () => {
  if (currentItem.value) {
    affectable(currentItem.value);
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
      allBySession();
  });
}
let json_fields = {
  "Code": "code" || "", // Si code est null, utilisez une chaîne vide
  "Nom": "nom" || "",
  "Prenoms": "prenoms" || "",
  "Matricule": "matricule" || "",
  "Email": "email" || "",
  "Sexe": "sexe" || "",
  "Telephone": "telephone" || "",
  "Etablissement de provenance": "etablissement" || "",
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
  width: 220px;
}
.etablissement-wrapper{
 width: 240px;
}
</style>
