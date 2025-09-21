import axios from 'axios'

const baseUrl = import.meta.env.VITE_SBDI_API
// const speciesSearchUrl = import.meta.env.VITE_SBDI_SPECIES_SEARCH
// const institutionId = import.meta.env.VITE_SUPPORTED_INSTITUTIONS
const resultsPerPage = 10

// const samlingApi = import.meta.env.VITE_SAMLINGAR_API_STAGE
const samlingApi = import.meta.env.VITE_SAMLINGAR_API_LOCAL

const fiedList =
  'id%2CcollectionName%2CcatalogNumber%2CscientificName%2C%20kingdom%2C%20phylum%2C%20class%2C%20order%2C%20family%2C%20genus%2C%20species'
const facetList = 'collectionName,point-0.01,typeStatus,class,family,genus&flimit=40000'

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

  async apiFreeTextSearch(searchText) {
    let url = `${samlingApi}/freeTextSearch?catchall=${searchText}`

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

  // async apiPreparaExport(params, total) {
  //   let url = `${samlingApi}/download?${params.toString()}&numRows=${total}`

  //   const response = await axios.get(url)

  //   return response.data
  // }

  async apiPreparaExport(params, total) {
    let url = `${samlingApi}/download?${params.toString()}&numRows=${total}&sort=catalogedDate desc`

    const response = await axios.get(url)

    return response.data
  }

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
}
