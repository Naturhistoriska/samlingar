<template>
  <v-card class="w-100">
    <v-layout class="w-100">
      <v-app-bar app color="primary" dark density="prominent">
        <div class="d-flex align-center pl-3 pt-4">
          <img src="/nrm-logo.png" />
        </div>
        <div class="title-text pt-16">{{ $t('title') }}</div>

        <v-spacer></v-spacer>

        <Search class="pt-15 pr-10" />
      </v-app-bar>
      <v-app-bar density="compact" class="pt-4">
        <template v-slot:extension> <Navigater /></template>
      </v-app-bar>

      <v-navigation-drawer
        v-model="drawer"
        :location="$vuetify.display.mobile ? 'bottom' : undefined"
        temporary
      >
        <v-list :items="items"></v-list>
      </v-navigation-drawer>

      <v-main
        class="d-flex align-center justify-center"
        style="min-width: 1200px; min-height: 1000px"
      >
        <router-view />
      </v-main>

      <v-footer color="lighten-4" fixed app padless>
        <Foot />
      </v-footer>
    </v-layout>
  </v-card>
</template>

<script>
import Search from '@/components/Search.vue'
import Foot from '@/components/Footer.vue'
// import LocaleSwitcher from '@/components/LocaleSwitcher.vue'
import Navigater from '@/components/Nav.vue'

import { useI18n } from 'vue-i18n'
import Tr from '@/i18n/translation'
export default {
  name: 'App',
  components: {
    Foot,
    Navigater,
    Search
  },
  data: () => ({
    dialogStatus: false,
    selectedLocale: 'en',
    drawer: true,
    items: [
      {
        title: 'Foo',
        value: 'foo'
      },
      {
        title: 'Bar',
        value: 'bar'
      },
      {
        title: 'Fizz',
        value: 'fizz'
      },
      {
        title: 'Buzz',
        value: 'buzz'
      }
    ]
  }),
  setup() {
    const { t, locale } = useI18n()
    const supportedLocales = Tr.supportedLocales
    const switchLanguage = async (event) => {
      const newLocale = event.target.value
      await Tr.switchLanguage(newLocale)
    }

    return { t, locale, supportedLocales, switchLanguage }
  },

  watch: {
    selectedLocale: function () {
      console.log('locale ... ' + this.selectedLocale)
      Tr.switchLanguage(this.selectedLocale)
    }
  },

  mounted() {
    // this.window.addEventListener('resize', this.handleResize)
    // window.onpopstate = (event) => {
    //   console.log('event : ' + event)
    //   this.hasHistory
    //   // this.$router.push({
    //   //   path: '/contact'
    //   // })
    // }
    // function() {
    //   this.window.onpopstate = (event) => {
    //     console.log('event : ' + event)
    //     this.$router.push({
    //       path: '/contact'
    //     })
    //   }
    // }
  },
  computed: {},
  // mounted() {},
  methods: {
    onContackLinkclick() {
      const locale = this.$i18n.locale
      const pushUrl = locale === 'sv' ? `/kontakt` : `/contact`
      this.$router.push(pushUrl)
    }
  }
}
</script>

<style scoped>
.v-toolbar__content,
.v-toolbar__extension {
  max-height: 30px !important;
}
/* #app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
} */
/* #header {
  line-height: 3;
} */

/* .logo { */
/* display: block; */
/* margin: 0 auto 2rem; */
/* } */

.title-text {
  /* padding-top: 60px; */
  width: 500px;
  font-size: 18px;
}

.search-input {
  padding-top: 40px;
}

/* @media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
} */
</style>
