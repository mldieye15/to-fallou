<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-5"
      elevation="8"
      max-width="500"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.demande.demande') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="submit" ref="demandeForm" :value="formValid">
      <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        name="session"
        density="compact"
        :label="$t('apps.forms.session.nom')"
        color="balck"
        v-model="inputForm.session"
        variant="solo"
        :items="dataListeSession"
        persistent-hint
        readonly
        single-line
        item-title="libelleLong"
        item-value="id"
      ></v-select>

      <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        name="ville"
        density="compact"
        :label="$t('apps.forms.ville.nom')"
        color="balck"
        v-model="inputForm.ville"
        variant="solo"
        :items="dataListeVille"
        persistent-hint
        readonly
        single-line
        item-title="libelleLong"
        item-value="id"
      ></v-select>

      <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        name="academie"
        density="compact"
        color="balck"
        v-model="inputForm.academie"
        variant="solo"
        :items="dataListe"
        persistent-hint
        readonly
        item-title="libelleLong"
        item-value="id"
      >
    </v-select>
    <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        name="centre"
        density="compact"
        color="balck"
        v-model="inputForm.centre"
        variant="solo"
        :items="dataListeCentre"
        persistent-hint
        item-title="libelleLong"
        item-value="id"
      >
    </v-select>

      <v-btn block class="mt-2 mb-8" size="large" color="primary" @click="handleSave">{{ $t('apps.forms.enregistrer') }}</v-btn>
    </v-form>
    </v-card>
  </div>
</template>

<script setup>
import { reactive, getCurrentInstance } from "vue";
import { onMounted } from "vue"
import { storeToRefs } from "pinia";
import { useVilleStore } from "@/modules/ville/store";
import { useAcademieStore } from "@/modules/academie/store";
import { useCentreStore } from "@/modules/centre/store";
import { useSessionStore } from "@/modules/session/store";

const instance = getCurrentInstance();

const villeStore = useVilleStore();
const {dataListeVille} = storeToRefs(villeStore);

const academieStore = useAcademieStore();
const { dataListe } = storeToRefs(academieStore);
const sesssionStore=useSessionStore();
const { dataListeSession } = storeToRefs(sesssionStore);
const centreStore=useCentreStore();
const { dataListeCentre,dataDetails} = storeToRefs(centreStore);


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
  if(instance.refs.demandeForm.validate){
    actionSubmit(inputForm);
  }
}

onMounted(()=>{
  villeStore.all();
  academieStore.all();
  sesssionStore.all();
  centreStore.all();
});

</script>
