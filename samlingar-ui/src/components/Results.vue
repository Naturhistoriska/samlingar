<template>
  <v-container>
    <!-- <Scroll /> -->
    <legend>{{ $t('results.searchResults') }}</legend>
    <p>{{ $t('results.num_results', totalRecords) }}</p>
    <ResultDetail v-if="showDetail" />
    <div v-else>
      <Pagination />

      <ResultList />
    </div>
  </v-container>
</template>
<script>
import Pagination from '@/components/Pagination.vue'
import ResultDetail from '@/components/ResultDetail.vue'
import ResultList from '@/components/ResultList.vue'
// import Scroll from '@/components/Scroll.vue'
import { mapGetters } from 'vuex'

export default {
  name: 'Results',
  components: {
    Pagination,
    ResultList,
    ResultDetail
    // Scroll
  },
  data: () => ({
    // page: 1
  }),

  computed: {
    ...mapGetters(['showDetail', 'totalRecords'])
  },

  watch: {
    // page: function (newPage) {
    //   console.log('page ... ' + newPage)
    // }
  },

  methods: {}
}
</script>
<style scoped>
.v-pagination__first,
.v-pagination__prev,
.v-pagination__item,
.v-pagination__item--is-active,
.v-pagination__next,
.v-pagination__last {
  color: rgb(var(--v-theme-primary));
}
</style>
