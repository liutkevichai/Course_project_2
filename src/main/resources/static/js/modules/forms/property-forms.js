// =========================
// Модуль для форм недвижимости (добавление и поиск)
// =========================

import { resetAndDisable, createOption } from '../utils.js';
import { fetchJson } from '../api.js';

/**
 * Инициализация форм недвижимости
 */
export function initPropertyForms() {
  initPropertyAddForm();
  initPropertySearchForm();
}

/**
 * Форма добавления недвижимости
 */
function initPropertyAddForm() {
  const propertyForm = document.getElementById('propertyAddForm');
  if (!propertyForm) return;

  const countrySelect = propertyForm.querySelector('#idCountry');
  const regionSelect = propertyForm.querySelector('#idRegion');
  const citySelect = propertyForm.querySelector('#idCity');
  const districtSelect = propertyForm.querySelector('#idDistrict');
  const streetSelect = propertyForm.querySelector('#idStreet');

  async function updateRegions(countryId) {
    if (!countryId) {
      resetAndDisable(regionSelect, 'Выберите регион');
      resetAndDisable(citySelect, 'Выберите город');
      resetAndDisable(districtSelect, 'Выберите район');
      resetAndDisable(streetSelect, 'Выберите улицу');
      return;
    }
    const regions = await fetchJson(`/api/geography/countries/${countryId}/regions`);
    regionSelect.innerHTML = '';
    regionSelect.appendChild(createOption('', 'Выберите регион'));
    regions.forEach(r => regionSelect.appendChild(createOption(r.idRegion, r.name)));
    regionSelect.disabled = false;
  }

  async function updateCities(regionId) {
    if (!regionId) {
      resetAndDisable(citySelect, 'Выберите город');
      resetAndDisable(districtSelect, 'Выберите район');
      resetAndDisable(streetSelect, 'Выберите улицу');
      return;
    }
    const cities = await fetchJson(`/api/geography/regions/${regionId}/cities`);
    citySelect.innerHTML = '';
    citySelect.appendChild(createOption('', 'Выберите город'));
    cities.forEach(c => citySelect.appendChild(createOption(c.idCity, c.cityName)));
    citySelect.disabled = false;
  }

  async function updateDistrictsAndStreets(cityId) {
    if (!cityId) {
      resetAndDisable(districtSelect, 'Выберите район');
      resetAndDisable(streetSelect, 'Выберите улицу');
      return;
    }
    const districts = await fetchJson(`/api/geography/cities/${cityId}/districts`);
    districtSelect.innerHTML = '';
    districtSelect.appendChild(createOption('', 'Выберите район'));
    districts.forEach(d => districtSelect.appendChild(createOption(d.idDistrict, d.districtName)));
    districtSelect.disabled = false;

    const streets = await fetchJson(`/api/geography/cities/${cityId}/streets`);
    streetSelect.innerHTML = '';
    streetSelect.appendChild(createOption('', 'Выберите улицу'));
    streets.forEach(s => streetSelect.appendChild(createOption(s.idStreet, s.streetName)));
    streetSelect.disabled = false;
  }

  countrySelect?.addEventListener('change', () => {
    updateRegions(countrySelect.value);
    resetAndDisable(citySelect, 'Выберите город');
    resetAndDisable(districtSelect, 'Выберите район');
    resetAndDisable(streetSelect, 'Выберите улицу');
  });

  regionSelect?.addEventListener('change', () => {
    updateCities(regionSelect.value);
    resetAndDisable(districtSelect, 'Выберите район');
    resetAndDisable(streetSelect, 'Выберите улицу');
  });

  citySelect?.addEventListener('change', () => {
    updateDistrictsAndStreets(citySelect.value);
  });

  resetAndDisable(regionSelect, 'Сначала выберите страну');
  resetAndDisable(citySelect, 'Сначала выберите регион');
  resetAndDisable(districtSelect, 'Сначала выберите город');
  resetAndDisable(streetSelect, 'Сначала выберите город');
}

/**
 * Форма поиска недвижимости
 */
function initPropertySearchForm() {
  const propertySearchForm = document.getElementById('propertySearchForm');
  if (!propertySearchForm) return;

  const citySelect = propertySearchForm.querySelector('#searchCityId');
  const districtSelect = propertySearchForm.querySelector('#searchDistrictId');
  const streetSelect = propertySearchForm.querySelector('#searchStreetId');

  async function updateDistrictsAndStreets(cityId, selectedDistrictId, selectedStreetId) {
    resetAndDisable(districtSelect, 'Все районы');
    resetAndDisable(streetSelect, 'Все улицы');
    if (!cityId) return;

    const districts = await fetchJson(`/api/geography/cities/${cityId}/districts`);
    districtSelect.innerHTML = '';
    districtSelect.appendChild(createOption('', 'Все районы'));
    districts.forEach(d =>
      districtSelect.appendChild(createOption(d.idDistrict, d.districtName, selectedDistrictId && String(d.idDistrict) === String(selectedDistrictId)))
    );
    districtSelect.disabled = false;

    const streets = await fetchJson(`/api/geography/cities/${cityId}/streets`);
    streetSelect.innerHTML = '';
    streetSelect.appendChild(createOption('', 'Все улицы'));
    streets.forEach(s =>
      streetSelect.appendChild(createOption(s.idStreet, s.streetName, selectedStreetId && String(s.idStreet) === String(selectedStreetId)))
    );
    streetSelect.disabled = false;
  }

  citySelect?.addEventListener('change', () => {
    updateDistrictsAndStreets(citySelect.value, null, null);
  });

  if (citySelect.value) {
    const params = new URLSearchParams(window.location.search);
    const selectedDistrictId = params.get('districtId');
    const selectedStreetId = params.get('streetId');
    updateDistrictsAndStreets(citySelect.value, selectedDistrictId, selectedStreetId);
  }
}