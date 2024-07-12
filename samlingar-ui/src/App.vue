<template>
  <v-app>
    <LocaleSwitcher @close-dialog="closeDialog" v-bind:dialogStatus="dialogStatus" />
    <v-app-bar app clipped-left color="primary" dark density="prominent">
      <div class="d-flex align-center pl-3 pt-4">
        <img src="/nrm-logo.png" />
      </div>
      <div class="title-text pt-16">{{ $t('title') }}</div>

      <v-spacer></v-spacer>
      <Search class="pt-15 pr-10" />
      <template v-slot:extension>
        <Navigater />
      </template>
    </v-app-bar>

    <v-main style="width: 1000px">
      <router-view />
    </v-main>
    <v-footer color="lighten-4" fixed app padless>
      <Foot @language-click="languageClick" />
      <!-- <v-row no-gutters>
        <v-col class="pl-3" cols="10">
          <span class="grey--text text--darken-3">
            {{ $t('footer.nrm') }} - {{ new Date().getFullYear() }}
          </span>
          <v-btn color="darken-3" text @click="onContackLinkclick()">
            <v-icon left dark> mdi-email-outline </v-icon>{{ $t('common.contactus') }}
          </v-btn>
        </v-col>

        <v-col class="pl-3 text-right" cols="2">
          <v-btn color="darken-3" text @click="onLanguageClick()">
            <v-icon left dark> mdi-web </v-icon>{{ $t('footer.currentLanguage') }}
          </v-btn>
        </v-col>
      </v-row> -->
    </v-footer>
  </v-app>
</template>

<script>
import Search from '@/components/Search.vue'
import Foot from '@/components/Footer.vue'
import LocaleSwitcher from '@/components/LocaleSwitcher.vue'
import Navigater from '@/components/Nav.vue'

export default {
  name: 'App',
  components: {
    Foot,
    LocaleSwitcher,
    Navigater,
    Search
  },
  data: () => ({
    dialogStatus: false
  }),

  // mounted() {},
  methods: {
    onContackLinkclick() {
      const locale = this.$i18n.locale
      const pushUrl = locale === 'sv' ? `/kontakt` : `/contact`
      this.$router.push(pushUrl)
    },
    languageClick() {
      this.currentLocale = this.$i18n.locale
      this.dialogStatus = true
    },
    closeDialog(selectedLocale) {
      console.log(selectedLocale)
      // const locale = selectedLocale

      // const query = this.$route.query

      this.dialogStatus = false
      // if (this.$i18n.locale !== locale) {
      //   const to = this.$router.resolve({
      //     params: { locale }
      //   })
      //   return Trans.changeLocale(locale).then(() => {
      //     this.$router.push(to.location)
      //   })
      // }
    }
  }
}
</script>

<style scoped>
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
