import { createStore } from 'vuex'

export default createStore({
  state: {
    catalogNumber: null,
    collectons: [],
    dataset: null,
    endDate: null,
    isAdvanceSearch: false,
    isType: false,
    latLong: null,
    mapRecords: [],
    numPerPage: 10,
    occurrenceYears: null,
    results: [],
    scientificName: null,
    searchText: null,
    selectedCollection: '',
    selectedResult: null,
    showDetail: false,
    showResults: false,
    speciesGrouup: null,
    startDate: null,
    startRecord: 0,
    totalRecords: 0,
    year: null
  },

  getters: {
    catalogNumber(state) {
      return state.catalogNumber
    },
    collectons(state) {
      return state.collectons
    },
    dataset(state) {
      return state.dataset
    },
    endDate(state) {
      return state.endDate
    },
    isAdvanceSearch(state) {
      return state.isAdvanceSearch
    },
    isType(state) {
      return state.isType
    },
    latLong(state) {
      return state.latLong
    },
    mapRecords(state) {
      return state.mapRecords
    },
    numPerPage(state) {
      return state.numPerPage
    },
    occurrenceYears(state) {
      return state.occurrenceYears
    },
    results(state) {
      return state.results
    },
    scientificName(state) {
      return state.scientificName
    },
    searchText(state) {
      return state.searchText
    },
    selectedCollection(state) {
      return state.selectedCollection
    },
    selectedResult(state) {
      return state.selectedResult
    },
    showDetail(state) {
      return state.showDetail
    },
    showResults(state) {
      return state.showResults
    },
    speciesGrouup(state) {
      return state.speciesGrouup
    },
    startDate(state) {
      return state.startDate
    },
    startRecord(state) {
      return state.startRecord
    },
    totalRecords(state) {
      return state.totalRecords
    },
    year(state) {
      return state.year
    }
  },

  mutations: {
    setCatalogNumber: (state, payload) => (state.catalogNumber = payload),
    setCollections: (state, payload) => (state.collectons = payload),
    setDataset: (state, payload) => (state.dataset = payload),
    setEndDate: (state, payload) => (state.endDate = payload),
    setIsAdvanceSearch: (state, payload) => (state.isAdvanceSearch = payload),
    setIsType: (state, payload) => (state.isType = payload),
    setLatLong: (state, payload) => (state.latLong = payload),
    setMapRecords: (state, payload) => (state.mapRecords = payload),
    setNumPerPage: (state, payload) => (state.numPerPage = payload),
    setOccurrenceYears: (state, payload) => (state.occurrenceYears = payload),
    setResults: (state, payload) => (state.results = payload),
    setScientificName: (state, payload) => (state.scientificName = payload),
    setSearchText: (state, payload) => (state.searchText = payload),
    setSelectedCollection: (state, payload) => (state.selectedCollection = payload),
    setSelectedResult: (state, payload) => (state.selectedResult = payload),
    setShowDetail: (state, payload) => (state.showDetail = payload),
    setShowResults: (state, payload) => (state.showResults = payload),
    setSpeciesGroup: (state, payload) => (state.speciesGrouup = payload),
    setStartDate: (state, payload) => (state.startDate = payload),
    setStartRecord: (state, payload) => (state.startRecord = payload),
    setTotalRecords: (state, payload) => (state.totalRecords = payload),
    setYear: (state, payload) => (state.year = payload)
  }
})
