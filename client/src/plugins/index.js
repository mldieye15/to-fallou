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
//import AxiosPlugin from 'vue-axios-cors';


export function registerPlugins (app) {
  loadFonts()
  app.component('EasyDataTable', Vue3EasyDataTable);
  //app.component('AxiosPlugin', AxiosPlugin);
  app
    .use(vuetify)
    .use(pinia)
    .use(router)
    .use(i18n)
    //.use(AxiosPlugin)
}
