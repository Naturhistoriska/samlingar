<template>
  <div id="container" class="container container--fluid">
    <v-row no-gutters>
      <v-col class="pl-3" cols="12">
        <AdvanceSearch v-if="isAdvanceSearch" />
        <Search v-else />
      </v-col>
    </v-row>
    <v-row align="start" no-gutters>
      <v-col class="pl-3" cols="3" align-self="start">
        <v-spacer></v-spacer>
      </v-col>
      <v-col class="pl-3" cols="2" align-self="start">
        <a
          href=""
          class="grey--text text--darken-3 body-2 pl-1"
          @click.prevent="advanceSeach()"
          id="backResultListLink"
          >{{ searchText }}</a
        >
      </v-col>
      <v-col class="pl-3" cols="6" align-self="start">
        <v-spacer></v-spacer>
      </v-col>
    </v-row>
    <div>
      <Results v-if="isShowResults" />
    </div>
  </div>
</template>

<script>
import AdvanceSearch from '@/components/AdvanceSearch.vue'
import Results from '@/components/Results.vue'
import Search from '@/components/Search.vue'

import { mapGetters, mapMutations } from 'vuex'
export default {
  name: 'Home',
  components: {
    AdvanceSearch,
    Results,
    Search
  },
  data: () => ({
    isAdvanceSearch: false
  }),

  computed: {
    ...mapGetters(['showResults']),

    isShowResults: function () {
      return this.showResults
    },
    searchText: function () {
      return this.isAdvanceSearch
        ? this.$t('search.closeAdvanceSearch')
        : this.$t('search.advanceSearch')
    }
  },
  watch: {},
  methods: {
    ...mapMutations(['setIsAdvanceSearch']),
    advanceSeach() {
      this.isAdvanceSearch = !this.isAdvanceSearch
      this.setIsAdvanceSearch(this.isAdvanceSearch)
    }
  }
}
</script>
<style scoped>
#container {
  position: relative;
  background: transparent;
  /* overflow: auto; */
  width: 100%;
  height: 100%; /* To make sure height is ok in Safari*/
}

#infoi {
  width: 100%;
  height: 100%;
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  z-index: 0;
}
</style>
