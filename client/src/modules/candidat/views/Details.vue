<template>
  
  <v-container>
    <h2 class="text-center">
    Fiche Candidat
   </h2>
    <div v-if="role !=='ROLE_SUPERVISSEUR'" class="text-right">
      <v-btn @click.prevent="redirectToBonus(route.id)" class="mb-2 " variant="flat" size="small" color="green">
        <v-icon>mdi-plus-circle-outline</v-icon> <!-- Icône de bonus -->
        Bonus
      </v-btn>
      <v-btn @click.prevent="redirectToMalus(route.id)" class="mb-2 ml-2" variant="flat" size="small" color="red">
        <v-icon>mdi-minus-circle-outline</v-icon> <!-- Icône de malus -->
        Malus
      </v-btn>
    </div>
          
    <v-row justify="center">
      <v-col cols="12" sm="6" md="6">
        <v-card>
          <v-card-text>
            <v-list>
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">Prénom(s):</v-list-item-title>
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
                      <v-list-item-title class="font-weight-bold">Code:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.code }}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">Nom d'utilisateur:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.username }}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">Téléphone:</v-list-item-title>
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
                      <v-list-item-title class="font-weight-bold">Fonction:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.fonction }}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">Etablissement de provenance:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right ">
                      <v-list-item-subtitle>{{ inputForm.etablissement }}</v-list-item-subtitle>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
              </v-card>
            </v-list>
          </v-card-text>
        </v-card>
        <div class="d-flex">
           <v-btn class="mt-4 mb-8 mr-2" color="blue" @click.prevent="redirectToListe()"><v-icon dark left> mdi-arrow-left </v-icon>{{ $t('apps.forms.retour') }}</v-btn>
        </div>
      </v-col>
      <v-col>
        <v-card>
          <v-card-text>
            <v-list>
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">Année:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.annee}}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">Bonus:</v-list-item-title>
                    </v-col >
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.bonus }}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">Malus:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.malus }}</v-list-item-subtitle>
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
                      <v-list-item-subtitle>{{ inputForm.noteAnciennete }}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">Note de l'établissement de provenance:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.noteEtablissementProvenance }}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold"> Note du supervisseur:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.noteSupervisseur}}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">Note de la fonction (grade):</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.noteFonction }}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold">Affectable:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle :style="{ color: inputForm.affectable === 'OUI' ? 'green' : 'red' }">{{ inputForm.affectable }}</v-list-item-subtitle>
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
                      <v-list-item-title class="font-weight-bold"> Score:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.note }}</v-list-item-subtitle>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
              </v-card>
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row >
                    <v-col>
                      <v-list-item-title class="font-weight-bold"> Appreciation:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.appreciation }}</v-list-item-subtitle>
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

import { useCandidatStore } from "../store";
const i18n = useI18n();

const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;

const instance = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const userStore = useCandidatStore();
const { dataDetails, loading } = storeToRefs(userStore);
const { one, modify } = userStore;
const redirectToListe = () => {
  router.push({ name: 'candidat-liste'});
};

const inputForm = reactive({
  prenoms: "",
  nom: "",
  matricule: "",
  code:"",
  email: "",
  mdpasse: "",
  sexe: "",
  telephone: "",
  annee: "",
  bonus: "",
  malus: "",
  noteAnciennete: "",
  note: "",
  noteEtablissementProvenance: "",
  appreciation: "",
  noteSupervisseur: "",
  noteFonction: "",
  affectable: "",
  fonction: null,
  etablissement: null,
});
onMounted(()=>{
  one(route.params.id ).then( () => {
    inputForm.prenoms = dataDetails.value.candidat.prenoms,
    inputForm.nom = dataDetails.value.candidat.nom,
    inputForm.matricule = dataDetails.value.candidat.matricule,
    inputForm.code = dataDetails.value.candidat.code,
    inputForm.email = dataDetails.value.candidat.email,
    inputForm.mdpasse = dataDetails.value.candidat.mdpasse
    inputForm.sexe = dataDetails.value.candidat.sexe,
    inputForm.telephone = dataDetails.value.candidat.telephone,
    inputForm.fonction=dataDetails.value.candidat.etablissement.typeEtablissement.fonction.libelleLong,
    inputForm.etablissement=dataDetails.value.candidat.etablissement.libelleLong 
    inputForm.annee = dataDetails.value.annee.libelleLong,
    inputForm.bonus = dataDetails.value.bonus,
    inputForm.malus = dataDetails.value.malus,
    inputForm.noteFonction = dataDetails.value.noteFonction,
    inputForm.noteEtablissementProvenance = dataDetails.value.noteEtablissementProvenance
    inputForm.noteAnciennete = dataDetails.value.noteAnciennete,
    inputForm.note = dataDetails.value.note,
    inputForm.noteSupervisseur=dataDetails.value.noteSupervisseur,
    inputForm.appreciation=dataDetails.value.appreciation
    inputForm.affectable=dataDetails.value.affectable?'OUI':'NON'     
  });
});
let role= localStorage.getItem('role');
const redirectToBonus = (id) => {
  router.push({ name: 'candidat-bonus', params: { id } });
};
const redirectToMalus = (id) => {
  router.push({ name: 'candidat-malus', params: { id } });
};
</script>
<style scoped>
.custom-card {
  border: 1px solid #0ad7ea;
  margin-bottom: 5px;
  height: 40px;
}
</style>