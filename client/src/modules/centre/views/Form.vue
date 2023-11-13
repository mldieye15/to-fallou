<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-2"
      elevation="8"
      max-width="700"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.centre.centre') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="submit" ref="centreForm" :value="formValid">
      <v-text-field
        id="libelleLong"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="libelleLong"
        density="compact"
        :label="$t('apps.forms.centre.nom')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.libelleLong"
        variant="underlined"
      ></v-text-field>

      <v-text-field
        id="libelleCourt"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="libelleCourt"
        density="compact"
        :label="$t('apps.forms.centre.abreviation')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.libelleCourt"
        variant="underlined"
      ></v-text-field>
      <v-text-field
        id="nombreJury"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="nombreJury"
        density="compact"
        :label="$t('apps.forms.centre.nombreJury')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.nombreJury"
        variant="underlined"
      ></v-text-field>
      <v-row>
        <v-col>
              <v-select
            prepend-inner-icon="mdi-alpha-a-circle"
            name="ville"
            density="compact"
            :label="$t('apps.forms.ville.nom')"
            color="balck"
            v-model="inputForm.ville"
            variant="underlined"
            :items="dataListeVille"
            persistent-hint
            
            single-line
            item-title="libelleLong"
            item-value="id"
          ></v-select>
        </v-col>
        <v-col>
            <v-select
              prepend-inner-icon="mdi-alpha-a-circle"
              name="typeCentre"
              density="compact"
              :label="$t('apps.forms.typeCentre.nom')"
              color="balck"
              v-model="inputForm.typeCentre"
              variant="underlined"
              :items="dataListeTypeCentre"
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
import { reactive, getCurrentInstance } from "vue";
import { onMounted } from "vue"
import { storeToRefs } from "pinia";
import { useVilleStore } from "@/modules/ville/store";
import { useTypeCentreStore } from "@/modules/typeCentre/store";


const instance = getCurrentInstance();

const villeStore = useVilleStore();
const { dataListeVille } = storeToRefs(villeStore);

const typeCentreStore = useTypeCentreStore();
const { dataListeTypeCentre } = storeToRefs(typeCentreStore);

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
  if(instance.refs.centreForm.validate){
    actionSubmit(inputForm);
  }
}

onMounted(()=>{
  villeStore.all();
  typeCentreStore.all();
});

</script>
