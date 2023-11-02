/**
 * main.js
 *
 * Bootstraps Vuetify and other plugins then mounts the App`
 */

// Components
import App from './App.vue'
import axios from '@/plugins/axios.js'

// Composables
import { createApp } from 'vue'

// Plugins
import { registerPlugins } from '@/plugins'

const app = createApp(App)

registerPlugins(app)
app.config.globalProperties.http = axios;
app.config.globalProperties.$axios = { ...axios }
app.provide('http', axios);
app.provide('$axios', axios);


app.mount('#app')
