<template>
  <div class="card" @click="onclick()" style="cursor: pointer">
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
        {{ higherClassification }}
        <!-- {{ kingdom }}/ {{ phylum }}/ {{ classs }}/ {{ order }}/ {{ family }}/ {{ genus }} -->
      </div>
      <div class="col-3" no-gutters>Collection name</div>
      <div class="col-9">
        {{ collectionName }}
      </div>
      <div class="col-3" no-gutters>Locality</div>
      <div class="col-9">{{ locality }} {{ country }}</div>
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
      // kingdom: null,
      // phylum: null,
      // classs: null,
      country: null,
      higherClassification: null,
      locality: null,
      // order: null,
      // family: false,
      // genus: null,
      // species: null,
      scientificName: null,
      catalogNumber: null,
      collectionName: null
    }
  },
  mounted() {
    const { catalogNumber, collectionName, locality, country, higherTx, txFullName } = this.result
    this.catalogNumber = catalogNumber
    this.higherClassification = higherTx
    this.locality = locality
    this.scientificName = txFullName
    this.country = country
    this.collectionName = collectionName
    // const {
    //   raw_catalogNumber,
    //   collectionName,
    //   kingdom,
    //   phylum,
    //   classs,
    //   order,
    //   family,
    //   genus,
    //   species,
    //   scientificName
    // } = this.result
    // this.catalogNumber = raw_catalogNumber
    // this.collectionName = collectionName
    // this.kingdom = kingdom
    // this.phylum = phylum
    // this.classs = classs
    // this.order = order
    // this.family = family
    // this.genus = genus
    // this.species = species
    // this.scientificName = scientificName
  },
  computed: {
    resultcolor: function () {
      return 'unselected'
      // return this.isActive ? 'selected' : 'unselected'
    }
  },
  methods: {
    ...mapMutations(['setShowResults', 'setShowDetail', 'setSelectedResult']),
    onclick() {
      this.setSelectedResult(this.result)
      this.setShowDetail(true)
      // service
      //   .uuidSearch(this.result.uuid)
      //   .then((response) => {
      //     this.setSelectedResult(response)
      //     this.setShowDetail(true)
      //   })
      //   .catch()
      //   .finally(() => {})
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
