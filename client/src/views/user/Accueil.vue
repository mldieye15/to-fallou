<template>
    <v-container fluid >
<div v-if="inputForm.cleRib===null">
  <h1 class="text-center">Attention votre RIB n'est pas disponible ! Ajouter votre RIB pour continuer votre candidature.</h1>
  <br>
  <hr>
  <h2 class="text-center bg-blue">Mise à jour du RIB
     <v-btn @click.prevent="redirectToProfile()" class="ma-0 text-caption" variant="text"  >
     <h1> (cliquer ici)</h1>  
     </v-btn>  </h2>
  <hr>
</div>
<div v-else>
  <div v-if="dataListeSession.length > 0">
     <div v-if="dataListeForUser.length> 0">
  <div  class="mt-1 ">
    <div  v-for="session in dataListeSession" :key="session.id" class="bg-cyan text-center text-white w-70 ">
      <div v-if="session.candidatureOuvert==='ouverte'">
        <h3 class="subheading font-weight-bold text-white">
        <VueTyper 
        pre-type-delay='1000'
        :text="`La date limite des candidatures est fixée au  ${session.dateClotureDepotCandidature}. Au-delà de cette date, vous ne pourriez plus  modifier votre demande.`" 
        style="color: white;"
        
         />
      </h3>
      </div>
      <div v-if="session.modification==='ouverte'">
        <h3 class="subheading font-weight-bold text-white">
        <VueTyper 
        pre-type-delay='1000'
        text="Les modifications sont réouvertes, vous pouvez modifier vos demandes 'OBSOLETE'." 
        color="white"
        style=""
         />
      </h3> 
      </div>
      
    
   
</div>
</div>
  <v-container fluid> 
    <div class="text-center mb-10">
      <span>  Demande utilisateur</span>
     
    </div> 
  <v-row justify="center">
  <v-col v-for="demande in dataListeForUser" :key="demande.id" cols="12" sm="6" md="3" class="demande-bloc ml-10">
    <v-card class="ma-1">
      <p> Académie: {{ demande.academie }}</p>
            <p> Ville: {{ demande.ville }}</p>
            <p>Statut:
              <v-chip :style="{ 'font-size': '15px', 'height': '20px' }" :color="etatCouleurs[demande.etatDemande]" variant="tonal">
                <span>{{ demande.etatDemande }}</span>
              </v-chip>
            </p>
            <div class="actions-wrapper"  v-if="demande.modification==='OUI' && demande.etatDemande==='obsolète'" >
                <div class="demandes"> Veuillez cliquer sur le bouton 'modifier' pour modifier votre demande</div>
             <v-btn  variant="flat" color="light-blue-darken-4" size="small" @click.prevent="redirectToEditDemande(demande.id)" class="mt-3">
              modifier
             </v-btn>   
          </div>
          <div class="actions-wrapper" v-else-if="demande.candidatureOuvert==='OUI' && demande.etatDemande==='en attente'" >
                <div class="demandes"> Veuillez cliquer sur le bouton 'modifier' pour modifier votre demande</div>
             <v-btn  variant="flat" color="light-blue-darken-4" size="small" @click.prevent="redirectToEditDemande(demande.id)" class="mt-3 mb-3">
              modifier
             </v-btn>   
          </div>
            <div v-if="demande.etatDemande==='acceptée'" class="mt-4">
            <v-dialog v-model="dialog" transition="dialog-top-transition" width="50%" height="auto">
              <template v-slot:activator="{ props }">
                 <div class="demandes"> Veuillez cliquer sur le bouton 'valider' pour valider votre demande</div>
                  <v-btn variant="flat" color="green" size="small" v-bind="props">
                   valider
                  </v-btn>
              </template>
              <template v-slot:default="{ isActive }">
                <v-card>
                  <v-toolbar color="primary" :title="$t('apps.forms.demande.demande')"></v-toolbar>
                  <v-card-text>
                    <div class="text-h6">{{ $t('apps.forms.validerMessage') }}</div>
                  </v-card-text>
                  <v-card-actions class="justify-end">
                    <v-btn variant="text" color="primary" @click="isActive.value = false">{{ $t('apps.forms.annuler') }}</v-btn>
                    <v-btn variant="outlined" color="black"  @click="valider(demande.id)">{{ $t('apps.forms.oui') }}</v-btn>
                  </v-card-actions>
                </v-card>
              </template>
            </v-dialog>
          </div>
    </v-card>
  </v-col>
</v-row>
</v-container>
</div>
<div class="text-center" v-else>
  <h2>Sessions en cours</h2>
      <ul>
        <div v-for="session in dataListeSession" :key="session.id" :to="{ name: 'demande', params: { id: session.id } }">
          <span class="session-row">
           <v-chip color='green'><h2>{{ session.libelleLong }}:</h2></v-chip>
           <v-chip color='blue'>
            <div v-if="session.candidatureOuvert==='ouverte'">
            <router-link :to="{ name: 'demande', params: { id: session.id } }" class="candidater-link">
              Postuler ici
            </router-link>
            </div>
            <div v-else>
              <p>Depot candidature cloturée dépuis le {{ session.dateClotureDepotCandidature }}</p>
            </div>
           </v-chip>
          </span>
        </div>
      </ul>
</div>
      
    </div>
    <div v-else>
      <h2> Aucune Session en cours actuellement.</h2>
    </div>
</div>
      
    </v-container>
    
    <Footer :copyrightName="copyrightName" />
</template>

<script setup>
import { onMounted,ref,reactive } from 'vue';
import { storeToRefs } from "pinia";
import { useAppStore } from "@/store/app";
import { useNotificationStore } from "@/store/notification";
import { useUserStore } from "@/store/user";
import { useSessionStore } from "@/modules/session/store";
import { useDemandeStore } from '@/modules/demande/store';
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";
const currentUser = useUserStore();
const {current} = currentUser;
const { dataDetails, loading } = storeToRefs(currentUser);
const router = useRouter();
const appStore = useAppStore();
const sessionStore = useSessionStore();
const {dataListeSession}= storeToRefs(sessionStore);
const demandeStore = useDemandeStore();
const {dataListeForUser,etatCouleurs,}= storeToRefs(demandeStore);
const notificationStore = useNotificationStore();
const { addNotification } = notificationStore;
const { removeNotification } = notificationStore;
const {validerDemande,allForUser } = demandeStore;

const userStore = useUserStore();
//const { notifications, modules, fonctionnalites } = storeToRefs(userStore);
const { changeLoggedIn } = useUserStore();
const i18n = useI18n();
const dialog = ref(false);
let animate = true;

//  nom de l'application défin au niveau du fihcier .env
const appName = import.meta.env.VITE_APP_NAME;
const copyrightName = import.meta.env.VITE_APP_COPYRIGHT;
const inputForm = reactive({
  // id: "",
  // prenoms: "",
  // nom: "",
  // matricule: "",
  // email: "",
  // sexe: "",
  // code: "",
  // telephone: "",
  // anciennete: "",
  banque: "",
  codeBanque: "",
  codeAgence:"",
  numeroCompte: "",
  cleRib: "",
  // fonction: null,
  // etablissement: null,
  // libelleLong: "",
});
const valider = (id) => {
  validerDemande(id).then( () => {
    // addNotification({
    //     show: true,
    //     text:  i18n.t('valider'),
    //     color: 'blue'
    //   });
      dialog.value=false;
      allForUser();
  });
}
onMounted(()=>{
  changeLoggedIn();
  sessionStore.enCoursSession();
  demandeStore.allForUser();
  current().then( () => {
    // inputForm.id = dataDetails.value.userId,
    // inputForm.prenoms = dataDetails.value.prenoms,
    // inputForm.nom = dataDetails.value.nom,
    // inputForm.matricule = dataDetails.value.matricule,
    // inputForm.email = dataDetails.value.email,
    // inputForm.sexe = dataDetails.value.sexe,
    // inputForm.code = dataDetails.value.code,
    // inputForm.telephone = dataDetails.value.telephone,
    // inputForm.anciennete = dataDetails.value.anciennete,
    // inputForm.etablissement=dataDetails.value.etablissement?dataDetails.value.etablissement.id:null,
    // inputForm.libelleLong=dataDetails.value.etablissement?dataDetails.value.etablissement.libelleLong:null,
    inputForm.banque=dataDetails.value.banque,
    inputForm.codeBanque=dataDetails.value.codeBanque,
    inputForm.codeAgence=dataDetails.value.codeAgence,
    inputForm.numeroCompte=dataDetails.value.numeroCompte,
    inputForm.cleRib=dataDetails.value.cleRib 
  });

});
const redirectToEditDemande = (id) => {
  router.push({ name: 'edit', params: { id } });
};
const redirectToProfile= () => {
  router.push({ name: 'profileUser'});
};
</script>
<style>
.session-row {
  display: block;
  margin-bottom: 10px;
}

.session-row router-link {
  margin-left: 10px; 
}

.candidater-link {
  color: blue; 
  text-decoration: underline;
}
/* .demande-bloc {
    border: 1px solid #ccc;
    padding: 10px;
    margin-bottom: 10px;
    max-width: 300px;
  } */
  .demande-bloc p {
    border: 1px solid #ccc;
    padding: 8px; /* Ajustez le remplissage selon vos préférences */
  }
  .demandes{
    color: red; 
  }
  .actions-wrapper{
    text-align: center;
  }
  .vue-typer {
  color: white;
}
.vue-typer .custom.char{
  color: white; 
}


</style>