<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-1"
      elevation="8"
      max-width="900"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.session.session') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="submit" ref="sessionForm" :value="formValid">
      <v-row style="height: 12vh">
        <v-col>
      <v-text-field 
        id="libelleLong"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="libelleLong"
        density="compact"
        :label="$t('apps.forms.session.nom')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.libelleLong"
        variant="solo" 
      ></v-text-field >
      </v-col>
      <v-col>
        <v-text-field
        id="nombreDemandeAutorise"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="nombreDemandeAutorise"
        density="compact"
        :label="$t('apps.forms.session.nombreDemandeAutorise')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.nombreDemandeAutorise"
        variant="solo"
      ></v-text-field>
      </v-col>
       
    </v-row>
    <v-row style="height: 12vh">
      <v-col>
        <v-text-field
        id="dateDebut"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="dateDebut"
        density="compact"
        :label="$t('apps.forms.session.dateDebut')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.dateDebut"
        variant="solo"
        type="date"
      ></v-text-field>
      </v-col>
      <v-col>
        <v-text-field
        id="dateFin"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="dateFin"
        density="compact"
        :label="$t('apps.forms.session.dateFin')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.dateFin"
        type="date"
        variant="solo"
      ></v-text-field>
      </v-col>
    </v-row >
      <v-row style="height: 12vh">
        <v-col>
          <v-text-field
        id="dateDeOuvertureDepotCandidature"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="dateDeOuvertureDepotCandidature"
        density="compact"
        :label="$t('apps.forms.session.dateOuvertureDepotCandidature')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.dateOuvertureDepotCandidature"
        variant="solo"
        type="date"
      ></v-text-field>
        </v-col>
        <v-col>
      <v-text-field
        id="dateDeClotureDepotCandidature"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="dateDeClotureDepotCandidature"
        density="compact"
        :label="$t('apps.forms.session.dateClotureDepotCandidature')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.dateClotureDepotCandidature"
        variant="solo"
        type="date"
      ></v-text-field>
        </v-col>
      </v-row>
      <v-row class="reduce-margin">
        <v-col>
          <v-text-field
        id="delaisValidation"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="delaisValidation"
        density="compact"
        :label="$t('apps.forms.session.delaisValidation')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.delaisValidation"
        variant="solo"
      ></v-text-field>
        </v-col>
        <v-col>
          <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        name="annee"
        density="compact"
        :label="$t('apps.forms.annee.libelle')"
        color="balck"
        v-model="inputForm.annee"
        variant="solo"
        :items="dataListe"
        persistent-hint
        
        single-line
        item-title="libelleLong"
        item-value="id"
      ></v-select>
        </v-col>
        <v-col>
          <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        name="typeSession"
        density="compact"
        :label="$t('apps.forms.typeSession.nom')"
        color="balck"
        v-model="inputForm.typeSession"
        variant="solo"
        :items="dataListeTypeSession"
        persistent-hint
        
        single-line
        item-title="libelleLong"
        item-value="id"
      ></v-select>
        </v-col>

      </v-row>
      

      <v-btn block class="mt-2 mb-8" size="large" color="blue" @click="handleSave">{{ $t('apps.forms.enregistrer') }}</v-btn>
    </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance ,watchEffect} from "vue";
import { onMounted } from "vue"
import { storeToRefs } from "pinia";
import { useAnneeStore } from "@/modules/annee/store";
import { useTypeSessionStore } from "@/modules/typeSession/store";
import { format } from 'date-fns';
import { fr } from "date-fns/locale";

const instance = getCurrentInstance();

const anneeStore = useAnneeStore();
const typeSessionStore= useTypeSessionStore();
const { dataListe } = storeToRefs(anneeStore);
const {dataListeTypeSession} = storeToRefs(typeSessionStore);
const rules = reactive({
  required: value => !!value || 'Champ obligatoire.',
  min: v => v.length >= 2 || '2 cractére au moins',
});

const { inputForm, actionSubmit } = defineProps({
  inputForm: Object,
  actionSubmit: {
    type: Function,
  }
});
watchEffect(() => {
  if (inputForm.dateClotureDepotCandidature, 
  inputForm.dateOuvertureDepotCandidature,
  inputForm.dateDebut,
  inputForm.dateFin
  ) {
    console.log('Avant formatage :', inputForm.dateClotureDepotCandidature);
    console.log('Avant formatage :', inputForm.dateDebut);
    inputForm.dateClotureDepotCandidature = formatDateForInput(inputForm.dateClotureDepotCandidature);
    inputForm.dateOuvertureDepotCandidature= formatDateForInput(inputForm.dateOuvertureDepotCandidature);
    inputForm.dateDebut=formatDateForInput(inputForm.dateDebut);
    inputForm.dateFin=formatDateForInput(inputForm.dateFin);
  }
  console.log('Après formatage :', inputForm.dateClotureDepotCandidature);
  console.log('Apres formatage :', inputForm.dateDebut);
});

function formatDateForInput(date) {
  const formattedDate = format(new Date(date), 'yyyy-MM-dd', { locale: fr });
  return formattedDate;
}

const handleSave = () => {
  if(instance.refs.sessionForm.validate){
    actionSubmit(inputForm);
  }
}

onMounted(()=>{
  anneeStore.all();
  typeSessionStore.all();

});

</script>

<!-- <style>
.reduce-margin .v-text-field {
  margin-right: 1px; /* Ajustez la valeur pour réduire ou augmenter l'espace entre les champs horizontalement */
}
</style> -->

