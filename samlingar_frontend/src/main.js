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
// import Lara from '@primevue/themes/lara'

import Ripple from 'primevue/ripple'

import ToastService from 'primevue/toastservice'

import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import VueAxios from 'vue-axios'
import moment from 'moment'

import JsonExcel from 'vue-json-excel3'

import VueLazyLoad from 'vue3-lazyload'

import { VueSpinnersPlugin } from 'vue3-spinners'


import AutoComplete from 'primevue/autocomplete'
import Breadcrumb from 'primevue/breadcrumb'
import Button from 'primevue/button'
import Card from 'primevue/card'
import Checkbox from 'primevue/checkbox'
import Column from 'primevue/column'
import DataTable from 'primevue/datatable'

import DataView from 'primevue/dataview'

// import DataView from 'primevue/dataview'

import Dialog from 'primevue/dialog'
import Divider from 'primevue/divider'
import IconField from 'primevue/iconfield'
import Fieldset from 'primevue/fieldset'

import Galleria from 'primevue/galleria'
import Image from 'primevue/image'

import InputGroup from 'primevue/inputgroup'
import InputIcon from 'primevue/inputicon'
import InputText from 'primevue/inputtext'
import FloatLabel from 'primevue/floatlabel'

import Menu from 'primevue/menu'
import MultiSelect from 'primevue/multiselect'
import Paginator from 'primevue/paginator'
import Panel from 'primevue/panel'

import ProgressSpinner from 'primevue/progressspinner'

import RadioButton from 'primevue/radiobutton'
import Select from 'primevue/select'
import SelectButton from 'primevue/selectbutton'

import Skeleton from 'primevue/skeleton'
import Tab from 'primevue/tab'
import Tabs from 'primevue/tabs'
import TabList from 'primevue/tablist'
import TabPanels from 'primevue/tabpanels'
import TabPanel from 'primevue/tabpanel'
import TieredMenu from 'primevue/tieredmenu'
import Toolbar from 'primevue/toolbar'



// const i18n = createI18n({
//   // something vue-i18n options here ...
// })

const app = createApp(App, {
  // onFreeTextSearch() {
  //   console.log('expand................')
  // }
})
app.use(router)
app.use(store)
app.use(i18n)
app.use(VueAxios, axios)
app.provide('axios', app.config.globalProperties.axios) // provide 'axios'
app.use(moment)
app.use(VueLazyLoad)
app.use(VueSpinnersPlugin)
app.use(ToastService)

app.use(PrimeVue, {
  theme: {
    pt: {
      panel: {
        header: {
          class: 'bg-primary',
          color: '#FF6E40'
        }
      }
    },
    preset: Aura,
    options: {
      prefix: 'p',
      darkModeSelector: 'system',
      cssLayer: false,
      primary: '#FF6E40',
      secondary: '#FF9E80',
      accent: '#26A69A',
      error: '#FF3D00',
      warning: '#EA80FC',
      info: '#29B6F6',
      success: '#64DD17'
    }
  },
  ripple: true
})

app.component('AutoComplete', AutoComplete)
app.component('Breadcrumb', Breadcrumb)
app.component('Button', Button)
app.component('Card', Card)
app.component('Checkbox', Checkbox)
app.component('Column', Column)
app.component('DataTable', DataTable)
app.component('DataView', DataView)





app.component('Dialog', Dialog)
app.component('Divider', Divider)
app.component('Fieldset', Fieldset),
app.component('IconField', IconField)
app.component('Galleria', Galleria)
app.component('Image', Image)
app.component('InputGroup', InputGroup)
app.component('InputIcon', InputIcon)
app.component('InputText', InputText)
app.component('FloatLabel', FloatLabel)
app.component('Menu', Menu)

app.component('MultiSelect', MultiSelect)

app.component('Paginator', Paginator)
app.component('Panel', Panel)


app.component('ProgressSpinner', ProgressSpinner)
app.component('RadioButton', RadioButton)
app.component('Select', Select)
app.component('SelectButton', SelectButton)
app.component('Skeleton', Skeleton)
app.component('Tab', Tab)
app.component('Tabs', Tabs)
app.component('TabList', TabList)
app.component('TabPanels', TabPanels)
app.component('TabPanel', TabPanel)

app.component('TieredMenu', TieredMenu)
app.component('Toolbar', Toolbar)

app.component('downloadExcel', JsonExcel)

app.directive('ripple', Ripple)
app.mount('#app')
