import axios from 'axios'

const baseUrl = import.meta.env.VITE_SBDI_API
// const speciesSearchUrl = import.meta.env.VITE_SBDI_SPECIES_SEARCH
const institutionId = import.meta.env.VITE_SUPPORTED_INSTITUTIONS
const resultsPerPage = 10

export default class Service {
  async autoComplete(searchText, start, rows) {
    const url = `${baseUrl}/search?q=data_hub_uid:${institutionId} AND text:${searchText}*&start=${start}&pageSize=${rows}&sort=eventDate&dir=desc&fl=scientificName`
    // const url = `${baseUrl}/search?q=${searchText}*  AND data_hub_uid:${institutionId}&start=${start}&pageSize=${rows}&sort=eventDate&dir=desc`

    const response = await axios.get(url)

    return response.data
  }

  async quickSearch(searchText, start, rows) {
    // const url = `${baseUrl}/search?q=taxa:"${searchText}" AND data_hub_uid:${institutionId}&start=${start}&pageSize=${rows}&sort=eventDate&dir=desc&facets=collectionName,year,typeStatus&flimit=2000`
    const url = `${baseUrl}/search?q=taxa:"${searchText}" AND data_hub_uid:${institutionId}&start=${start}&pageSize=${rows}&sort=eventDate&dir=desc&fl=id%2CcollectionName%2CcatalogNumber%2CscientificName%2C%20kingdom%2C%20phylum%2C%20classs%2C%20order%2C%20family%2C%20genus%2C%20species&facets=collectionName,point-0.01,typeStatus&flimit=3500`
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
      url = url + '&fq=typeStatus:*'
    }

    if (selectedCollection) {
      url += `&fq=collection_name:"${selectedCollection}"`
    }

    if (selectedTypeStatus) {
      url += `&fq=typeStatus:"${selectedTypeStatus}"`
    }

    // if (selectedYear) {
    // if (selectedYear === 'Not supplied') {
    // url += '&fq=-year:*'
    // } else {
    // url += `&fq=year:${selectedYear}`
    // }
    // }

    url += `&start=${start}&pageSize=${rows}&fl=id%2CcollectionName%2CcatalogNumber%2CscientificName%2C%20kingdom%2C%20phylum%2C%20classs%2C%20order%2C%20family%2C%20genus%2C%20species&facets=collectionName,point-0.01,typeStatus&flimit=3500`
    const response = await axios.get(url)

    return response.data
  }

  async simpleCoordinatesSearch(searchText, selectedCollection, selectedType, coordinates, total) {
    let url = `${baseUrl}/search?q=data_hub_uid:${institutionId}`

    if (searchText) {
      url += ` AND taxa:"${searchText}"&start=${0}&pageSize=${total}`
    }

    if (selectedCollection) {
      url += `&fq=collection_name:"${selectedCollection}"`
    }

    if (selectedType) {
      url += `&fq=typeStatus:"${typeStatus}"`
    }

    if (coordinates) {
      url += `&fq=point-0.01:${coordinates}`
    }

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

    if (isType) {
      url += '&fq=typeStatus:*'
    }

    if (selectedCollection) {
      url += `&fq=collection_name:"${selectedCollection}"`
    }

    if (selectedTypeStatus) {
      url += `&fq=typeStatus:"${selectedTypeStatus}"`
    }

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

    if (selectedType) {
      url += `&fq=typeStatus:"${selectedType}"`
    }

    if (coordinates) {
      url += `&fq=point-0.01:${coordinates}`
    }
    return url
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
    url =
      url +
      `&start=${start}&pageSize=${rows}&fl=id%2CcollectionName%2CcatalogNumber%2CscientificName%2C%20kingdom%2C%20phylum%2C%20classs%2C%20order%2C%20family%2C%20genus%2C%20species&facets=collectionName,point-0.01,typeStatus&flimit=3500`
    // url = url + `&start=${start}&pageSize=${rows}&facets=collectionName,year,lat_long,typeStatus`
    const response = await axios.get(url)

    return response.data
  }

  async uuidSearch(uuid) {
    const url = `${baseUrl}/${uuid}`
    const response = await axios.get(url)
    return response.data
  }

  async searchFiltByCollection(searchText, start, collectionName, year, rows) {
    // const url = `${baseUrl}/search?q=taxa:"${searchText}" AND data_hub_uid:${institutionId}&start=${start}&pageSize=${rows}&fq=collection_name:"${collectionName}"&facets=collectionName,year`
    const url = this.buildUrl(searchText, start, collectionName, year, rows, false)
    const response = await axios.get(url)
    return response.data
  }

  async searchFiltByYear(searchText, start, collectionName, year, rows) {
    const url = this.buildUrl(searchText, start, collectionName, year, rows, false)
    const response = await axios.get(url)
    return response.data
  }

  buildUrl(searchText, start, collectionName, typeStatus, rows, isMap) {
    let url = `${baseUrl}/search?q=data_hub_uid:${institutionId}`

    if (searchText) {
      url += ` AND taxa:"${searchText}"&start=${start}&pageSize=${rows}`
    }

    if (collectionName) {
      url += `&fq=collection_name:"${collectionName}"`
    }

    if (typeStatus) {
      url += `&fq=typeStatus:"${typeStatus}"`
    }

    url +=
      '&fl=id%2CcollectionName%2CcatalogNumber%2CscientificName%2C%20kingdom%2C%20phylum%2C%20classs%2C%20order%2C%20family%2C%20genus%2C%20species&&facets=collectionName,point-0.01,typeStatus&flimit=3500'
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
