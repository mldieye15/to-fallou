<template>
  <v-container>
    <p class="text-h6">{{ $t('apps.forms.academie.academie') }}</p>
    <v-row justify="center">
      <v-col cols="12" sm="8" md="9">
        <v-card>
          <v-card-title class="headline text-center">
            Détail Académie
          </v-card-title>
          <v-card-text>
            <v-list>
              <v-card class="custom-card">
              <v-list-item>
                <v-list-item-content>
                  <v-row>
                    <v-col>
                      <v-list-item-title class="font-weight-bold">Nom de la academie:</v-list-item-title>
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
                      <v-list-item-title class="font-weight-bold">Abreviation:</v-list-item-title>
                    </v-col>
                    <v-col class="text-right">
                      <v-list-item-subtitle>{{ inputForm.libelleCourt }}</v-list-item-subtitle>
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
        <v-btn class="mt-16 mb-8 mr-2" color="blue" @click.prevent="redirectToAcademies()"><v-icon dark left> mdi-arrow-left </v-icon>{{ $t('apps.forms.retour') }}</v-btn>
      </div>
</template>

<script setup>
import { reactive, getCurrentInstance, onMounted} from "vue";
import { storeToRefs } from "pinia";
import { useRouter, useRoute } from 'vue-router';
import { useI18n } from "vue-i18n";
import { useCandidatAuthoriserStore } from "../store";

const i18n = useI18n();

const academieStore= useCandidatAuthoriserStore()
const instance = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const { dataDetails, loading } = storeToRefs(academieStore);
const { one, } = academieStore;
const redirectToAcademies = () => {
  router.push({ name: 'academie-liste'});
};
const inputForm = reactive({
  libelleLong:'',
  libelleCourt: '',
});
onMounted(()=>{
  one(route.params.id ).then( () => {
    inputForm.libelleLong = dataDetails.value.libelleLong,
    inputForm.libelleCourt = dataDetails.value.libelleCourt
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

