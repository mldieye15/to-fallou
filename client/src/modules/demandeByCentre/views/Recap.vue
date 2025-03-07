<template>
    <div>
      <p class="text-h6">{{ $t('apps.forms.centre.centre') }}</p>
      
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
        <EasyDataTable
          :headers="headerTable"
          :items="dataListe"
          :loading="loading"
          alternating
          buttons-pagination
          :search-value="searchValue"
        >
        </EasyDataTable>
      </v-container> 
    </div>    
  </template>
  
  <script setup>
  import { storeToRefs } from "pinia";
  import { useDemandeByCentreStore } from "../store";
  import { onMounted, reactive, ref } from "vue"
  import { useI18n } from "vue-i18n";
  import  { useVilleStore } from "../../ville/store"
  import { useToast } from 'vue-toastification';
  
  
  const toast= useToast();
                           
  const i18n = useI18n();
  
  
  const centreStore = useDemandeByCentreStore(); //;
  const { dataListe, headerTable, loading } = storeToRefs(centreStore);
  const { recap, destroy } = centreStore;
  let json_fields = {
  "Centre": "centre",
  "Prenom": "prenoms",
  "Nom": "nom",
  "Etablissement de provenance": "etablissement",
  "Telephone": "telephone",
  "Matricule": "matricule",
  "Code": "code",
  "Ville": "ville",
  "Nom de la banque": "banque",
  "Code banque": "codeBanque",
  "Code agence": "codeAgence",
  "Numero compte": "numeroCompte",
  "cle Rib": "cleRib"
 };
  const headers = reactive({ items: [] });
  const searchValue = ref("");
  const dialog = ref(false);
  
  onMounted(()=>{
    recap(); 
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