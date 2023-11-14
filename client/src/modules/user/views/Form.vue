<template>
  <div>
    <v-card
      class="mx-auto pa-12 pb-8 mt-1"
      elevation="8"
      max-width="900"
      rounded="lg"
    >
    <h2 class="mx-auto text-subtitle-6 text-medium-emphasis text-center">{{ $t('apps.forms.user.user') }}</h2>
    <v-divider class="my-3" color="white"></v-divider>
    <v-form @submit.prevent="submit" ref="userForm" :value="formValid">
      <v-row style="height: 12vh">
        <v-col>
      <v-text-field 
        id="prenoms"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="prenoms"
        density="compact"
        :label="$t('apps.forms.user.prenoms')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.prenoms"
        variant="underlined" 
      ></v-text-field >
      </v-col>
      <v-col>
        <v-text-field
        id="nom"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="nom"
        density="compact"
        :label="$t('apps.forms.user.nom')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.nom"
        variant="underlined"
      ></v-text-field>
      </v-col>
       
    </v-row>
    <v-row style="height: 12vh">
        <v-col>
      <v-text-field 
        id="matricule"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="matricule"
        density="compact"
        :label="$t('apps.forms.user.matricule')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.matricule"
        variant="underlined" 
      ></v-text-field >
      </v-col>
      <v-col>
        <v-text-field
        id="dateNaiss"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="dateNaiss"
        density="compact"
        :label="$t('apps.forms.user.dateNaiss')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.dateNaiss"
        variant="underlined"
      ></v-text-field>
      </v-col>
      <v-col>
        <v-text-field
        id="email"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="email"
        density="compact"
        :label="$t('apps.forms.user.email')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.email"
        variant="underlined"
      ></v-text-field>
      </v-col>
       
    </v-row>
    <v-row style="height: 12vh">
      <v-col>
        <v-text-field
        id="username"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="username"
        density="compact"
        :label="$t('apps.forms.user.username')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.username"
        variant="underlined"
      ></v-text-field>
      </v-col>
      <v-col>
        <v-text-field
        id="mdpasse"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="mdpasse"
        density="compact"
        :label="$t('apps.forms.user.mdpasse')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.mdpasse"
        variant="underlined"
      ></v-text-field>
      </v-col>
      <v-col>
        <v-text-field
        id="sexe"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="sexe"
        density="compact"
        :label="$t('apps.forms.user.sexe')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.sexe"
        variant="underlined"
      ></v-text-field>
      </v-col>
    </v-row >
      <v-row style="height: 12vh">
        <v-col>
          <v-text-field
        id="telephone"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="telephone"
        density="compact"
        :label="$t('apps.forms.user.telephone')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.telephone"
        variant="underlined"
      ></v-text-field>
        </v-col>
        <v-col>
      <v-text-field
        id="code"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="code"
        density="compact"
        :label="$t('apps.forms.user.code')"
        color="balck"
        :rules="[rules.required, rules.min]"
        v-model="inputForm.code"
        variant="underlined"
      ></v-text-field>
        </v-col>
      </v-row>
      <v-row class="reduce-margin">
        <v-col>
          <v-text-field
        id="anciennete"
        prepend-inner-icon="mdi-alpha-a-circle"
        name="anciennete"
        density="compact"
        :label="$t('apps.forms.user.anciennete')"
        color="balck"
        :rules="[rules.required]"
        v-model="inputForm.anciennete"
        variant="underlined"
      ></v-text-field>
        </v-col>
        <v-col>
          <v-select
        prepend-inner-icon="mdi-alpha-a-circle"
        name="fonction"
        density="compact"
        :label="$t('apps.forms.fonction.nom')"
        color="balck"
        v-model="inputForm.fonction"
        variant="underlined"
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
        name="etablissement"
        density="compact"
        :label="$t('apps.forms.etablissement.nom')"
        color="balck"
        v-model="inputForm.etablissement"
        variant="underlined"
        :items="dataListeEtab"
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
import { useFonctionStore } from "@/modules/fonction/store";
import { useEtablissementStore } from "@/modules/etablissement/store";

const instance = getCurrentInstance();

const fonctionStore = useFonctionStore();
const etablissementStore= useEtablissementStore();
const { dataListe } = storeToRefs(fonctionStore);
const { dataListeEtab } = storeToRefs(etablissementStore);
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
  if(instance.refs.userForm.validate){
    actionSubmit(inputForm);
  }
}

onMounted(()=>{
  fonctionStore.all();
  etablissementStore.all();

});

</script>

<!-- <style>
.reduce-margin .v-text-field {
  margin-right: 1px; /* Ajustez la valeur pour réduire ou augmenter l'espace entre les champs horizontalement */
}
</style> -->

