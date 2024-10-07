<template>
  <v-bottom-navigation bg-color="secondary" dense class="pl-3">
    <v-tabs v-model="tabs" align-tabs="title">
      <v-tab>
        <router-link to="/"> {{ $t('nav.home') }}</router-link>
      </v-tab>
      <v-tab>
        <router-link to="/about">{{ $t('nav.about') }}</router-link>
      </v-tab>
      <v-tab>
        <router-link to="/contact">{{ $t('nav.contactUs') }}</router-link>
      </v-tab>
    </v-tabs>

    <v-spacer></v-spacer>

    <locale-switcher />
  </v-bottom-navigation>
</template>
<script>
import LocaleSwitcher from '@/components/LocaleSwitcher.vue'
export default {
  components: {
    LocaleSwitcher
  },
  data() {
    return {
      tabs: null
    }
  },
  setup() {},

  watch: {
    $route(to) {
      const { name } = to
      this.routeName = name

      console.log('route name : ' + name + ' ... ' + this.tabs)

      switch (name) {
        case 'Home':
          this.tabs = 0
          break
        case 'About':
          this.tabs = 1
          break
        case 'Contact':
          this.tabs = 2
          break
        default:
          this.tabs = 0
      }
    }
  },
  computed: {},
  methods: {}
}
</script>

<style scoped></style>
