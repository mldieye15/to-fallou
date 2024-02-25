<template>
  <div class="d-flex flex-column align-center" >
      <v-row no-gutters class="bg-black w-100 px-12 pt-10 mb-3 pb-10 flex-sm-row">
        <v-col cols="8">
          <v-flex xs12 sm8 md4>
            <h1 class=" font-weight-bold mt-3">
              {{ $t('public.hero.titre') }}
            </h1>
            <p class="subheading font-weight-regular text-white-50">
              {{ $t('public.hero.soustitre') }},
              <br>{{ $t('public.hero.pasdecompte') }}
              <v-btn class="text-caption large" large>
                <router-link :to="{name:'register'}"><span class="text hidden-sm-and-down text-black"> {{ $t('public.nav.top.inscription') }}</span></router-link>
              </v-btn>
            </p>
          </v-flex>
        </v-col>
        <v-col cols="4">
          <v-flex xs12 sm8 md4>
            <v-card>
              <v-img
                :src="heroImage2"
                :lazy-src="heroImage"
                cover
                class="mx-auto"
                max-height="500"
                max-width="500"
              ></v-img>
            </v-card>
          </v-flex>
        </v-col>
      </v-row>
      <v-row no-gutters class="w-100 px-12 mb-3 pb-10 justify-between">
        <!-- <v-col cols="3">
            <v-card elevation="10">
              <v-img
                :src="dir"
                :lazy-src="heroImage"
                cover
                class="mx-auto"
                max-height="500"
                max-width="500"
              ></v-img>
            </v-card>
        </v-col> -->
        <v-col cols="10" justify-end class="ma-5">
          <div class="ml-10">
          <p class="mb-2"><strong>Responsabilités du Président de Jury :</strong></p>
          <ul>
            <li>Disposer d’un ordinateur portable ayant un système d’exploitation compatible avec le logiciel d’organisation de l’examen du baccalauréat</li>
            <li>Avoir des aptitudes liées à l’utilisation d’un ordinateur portable, d’un logiciel de traitement de texte, d’un tableur, l’impression de documents, et la transmission de documents par Internet</li>
            <li>Connaître le logiciel d’organisation de l’examen du baccalauréat</li>
            <li>Coopérer avec le superviseur des présidents de jury</li>
            <li>Respecter l’éthique et la déontologie de l’office du baccalauréat</li>
          </ul>
          <p class="mb-2 mt-3"><strong>Mission Pédagogique et Administrative :</strong></p>
          <ul>
            <li>Garantir l’impartialité des débats et l’égalité de traitement des candidats</li>
            <li>Réguler harmonieusement l’ensemble des documents pédagogique et administratif du jury</li>
            <li>Garantir qu’aucune erreur matérielle n’est commise dans les différentes opérations qui incombent au jury</li>
            <li>Garantir la souveraineté et le secret des délibérations</li>
          </ul>
        </div>
        </v-col>
      </v-row>
      <v-row no-gutters class=" w-100 px-12 mb-3 pb-10 flex-sm-row">

      </v-row>
  </div>
</template>

<script setup>
import heroImage from '@/assets/imgs/off1.jpeg'
import heroImage2 from '@/assets/imgs/off2.jpg'
import dir from '@/assets/imgs/dir3.jpg'
</script>
