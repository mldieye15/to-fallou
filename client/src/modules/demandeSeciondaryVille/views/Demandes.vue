<template>
  <v-container class="my-1" grid-list-xl>
    <p class="mt-1">Ville <v-chip class="ml-2" color="green" variant="flat" size="x-small" > {{ inputForm.libelleLong }}</v-chip></p>
    <p class="mt-1">Academie <v-chip class="ml-2" color="indigo" variant="flat" size="x-small"> {{ inputForm.academie }}</v-chip></p>
    <p class="mt-1">Nombre de demandes <v-chip class="ml-2" color="yellow-accent-4" variant="flat" size="x-small">{{ inputForm.totalDemandes }} </v-chip></p>
    <p class="mt-1">Quota  
        <template v-if="inputForm.quota === 0">
            <v-chip class="ml-2" color="red-darken-4" variant="flat" size="x-small">ATTEINT</v-chip>
        </template>
        <template v-else>
            <v-chip class="ml-2" color="light-blue-accent-4" variant="flat" size="x-small">{{ inputForm.quota }}</v-chip>
        </template>
    </p>
    <v-row class="mb-0 mx-auto pa-0"  align="center">
    <v-spacer></v-spacer>
    <v-col class="text-right" md="8" cols="auto">
      <v-btn  @click.prevent="redirectToVilles()" class="ma-1" variant="outlined" color="cyan-darken-1">Demandes par ville</v-btn>
      <v-btn  @click.prevent="redirectToCentres()" class="ma-1" variant="outlined" color="cyan-darken-1">Demandes par centre</v-btn>
      <!-- <v-chip  @click.prevent="redirectToAdmins()" class="ma-0" variant="outlined" color="blue"> Administrateurs</v-chip>
      <v-chip @click.prevent="redirectToUsers()" class="ma-0" variant="outlined" color="blue">Utilisateurs </v-chip> -->
    </v-col>
    </v-row>
  <vue-good-table
    :columns="columns"
    :rows="dataListe"
    :pagination-options="{
          enabled: true,
          mode: 'records',
          perPageDropdown: [5, 10, 15,20, 30, 40, 50]
          }"
         :search-options="{
            enabled: true
          }" 
    > 
    <template #table-row="props">
          <span v-if="props.column.field == 'affectable'">
            <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" 
                 :color="props.row.affectable=== 'OUI' ? 'green' : 'red'" text variant="tonal">
              {{ props.row.affectable}}
          </v-chip>
          </span>
          <span v-if="props.column.field == 'etatDemande'">
             <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" :color="etatCouleurs[props.row.etatDemande]" variant="tonal">
            <span>{{ props.row.etatDemande }}</span>
          </v-chip>
          </span>
          <span v-if="props.column.field == 'ordreArrivee'">
            <v-chip color="pink"  size="small" variant="flat" >
              {{ props.row.ordreArrivee}}
          </v-chip>
          </span>
          <span v-if="props.column.field == 'rang'">
            <v-chip color="cyan"  size="small" variant="flat" >
              {{ props.row.rang}}
           </v-chip>
          </span>
          <div v-if="props.column.field === 'actions'">
            <div class="actions-wrapper"
         v-if="props.row.etatDemande === 'en attente' &&
          props.row.quota === 'OUI' && 
          props.row.hasAcceptedDemande === 'NON'&&
          props.row.affectable === 'OUI'">
            <v-btn  variant="flat" color="teal" size="small" @click.prevent="redirectToDemandes(props.row.id)" class="">
              Affecté
            </v-btn>
         </div>
         <div class="actions-wrapper"
         v-else-if="props.row.etatDemande === 'déclinée' &&
          props.row.quota === 'OUI' && 
          props.row.hasAcceptedDemande === 'NON'&&
          props.row.affectable === 'OUI'">
            <v-btn  variant="flat" color="pink" size="small" @click.prevent="redirectToDemandes(props.row.id)" class="">
              réaffecté
            </v-btn>
         </div>

         <div class="actions-wrapper"
         v-else-if="props.row.etatDemande === 'obsolète'&&
          props.row.quota === 'OUI' && 
          props.row.hasAcceptedDemande === 'NON'&&
          props.row.affectable === 'OUI'">
            <v-btn  variant="flat" color="pink" size="small" @click.prevent="redirectToDemandes(props.row.id)" class="">
              Affecté
            </v-btn>
         </div>
          <div class="actions-wrapper"
         v-else-if="props.row.etatDemande === 'en attente' &&
          props.row.affectable === 'NON' ">
            <v-dialog transition="dialog-top-transition" width="50%" height="auto">
              <template v-slot:activator="{ props }">
                <v-btn variant="outlined" color="red" class="text" v-bind="props" size="small">
              <!-- <v-icon small flat color="red dark">mdi-delete</v-icon> -->
                  rejetée
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
                    <v-btn variant="outlined" color="black"  @click="rejete(props.row.id)">{{ $t('apps.forms.oui') }}</v-btn>
                  </v-card-actions>
                </v-card>
              </template>
            </v-dialog>
         </div>
        <div v-else>
        <div class="actions-wrapper" v-if="props.row.hasAcceptedDemande === 'OUI'">
          <v-chip  variant="flat" color="grey" size="small">
            DÉJÀ ACCEPTÉE
            </v-chip>
        </div>
        <div class="actions-wrapper" v-else-if="props.row.etatDemande === 'validée'">
          <v-chip  variant="flat" color="green-darken-4" size="small">
            DÉJÀ AFFECTÉ
            </v-chip>
        </div>
        <div class="actions-wrapper" v-else-if="props.row.etatDemande === 'rejetée'">
          <v-chip  variant="flat" color="red-darken-3" size="small">
            DÉJÀ REJETÉ
            </v-chip>
        </div>
        <div class="actions-wrapper" v-else>
          <v-chip  variant="flat" color="red" size="small">
             QUOTA ATTEINT
            </v-chip>
        </div>
        </div>
      </div>
        </template>
  </vue-good-table>
  </v-container>
  <div class="d-flex justify-end">
  <v-btn class="mt-16 mb-8 mr-2" color="blue" @click.prevent="redirectToListe()"><v-icon dark left> mdi-arrow-left </v-icon>{{ $t('apps.forms.retour') }}</v-btn>
</div>
</template>
<script setup>
import { storeToRefs } from "pinia";
import { useDemandeSecondaryVilleStore } from "../store";
import { onMounted, reactive, ref,computed } from "vue"
import { useNotificationStore } from "@/store/notification";
import { useCentreStore } from "@/modules/centre/store";
import { useVilleStore } from "@/modules/ville/store";
import { useI18n } from "vue-i18n";
import { watchEffect,watch } from "vue";
import { useRouter,useRoute } from "vue-router";
import { useToast } from 'vue-toastification';

const toast= useToast();


const router = useRouter();
const route = useRoute();
const redirectToListe = () => {
    router.push({ name: 'demandeByVille-liste'});
  };
const centreStore=useCentreStore();
const { dataListeByVille} = storeToRefs(centreStore);

const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;
const villeStore=useVilleStore();
const demandeByVilleStore = useDemandeSecondaryVilleStore();
const {columns,loading,etatCouleurs,dataListe } = storeToRefs(demandeByVilleStore);
const { demandeByVille,rejeter } = demandeByVilleStore;
const {one,oneVille}=villeStore;
const { dataDetails } = storeToRefs(villeStore);
const inputForm = reactive({
  libelleLong:'',
  libelleCourt: '',
  academie:null,
  totalJury:null,
  totalDemandes:null,
  rapport:null,
  quota:null,
});

const dialog = ref(false);
onMounted(()=>{
const villeId=route.params.id;
demandeByVille(villeId)
oneVille(villeId ).then( () => {
    inputForm.libelleLong = dataDetails.value.libelleLong,
    inputForm.libelleCourt = dataDetails.value.libelleCourt,
    inputForm.academie=dataDetails.value.academie.libelleLong,
    inputForm.totalJury=dataDetails.value.totalJury,
    inputForm.totalDemandes=dataDetails.value.totalDemandes,
    inputForm.rapport=dataDetails.value.rapportJuryDemande,
    inputForm.quota=dataDetails.value.quota
  });
console.log(dataListe) // ajustez le nombre d'éléments par page selon vos besoins
});
// const currentPage = ref(1);
// const onItemsPerPageChange = () => {
// currentPage.value = 1; // Réinitialisez la page actuelle lorsque le nombre d'éléments par page change
// };

// const itemsPerPageOptions = ref([1,2,3,5, 10, 20, 30]); // Ajoutez d'autres valeurs au besoin
// const selectedItemsPerPage = ref(5);
// const paginatedData = computed(() => {
// const start = (currentPage.value - 1) * selectedItemsPerPage.value;
// const end = start + selectedItemsPerPage.value;
// const dataList = dataListeGroupedByUser.value || [];
// return dataList.slice(start, end);
// });
// const totalPages = computed(() => Math.ceil(
// (dataListeGroupedByUser.value || []).length / selectedItemsPerPage.value));
// watch(() => selectedItemsPerPage.value, () => {
// currentPage.value = 1; 
// allGroupedByUser();
// });
// watch(() => currentPage.value, () => {
// });
// const getPageClass = (pageNumber) => {
// return pageNumber === currentPage.value ? 'active-page' : '';
// }; 

const redirectToVilles = () => {
router.push({ name: 'demandeByVille-liste' });
};
const redirectToCentres = () => {
router.push({ name: 'demandeByCentre-liste' });
};
const redirectToDemandes = (id) => {
  router.push({ name: 'accepte-DemandeSecondaryVille', params: { id } });
};
const rejete = (id) => {
  rejeter(id).then( () => {
    // addNotification({
    //     show: true,
    //     text:  i18n.t('deleted'),
    //     color: 'blue'
    //   });
    toast.success(i18n.t('rejeter'));
      dialog.value=false;
      const villeId=route.params.id;
      demandeByVille(villeId)
  });
  }
</script>
<style>
.v-text-field {
background-color: white;
}
.v-text-field:hover {
background-color: white;
}
.select-width {
max-width: 85px;
}
.active-page {
background-color: green;
}
.vgt-table td,
  .vgt-table th {
  font-size: 14px;
  width: auto;
  }
</style>
