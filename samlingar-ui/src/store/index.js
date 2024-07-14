import { createStore } from 'vuex'

export default createStore({
  state: {
    showResults: false,
    results: [],
    totalRecords: 0
  },

  getters: {
    showResults(state) {
      return state.showResults
    },
    results(state) {
      return state.results
    },
    totalRecords(state) {
      return state.totalRecords
    }
  },

  mutations: {
    setShowResults: (state, payload) => (state.showResults = payload),
    setResults: (state, payload) => (state.results = payload),
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
