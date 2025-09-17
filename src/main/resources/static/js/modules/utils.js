// =========================
// Общие константы и утилиты
// =========================

// Флаг отладочного логирования UI/API. В продакшене должен быть false.
export const DEBUG = false;

// Ключи localStorage
export const STORAGE_KEYS = {
  dropdownMenuState: 'dropdownMenuState'
};

// Единый форматер чисел для РФ
export const NUMBER_FORMAT_RU = new Intl.NumberFormat('ru-RU');

// Утилита безопасного получения одного элемента
export const qs = (root, selector) => (root || document).querySelector(selector);

// Утилита безопасного получения списка элементов
export const qsa = (root, selector) => Array.from((root || document).querySelectorAll(selector));

// Фабрика <option>
export const createOption = (value, text, selected = false) => {
  const option = document.createElement('option');
  option.value = value ?? '';
  option.textContent = text ?? '';
  if (selected) option.selected = true;
  return option;
};

// Парсинг денежной строки в число
export const parseCurrency = (text) => {
  if (text == null) return 0;
  const normalized = String(text).replace(/[^\d.,-]/g, '').replace(',', '.');
  const num = parseFloat(normalized);
  return Number.isFinite(num) ? num : 0;
};

// Форматирование числа в валютный вывод с символом ₽
export const formatCurrency = (num) => `${NUMBER_FORMAT_RU.format(num ?? 0)} ₽`;

// Форматирование даты из YYYY-MM-DD в DD.MM.YYYY
export const formatDateToDDMMYYYYUtil = (dateString) => {
  if (!dateString) return '';
  const parts = String(dateString).split('-');
  if (parts.length === 3) {
    return `${parts[2]}.${parts[1]}.${parts[0]}`;
  }
  return dateString;
};

// Короткий формат имени клиента "Фамилия И."
export const formatClientNameShortUtil = (clientName) => {
  if (!clientName) return '';
  const parts = clientName.trim().split(/\s+/);
  if (parts.length >= 2) {
    return `${parts[0]} ${parts[1].charAt(0)}.`;
  }
  return clientName;
};

// Единая функция показа ошибок пользователю
export const showError = (message) => {
  alert(message || 'Произошла ошибка. Пожалуйста, попробуйте еще раз.');
};

// Универсальный сброс и дизейбл для select
export const resetAndDisable = (select, defaultOptionText) => {
  if (select) {
    select.innerHTML = `<option value="">${defaultOptionText}</option>`;
    select.disabled = true;
  }
};

// Управление режимом редактирования строки таблицы
export const setRowEditing = (row, isEditing) => {
  if (!row) return;

  if (isEditing) {
    if (!row.dataset.originalContent) {
      row.dataset.originalContent = row.innerHTML;
    }
    row.classList.add('editing');
  } else {
    if (row.dataset.originalContent) {
      row.innerHTML = row.dataset.originalContent;
      delete row.dataset.originalContent;
    }
    row.classList.remove('editing');
  }
};

// Добавляет кнопки действий в строку таблицы
export const addActionButtonsUtil = (row) => {
  if (!row) return null;
  const existingActions = row.querySelector('.edit-actions');
  if (existingActions) {
    return existingActions.parentElement;
  }
  const table = row.closest('table');
  if (table) {
    const headerRow = table.querySelector('thead tr');
    if (headerRow && !headerRow.querySelector('th[data-actions-header]')) {
      const actionsHeader = document.createElement('th');
      actionsHeader.textContent = 'Действия';
      actionsHeader.setAttribute('data-actions-header', 'true');
      headerRow.appendChild(actionsHeader);
    }
  }
  const actionsCell = document.createElement('td');
  actionsCell.innerHTML = `
      <div class="edit-actions">
          <button class="save-btn">Сохранить</button>
          <button class="cancel-btn">Отмена</button>
          <button class="delete-btn" title="Удалить"></button>
      </div>
  `;
  row.appendChild(actionsCell);
  return actionsCell;
};

// Удаляет заголовок "Действия" если нет редактируемых строк
export const removeActionsHeaderIfNeededUtil = (table) => {
  if (!table) return;
  const editingRows = table.querySelectorAll('tbody tr.editing');
  if (editingRows.length === 0) {
    const actionsHeader = table.querySelector('th[data-actions-header]');
    if (actionsHeader) {
      actionsHeader.remove();
    }
  }
};