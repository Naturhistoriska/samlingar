<template>
  <div>
    <Results v-if="isShowResults" />
    <start-page v-else />
  </div>
</template>
<script>
import Results from '../components/Results.vue'
import StartPage from '../components/StartPage.vue'

import { mapGetters } from 'vuex'

export default {
  name: 'Home',
  components: {
    Results,
    StartPage
  },
  data: () => ({
    isAdvanceSearch: false
  }),

  computed: {
    ...mapGetters(['showResults']),

    isShowResults: function () {
      return this.showResults
    }
  },
  watch: {},
  methods: {}
}
</script>
