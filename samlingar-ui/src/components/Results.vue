<template>
  <v-container>
    <ResultDetail v-if="showDetail" />
    <ResultList v-else />
  </v-container>
</template>
<script>
import ResultDetail from '@/components/ResultDetail.vue'
import ResultList from '@/components/ResultList.vue'
import { mapGetters } from 'vuex'

export default {
  name: 'Results',
  components: {
    ResultList,
    ResultDetail
  },
  data: () => ({
    // showDetail: false
  }),

  computed: {
    ...mapGetters(['results', 'showDetail', 'totalRecords'])
  },

  methods: {}
}
</script>
<style scoped></style>
