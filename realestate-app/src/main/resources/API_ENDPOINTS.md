# API Endpoints - Агентство недвижимости

## Клиенты (/api/clients)
```
GET    /api/clients
GET    /api/clients/{id}
POST   /api/clients
PUT    /api/clients/{id}
DELETE /api/clients/{id}
GET    /api/clients/search/by-lastname
GET    /api/clients/search/by-phone
GET    /api/clients/search/by-email
GET    /api/clients/count
```

## Недвижимость (/api/properties)
```
GET    /api/properties
GET    /api/properties/{id}
POST   /api/properties
PUT    /api/properties/{id}
DELETE /api/properties/{id}
GET    /api/properties/search/by-price-range
GET    /api/properties/search/by-city/{cityId}
GET    /api/properties/search/by-type/{propertyTypeId}
GET    /api/properties/count
GET    /api/properties/with-details
GET    /api/properties/{id}/with-details
GET    /api/properties/for-table
GET    /api/properties/search/by-price-range-with-details
GET    /api/properties/search/by-city/{cityId}/with-details
GET    /api/properties/search/by-type/{propertyTypeId}/with-details
```

## Риелторы (/api/realtors)
```
GET    /api/realtors
GET    /api/realtors/{id}
POST   /api/realtors
PUT    /api/realtors/{id}
DELETE /api/realtors/{id}
GET    /api/realtors/search/by-lastname
GET    /api/realtors/search/by-experience
GET    /api/realtors/search/by-phone
GET    /api/realtors/search/by-email
GET    /api/realtors/count
```

## Сделки (/api/deals)
```
GET    /api/deals
GET    /api/deals/{id}
POST   /api/deals
PUT    /api/deals/{id}
DELETE /api/deals/{id}
GET    /api/deals/search/by-date
GET    /api/deals/search/by-date-range
GET    /api/deals/search/by-realtor/{realtorId}
GET    /api/deals/search/by-client/{clientId}
GET    /api/deals/search/by-property/{propertyId}
GET    /api/deals/search/by-type/{dealTypeId}
GET    /api/deals/search/by-cost-range
GET    /api/deals/total-amount
GET    /api/deals/count
GET    /api/deals/with-details
GET    /api/deals/{id}/with-details
GET    /api/deals/for-table
GET    /api/deals/search/by-date-with-details
GET    /api/deals/search/by-date-range-with-details
GET    /api/deals/search/by-realtor/{realtorId}/with-details
GET    /api/deals/search/by-client/{clientId}/with-details
```

## Типы сделок (/api/deal-types)
```
GET    /api/deal-types
GET    /api/deal-types/{id}
GET    /api/deal-types/search/by-name
```

## Типы недвижимости (/api/property-types)
```
GET    /api/property-types
GET    /api/property-types/{id}
GET    /api/property-types/search/by-name
```

## География (/api/geography)

### Страны
```
GET    /api/geography/countries
GET    /api/geography/countries/{id}
GET    /api/geography/countries/search/by-name
```

### Регионы
```
GET    /api/geography/regions
GET    /api/geography/regions/{id}
GET    /api/geography/regions/search/by-code
GET    /api/geography/countries/{countryId}/regions
GET    /api/geography/regions/search/by-name-and-country
GET    /api/geography/regions/with-details
GET    /api/geography/regions/{id}/with-details
GET    /api/geography/countries/{countryId}/regions/with-details
```

### Города
```
GET    /api/geography/cities
GET    /api/geography/cities/{id}
GET    /api/geography/regions/{regionId}/cities
GET    /api/geography/countries/{countryId}/cities
GET    /api/geography/cities/search/by-name-and-region
GET    /api/geography/cities/search/by-name-pattern
GET    /api/geography/cities/with-details
GET    /api/geography/cities/{id}/with-details
GET    /api/geography/regions/{regionId}/cities/with-details
```

### Районы
```
GET    /api/geography/districts
GET    /api/geography/districts/{id}
GET    /api/geography/cities/{cityId}/districts
GET    /api/geography/regions/{regionId}/districts
GET    /api/geography/countries/{countryId}/districts
GET    /api/geography/districts/search/by-name-and-city
GET    /api/geography/districts/with-details
GET    /api/geography/districts/{id}/with-details
GET    /api/geography/cities/{cityId}/districts/with-details
```

### Улицы
```
GET    /api/geography/streets
GET    /api/geography/streets/{id}
GET    /api/geography/cities/{cityId}/streets
GET    /api/geography/streets/search/by-name-and-city
GET    /api/geography/streets/search/by-name-pattern
GET    /api/geography/streets/with-details
GET    /api/geography/streets/{id}/with-details
GET    /api/geography/cities/{cityId}/streets/with-details
```

---

**Всего endpoints: 84**
- **CRUD операции**: 28
- **Поиск и фильтрация**: 32  
- **DTO с детальной информацией**: 24 