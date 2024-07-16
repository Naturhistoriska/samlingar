import { createStore } from 'vuex'

export default createStore({
  state: {
    showDetail: false,
    showResults: false,
    selectedResult: null,
    results: [],
    totalRecords: 0
  },

  getters: {
    showDetail(state) {
      return state.showDetail
    },
    showResults(state) {
      return state.showResults
    },
    results(state) {
      return state.results
    },
    selectedResult(state) {
      return state.selectedResult
    },
    totalRecords(state) {
      return state.totalRecords
    }
  },

  mutations: {
    setShowDetail: (state, payload) => (state.showDetail = payload),
    setShowResults: (state, payload) => (state.showResults = payload),
    setResults: (state, payload) => (state.results = payload),
    setSelectedResult: (state, payload) => (state.selectedResult = payload),
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
