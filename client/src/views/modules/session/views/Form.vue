<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.session.session') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="submit" ref="sessionForm" :value="formValid">
      <v-text-field
        id="libelleLong"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="libelleLong"
        density="compact"
        :label="$t('apps.forms.session.nom')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.libelleLong"
        variant="underlined"
      ></v-text-field>

      <v-text-field
        id="dateDebut"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="dateDebut"
        density="compact"
        :label="$t('apps.forms.session.dateDebut')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.dateDebut"
        variant="underlined"
      ></v-text-field>

      <v-text-field
        id="dateFin"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="dateFin"
        density="compact"
        :label="$t('apps.forms.session.dateFin')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.dateFin"
        variant="underlined"
      ></v-text-field>

      <v-text-field
        id="nombreDemandeAutorise"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="nombreDemandeAutorise"
        density="compact"
        :label="$t('apps.forms.session.nombreDemandeAutorise')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.nombreDemandeAutorise"
        variant="underlined"
      ></v-text-field>

      <v-text-field
        id="delaisValidation"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="delaisValidation"
        density="compact"
        :label="$t('apps.forms.session.delaisValidation')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.delaisValidation"
        variant="underlined"
      ></v-text-field>

      <v-text-field
        id="dateOuvertureDepotCandidature"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="dateOuvertureDepotCandidature"
        density="compact"
        :label="$t('apps.forms.session.dateOuvertureDepotCandidature')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.dateOuvertureDepotCandidature"
        variant="underlined"
      ></v-text-field>

      <v-text-field
        id="dateClotureDepotCandidature"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="dateClotureDepotCandidature"
        density="compact"
        :label="$t('apps.forms.session.dateClotureDepotCandidature')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.dateClotureDepotCandidature"
        variant="underlined"
      ></v-text-field>

      <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        name="annee"
        density="compact"
        :label="$t('apps.forms.annee.libelle')"
        color="balck"
        v-model="inputForm.annee"
        variant="underlined"
        :items="dataListe"
        persistent-hint
        
        single-line
        item-title="libelle"
        item-value="id"
      ></v-select>

      <v-btn block class="mt-2 mb-8" size="large" color="primary" @click="handleSave">{{ $t('apps.forms.enregistrer') }}</v-btn>
    </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance } from "vue";
import { onMounted } from "vue"
import { storeToRefs } from "pinia";
import { useAnneeStore } from "@/modules/annee/store";

const instance = getCurrentInstance();

const anneeStore = useAnneeStore();
const { dataListe } = storeToRefs(anneeStore);

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

const handleSave = () => {
  if(instance.refs.sessionForm.validate){
    actionSubmit(inputForm);
  }
}

onMounted(()=>{
  anneeStore.all();
});

</script>
