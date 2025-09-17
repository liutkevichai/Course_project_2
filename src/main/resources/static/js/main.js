// =========================
// Главная точка входа JS
// Подключение и инициализация модулей
// =========================

import { initClientsTable } from './modules/tables/clients.js';
import { initPaymentsTable } from './modules/tables/payments.js';
import { initRealtorsTable } from './modules/tables/realtors.js';
import { initDealsTable } from './modules/tables/deals.js';
import { initPropertiesTable } from './modules/tables/properties.js';

import { initPropertyForms } from './modules/forms/property-forms.js';

import { initDropdownMenu } from './modules/components/dropdown-menu.js';
import { initDeleteHandler } from './modules/components/delete-handler.js';

// Инициализация после загрузки документа
document.addEventListener('DOMContentLoaded', () => {
  initDropdownMenu();
  initDeleteHandler();

  initPropertyForms();

  initClientsTable();
  initPaymentsTable();
  initRealtorsTable();
  initDealsTable();
  initPropertiesTable();
});