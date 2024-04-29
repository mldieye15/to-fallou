<template>
  <v-container>
    <h2 class="text-center mb-5">
    Détail utilisateur
   </h2>
  <div>
    <v-expansion-panels
    >
      <v-expansion-panel>
        <v-expansion-panel-title><h4>Informations Personnelles</h4></v-expansion-panel-title>
        <v-expansion-panel-text>
                  <v-row class="ma-2 py-0" style="height: 5vh">
                    <v-col>
                      Prénom(s):
                    </v-col>
                    <v-col class="">
                      {{ inputForm.prenoms}}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2 py-0" style="height: 5vh">
                    <v-col>
                      Nom:
                    </v-col >
                    <v-col class="">
                      {{ inputForm.nom }}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      Adresse email:
                    </v-col>
                    <v-col class="">
                      {{ inputForm.email }}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      Sexe:
                    </v-col>
                    <v-col class="">
                      {{ inputForm.sexe }}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      Téléphone:
                    </v-col>
                    <v-col class="">
                      {{ inputForm.telephone }}
                    </v-col>
                  </v-row>
        </v-expansion-panel-text>
      </v-expansion-panel>

      <v-expansion-panel>
        <v-expansion-panel-title><h4>Informations Professionnelles</h4></v-expansion-panel-title>
        <v-expansion-panel-text>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      Matricule:
                    </v-col>
                    <v-col class="">
                      {{ inputForm.matricule }}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col >
                      Code:
                    </v-col>
                    <v-col class="">
                      {{ inputForm.code }}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      Fonction:
                    </v-col>
                    <v-col class="">
                      {{ inputForm.fonction }}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 8vh">
                    <v-col>
                      Etablissement de provenance:
                    </v-col>
                    <v-col class=" ">
                      {{ inputForm.etablissement }}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      Ancienneté:
                    </v-col>
                    <v-col class=" ">
                      {{ inputForm.anciennete }}
                    </v-col>
                  </v-row>
        </v-expansion-panel-text>
      </v-expansion-panel>

      <v-expansion-panel>
        <v-expansion-panel-title><h4>Informations bancaires</h4></v-expansion-panel-title>
        <v-expansion-panel-text>
                 <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      Banque:
                    </v-col>
                    <v-col class="">
                      {{ inputForm.banque }}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col >
                      CodeBanque:
                    </v-col>
                    <v-col class="">
                      {{ inputForm.codeBanque }}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      CodeAgence:
                    </v-col>
                    <v-col class="">
                      {{ inputForm.codeAgence }}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      NumeroCompte:
                    </v-col>
                    <v-col class=" ">
                      {{ inputForm.numeroCompte }}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      CleRib:
                    </v-col>
                    <v-col class=" ">
                      {{ inputForm.cleRib }}
                    </v-col>
                  </v-row>
        </v-expansion-panel-text>
      </v-expansion-panel>
    </v-expansion-panels>
  </div>
  </v-container>
  
      <div class="d-flex justify-end">
        <v-btn class="mt-3 mb-8 mr-2" color="blue" @click.prevent="redirectToUsers"><v-icon dark left> mdi-arrow-left </v-icon>{{ $t('apps.forms.retour') }}</v-btn>
      </div>
</template>

<script setup>
import { reactive, getCurrentInstance, onMounted, watchEffect} from "vue";
import { storeToRefs } from "pinia";
import { useRouter, useRoute } from 'vue-router';
import { useNotificationStore } from "@/store/notification";
import { useI18n } from "vue-i18n";
import { format } from 'date-fns';
import { fr } from "date-fns/locale";

import { useUtilisateurStore } from "../store";
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const userStore = useUtilisateurStore();
const { dataDetails, loading } = storeToRefs(userStore);
const { one, modify } = userStore;
const redirectToUsers = () => {
  router.push({ name: 'user-liste'});
};
const inputForm = reactive({
  prenoms: "",
  nom: "",
  matricule: "",
  // dateNaiss:null,
  email: "",
  username: "",
  mdpasse: "",
  sexe: "",
  code: "",
  telephone: "",
  anciennete: "",
  banque: "",
  codeBanque: "",
  codeAgence:"",
  numeroCompte: "",
  cleRib: "",
  fonction: null,
  etablissement: null,
});
// function formatDateForInput(date) {
//   const formattedDate = format(new Date(date), 'dd-MM-yyyy', { locale: fr });
//   return formattedDate;
// }
// watchEffect(() => {
//   if (
//   inputForm.dateNaiss
//   ) {
//     inputForm.dateNaiss=formatDateForInput(inputForm.dateNaiss);
//   }
// });
onMounted(()=>{
  one(route.params.id ).then( () => {
    inputForm.prenoms = dataDetails.value.prenoms,
    inputForm.nom = dataDetails.value.nom,
    inputForm.matricule = dataDetails.value.matricule,
    // inputForm.dateNaiss = dataDetails.value.dateNaiss,
    inputForm.email = dataDetails.value.email,
    inputForm.username = dataDetails.value.username,
    inputForm.mdpasse = dataDetails.value.mdpasse
    inputForm.sexe = dataDetails.value.sexe,
    inputForm.code = dataDetails.value.code,
    inputForm.telephone = dataDetails.value.telephone,
    inputForm.anciennete = dataDetails.value.anciennete,
    inputForm.fonction=dataDetails.value.etablissement.typeEtablissement.fonction.libelleLong,
    inputForm.etablissement=dataDetails.value.etablissement.libelleLong,
    inputForm.banque=dataDetails.value.banque,
    inputForm.codeBanque=dataDetails.value.codeBanque,
    inputForm.codeAgence=dataDetails.value.codeAgence,
    inputForm.numeroCompte=dataDetails.value.numeroCompte,
    inputForm.cleRib=dataDetails.value.cleRib   
  });
});

</script>
<style scoped>
.custom-card {
  border: 1px solid #0ad7ea;
  margin-bottom: 5px;
  height: 40px;
}
</style>