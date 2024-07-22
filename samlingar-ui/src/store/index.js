import { createStore } from 'vuex'

export default createStore({
  state: {
    catalogNumber: null,
    isAdvanceSearch: false,
    results: [],
    scientificName: null,
    searchText: null,
    selectedResult: null,
    showDetail: false,
    showResults: false,
    speciesGrouup: null,
    totalRecords: 0
  },

  getters: {
    catalogNumber(state) {
      return state.catalogNumber
    },
    isAdvanceSearch(state) {
      return state.isAdvanceSearch
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
    totalRecords(state) {
      return state.totalRecords
    }
  },

  mutations: {
    setCatalogNumber: (state, payload) => (state.catalogNumber = payload),
    setIsAdvanceSearch: (state, payload) => (state.isAdvanceSearch = payload),
    setResults: (state, payload) => (state.results = payload),
    setScientificName: (state, payload) => (state.scientificName = payload),
    setSearchText: (state, payload) => (state.searchText = payload),
    setSelectedResult: (state, payload) => (state.selectedResult = payload),
    setShowDetail: (state, payload) => (state.showDetail = payload),
    setShowResults: (state, payload) => (state.showResults = payload),
    setSpeciesGroup: (state, payload) => (state.speciesGrouup = payload),
    setTotalRecords: (state, payload) => (state.totalRecords = payload)
  }

  // mutations: {
  // addCartItem(state, item) {
  // item.quantity = 1
  // state.cart.push(item)
  // },
  // getResults(state) {
  // return state.results
  // },
  // removeCartItem(state, item) {
  // state.cart = state.cart.filter((cartItem) => {
  // return cartItem.id != item.id
  // })
  // }
  // }
})
