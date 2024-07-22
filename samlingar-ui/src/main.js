import './assets/main.css'
// import Vue from 'vue'

// Plugins
import { registerPlugins } from '@/plugins'
import { createApp } from 'vue'
// import { createVuetify } from 'vuetify'
// import * as components from 'vuetify/components'
// import * as directives from 'vuetify/directives'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import VueAxios from 'vue-axios'
import i18n from './i18n'





// import axios2 from 'axios'
// Vue.use(VueAxios, { $myHttp: axios, axios2: axios2 })


// Vuetify
// import 'vuetify/styles'
// import { createVuetify } from 'vuetify'
// import * as components from 'vuetify/components'
// import * as directives from 'vuetify/directives'


// const vuetify = createVuetify({
  // components,
  // directives
// })

const app = createApp(App)
app.use(VueAxios, axios)
app.provide('axios', app.config.globalProperties.axios) // provide 'axios'
app.use(router)
app.use(store)
app.use(i18n)

// app.use(vuetify)

// app.use(vuetify)
registerPlugins(app)

app.mount('#app')


