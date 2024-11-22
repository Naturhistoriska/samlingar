import { createStore } from 'vuex'

export default createStore({
  state: {
    botanyCollectionTotal: 0,
    catalogNumber: null,
    collectons: [],
    collectionGroup: null,
    classs: [],
    dataset: null,
    dateRange: null,
    endDate: null,
    exportData: [],
    family: [],
    filterImage: false,
    filterType: false,
    filterInSweden: false,
    filterCoordinates: false,
    genus: [],
    geoData: [],
    geoCollectionTotal: 0,
    imageCount: 0,
    inSwedenCount: 0,
    isTypeCount: 0,
    hasCoordinatesCount: 0,
    isAdvanceSearch: false,
    isType: false,
    latLong: [],
    mapRecords: [],
    monthData: [],
    numPerPage: 10,
    occurrenceYears: [],
    openGalleria: false,
    resetPaging: false,
    paleaCollectionTotal: 0,
    results: [],
    scientificName: null,
    searchText: null,
    selectedCollection: null,
    selectedDataset: null,
    selectedFamily: null,
    selectedResult: null,
    selectedType: null,
    selectedTypes: [],
    showDetail: false,
    showResults: false,
    speciesGrouup: null,
    startDate: null,
    startRecord: 0,
    synonym: null,
    totalCount: 0,
    typeStatus: [],
    totalRecords: 0,
    totalGeoData: 0,
    zooCollectionTotal: 0,
    year: null,
    yearData: []
  },

  getters: {
    botanyCollectionTotal(state) {
      return state.botanyCollectionTotal
    },
    catalogNumber(state) {
      return state.catalogNumber
    },
    classs(state) {
      return state.classs
    },
    collectons(state) {
      return state.collectons
    },
    collectionGroup(state) {
      return state.collectionGroup
    },
    dataset(state) {
      return state.dataset
    },
    dateRange(state) {
      return state.dateRange
    },
    endDate(state) {
      return state.endDate
    },
    exportData(state) {
      return state.exportData
    },
    family(state) {
      return state.family
    },
    filterImage(state) {
      return state.filterImage
    },
    filterType(state) {
      return state.filterType
    },
    filterInSweden(state) {
      return state.filterInSweden
    },
    filterCoordinates(state) {
      return state.filterCoordinates
    },
    genus(state) {
      return state.genus
    },
    geoData(state) {
      return state.geoData
    },
    geoCollectionTotal(state) {
      return state.geoCollectionTotal
    },
    imageCount(state) {
      return state.imageCount
    },
    inSwedenCount(state) {
      return state.inSwedenCount
    },
    isTypeCount(state) {
      return state.isTypeCount
    },
    hasCoordinatesCount(state) {
      return state.hasCoordinatesCount
    },
    totalCount(state) {
      return state.totalCount
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
    monthData(state) {
      return state.monthData
    },
    numPerPage(state) {
      return state.numPerPage
    },
    occurrenceYears(state) {
      return state.occurrenceYears
    },
    openGalleria(state) {
      return state.openGalleria
    },
    paleaCollectionTotal(state) {
      return state.paleaCollectionTotal
    },
    resetPaging(state) {
      return state.resetPaging
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
    selectedDataset(state) {
      return state.selectedDataset
    },
    selectedFamily(state) {
      return state.selectedFamily
    },
    selectedResult(state) {
      return state.selectedResult
    },
    selectedType(state) {
      return state.selectedType
    },
    selectedTypes(state) {
      return state.selectedTypes
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
    synonym(state) {
      return state.synonym
    },
    totalGeoData(state) {
      return state.totalGeoData
    },
    totalRecords(state) {
      return state.totalRecords
    },
    typeStatus(state) {
      return state.typeStatus
    },
    zooCollectionTotal(state) {
      return state.zooCollectionTotal
    },
    year(state) {
      return state.year
    },
    yearData(state) {
      return state.yearData
    }
  },

  mutations: {
    setBotanyCollectionTotal: (state, payload) => (state.botanyCollectionTotal = payload),
    setCatalogNumber: (state, payload) => (state.catalogNumber = payload),
    setClasss: (state, payload) => (state.classs = payload),
    setCollections: (state, payload) => (state.collectons = payload),
    setCollectionGroup: (state, payload) => (state.collectionGroup = payload),
    setDataset: (state, payload) => (state.dataset = payload),
    setDateRange: (state, payload) => (state.dateRange = payload),
    setEndDate: (state, payload) => (state.endDate = payload),
    setExportData: (state, payload) => (state.exportData = payload),
    setImageCount: (state, payload) => (state.imageCount = payload),
    setFamily: (state, payload) => (state.family = payload),
    setFilterImage: (state, payload) => (state.filterImage = payload),
    setFilterType: (state, payload) => (state.filterType = payload),
    setFilterInSweden: (state, payload) => (state.filterInSweden = payload),
    setFilterCoordinates: (state, payload) => (state.filterCoordinates = payload),
    setGenus: (state, payload) => (state.genus = payload),
    setGeoData: (state, payload) => (state.geoData = payload),
    setGeoCollectionTotal: (state, payload) => (state.geoCollectionTotal = payload),
    setInSwedenCount: (state, payload) => (state.inSwedenCount = payload),
    setIsTypeCount: (state, payload) => (state.isTypeCount = payload),
    setHasCoordinatesCount: (state, payload) => (state.hasCoordinatesCount = payload),
    setTotalCount: (state, payload) => (state.totalCount = payload),
    setIsAdvanceSearch: (state, payload) => (state.isAdvanceSearch = payload),
    setIsType: (state, payload) => (state.isType = payload),
    setLatLong: (state, payload) => (state.latLong = payload),
    setMapRecords: (state, payload) => (state.mapRecords = payload),
    setMonthData: (state, payload) => (state.monthData = payload),
    setNumPerPage: (state, payload) => (state.numPerPage = payload),
    setOccurrenceYears: (state, payload) => (state.occurrenceYears = payload),
    setOpenGalleria: (state, payload) => (state.openGalleria = payload),
    setPaleaCollectionTotal: (state, payload) => (state.paleaCollectionTotal = payload),
    setResetPaging: (state, payload) => (state.resetPaging = payload),
    setResults: (state, payload) => (state.results = payload),
    setScientificName: (state, payload) => (state.scientificName = payload),
    setSearchText: (state, payload) => (state.searchText = payload),
    setSelectedCollection: (state, payload) => (state.selectedCollection = payload),
    setSelectedDataset: (state, payload) => (state.selectedDataset = payload),
    setSelectedFamily: (state, payload) => (state.selectedFamily = payload),
    setSelectedResult: (state, payload) => (state.selectedResult = payload),
    setSelectedType: (state, payload) => (state.selectedType = payload),
    setSelectedTypes: (state, payload) => (state.selectedTypes = payload),
    setShowDetail: (state, payload) => (state.showDetail = payload),
    setShowResults: (state, payload) => (state.showResults = payload),
    setSpeciesGroup: (state, payload) => (state.speciesGrouup = payload),
    setStartDate: (state, payload) => (state.startDate = payload),
    setStartRecord: (state, payload) => (state.startRecord = payload),
    setSynonym: (state, payload) => (state.synonym = payload),
    setTotalGeoData: (state, payload) => (state.totalGeoData = payload),
    setTotalRecords: (state, payload) => (state.totalRecords = payload),
    setTypeStatus: (state, payload) => (state.typeStatus = payload),
    setZooCollectionTotal: (state, payload) => (state.zooCollectionTotal = payload),
    setYear: (state, payload) => (state.year = payload),
    setYearData: (state, payload) => (state.yearData = payload)
  }
})
