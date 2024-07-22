<template>
  <div id="resultList">
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
