import { createStore } from 'vuex'

export default createStore({
  state: {
    chartCode: null,
    clearSearch: false,
    collectionGroup: null, // group collection code
    collections: null,

    dataResource: null, // group: zoo, pal, bot, geo
    dates: null,
    dateFilter: 'date',

    endDate: null,
    endYear: null,
    filterCoordinates: false,
    filterImage: false,
    filterInSweden: false,
    filterNordicCountry: false,
    filterType: false,

    fullTextSearchMode: 'contains',
    geoJson: null,

    hasCoordinatesCount: 0,
    imageCount: 0,
    inSwedenCount: 0,
    isTypeCount: 0,

    pageNum: 0,
    rowsPerPage: 20,

    resetMapData: false,

    scientificName: null,
    selectedCollection: null, // selectedCollection Code
    selectedCollectionGroup: null, // selectedCollections name

    searchMode: 'contains',
    searchParams: null, // saved search params
    searchText: null,

    startDate: null,
    startYear: null,

    totalRecords: 0,
    totalGeoData: 0,

    //
    //
    //
    //
    //
    //
    //
    //
    //
    //

    clusterGroup: null,

    fields: [],

    isFuzzySearch: undefined,
    // isUrlPush: false,
    results: [],

    selectedRecord: null,

    totalCount: 0,

    //
    //
    //
    //
    //
    //

    dateRange: null,
    showDetail: false,
    botanyCollectionTotal: 0,
    catalogNumber: null,
    chartDataAlgae: [],
    chartDataBird: [],
    chartDataEntomology: [],
    chartDataEt: [],
    chartDataEv: [],
    chartDataFish: [],
    chartDataFungi: [],
    chartDataHerps: [],
    chartDataIsotope: [],
    chartDataMammals: [],
    chartDataMineralogy: [],
    chartDataMosses: [],
    chartDataNodules: [],
    chartDataPb: [],
    chartDataPz: [],
    chartDataSmtpObject: [],
    chartDataSmtpList: [],
    chartDataVp: [],

    classs: [],
    dataset: null,

    exportData: [],
    family: [],

    genus: [],

    geoCollectionTotal: 0,

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

    selectedDataset: null,
    selectedFamily: null,
    selectedResult: null,
    selectedType: null,
    selectedTypes: [],

    showResults: false,
    speciesGrouup: null,

    startRecord: 0,
    synonym: null,

    typeStatus: [],

    zooCollectionTotal: 0,
    year: null,
    yearData: []
  },

  getters: {
    chartCode(state) {
      return state.chartCode
    },
    clearSearch(state) {
      return state.clearSearch
    },
    collectionGroup(state) {
      return state.collectionGroup
    },
    collections(state) {
      return state.collections
    },
    dateFilter(state) {
      return state.dateFilter
    },
    endYear(state) {
      return state.endYear
    },
    fullTextSearchMode(state) {
      return state.fullTextSearchMode
    },
    filterNordicCountry(state) {
      return state.filterNordicCountry
    },

    geoJson(state) {
      return state.geoJson
    },

    pageNum(state) {
      return state.pageNum
    },

    rowsPerPage(state) {
      return state.rowsPerPage
    },

    resetMapData(state) {
      return state.resetMapData
    },

    selectedCollectionGroup(state) {
      return state.selectedCollectionGroup
    },
    startYear(state) {
      return state.startYear
    },

    totalGeoData(state) {
      return state.totalGeoData
    },

    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //

    clusterGroup(state) {
      return state.clusterGroup
    },
    dates(state) {
      return state.dates
    },
    dataResource(state) {
      return state.dataResource
    },
    endDate(state) {
      return state.endDate
    },
    fields(state) {
      return state.fields
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
    isFuzzySearch(state) {
      return state.isFuzzySearch
    },
    // isUrlPush(state) {
    //   return state.isUrlPush
    // },
    results(state) {
      return state.results
    },

    searchMode(state) {
      return state.searchMode
    },
    searchText(state) {
      return state.searchText
    },
    searchParams(state) {
      return state.searchParams
    },
    selectedRecord(state) {
      return state.selectedRecord
    },
    scientificName(state) {
      return state.scientificName
    },
    showDetail(state) {
      return state.showDetail
    },
    startDate(state) {
      return state.startDate
    },
    totalCount(state) {
      return state.totalCount
    },
    totalRecords(state) {
      return state.totalRecords
    },

    //
    //
    //
    //
    //
    //
    //
    //
    //
    //

    botanyCollectionTotal(state) {
      return state.botanyCollectionTotal
    },
    catalogNumber(state) {
      return state.catalogNumber
    },
    classs(state) {
      return state.classs
    },
    chartDataAlgae(state) {
      return state.chartDataAlgae
    },
    chartDataBird(state) {
      return state.chartDataBird
    },
    chartDataEntomology(state) {
      return state.chartDataEntomology
    },
    chartDataEt(state) {
      return state.chartDataEt
    },
    chartDataEv(state) {
      return state.chartDataEv
    },
    chartDataFish(state) {
      return state.chartDataFish
    },
    chartDataFungi(state) {
      return state.chartDataFungi
    },
    chartDataHerps(state) {
      return state.chartDataHerps
    },
    chartDataIsotope(state) {
      return state.chartDataIsotope
    },
    chartDataMammals(state) {
      return state.chartDataMammals
    },
    chartDataMineralogy(state) {
      return state.chartDataMineralogy
    },
    chartDataMosses(state) {
      return state.chartDataMosses
    },
    chartDataNodules(state) {
      return state.chartDataNodules
    },
    chartDataPb(state) {
      return state.chartDataPb
    },
    chartDataPz(state) {
      return state.chartDataPz
    },
    chartDataSmtpObject(state) {
      return state.chartDataSmtpObject
    },
    chartDataSmtpList(state) {
      return state.chartDataSmtpList
    },
    chartDataVp(state) {
      return state.chartDataVp
    },
    dataset(state) {
      return state.dataset
    },
    dateRange(state) {
      return state.dateRange
    },

    exportData(state) {
      return state.exportData
    },
    family(state) {
      return state.family
    },

    genus(state) {
      return state.genus
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

    showResults(state) {
      return state.showResults
    },
    speciesGrouup(state) {
      return state.speciesGrouup
    },

    startRecord(state) {
      return state.startRecord
    },
    synonym(state) {
      return state.synonym
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
    setChartCode: (state, payload) => (state.chartCode = payload),
    setClearSearch: (state, payload) => (state.clearSearch = payload),
    setCollections: (state, payload) => (state.collections = payload),
    setCollectionGroup: (state, payload) => (state.collectionGroup = payload),
    setDateFilter: (state, payload) => (state.dateFilter = payload),
    setEndYear: (state, payload) => (state.endYear = payload),
    setFilterCoordinates: (state, payload) => (state.filterCoordinates = payload),
    setFilterImage: (state, payload) => (state.filterImage = payload),
    setFilterInSweden: (state, payload) => (state.filterInSweden = payload),
    setFilterType: (state, payload) => (state.filterType = payload),
    setFilterNordicCountry: (state, payload) => (state.filterNordicCountry = payload),
    setFullTextSearchMode: (state, payload) => (state.fullTextSearchMode = payload),
    setGeoJson: (state, payload) => (state.geoJson = payload),
    setPageNum: (state, payload) => (state.pageNum = payload),
    setRowsPerPage: (state, payload) => (state.rowsPerPage = payload),
    setResetMapData: (state, payload) => (state.resetMapData = payload),
    setSelectedCollectionGroup: (state, payload) => (state.selectedCollectionGroup = payload),
    setStartYear: (state, payload) => (state.startYear = payload),
    setTotalGeoData: (state, payload) => (state.totalGeoData = payload),
    //
    //
    //
    //
    //
    //
    //
    //
    //

    setClusterGroup: (state, payload) => (state.clusterGroup = payload),
    setDates: (state, payload) => (state.dates = payload),
    setDataResource: (state, payload) => (state.dataResource = payload),
    setEndDate: (state, payload) => (state.endDate = payload),
    setFields: (state, payload) => (state.fields = payload),

    setIsFuzzySearch: (state, payload) => (state.isFuzzySearch = payload),
    // setIsUrlPush: (state, payload) => (state.isUrlPush = payload),
    setResults: (state, payload) => (state.results = payload),
    setScientificName: (state, payload) => (state.scientificName = payload),
    setSearchMode: (state, payload) => (state.searchMode = payload),
    setSearchParams: (state, payload) => (state.searchParams = payload),
    setSearchText: (state, payload) => (state.searchText = payload),
    setSelectedRecord: (state, payload) => (state.selectedRecord = payload),
    setShowDetail: (state, payload) => (state.showDetail = payload),
    setStartDate: (state, payload) => (state.startDate = payload),
    setTotalCount: (state, payload) => (state.totalCount = payload),
    setTotalRecords: (state, payload) => (state.totalRecords = payload),
    //
    //
    //
    //
    //

    setBotanyCollectionTotal: (state, payload) => (state.botanyCollectionTotal = payload),
    setCatalogNumber: (state, payload) => (state.catalogNumber = payload),
    setClasss: (state, payload) => (state.classs = payload),

    setChartDataAlgae: (state, payload) => (state.chartDataAlgae = payload),
    setChartDataEntomology: (state, payload) => (state.chartDataEntomology = payload),
    setChartDataEt: (state, payload) => (state.chartDataEt = payload),
    setChartDataEv: (state, payload) => (state.chartDataEv = payload),
    setChartDataMineralogy: (state, payload) => (state.chartDataMineralogy = payload),
    setChartDataFish: (state, payload) => (state.chartDataFish = payload),
    setChartDataFungi: (state, payload) => (state.chartDataFungi = payload),
    setChartDataHerps: (state, payload) => (state.chartDataHerps = payload),
    setChartDataIsotope: (state, payload) => (state.chartDataIsotope = payload),
    setChartDataMammals: (state, payload) => (state.chartDataMammals = payload),
    setChartDataMosses: (state, payload) => (state.chartDataMosses = payload),
    setChartDataNodules: (state, payload) => (state.chartDataNodules = payload),
    setChartDataPb: (state, payload) => (state.chartDataPb = payload),
    setChartDataPz: (state, payload) => (state.chartDataPz = payload),
    setChartDataSmtpObject: (state, payload) => (state.chartDataSmtpObject = payload),
    setChartDataSmtpList: (state, payload) => (state.chartDataSmtpList = payload),
    setChartDataBird: (state, payload) => (state.chartDataBird = payload),
    setChartDataVp: (state, payload) => (state.chartDataVp = payload),

    setDataset: (state, payload) => (state.dataset = payload),
    setDateRange: (state, payload) => (state.dateRange = payload),

    setExportData: (state, payload) => (state.exportData = payload),
    setImageCount: (state, payload) => (state.imageCount = payload),
    setFamily: (state, payload) => (state.family = payload),

    setGenus: (state, payload) => (state.genus = payload),

    setGeoCollectionTotal: (state, payload) => (state.geoCollectionTotal = payload),
    setInSwedenCount: (state, payload) => (state.inSwedenCount = payload),
    setIsTypeCount: (state, payload) => (state.isTypeCount = payload),
    setHasCoordinatesCount: (state, payload) => (state.hasCoordinatesCount = payload),

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

    setSelectedCollection: (state, payload) => (state.selectedCollection = payload),
    setSelectedDataset: (state, payload) => (state.selectedDataset = payload),
    setSelectedFamily: (state, payload) => (state.selectedFamily = payload),
    setSelectedResult: (state, payload) => (state.selectedResult = payload),
    setSelectedType: (state, payload) => (state.selectedType = payload),
    setSelectedTypes: (state, payload) => (state.selectedTypes = payload),

    setShowResults: (state, payload) => (state.showResults = payload),
    setSpeciesGroup: (state, payload) => (state.speciesGrouup = payload),

    setStartRecord: (state, payload) => (state.startRecord = payload),
    setSynonym: (state, payload) => (state.synonym = payload),

    setTypeStatus: (state, payload) => (state.typeStatus = payload),
    setZooCollectionTotal: (state, payload) => (state.zooCollectionTotal = payload),
    setYear: (state, payload) => (state.year = payload),
    setYearData: (state, payload) => (state.yearData = payload)
  }
})
