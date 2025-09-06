document.addEventListener('DOMContentLoaded', function () {
    const dropdownToggle = document.querySelector('aside .dropdown-toggle');
    const dropdownMenu = document.querySelector('aside .dropdown-menu');
    const menuStateKey = 'dropdownMenuState';

    // Функция для установки состояния меню
    const setMenuState = (state) => {
        if (state === 'open') {
            dropdownMenu.classList.add('show');
            localStorage.setItem(menuStateKey, 'open');
        } else {
            dropdownMenu.classList.remove('show');
            localStorage.setItem(menuStateKey, 'closed');
        }
    };

    // Проверяем состояние меню при загрузке страницы
    if (localStorage.getItem(menuStateKey) === 'open') {
        setMenuState('open');
    }

    // Обработчик для кнопки открытия/закрытия меню
    if (dropdownToggle) {
        dropdownToggle.addEventListener('click', function (e) {
            e.preventDefault();
            const isMenuOpen = dropdownMenu.classList.contains('show');
            setMenuState(isMenuOpen ? 'closed' : 'open');
        });
    }

    // Получаем все ссылки в боковой панели
    const sidebarLinks = document.querySelectorAll('aside nav a');

    sidebarLinks.forEach(link => {
        link.addEventListener('click', function() {
            // Если ссылка находится внутри выпадающего меню, сохраняем его открытым
            if (this.closest('.dropdown-menu')) {
                localStorage.setItem(menuStateKey, 'open');
            } else {
                // Если ссылка вне меню, закрываем его
                localStorage.setItem(menuStateKey, 'closed');
            }
        });
    });

    // Обработчики для формы добавления недвижимости
    // ВАЖНО: атрибуты th:* удаляются на стороне сервера, поэтому селектор по th:object не работает в браузере
    // Используем id формы, заданный в шаблоне
    const propertyForm = document.getElementById('propertyAddForm');
    if (propertyForm) {
        const countrySelect = propertyForm.querySelector('#idCountry');
        const regionSelect = propertyForm.querySelector('#idRegion');
        const citySelect = propertyForm.querySelector('#idCity');
        const districtSelect = propertyForm.querySelector('#idDistrict');
        const streetSelect = propertyForm.querySelector('#idStreet');

        const resetAndDisable = (selectElement, defaultOptionText) => {
            if (selectElement) {
                selectElement.innerHTML = `<option value="">${defaultOptionText}</option>`;
                selectElement.disabled = true;
            }
        };

        // Функция для обновления регионов по ID страны
        const updateRegionsByCountry = (countryId) => {
            if (!countryId) {
                resetAndDisable(regionSelect, 'Выберите регион');
                resetAndDisable(citySelect, 'Выберите город');
                resetAndDisable(districtSelect, 'Выберите район');
                resetAndDisable(streetSelect, 'Выберите улицу');
                return;
            }

            fetch(`/api/geography/countries/${countryId}/regions`)
                .then(response => response.json())
                .then(regions => {
                    regionSelect.innerHTML = '<option value="">Выберите регион</option>';
                    regions.forEach(region => {
                        const option = document.createElement('option');
                        option.value = region.idRegion;
                        option.textContent = region.name;
                        regionSelect.appendChild(option);
                    });
                    regionSelect.disabled = false;
                })
                .catch(error => console.error('Ошибка при получении списка регионов:', error));
        };

        // Функция для обновления городов по ID региона
        const updateCitiesByRegion = (regionId) => {
            if (!regionId) {
                resetAndDisable(citySelect, 'Выберите город');
                resetAndDisable(districtSelect, 'Выберите район');
                resetAndDisable(streetSelect, 'Выберите улицу');
                return;
            }

            fetch(`/api/geography/regions/${regionId}/cities`)
                .then(response => response.json())
                .then(cities => {
                    citySelect.innerHTML = '<option value="">Выберите город</option>';
                    cities.forEach(city => {
                        const option = document.createElement('option');
                        option.value = city.idCity;
                        option.textContent = city.cityName;
                        citySelect.appendChild(option);
                    });
                    citySelect.disabled = false;
                })
                .catch(error => console.error('Ошибка при получении списка городов:', error));
        };

        // Функция для обновления районов и улиц по ID города
        const updateDistrictsAndStreetsByCity = (cityId) => {
            if (!cityId) {
                resetAndDisable(districtSelect, 'Выберите район');
                resetAndDisable(streetSelect, 'Выберите улицу');
                return;
            }

            // Обновление районов
            fetch(`/api/geography/cities/${cityId}/districts`)
                .then(response => response.json())
                .then(districts => {
                    districtSelect.innerHTML = '<option value="">Выберите район</option>';
                    districts.forEach(district => {
                        const option = document.createElement('option');
                        option.value = district.idDistrict;
                        option.textContent = district.districtName;
                        districtSelect.appendChild(option);
                    });
                    districtSelect.disabled = false;
                })
                .catch(error => console.error('Ошибка при получении списка районов:', error));

            // Обновление улиц
            fetch(`/api/geography/cities/${cityId}/streets`)
                .then(response => response.json())
                .then(streets => {
                    streetSelect.innerHTML = '<option value="">Выберите улицу</option>';
                    streets.forEach(street => {
                        const option = document.createElement('option');
                        option.value = street.idStreet;
                        option.textContent = street.streetName;
                        streetSelect.appendChild(option);
                    });
                    streetSelect.disabled = false;
                })
                .catch(error => console.error('Ошибка при получении списка улиц:', error));
        };

        // Обработчики событий
        if (countrySelect) {
            countrySelect.addEventListener('change', () => {
                updateRegionsByCountry(countrySelect.value);
                resetAndDisable(citySelect, 'Выберите город');
                resetAndDisable(districtSelect, 'Выберите район');
                resetAndDisable(streetSelect, 'Выберите улицу');
            });
        }
        if (regionSelect) {
            regionSelect.addEventListener('change', () => {
                updateCitiesByRegion(regionSelect.value);
                resetAndDisable(districtSelect, 'Выберите район');
                resetAndDisable(streetSelect, 'Выберите улицу');
            });
        }
        if (citySelect) {
            citySelect.addEventListener('change', () => updateDistrictsAndStreetsByCity(citySelect.value));
        }

        // Инициализация формы
        const initializeForm = () => {
            resetAndDisable(regionSelect, 'Сначала выберите страну');
            resetAndDisable(citySelect, 'Сначала выберите регион');
            resetAndDisable(districtSelect, 'Сначала выберите город');
            resetAndDisable(streetSelect, 'Сначала выберите город');
        };

        initializeForm();
    // Обработчики для формы поиска недвижимости
    const propertySearchForm = document.getElementById('propertySearchForm');
    if (propertySearchForm) {
        const citySelect = propertySearchForm.querySelector('#searchCityId');
        const districtSelect = propertySearchForm.querySelector('#searchDistrictId');
        const streetSelect = propertySearchForm.querySelector('#searchStreetId');

        const resetAndDisable = (selectElement, defaultOptionText) => {
            if (selectElement) {
                selectElement.innerHTML = `<option value="">${defaultOptionText}</option>`;
                selectElement.disabled = true;
            }
        };

        const updateDistrictsAndStreets = (cityId, selectedDistrictId, selectedStreetId) => {
            resetAndDisable(districtSelect, 'Все районы');
            resetAndDisable(streetSelect, 'Все улицы');

            if (!cityId) return;

            // Загружаем районы
            fetch(`/api/geography/cities/${cityId}/districts`)
                .then(response => response.json())
                .then(districts => {
                    districts.forEach(district => {
                        const option = document.createElement('option');
                        option.value = district.idDistrict;
                        option.textContent = district.districtName;
                        if (district.idDistrict == selectedDistrictId) {
                            option.selected = true;
                        }
                        districtSelect.appendChild(option);
                    });
                    districtSelect.disabled = false;
                });

            // Загружаем улицы
            fetch(`/api/geography/cities/${cityId}/streets`)
                .then(response => response.json())
                .then(streets => {
                    streets.forEach(street => {
                        const option = document.createElement('option');
                        option.value = street.idStreet;
                        option.textContent = street.streetName;
                        if (street.idStreet == selectedStreetId) {
                            option.selected = true;
                        }
                        streetSelect.appendChild(option);
                    });
                    streetSelect.disabled = false;
                });
        };

        citySelect.addEventListener('change', () => {
            updateDistrictsAndStreets(citySelect.value, null, null);
        });

        if (citySelect.value) {
            const selectedDistrictId = new URLSearchParams(window.location.search).get('districtId');
            const selectedStreetId = new URLSearchParams(window.location.search).get('streetId');
            updateDistrictsAndStreets(citySelect.value, selectedDistrictId, selectedStreetId);
        }
    }
    }
});