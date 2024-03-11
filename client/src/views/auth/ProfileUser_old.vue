<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" sm="8" md="9">
        <v-card>
          <v-card-title class="headline">
            Détail de l'utilisateur
          </v-card-title>
          <v-card-text>
            <v-list>
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">Prenom:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.prenoms}}</v-list-item-subtitle>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
            </v-card>
            <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">Nom:</v-list-item-title>
                    </v-col >
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.nom }}</v-list-item-subtitle>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
              </v-card>
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">Adresse email:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.email }}</v-list-item-subtitle>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
              </v-card>
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">Matricule:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.matricule }}</v-list-item-subtitle>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
              </v-card>
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">Sexe:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.sexe }}</v-list-item-subtitle>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
              </v-card>
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">Code:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.code }}</v-list-item-subtitle>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
              </v-card>
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">Telephone:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.telephone }}</v-list-item-subtitle>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
              </v-card>
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">Ancienneté:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.anciennete }}</v-list-item-subtitle>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
              </v-card>
              <!-- <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">Fonction:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.fonction }}</v-list-item-subtitle>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
              </v-card> -->
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">Etablissement de Provenance:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.etablissement }}</v-list-item-subtitle>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
              </v-card>
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">Banque:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.banque }}</v-list-item-subtitle>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
              </v-card>
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">CodeBanque:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.codeBanque }}</v-list-item-subtitle>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
              </v-card>
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">CodeAgence:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.codeAgence }}</v-list-item-subtitle>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
              </v-card>
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">NumeroCompte:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.numeroCompte}}</v-list-item-subtitle>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
              </v-card>
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">CleRip:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.cleRib}}</v-list-item-subtitle>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
              </v-card>
            </v-list>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
      <div class="d-flex justify-end">
        <v-btn class="mt-16 mb-8 mr-2" color="blue" @click.prevent="redirectToUsers"><v-icon dark left> mdi-arrow-left </v-icon>{{ $t('apps.forms.retour') }}</v-btn>
      </div>
</template>

<script setup>
import { reactive, getCurrentInstance, onMounted, watchEffect} from "vue";
import { storeToRefs } from "pinia";
import { useRouter, useRoute } from 'vue-router';
import { useNotificationStore } from "@/store/notification";
import { useUserStore } from "@/store/user";
const currentUser = useUserStore();
const {current} = currentUser;
import { format } from 'date-fns';
import { fr } from "date-fns/locale";

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const { dataDetails, loading } = storeToRefs(currentUser);
const redirectToUsers = () => {
  router.push({ name: 'accueil'});
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
  current().then( () => {
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
    // inputForm.fonction=dataDetails.value.fonction.libelleLong,
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