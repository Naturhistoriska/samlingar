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

  async apiScientificnameSearch(searchText, searchMode, fuzzySearch, start, rows) {
    searchText = searchText.replace(/&/g, '%26')

    const url = `${samlingApi}/scientificname?text=${searchText}&searchMode=${searchMode}&fuzzySearch=${fuzzySearch}&start=${start}&numPerPage=${rows}&sort=createdDate_dt desc`
    const response = await axios.get(url)
    return response.data
  }

  async apiSearch(params, start, numPerPage) {
    let url = `${samlingApi}/search?${params.toString()}&start=${start}&numPerPage=${numPerPage}&sort=createdDate_dt desc`

    // url += `&start=${start}&numPerPage=${numPerPage}&sort=createdDate_dt desc`
    const response = await axios.get(url)
    return response.data
  }

  async apiIdSearch(id) {
    const url = `${samlingApi}/id?id=${id}`
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
    let url = `${samlingApi}/download?${params.toString()}&numRows=${total}`

    const response = await axios.get(url)

    return response.data
  }

  async apiPreparaExport(params, total) {
    let url = `${samlingApi}/download?${params.toString()}&numRows=${total}&sort=createdDate_dt desc`

    const response = await axios.get(url)

    return response.data
  }

  async apiFreeTextSearch(searchText, start, numPerPage) {
    let url = `${samlingApi}/search?text=${searchText}&start=${start}&numPerPage=${numPerPage}&sort=createdDate_dt desc`

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

  async apiFilterSearch(
    filtCoordinates,
    filtImages,
    filtInSweden,
    filtTypeStatus,
    start,
    numPerPage
  ) {
    let url = `${samlingApi}/search?text=*`

    if (filtCoordinates) {
      url += '&lat_long=*'
    }

    if (filtImages) {
      url += '&associatedMedia=*'
    }

    if (filtTypeStatus) {
      url += '&typeStatus=*'
    }

    if (filtInSweden) {
      url += '&country=Sweden'
    }
    url += `&start=${start}&numPerPage=${numPerPage}&sort=createdDate_dt desc`

    const response = await axios.get(url)
    return response.data
  }

  async apiFilterCollectionsSearch(dataResource, start, numPerPage) {
    let url = `${samlingApi}/search?text=*&dataResourceName=${dataResource}&start=${start}&numPerPage=${numPerPage}&sort=createdDate_dt desc`

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

  // SBDI
  async autoComplete(searchText, start, rows) {
    const url = `${baseUrl}/search?q=data_hub_uid:${institutionId} AND text:${searchText}*&offset=${start}&pageSize=${rows}&sort=eventDate&dir=desc&fl=scientificName`
    // const url = `${baseUrl}/search?q=${searchText}*  AND data_hub_uid:${institutionId}&start=${start}&pageSize=${rows}&sort=eventDate&dir=desc`

    const response = await axios.get(url)

    return response.data
  }

  async quickSearch(searchText, start, rows) {
    // const url = `${baseUrl}/search?q=taxa:"${searchText}" AND data_hub_uid:${institutionId}&start=${start}&pageSize=${rows}&sort=eventDate&dir=desc&facets=collectionName,year,typeStatus&flimit=2000`
    const url = `${baseUrl}/search?q=taxa:"${searchText}" AND data_hub_uid:${institutionId}&offset=${start}&pageSize=${rows}&sort=eventDate&dir=desc&fl=${fiedList}&facets=${facetList}`
    const response = await axios.get(url)

    return response.data
  }

  async advanceSearch(
    scientificName,
    species_group,
    dataset,
    catalogNumber,
    startDate,
    endDate,
    isType,
    start,
    rows
  ) {
    let url = `${baseUrl}/search?q=data_hub_uid:${institutionId}`

    if (scientificName) {
      url = url + ` AND taxa:${scientificName}`
    }

    if (species_group) {
      url = url + ` AND species_group:${species_group}`
    }

    if (dataset) {
      url = url + ` AND collectionName:"${dataset}"`
    }

    if (catalogNumber) {
      url = url + ` AND catalogue_number:"${catalogNumber}"`
    }

    if (startDate && endDate) {
      url += ` AND occurrence_date:%5B${startDate}T00:00:00Z TO ${endDate}T00:00:00Z%5D`
    } else if (startDate && !endDate) {
      url += ` AND occurrence_date:%5B${startDate}T00:00:00Z TO *%5D`
    } else if (!startDate && endDate) {
      url += ` AND occurrence_date:%5B* TO ${endDate}T00:00:00Z%5D`
    }

    if (isType) {
      url += '&fq=typeStatus:*'
    }
    url += `&offset=${start}&pageSize=${rows}&fl=${fiedList}&facets=${facetList}`
    // url = url + `&start=${start}&pageSize=${rows}&facets=collectionName,year,lat_long,typeStatus`
    const response = await axios.get(url)

    return response.data
  }

  async conditionalSearch(searchText, start, collectionName, typeStatus, rows) {
    const url = this.buildUrl(searchText, start, collectionName, typeStatus, rows, false)
    const response = await axios.get(url)
    return response.data
  }

  async advanceConditionalSearch(
    scientificName,
    species_group,
    dataset,
    catalogNumber,
    startDate,
    endDate,
    isType,
    selectedCollection,
    selectedTypeStatus,
    start,
    rows
  ) {
    let url = `${baseUrl}/search?q=data_hub_uid:${institutionId}`

    if (scientificName) {
      url = url + ` AND taxa:${scientificName}`
    }

    if (species_group) {
      url = url + ` AND species_group:${species_group}`
    }

    if (dataset) {
      url = url + ` AND collectionName:"${dataset}"`
    }

    if (catalogNumber) {
      url = url + ` AND catalogue_number:"${catalogNumber}"`
    }

    if (startDate && endDate) {
      url = url + ` AND occurrence_date:%5B${startDate}T00:00:00Z TO ${endDate}T00:00:00Z%5D`
    } else if (startDate && !endDate) {
      url = url + ` AND occurrence_date:%5B${startDate}T00:00:00Z TO *%5D`
    } else if (!startDate && endDate) {
      url = url + ` AND occurrence_date:%5B* TO ${endDate}T00:00:00Z%5D`
    }
    if (isType) {
      if (selectedTypeStatus) {
        url += `&fq=typeStatus:${selectedTypeStatus}`
      } else {
        url = url + '&fq=typeStatus:*'
      }
    } else {
      if (selectedTypeStatus) {
        if (selectedTypeStatus === 'Not supplied') {
          url += '&fq=-typeStatus:*'
        } else {
          url += `&fq=typeStatus:${selectedTypeStatus}`
        }
      }
    }

    if (selectedCollection) {
      url += `&fq=collection_name:"${selectedCollection}"`
    }

    // if (selectedTypeStatus) {
    //   url += `&fq=typeStatus:"${selectedTypeStatus}"`
    // }
    url += `&offset=${start}&pageSize=${rows}&fl=${fiedList}&facets=${facetList}`
    const response = await axios.get(url)

    return response.data
  }

  async simpleCoordinatesSearch(searchText, selectedCollection, selectedType, coordinates, total) {
    let url = `${baseUrl}/search?q=data_hub_uid:${institutionId}`

    if (searchText) {
      url += ` AND taxa:"${searchText}"&offset=${0}&pageSize=${total}`
    }

    if (selectedCollection) {
      url += `&fq=collection_name:"${selectedCollection}"`
    }

    if (selectedType) {
      if (selectedType === 'Not supplied') {
        url += '&fq=-typeStatus:*'
      } else {
        url += `&fq=typeStatus:"${selectedType}"`
      }
    }

    if (coordinates) {
      url += `&fq=point-0.01:${coordinates}`
    }

    const response = await axios.get(url)
    return response.data
  }

  async export(
    scientificName,
    species_group,
    dataset,
    catalogNumber,
    startDate,
    endDate,
    isType,
    selectedCollection,
    selectedTypeStatus,
    total
  ) {
    let url = `${baseUrl}/search?q=data_hub_uid:${institutionId}`

    if (scientificName) {
      url = url + ` AND taxa:${scientificName}`
    }

    if (species_group) {
      url = url + ` AND species_group:${species_group}`
    }

    if (dataset) {
      url = url + ` AND collectionName:"${dataset}"`
    }

    if (catalogNumber) {
      url = url + ` AND catalogue_number:"${catalogNumber}"`
    }

    if (startDate && endDate) {
      url = url + ` AND occurrence_date:%5B${startDate}T00:00:00Z TO ${endDate}T00:00:00Z%5D`
    } else if (startDate && !endDate) {
      url = url + ` AND occurrence_date:%5B${startDate}T00:00:00Z TO *%5D`
    } else if (!startDate && endDate) {
      url = url + ` AND occurrence_date:%5B* TO ${endDate}T00:00:00Z%5D`
    }

    // if (isType) {
    //   url = url + '&fq=typeStatus:*'
    // }

    if (isType) {
      if (selectedTypeStatus) {
        url += `&fq=typeStatus:${selectedTypeStatus}`
      } else {
        url = url + '&fq=typeStatus:*'
      }
    } else {
      if (selectedTypeStatus) {
        if (selectedTypeStatus === 'Not supplied') {
          url += '&fq=-typeStatus:*'
        } else {
          url += `&fq=typeStatus:${selectedTypeStatus}`
        }
      }
    }

    if (selectedCollection) {
      url += `&fq=collection_name:"${selectedCollection}"`
    }

    // if (selectedTypeStatus) {
    //   url += `&fq=typeStatus:"${selectedTypeStatus}"`
    // }

    url += `&offset=0&pageSize=${total}&fl=${fiedList}`
    const response = await axios.get(url)

    return response.data
  }

  async advanceCoordinatesSearch(
    scientificName,
    species_group,
    dataset,
    catalogNumber,
    startDate,
    endDate,
    isType,
    selectedCollection,
    selectedTypeStatus,
    coordinates,
    total
  ) {
    let url = `${baseUrl}/search?q=data_hub_uid:${institutionId}`

    if (scientificName) {
      url = url + ` AND taxa:${scientificName}`
    }

    if (species_group) {
      url = url + ` AND species_group:${species_group}`
    }

    if (dataset) {
      url = url + ` AND collectionName:"${dataset}"`
    }

    if (catalogNumber) {
      url = url + ` AND catalogue_number:"${catalogNumber}"`
    }

    if (startDate && endDate) {
      url = url + ` AND occurrence_date:%5B${startDate}T00:00:00Z TO ${endDate}T00:00:00Z%5D`
    } else if (startDate && !endDate) {
      url = url + ` AND occurrence_date:%5B${startDate}T00:00:00Z TO *%5D`
    } else if (!startDate && endDate) {
      url = url + ` AND occurrence_date:%5B* TO ${endDate}T00:00:00Z%5D`
    }

    if (isType) {
      if (selectedTypeStatus) {
        url += `&fq=typeStatus:${selectedTypeStatus}`
      } else {
        url = url + '&fq=typeStatus:*'
      }
    } else {
      if (selectedTypeStatus) {
        if (selectedTypeStatus === 'Not supplied') {
          url += '&fq=-typeStatus:*'
        } else {
          url += `&fq=typeStatus:${selectedTypeStatus}`
        }
      }
    }
    // if (isType) {
    //   url = url + '&fq=typeStatus:*'
    // }

    if (selectedCollection) {
      url += `&fq=collection_name:"${selectedCollection}"`
    }

    // if (selectedTypeStatus) {
    //   url += `&fq=typeStatus:"${selectedTypeStatus}"`
    // }

    if (coordinates) {
      url += `&fq=point-0.01:${coordinates}`
    }

    url += `&offset=0&pageSize=${total}`
    const response = await axios.get(url)

    return response.data
  }

  async advancesearchMapData(
    scientificName,
    species_group,
    dataset,
    catalogNumber,
    startDate,
    endDate,
    isType,
    selectedCollection,
    selectedTypeStatus,
    coordinates
  ) {
    let url = `${baseUrl}/search?q=data_hub_uid:${institutionId}`

    if (scientificName) {
      url += ` AND taxa:${scientificName}`
    }

    if (species_group) {
      url += ` AND species_group:${species_group}`
    }

    if (dataset) {
      url += ` AND collectionName:"${dataset}"`
    }

    if (catalogNumber) {
      url += ` AND catalogue_number:"${catalogNumber}"`
    }

    if (startDate && endDate) {
      url += ` AND occurrence_date:%5B${startDate}T00:00:00Z TO ${endDate}T00:00:00Z%5D`
    } else if (startDate && !endDate) {
      url += ` AND occurrence_date:%5B${startDate}T00:00:00Z TO *%5D`
    } else if (!startDate && endDate) {
      url += ` AND occurrence_date:%5B* TO ${endDate}T00:00:00Z%5D`
    }

    // if (isType) {
    //   url += '&fq=typeStatus:*'
    // }
    if (isType) {
      if (selectedTypeStatus) {
        url += `&fq=typeStatus:${selectedTypeStatus}`
      } else {
        url = url + '&fq=typeStatus:*'
      }
    } else {
      if (selectedTypeStatus) {
        if (selectedTypeStatus === 'Not supplied') {
          url += '&fq=-typeStatus:*'
        } else {
          url += `&fq=typeStatus:${selectedTypeStatus}`
        }
      }
    }

    if (selectedCollection) {
      url += `&fq=collection_name:"${selectedCollection}"`
    }

    // if (selectedTypeStatus) {
    //   url += `&fq=typeStatus:"${selectedTypeStatus}"`
    // }

    if (coordinates) {
      url += `&fq=point-0.01:${coordinates}`
    }

    const response = await axios.get(url)

    return response.data
  }

  async simplesearchMapData(searchText, selectedCollection, selectedType, coordinates) {
    const url = this.buildLatLongUrl(searchText, selectedCollection, selectedType, coordinates)
    const response = await axios.get(url)
    return response.data
  }

  buildLatLongUrl(searchText, selectedCollection, selectedType, coordinates) {
    let url = `${baseUrl}/search?q=data_hub_uid:${institutionId}`

    if (searchText) {
      url += ` AND taxa:"${searchText}"`
    }

    if (selectedCollection) {
      url += `&fq=collection_name:"${selectedCollection}"`
    }

    if (typeStatus) {
      if (typeStatus === 'Not supplied') {
        url += '&fq=-typeStatus:*'
      } else {
        url += `&fq=typeStatus:${typeStatus}`
      }
    }
    // if (selectedType) {
    //   url += `&fq=typeStatus:"${selectedType}"`
    // }

    if (coordinates) {
      url += `&fq=point-0.01:${coordinates}`
    }
    return url
  }

  async uuidSearch(uuid) {
    const url = `${baseUrl}/${uuid}`
    const response = await axios.get(url)
    return response.data
  }

  // async searchFiltByCollection(searchText, start, collectionName, year, rows) {
  //   // const url = `${baseUrl}/search?q=taxa:"${searchText}" AND data_hub_uid:${institutionId}&start=${start}&pageSize=${rows}&fq=collection_name:"${collectionName}"&facets=collectionName,year`
  //   const url = this.buildUrl(searchText, start, collectionName, year, rows, false)
  //   const response = await axios.get(url)
  //   return response.data
  // }

  // async searchFiltByYear(searchText, start, collectionName, year, rows) {
  //   const url = this.buildUrl(searchText, start, collectionName, year, rows, false)
  //   const response = await axios.get(url)
  //   return response.data
  // }

  buildUrl(searchText, start, collectionName, typeStatus, rows, isMap) {
    let url = `${baseUrl}/search?q=data_hub_uid:${institutionId}`

    if (searchText) {
      url += ` AND taxa:"${searchText}"&offset=${start}&pageSize=${rows}`
    }

    if (collectionName) {
      url += `&fq=collection_name:"${collectionName}"`
    }

    if (typeStatus) {
      if (typeStatus === 'Not supplied') {
        url += '&fq=-typeStatus:*'
      } else {
        url += `&fq=typeStatus:${typeStatus}`
      }
    }

    url += `&fl=${fiedList}&facets=${facetList}`
    // if (year) {
    //   if (year === 'Not supplied') {
    //     url += '&fq=-year:*'
    //   } else {
    //     url += `&fq=year:${year}`
    //   }
    // }

    // if (isMap) {
    //   url += `&fq=lat_long:*`
    // }
    return url
  }
}
