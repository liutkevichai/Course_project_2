## Клиенты (`/api/clients`)
GET    /api/clients
- Возвращает: `array` (of `Client` objects)

GET    /api/clients/{id}
- Параметры: `id` (number) - ID клиента
- Возвращает: `object` (Client)

POST   /api/clients
- Тело запроса: `object` (Client)
- Возвращает: `object` - `{id: number, message: string}`

PUT    /api/clients/{id}
- Параметры: `id` (number) - ID клиента
- Тело запроса: `object` - Поля для обновления
- Возвращает: `object` - `{message: string}`

DELETE /api/clients/{id}
- Параметры: `id` (number) - ID клиента
- Возвращает: `object` - `{message: string}`

GET    /api/clients/search/by-lastname
- Параметры: `lastName` (string) - Фамилия
- Возвращает: `array` (of `Client` objects)

GET    /api/clients/search/by-phone
- Параметры: `phone` (string) - Номер телефона
- Возвращает: `object` (Client)

GET    /api/clients/search/by-email
- Параметры: `email` (string) - Email
- Возвращает: `object` (Client)

GET    /api/clients/count
- Возвращает: `object` - `{count: number}`

## Недвижимость (`/api/properties`)
GET    /api/properties
- Возвращает: `array` (of `Property` objects)

GET    /api/properties/{id}
- Параметры: `id` (number) - ID объекта
- Возвращает: `object` (Property)

POST   /api/properties
- Тело запроса: `object` (Property)
- Возвращает: `object` - `{id: number, message: string}`

PUT    /api/properties/{id}
- Параметры: `id` (number) - ID объекта
- Тело запроса: `object` - Поля для обновления
- Возвращает: `object` - `{message: string}`

DELETE /api/properties/{id}
- Параметры: `id` (number) - ID объекта
- Возвращает: `object` - `{message: string}`

GET    /api/properties/search/by-price-range
- Параметры: `minPrice` (number), `maxPrice` (number)
- Возвращает: `array` (of `Property` objects)

GET    /api/properties/search/by-city/{cityId}
- Параметры: `cityId` (number) - ID города
- Возвращает: `array` (of `Property` objects)

GET    /api/properties/search/by-type/{propertyTypeId}
- Параметры: `propertyTypeId` (number) - ID типа объекта
- Возвращает: `array` (of `Property` objects)

GET    /api/properties/count
- Возвращает: `object` - `{totalProperties: number}`

GET    /api/properties/with-details
- Возвращает: `array` (of `PropertyWithDetailsDto` objects)
- Структура `PropertyWithDetailsDto`:
  - `propertyId` (number)
  - `area` (number)
  - `cost` (number)
  - `description` (string)
  - `postalCode` (string)
  - `houseNumber` (string)
  - `houseLetter` (string, optional)
  - `buildingNumber` (string, optional)
  - `apartmentNumber` (string, optional)
  - `countryId` (number)
  - `countryName` (string)
  - `regionId` (number)
  - `regionName` (string)
  - `regionCode` (string)
  - `cityId` (number)
  - `cityName` (string)
  - `districtId` (number)
  - `districtName` (string)
  - `streetId` (number)
  - `streetName` (string)
  - `propertyTypeId` (number)
  - `propertyTypeName` (string)

GET    /api/properties/{id}/with-details
- Параметры: `id` (number) - ID объекта
- Возвращает: `object` (PropertyWithDetailsDto)

GET    /api/properties/for-table
- Возвращает: `array` (of `PropertyTableDto` objects)
- Структура `PropertyTableDto`:
  - `propertyId` (number)
  - `propertyTypeName` (string)
  - `area` (number)
  - `cost` (number)
  - `shortDescription` (string)
  - `cityName` (string)
  - `districtName` (string)
  - `streetName` (string)
  - `houseNumber` (string)
  - `apartmentNumber` (string, optional)
  - `houseLetter` (string, optional)
  - `buildingNumber` (string, optional)

GET    /api/properties/search/by-price-range-with-details
- Параметры: `minPrice` (number), `maxPrice` (number)
- Возвращает: `array` (of `PropertyWithDetailsDto` objects)

GET    /api/properties/search/by-city/{cityId}/with-details
- Параметры: `cityId` (number) - ID города
- Возвращает: `array` (of `PropertyWithDetailsDto` objects)

GET    /api/properties/search/by-type/{propertyTypeId}/with-details
- Параметры: `propertyTypeId` (number) - ID типа объекта
- Возвращает: `array` (of `PropertyWithDetailsDto` objects)

## Риелторы (`/api/realtors`)
GET    /api/realtors
- Возвращает: `array` (of `Realtor` objects)

GET    /api/realtors/{id}
- Параметры: `id` (number) - ID риелтора
- Возвращает: `object` (Realtor)

POST   /api/realtors
- Тело запроса: `object` (Realtor)
- Возвращает: `object` - `{id: number, message: string}`

PUT    /api/realtors/{id}
- Параметры: `id` (number) - ID риелтора
- Тело запроса: `object` - Поля для обновления
- Возвращает: `object` - `{message: string}`

DELETE /api/realtors/{id}
- Параметры: `id` (number) - ID риелтора
- Возвращает: `object` - `{message: string}`

GET    /api/realtors/search/by-lastname
- Параметры: `lastName` (string) - Фамилия
- Возвращает: `array` (of `Realtor` objects)

GET    /api/realtors/search/by-experience
- Параметры: `minExperience` (number) - Минимальный опыт
- Возвращает: `array` (of `Realtor` objects)

GET    /api/realtors/search/by-phone
- Параметры: `phone` (string) - Номер телефона
- Возвращает: `object` (Realtor)

GET    /api/realtors/search/by-email
- Параметры: `email` (string) - Email
- Возвращает: `object` (Realtor)

GET    /api/realtors/count
- Возвращает: `object` - `{count: number}`

## Сделки (`/api/deals`)
GET    /api/deals
- Возвращает: `array` (of `Deal` objects)

GET    /api/deals/{id}
- Параметры: `id` (number) - ID сделки
- Возвращает: `object` (Deal)

POST   /api/deals
- Тело запроса: `object` (Deal)
- Возвращает: `object` - `{id: number, message: string}`

PUT    /api/deals/{id}
- Параметры: `id` (number) - ID сделки
- Тело запроса: `object` - Поля для обновления
- Возвращает: `object` - `{message: string}`

DELETE /api/deals/{id}
- Параметры: `id` (number) - ID сделки
- Возвращает: `object` - `{message: string}`

GET    /api/deals/search/by-date
- Параметры: `date` (string, 'YYYY-MM-DD') - Дата
- Возвращает: `array` (of `Deal` objects)

GET    /api/deals/search/by-date-range
- Параметры: `startDate` (string, 'YYYY-MM-DD'), `endDate` (string, 'YYYY-MM-DD')
- Возвращает: `array` (of `Deal` objects)

GET    /api/deals/search/by-realtor/{realtorId}
- Параметры: `realtorId` (number) - ID риелтора
- Возвращает: `array` (of `Deal` objects)

GET    /api/deals/search/by-client/{clientId}
- Параметры: `clientId` (number) - ID клиента
- Возвращает: `array` (of `Deal` objects)

GET    /api/deals/search/by-property/{propertyId}
- Параметры: `propertyId` (number) - ID объекта
- Возвращает: `array` (of `Deal` objects)

GET    /api/deals/search/by-type/{dealTypeId}
- Параметры: `dealTypeId` (number) - ID типа сделки
- Возвращает: `array` (of `Deal` objects)

GET    /api/deals/search/by-cost-range
- Параметры: `minCost` (number), `maxCost` (number)
- Возвращает: `array` (of `Deal` objects)

GET    /api/deals/total-amount
- Возвращает: `object` - `{totalAmount: number}`

GET    /api/deals/count
- Возвращает: `object` - `{totalDeals: number}`

GET    /api/deals/with-details
- Возвращает: `array` (of `DealWithDetailsDto` objects)
- Структура `DealWithDetailsDto`:
  - `dealId` (number)
  - `dealDate` (string, 'YYYY-MM-DD')
  - `dealCost` (number)
  - `clientId` (number)
  - `clientFirstName` (string)
  - `clientLastName` (string)
  - `clientMiddleName` (string, optional)
  - `clientPhone` (string)
  - `clientEmail` (string, optional)
  - `realtorId` (number)
  - `realtorFirstName` (string)
  - `realtorLastName` (string)
  - `realtorMiddleName` (string, optional)
  - `realtorPhone` (string)
  - `realtorEmail` (string, optional)
  - `realtorExperience` (number)
  - `propertyId` (number)
  - `propertyArea` (number)
  - `propertyCost` (number)
  - `propertyDescription` (string)
  - `propertyPostalCode` (string)
  - `propertyHouseNumber` (string)
  - `propertyHouseLetter` (string, optional)
  - `propertyBuildingNumber` (string, optional)
  - `propertyApartmentNumber` (string, optional)
  - `countryName` (string)
  - `regionName` (string)
  - `cityName` (string)
  - `districtName` (string)
  - `streetName` (string)
  - `propertyTypeName` (string)
  - `dealTypeName` (string)

GET    /api/deals/{id}/with-details
- Параметры: `id` (number) - ID сделки
- Возвращает: `object` (DealWithDetailsDto)

GET    /api/deals/for-table
- Возвращает: `array` (of `DealTableDto` objects)
- Структура `DealTableDto`:
  - `dealId` (number)
  - `dealDate` (string, 'YYYY-MM-DD')
  - `dealCost` (number)
  - `clientName` (string)
  - `clientPhone` (string)
  - `realtorName` (string)
  - `propertyAddress` (string)
  - `propertyTypeName` (string)
  - `dealTypeName` (string)

GET    /api/deals/{id}/for-table
- Параметры: `id` (number) - ID сделки
- Возвращает: `object` (DealTableDto)

GET    /api/deals/search/by-date-with-details
- Параметры: `date` (string, 'YYYY-MM-DD') - Дата
- Возвращает: `array` (of `DealWithDetailsDto` objects)

GET    /api/deals/search/by-date-range-with-details
- Параметры: `startDate` (string, 'YYYY-MM-DD'), `endDate` (string, 'YYYY-MM-DD')
- Возвращает: `array` (of `DealWithDetailsDto` objects)

GET    /api/deals/search/by-realtor/{realtorId}/with-details
- Параметры: `realtorId` (number) - ID риелтора
- Возвращает: `array` (of `DealWithDetailsDto` objects)

GET    /api/deals/search/by-client/{clientId}/with-details
- Параметры: `clientId` (number) - ID клиента
- Возвращает: `array` (of `DealWithDetailsDto` objects)

## Типы сделок (`/api/deal-types`)
GET    /api/deal-types
- Возвращает: `array` (of `DealType` objects)

GET    /api/deal-types/{id}
- Параметры: `id` (number) - ID типа сделки
- Возвращает: `object` (DealType)

GET    /api/deal-types/search/by-name
- Параметры: `name` (string) - Название
- Возвращает: `object` (DealType)

## Типы недвижимости (`/api/property-types`)
GET    /api/property-types
- Возвращает: `array` (of `PropertyType` objects)

GET    /api/property-types/{id}
- Параметры: `id` (number) - ID типа объекта
- Возвращает: `object` (PropertyType)

GET    /api/property-types/search/by-name
- Параметры: `name` (string) - Название
- Возвращает: `object` (PropertyType)

## Платежи (`/api/payments`)
GET    /api/payments
- Возвращает: `array` (of `Payment` objects)

GET    /api/payments/{id}
- Параметры: `id` (number) - ID платежа
- Возвращает: `object` (Payment)

POST   /api/payments
- Тело запроса: `object` (Payment)
- Возвращает: `object` (Payment)

PUT    /api/payments/{id}
- Параметры: `id` (number) - ID платежа
- Тело запроса: `object` - Детали платежа
- Возвращает: `object` (Payment)

DELETE /api/payments/{id}
- Параметры: `id` (number) - ID платежа
- Возвращает: `204 No Content`

GET    /api/payments/deal/{dealId}
- Параметры: `dealId` (number) - ID сделки
- Возвращает: `array` (of `Payment` objects)

## География (`/api/geography`)
### Страны

GET    /api/geography/countries
- Возвращает: `array` (of `Country` objects)

GET    /api/geography/countries/{id}
- Параметры: `id` (number) - ID страны
- Возвращает: `object` (Country)

GET    /api/geography/countries/search/by-name
- Параметры: `name` (string) - Название
- Возвращает: `object` (Country)

### Регионы

GET    /api/geography/regions
- Возвращает: `array` (of `Region` objects)

GET    /api/geography/regions/{id}
- Параметры: `id` (number) - ID региона
- Возвращает: `object` (Region)

GET    /api/geography/regions/search/by-code
- Параметры: `code` (string) - Код региона
- Возвращает: `array` (of `Region` objects)

GET    /api/geography/countries/{countryId}/regions
- Параметры: `countryId` (number) - ID страны
- Возвращает: `array` (of `Region` objects)

GET    /api/geography/regions/search/by-name-and-country
- Параметры: `regionName` (string), `countryId` (number)
- Возвращает: `object` (Region)

GET    /api/geography/regions/with-details
- Возвращает: `array` (of `RegionWithDetailsDto` objects)
- Структура `RegionWithDetailsDto`:
  - `regionId` (number)
  - `regionName` (string)
  - `regionCode` (string)
  - `countryId` (number)
  - `countryName` (string)

GET    /api/geography/regions/{id}/with-details
- Параметры: `id` (number) - ID региона
- Возвращает: `object` (RegionWithDetailsDto)

GET    /api/geography/countries/{countryId}/regions/with-details
- Параметры: `countryId` (number) - ID страны
- Возвращает: `array` (of `RegionWithDetailsDto` objects)

### Города

GET    /api/geography/cities
- Возвращает: `array` (of `City` objects)

GET    /api/geography/cities/{id}
- Параметры: `id` (number) - ID города
- Возвращает: `object` (City)

GET    /api/geography/regions/{regionId}/cities
- Параметры: `regionId` (number) - ID региона
- Возвращает: `array` (of `City` objects)

GET    /api/geography/countries/{countryId}/cities
- Параметры: `countryId` (number) - ID страны
- Возвращает: `array` (of `City` objects)

GET    /api/geography/cities/search/by-name-and-region
- Параметры: `cityName` (string), `regionId` (number)
- Возвращает: `object` (City)

GET    /api/geography/cities/search/by-name-pattern
- Параметры: `pattern` (string) - Шаблон названия
- Возвращает: `array` (of `City` objects)

GET    /api/geography/cities/with-details
- Возвращает: `array` (of `CityWithDetailsDto` objects)
- Структура `CityWithDetailsDto`:
  - `cityId` (number)
  - `cityName` (string)
  - `regionId` (number)
  - `regionName` (string)
  - `regionCode` (string)
  - `countryId` (number)
  - `countryName` (string)

GET    /api/geography/cities/{id}/with-details
- Параметры: `id` (number) - ID города
- Возвращает: `object` (CityWithDetailsDto)

GET    /api/geography/regions/{regionId}/cities/with-details
- Параметры: `regionId` (number) - ID региона
- Возвращает: `array` (of `CityWithDetailsDto` objects)

### Районы

GET    /api/geography/districts
- Возвращает: `array` (of `District` objects)

GET    /api/geography/districts/{id}
- Параметры: `id` (number) - ID района
- Возвращает: `object` (District)

GET    /api/geography/cities/{cityId}/districts
- Параметры: `cityId` (number) - ID города
- Возвращает: `array` (of `District` objects)

GET    /api/geography/regions/{regionId}/districts
- Параметры: `regionId` (number) - ID региона
- Возвращает: `array` (of `District` objects)

GET    /api/geography/countries/{countryId}/districts
- Параметры: `countryId` (number) - ID страны
- Возвращает: `array` (of `District` objects)

GET    /api/geography/districts/search/by-name-and-city
- Параметры: `districtName` (string), `cityId` (number)
- Возвращает: `object` (District)

GET    /api/geography/districts/with-details
- Возвращает: `array` (of `DistrictWithDetailsDto` objects)
- Структура `DistrictWithDetailsDto`:
  - `districtId` (number)
  - `districtName` (string)
  - `cityId` (number)
  - `cityName` (string)
  - `regionId` (number)
  - `regionName` (string)
  - `countryId` (number)
  - `countryName` (string)

GET    /api/geography/districts/{id}/with-details
- Параметры: `id` (number) - ID района
- Возвращает: `object` (DistrictWithDetailsDto)

GET    /api/geography/cities/{cityId}/districts/with-details
- Параметры: `cityId` (number) - ID города
- Возвращает: `array` (of `DistrictWithDetailsDto` objects)

### Улицы

GET    /api/geography/streets
- Возвращает: `array` (of `Street` objects)

GET    /api/geography/streets/{id}
- Параметры: `id` (number) - ID улицы
- Возвращает: `object` (Street)

GET    /api/geography/cities/{cityId}/streets
- Параметры: `cityId` (number) - ID города
- Возвращает: `array` (of `Street` objects)

GET    /api/geography/streets/search/by-name-and-city
- Параметры: `streetName` (string), `cityId` (number)
- Возвращает: `object` (Street)

GET    /api/geography/streets/search/by-name-pattern
- Параметры: `pattern` (string) - Шаблон названия
- Возвращает: `array` (of `Street` objects)

GET    /api/geography/streets/with-details
- Возвращает: `array` (of `StreetWithDetailsDto` objects)
- Структура `StreetWithDetailsDto`:
  - `streetId` (number)
  - `streetName` (string)
  - `cityId` (number)
  - `cityName` (string)
  - `regionId` (number)
  - `regionName` (string)
  - `countryId` (number)
  - `countryName` (string)

GET    /api/geography/streets/{id}/with-details
- Параметры: `id` (number) - ID улицы
- Возвращает: `object` (StreetWithDetailsDto)

GET    /api/geography/cities/{cityId}/streets/with-details
- Параметры: `cityId` (number) - ID города
- Возвращает: `array` (of `StreetWithDetailsDto` objects)

---
Всего эндпоинтов: 92