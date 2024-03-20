/**
 * plugins/index.js
 *
 * Automatically included in `./src/main.js`
 */

// Plugins
import { loadFonts } from './webfontloader'
import i18n from './i18n'
import vuetify from './vuetify'
import pinia from '../store'
import router from '../router'
import Vue3EasyDataTable from 'vue3-easy-data-table';
import 'vue3-easy-data-table/dist/style.css';
import VueJsonExcel from 'vue-json-excel3';
import JsonExcel from "vue-json-excel3";
// import VueGoodTablePlugin from 'vue-good-table';
// import 'vue-good-table/dist/vue-good-table.css'
import Toast from "vue-toastification";
// Import the CSS or use your own!
import "vue-toastification/dist/index.css";
import VueGoodTablePlugin from 'vue-good-table-next';
import 'vue-good-table-next/dist/vue-good-table-next.css'
import AOS from 'aos';
import 'aos/dist/aos.css';
import  Typewriter from 'vue3-typer';
import "vue3-typer/dist/vue-typer.css"

//import AxiosPlugin from 'vue-axios-cors';


export function registerPlugins (app) {
  loadFonts()
  AOS.init();
  app.component('EasyDataTable', Vue3EasyDataTable);
  app.component('VueJsonExcel', VueJsonExcel);
  app.component("downloadExcel", JsonExcel);
  //app.component('AxiosPlugin', AxiosPlugin);
  app
    .use(vuetify)
    .use(pinia)
    .use(router)
    .use(i18n)
    .use(VueGoodTablePlugin)
    .use(Toast)
    .use(Typewriter)
    //.use(AxiosPlugin)
}
