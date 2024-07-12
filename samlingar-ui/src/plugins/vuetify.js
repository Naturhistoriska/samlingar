import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'

import { createVuetify } from 'vuetify'

const vuetify = createVuetify({
  ssr: true,

  theme: {
    defaultTheme: 'dark',
    themes: {
      dark: {
        dark: false,
        variables: {}, // ✅ this property is required to avoid Vuetify crash

        colors: {
          //green: '#00ff00' // cannot ue primary color names here, so use a custom color name (such as 'greenish')
          greenish: '#03DAC5',
          primary: '#144836',
          secondary: '#2b5a4a',
          error: '#CF6679',
          info: '#2196F3',
          success: '#4CAF50',
          warning: '#FB8C00'
        }
      },
      light: {
        primary: '#1976D2',
        secondary: '#BBDEFB',
        accent: '#FFD600'
      },
      darkBlue: {
        dark: true,
        variables: {}, // ✅ this property is required to avoid Vuetify crash

        colors: {
          //green: '#00ff00' // cannot use primary color names here, so use a custom color name (such as 'greenish')
          greenish: '#03DAC5',

          // Workaround: Custom colors seem to erase default colors, so we need to include the default colors (of `light` or `dark` theme)
          background: '#111928',
          surface: '#212121',
          primary: '#00ff00',
          'primary-darken-1': '#3700B3',
          secondary: '#03DAC5',
          'secondary-darken-1': '#03DAC5',
          error: '#CF6679',
          info: '#2196F3',
          success: '#4CAF50',
          warning: '#FB8C00'
        }
      }
    }
  }
})

export default vuetify
