<template>
  <v-list-item
    :key="result.uuid"
    :id="result.uuid"
    :class="resultcolor"
    @click="onclick()"
    tabindex="0"
  >
    <v-list-item-title @click="onclick()">{{ scientificName }} </v-list-item-title>
    <v-list-item-subtitle class="text--primary" @click="onclick()">
      {{ kingdom }}/ {{ phylum }}/ {{ classs }}/ {{ order }}/ {{ family }}/ {{ genus }}/
      {{ species }}
    </v-list-item-subtitle>
    <v-list-item-subtitle @click="onclick()">
      <span class="text-capitalize">{{ catalogNumber }}</span>
    </v-list-item-subtitle>
  </v-list-item>
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
          this.setSelectedResult(response.raw)
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
