<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" sm="8" md="9">
        <v-card>
          <v-card-title class="headline">
            User Details
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
                      <v-list-item-title class="font-weight-bold">Email:</v-list-item-title>
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
                      <v-list-item-title class="font-weight-bold">Date de Naissance:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.dateNaiss }}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">Nom d'Utilisateur:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.username }}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">Telephone:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.telephone }}</v-list-item-subtitle>
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

const inputForm = reactive({
  prenoms: "",
  nom: "",
  matricule: "",
  dateNaiss:null,
  email: "",
  username: "",
  mdpasse: "",
  sexe: "",
  code: "",
  telephone: "",
  anciennete: "",
  fonction: null,
  etablissement: null,
});
function formatDateForInput(date) {
  const formattedDate = format(new Date(date), 'dd-MM-yyyy', { locale: fr });
  return formattedDate;
}
watchEffect(() => {
  if (
  inputForm.dateNaiss
  ) {
    inputForm.dateNaiss=formatDateForInput(inputForm.dateNaiss);
  }
});
onMounted(()=>{
  one(route.params.id ).then( () => {
    inputForm.prenoms = dataDetails.value.prenoms,
    inputForm.nom = dataDetails.value.nom,
    inputForm.matricule = dataDetails.value.matricule,
    inputForm.dateNaiss = dataDetails.value.dateNaiss,
    inputForm.email = dataDetails.value.email,
    inputForm.username = dataDetails.value.username,
    inputForm.mdpasse = dataDetails.value.mdpasse
    inputForm.sexe = dataDetails.value.sexe,
    inputForm.code = dataDetails.value.code,
    inputForm.telephone = dataDetails.value.telephone,
    inputForm.anciennete = dataDetails.value.anciennete,
    inputForm.fonction=dataDetails.value.fonction.libelleLong,
    inputForm.etablissement=dataDetails.value.etablissement.libelleLong    
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