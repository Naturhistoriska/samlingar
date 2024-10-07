<template>
  <div class="card" @click="onclick()">
    <div class="grid">
      <div class="col-3" no-gutters>Scientific Name</div>
      <div class="col-9">
        {{ scientificName }}
      </div>
      <div class="col-3" no-gutters>Catalognumber</div>
      <div class="col-9">
        {{ catalogNumber }}
      </div>
      <div class="col-3" no-gutters>High classification</div>
      <div class="col-9">
        {{ kingdom }}/ {{ phylum }}/ {{ classs }}/ {{ order }}/ {{ family }}/ {{ genus }}/
        {{ species }}
      </div>
    </div>
  </div>
</template>
<script>
// import { mapGetters, mapMutations } from 'vuex'
import Service from '../Service'
const service = new Service()
import { mapMutations } from 'vuex'

export default {
  name: 'Result',
  props: ['result'],
  data() {
    return {
      kingdom: null,
      phylum: null,
      classs: null,
      order: null,
      family: false,
      genus: null,
      species: null,
      scientificName: null,
      catalogNumber: null
    }
  },
  mounted() {
    const {
      raw_catalogNumber,
      kingdom,
      phylum,
      classs,
      order,
      family,
      genus,
      species,
      scientificName
    } = this.result
    this.catalogNumber = raw_catalogNumber
    this.kingdom = kingdom
    this.phylum = phylum
    this.classs = classs
    this.order = order
    this.family = family
    this.genus = genus
    this.species = species
    this.scientificName = scientificName
  },
  computed: {
    resultcolor: function () {
      return 'unselected'
      // return this.isActive ? 'selected' : 'unselected'
    }
    // isActive: function () {
    //   return this.result.properties.gid === this.selectedResultId
    // }
    // isActive: function () {
    //   return this.result.uuid === this.selectedResultId
    // },
    // isHovered: function () {
    //   return this.result.properties.gid === this.hoveredResultId
    // },
    // resultcolor: function () {
    //   return this.isActive ? 'selected' : 'unselected'
    // },
    // iconcolor: function () {
    //   return this.isActive || this.isHovered ? 'primary' : 'grey lighten-1'
    // },
    // namecolor: function () {
    //   return this.isActive || this.isHovered ? 'blue--text text--darken-2' : ''
    // }
  },
  methods: {
    ...mapMutations(['setShowResults', 'setShowDetail', 'setSelectedResult']),
    onclick() {
      console.log('selected ... ' + this.result.uuid)
      service
        .uuidSearch(this.result.uuid)
        .then((response) => {
          console.log('response .... ' + response.raw.location.locality)
          this.setSelectedResult(response)
          this.setShowDetail(true)
        })
        .catch()
        .finally(() => {})
    }
  }
}
</script>

<style scoped>
.selected {
  background: #e6f2ff;
}

.unselected {
  background: transparent;
}

.unselected:hover {
  color: #1976d2 !important;
}
</style>
