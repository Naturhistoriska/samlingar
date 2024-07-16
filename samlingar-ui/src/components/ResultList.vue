<template>
  <div>
    <legend>{{ $t('results.searchResults') }}</legend>
    <div id="resultList">
      <p>{{ $t('results.num_results', totalRecords) }}</p>
      <template v-for="(result, index) in results" :key="result.uuid">
        <Result v-bind:result="result" />
        <v-divider
          v-if="index + 1 < results.length"
          :key="`devider-${index}`"
          class="border-opacity-50"
          color="white"
        ></v-divider>
      </template>
    </div>
  </div>
</template>
<script>
import Result from '@/components/Result.vue'
import { mapGetters } from 'vuex'

export default {
  name: 'Results',
  components: {
    Result
  },
  data: () => ({}),

  computed: {
    ...mapGetters(['results', 'totalRecords'])
  },

  methods: {}
}
</script>
<style scoped></style>
