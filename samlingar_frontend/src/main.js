import "primeicons/primeicons.css"
import './assets/main.css'
import '/node_modules/primeflex/primeflex.css'

import 'leaflet/dist/leaflet.css'
import 'leaflet.markercluster/dist/MarkerCluster.css'
import 'leaflet.markercluster/dist/MarkerCluster.Default.css'
// import 'leaflet.markercluster'

// import 'vue-map-ui/dist/normalize.css'
// import 'vue-map-ui/dist/style.css'
// import 'vue-map-ui/dist/theme-all.css'

import { createApp } from 'vue'

import i18n from './i18n'

import PrimeVue from 'primevue/config'

import Aura from '@primevue/themes/aura'

import Ripple from 'primevue/ripple'

import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import VueAxios from 'vue-axios'




import AutoComplete from 'primevue/autocomplete'
import Button from 'primevue/button'
import Card from 'primevue/card'
import Checkbox from 'primevue/checkbox'
import Column from 'primevue/column'
import DataTable from 'primevue/datatable'
import Divider from 'primevue/divider'
import IconField from 'primevue/iconfield'
import InputGroup from 'primevue/inputgroup'
import InputIcon from 'primevue/inputicon'
import InputText from 'primevue/inputtext'
import FloatLabel from 'primevue/floatlabel'
import Menu from 'primevue/menu'
import Paginator from 'primevue/paginator'
import RadioButton from 'primevue/radiobutton'
import Select from 'primevue/select'
import Tab from 'primevue/tab'
import Tabs from 'primevue/tabs'
import TabList from 'primevue/tablist'
import TieredMenu from 'primevue/tieredmenu'



// const i18n = createI18n({
//   // something vue-i18n options here ...
// })

const app = createApp(App)
app.use(router)
app.use(store)
app.use(i18n)
app.use(VueAxios, axios)
app.provide('axios', app.config.globalProperties.axios) // provide 'axios'


app.use(PrimeVue, {
  theme: {
    preset: Aura,
    options: {
      prefix: 'p',
      darkModeSelector: 'system',
      cssLayer: false
    }
  },
  ripple: true
})

app.component('AutoComplete', AutoComplete)
app.component('Button', Button)
app.component('Card', Card)
app.component('Checkbox', Checkbox)
app.component('Column', Column)
app.component('DataTable', DataTable)
app.component('Divider', Divider)
app.component('IconField', IconField)
app.component('InputGroup', InputGroup)
app.component('InputIcon', InputIcon)
app.component('InputText', InputText)
app.component('FloatLabel', FloatLabel)
app.component('Menu', Menu)
app.component('Paginator', Paginator)
app.component('RadioButton', RadioButton)
app.component('Select', Select)
app.component('Tab', Tab)
app.component('Tabs', Tabs)
app.component('TabList', TabList)
app.component('TieredMenu', TieredMenu)

app.directive('ripple', Ripple)
app.mount('#app')
