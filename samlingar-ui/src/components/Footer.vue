<template>
  <v-row no-gutters>
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
  </v-row>
</template>
<script>
export default {
  mounted() {},
  methods: {
    onContackLinkclick() {
      // const router = useRouter()
      const locale = this.$i18n.locale
      const pushUrl = locale === 'sv' ? '/kontakt' : '/contact'
      // this.pushUrl(pushUrl)
      // router.push(pushUrl)
      this.$router.push(pushUrl)
    },
    onLanguageClick() {
      console.log('clicked..')
      this.$emit('language-click')
    }
  }
}
</script>
<style scoped>
.v-application .show-btn-hover.transparent--text,
.v-application .show-btn.transparent--text {
  color: rgba(0, 0, 0, 0.54) !important;
}
</style>
