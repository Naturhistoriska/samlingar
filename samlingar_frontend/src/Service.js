import axios from 'axios'

// const samlingApi = import.meta.env.VITE_SAMLINGAR_API_PRODUCTION
// const samlingApi = import.meta.env.VITE_SAMLINGAR_API_STAGE
const samlingApi = import.meta.env.VITE_SAMLINGAR_API_LOCAL


export default class Service {
  async apiInitdata() {
    const url = `${samlingApi}/initialData`
    const response = await axios.get(url)
    return response.data
  }

  async apiChart(collectionCode, isYearChart) {
    const url = `${samlingApi}/chart?collectionCode=${collectionCode}&isYearChart=${isYearChart}`
    const response = await axios.get(url)
    return response.data
  }

  async apiAutoCompleteSearch(searchText, field) {
    const url = `${samlingApi}/autocomplete?text=${searchText}&field=${field}`

    const response = await axios.get(url)
    return response.data
  }

  async apiScientificnameSearch(searchText, searchMode) {
    searchText = searchText.replace(/&/g, '%26') // eplaces all instances of the ampersand character (&) in a string with %26

    const url = `${samlingApi}/scientificname?scientificname=${searchText}&searchMode=${searchMode}`
    const response = await axios.get(url)
    return response.data
  }

  async apiFilterSearch(params) {
    let url = `${samlingApi}/simpleSearch?${params.toString()}`

    const response = await axios.get(url)
    return response.data
  }

  async apiFreeTextSearch(params) {
    let url = `${samlingApi}/freeTextSearch?${params.toString()}`

    const response = await axios.get(url)
    return response.data
  }

  async apiIdSearch(id) {
    const url = `${samlingApi}/id?id=${id}`
    const response = await axios.get(url)
    return response.data
  }

  async apiSearch(params, start, numPerPage) {
    let url = `${samlingApi}/search?${params.toString()}&start=${start}&numPerPage=${numPerPage}`

    const response = await axios.get(url)
    return response.data
  }


  async apiGeoFetch(params, start, rows) {
    let url = `${samlingApi}/geojson?${params.toString()}&start=${start}&numPerPage=${rows}`
    const response = await axios.get(url)

    return response.data
  }

  async apiHeatmap(params, start, rows) {
    let url = `${samlingApi}/heatmap?${params.toString()}&start=${start}&numPerPage=${rows}`
    const response = await axios.get(url)

    return response.data
  }


  async apiPreparaExport(params, total) {
    let url = `${samlingApi}/download?${params.toString()}&numPerPage=${total}&sort=catalogedDate desc`

    const response = await axios.get(url)

    return response.data
  }

}
