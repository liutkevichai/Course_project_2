# ER-диаграмма базы данных недвижимости

## Физическая модель базы данных PostgreSQL

```mermaid
erDiagram
    %% Справочные таблицы
    DEAL_TYPES {
        int id_deal_type PK
        varchar deal_type_name UK
    }
    
    PROPERTY_TYPES {
        int id_property_type PK
        varchar property_type_name UK
    }
    
    %% Географическая структура
    COUNTRIES {
        int id_country PK
        varchar country_name UK
    }
    
    REGIONS {
        int id_region PK
        varchar name
        varchar code UK
        int id_country FK
    }
    
    CITIES {
        int id_city PK
        varchar city_name
        int id_region FK
    }
    
    DISTRICTS {
        int id_district PK
        varchar district_name
        int id_city FK
    }
    
    STREETS {
        int id_street PK
        varchar street_name
        int id_city FK
    }
    
    %% Основные сущности
    CLIENTS {
        int id_client PK
        varchar first_name
        varchar last_name
        varchar middle_name
        varchar phone UK
        varchar email UK
    }
    
    REALTORS {
        int id_realtor PK
        varchar first_name
        varchar last_name
        varchar middle_name
        varchar phone UK
        varchar email UK
        int experience_years
    }
    
    PROPERTIES {
        int id_property PK
        numeric area
        numeric cost
        text description
        varchar postal_code
        varchar house_number
        varchar house_letter
        varchar building_number
        varchar apartment_number
        int id_property_type FK
        int id_country FK
        int id_region FK
        int id_city FK
        int id_district FK
        int id_street FK
    }
    
    DEALS {
        int id_deal PK
        date deal_date
        numeric deal_cost
        int id_property FK
        int id_realtor FK
        int id_client FK
        int id_deal_type FK
    }
    
    PAYMENTS {
        int id_payment PK
        date payment_date
        numeric amount
        int id_deal FK
    }
    
    %% Связи - Географическая иерархия
    COUNTRIES ||--o{ REGIONS : "содержит"
    REGIONS ||--o{ CITIES : "включает"
    CITIES ||--o{ DISTRICTS : "содержит"
    CITIES ||--o{ STREETS : "содержит"
    
    %% Связи - Недвижимость с географией
    COUNTRIES ||--o{ PROPERTIES : "расположена в"
    REGIONS ||--o{ PROPERTIES : "находится в"
    CITIES ||--o{ PROPERTIES : "находится в"
    DISTRICTS ||--o{ PROPERTIES : "расположена в"
    STREETS ||--o{ PROPERTIES : "расположена на"
    
    %% Связи - Типы и основные сущности
    PROPERTY_TYPES ||--o{ PROPERTIES : "имеет тип"
    PROPERTIES ||--o{ DEALS : "продается/покупается"
    REALTORS ||--o{ DEALS : "ведет"
    CLIENTS ||--o{ DEALS : "участвует в"
    DEAL_TYPES ||--o{ DEALS : "имеет тип"
    DEALS ||--o{ PAYMENTS : "оплачивается"
```

## Описание структуры

### Справочные таблицы
- **deal_types** - типы сделок (продажа, аренда и т.д.)
- **property_types** - типы недвижимости (квартира, дом, офис и т.д.)

### Географическая структура
- **countries** - страны
- **regions** - регионы/области
- **cities** - города
- **districts** - районы городов
- **streets** - улицы

### Основные бизнес-сущности
- **clients** - клиенты
- **realtors** - риелторы
- **properties** - объекты недвижимости
- **deals** - сделки
- **payments** - платежи по сделкам

### Ключевые особенности модели
1. Полная географическая иерархия для точного позиционирования недвижимости
2. Связь многие-ко-многим между клиентами и недвижимостью через сделки
3. Отслеживание всех платежей по каждой сделке
4. Нормализованная структура справочников 