<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" sm="8" md="9">
        <v-card>
          <v-card-title class="headline">
            Session Details
          </v-card-title>
          <v-card-text>
            <v-list>
              <v-card class="custom-card" >
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">Nom de la Session:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.libelleLong}}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">date de Debut de la Session:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.dateDebut }}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">delais de Validation:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.delaisValidation }}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">date de Fin de la Session:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.dateFin }}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">nombre de Demande Autorise:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.nombreDemandeAutorise }}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">date Ouverture Depot de Candidature:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.dateOuvertureDepotCandidature }}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">date Cloture Depot de Candidature:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.dateClotureDepotCandidature }}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">Annee:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.annee }}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">type de Session:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.typeSession }}</v-list-item-subtitle>
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
  <v-btn class="mt-16 mb-8 mr-2" color="blue" @click.prevent="redirectToListe()"><v-icon dark left> mdi-arrow-left </v-icon>{{ $t('apps.forms.retour') }}</v-btn>
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

import { useSessionStore } from "../store";
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const sessionStore = useSessionStore();
const { dataDetails, loading } = storeToRefs(sessionStore);
const { one, modify } = sessionStore;

const inputForm = reactive({
  libelleLong:'',
  dateDebut:null,
  dateFin:null,
  nombreDemandeAutorise:'',
  delaisValidation:'',
  dateOuvertureDepotCandidature:null,
  dateClotureDepotCandidature:null,
  annee:null,
  typeSession:null,
});
watchEffect(() => {
  if (
  inputForm.dateDebut,
  inputForm.dateFin,
  inputForm.dateOuvertureDepotCandidature,
  inputForm.dateClotureDepotCandidature 
  ) {
    inputForm.dateDebut=formatDateForInput(inputForm.dateDebut);
    inputForm.dateFin=formatDateForInput(inputForm.dateFin);
    inputForm.dateOuvertureDepotCandidature= formatDateForInput(inputForm.dateOuvertureDepotCandidature);
    inputForm.dateClotureDepotCandidature = formatDateForInput(inputForm.dateClotureDepotCandidature);
  }
});

function formatDateForInput(date) {
  const formattedDate = format(new Date(date), 'yyyy-MM-dd', { locale: fr });
  return formattedDate;
}
const redirectToListe = () => {
  router.push({ name: 'session-liste'});
};
onMounted(()=>{
  one(route.params.id ).then( () => {
    inputForm.libelleLong = dataDetails.value.libelleLong,
    inputForm.dateDebut = dataDetails.value.dateDebut,
    inputForm.dateFin = dataDetails.value.dateFin,
    inputForm.nombreDemandeAutorise = dataDetails.value.nombreDemandeAutorise,
    inputForm.delaisValidation = dataDetails.value.delaisValidation,
    inputForm.dateOuvertureDepotCandidature = dataDetails.value.dateOuvertureDepotCandidature,
    inputForm.dateClotureDepotCandidature = dataDetails.value.dateClotureDepotCandidature,
    inputForm.annee = dataDetails.value.annee.libelleLong,
    inputForm.typeSession = dataDetails.value.typeSession.libelleLong   
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
