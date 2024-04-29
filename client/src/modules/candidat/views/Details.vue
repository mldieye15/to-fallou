<template>
  <h2 class="text-center">
    Fiche Candidat
   </h2>
    <div v-if="role !=='ROLE_SUPERVISSEUR'" class="text-right">
      <v-btn @click.prevent="redirectToBonus(route.id)" class="mb-2" variant="flat" size="small" color="green">
        <v-icon>mdi-plus-circle-outline</v-icon> <!-- Icône de bonus -->
        Bonus
      </v-btn>
      <v-btn @click.prevent="redirectToMalus(route.id)" class="mb-2 ml-2" variant="flat" size="small" color="red">
        <v-icon>mdi-minus-circle-outline</v-icon> <!-- Icône de malus -->
        Malus
      </v-btn>
    </div>
  <div>
    <v-expansion-panels
    >
      <v-expansion-panel>
        <v-expansion-panel-title><h4>Informations Personnelles</h4></v-expansion-panel-title>
        <v-expansion-panel-text>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      Prénom(s):
                    </v-col>
                    <v-col class="">
                      {{ inputForm.prenoms}}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
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
        </v-expansion-panel-text>
      </v-expansion-panel>

      <v-expansion-panel>
        <v-expansion-panel-title><h4>Evaluation</h4></v-expansion-panel-title>
        <v-expansion-panel-text>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      Année:
                    </v-col>
                    <v-col class="">
                      {{ inputForm.annee}}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      Bonus:
                    </v-col >
                    <v-col class="">
                      {{ inputForm.bonus }}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      Malus:
                    </v-col>
                    <v-col class="">
                      {{ inputForm.malus }}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      Ancienneté:
                    </v-col>
                    <v-col class="">
                      {{ inputForm.noteAnciennete}}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      Note de l'établissement de provenance:
                    </v-col>
                    <v-col class="">
                      {{ inputForm.noteEtablissementProvenance }}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                       Note du supervisseur:
                    </v-col>
                    <v-col class="">
                      {{ inputForm.noteSupervisseur}}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      Note de la fonction (grade):
                    </v-col>
                    <v-col class="">
                      {{ inputForm.noteFonction }}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                      Affectable:
                    </v-col>
                    <v-col :style="{ color: inputForm.affectable === 'OUI' ? 'green' : 'red' }" class="">
                      {{ inputForm.affectable }}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2" style="height: 5vh">
                    <v-col>
                       Score:
                    </v-col>
                    <v-col class="">
                      {{ inputForm.note }}
                    </v-col>
                  </v-row>
                  <v-row class="ma-2">
                    <v-col>
                       Appreciation:
                    </v-col>
                    <v-col class="">
                      {{ inputForm.appreciation }}
                    </v-col>
                  </v-row>
        </v-expansion-panel-text>
      </v-expansion-panel>
    </v-expansion-panels>
  </div>
        <div class="d-flex">
           <v-btn class="mt-4 mb-8 mr-2" color="blue" @click.prevent="redirectToListe()"><v-icon dark left> mdi-arrow-left </v-icon>{{ $t('apps.forms.retour') }}</v-btn>
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
/* .custom-card {
  border: 1px solid #0ad7ea;
  margin-bottom: 5px;
  height: 40px;
} */
</style>