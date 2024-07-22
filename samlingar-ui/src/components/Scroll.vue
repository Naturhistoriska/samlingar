<template>
  <div style="display: flex; height: 600px; background: gray; overflow-y: auto">
    <v-virtual-scroll :items="results">
      <template v-slot:default="{ item, index }">
        <Result v-bind:result="item" />
        <v-divider
          v-if="index + 1 < results.length"
          :key="`devider-${index}`"
          class="border-opacity-50"
          color="white"
        ></v-divider>
      </template>
    </v-virtual-scroll>
  </div>
</template>

<script>
import Result from '@/components/Result.vue'
import { mapGetters } from 'vuex'
import { onMounted } from 'vue'
export default {
  name: 'Scroll',
  components: { Result },
  data() {
    return {
      theResults: []
    }
  },
  setup() {
    onMounted(() => {
      window.addEventListener('scroll', handleScroll)
    })

    const handleScroll = (e) => {
      console.log('e ' + e)
    }
  },

  computed: {
    ...mapGetters(['results', 'totalRecords']),
    resultToDisplay() {
      return this.results
    }

    // results() {
    //   return this.results
    // }
    // results() {
    //   console.log('what ... ' + this.totalRecords)
    //   return [...Array(10000).keys()].map((i) => {
    //     if (this.totalRecords !== 0) {
    //       const result = this.results === null ? null : { ...this.results[i % this.totalRecords] }
    //       result.uuid = `${result.uuid} #${i}`
    //       return result
    //     }
    //     return null
    //   })
    // }
  },
  methods: {},
  mounted() {}
}
</script>
<style scoped>
.result {
  height: 80px;
}
.scrollable {
  overflow: hidden;
  overflow-y: scroll;
  height: calc(100vh - 20px);
}
#app {
  height: 300px;
}
</style>
