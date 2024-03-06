<template>
  <v-container>
    <p class="text-h6">{{ $t('apps.forms.codification.codification') }}</p>
    <v-row justify="center">
      <v-col cols="12" sm="8" md="9">
        <v-card>
          <v-card-title class="headline">
            Détail codification
          </v-card-title>
          <v-card-text>
            <v-list>
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">Adresse email:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.email}}</v-list-item-subtitle>
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
                      <v-list-item-subtitle >{{ inputForm.code }}</v-list-item-subtitle>
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
import { reactive, getCurrentInstance, onMounted} from "vue";
import { storeToRefs } from "pinia";
import { useRouter, useRoute } from 'vue-router';
import { useI18n } from "vue-i18n";
import { useCodificationStore } from "../store";
const i18n = useI18n();

const codificationStore= useCodificationStore()
const instance = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const { dataDetails, loading } = storeToRefs(codificationStore);
const { one, } = codificationStore;

const inputForm = reactive({
  email:'',
  code: '',
});
const redirectToListe = () => {
  router.push({ name: 'codification-liste'});
};
onMounted(()=>{
  one(route.params.id ).then( () => {
    inputForm.email = dataDetails.value.email
    inputForm.code = dataDetails.value.code   
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


