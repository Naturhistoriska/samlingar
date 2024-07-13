<template>
  <v-list-item :key="result.uuid" :id="result.uuid" @keypress.prevent="onclick()" tabindex="0">
    <!-- <template> -->

    <v-list-item-title>{{ scientificName }} </v-list-item-title>
    <v-list-item-subtitle class="text--primary">
      {{ kingdom }}/ {{ phylum }}/ {{ classs }}/ {{ order }}/ {{ family }}/ {{ genus }}/
      {{ species }}
    </v-list-item-subtitle>
    <v-list-item-subtitle>
      <span class="text-capitalize">{{ catalogNumber }}</span>
    </v-list-item-subtitle>
    <!-- </template> -->
  </v-list-item>
</template>
<script>
// import { mapGetters, mapMutations } from 'vuex'
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
    onclick() {}
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
