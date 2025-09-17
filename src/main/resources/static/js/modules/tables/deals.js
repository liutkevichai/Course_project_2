// =========================
// Модуль для таблицы сделок
// =========================

import { addActionButtonsUtil, removeActionsHeaderIfNeededUtil, showError, parseCurrency, formatCurrency, formatDateToDDMMYYYYUtil } from '../utils.js';

/**
 * Инициализация таблицы сделок
 */
export function initDealsTable() {
  if (document.querySelector('main h1') && document.querySelector('main h1').textContent.includes('Сделки')) {
    const dealsTable = document.querySelector('main table tbody');
    if (dealsTable) {
      dealsTable.addEventListener('click', function (e) {
        const row = e.target.closest('tr');
        if (row && !row.classList.contains('editing')) {
          if (e.target.classList.contains('delete-btn')) {
            return;
          }
          editDealRow(row);
        }
      });
    }
  }
}

/**
 * Редактирование сделки
 */
function editDealRow(row) {
  row.dataset.originalContent = row.innerHTML;
  const cells = row.querySelectorAll('td');
  const dealId = cells[0].textContent;
  row.dataset.recordId = dealId;
  row.classList.add('editing');

  // Подготавливаем редактируемые поля
  const dealDateCell = cells[1];
  const costCell = cells[5];
  let formattedDate = '';
  if (dealDateCell.textContent.includes('.')) {
    const [d, m, y] = dealDateCell.textContent.split('.');
    formattedDate = `${y}-${m}-${d}`;
  } else {
    formattedDate = dealDateCell.textContent;
  }
  dealDateCell.innerHTML = `<input type="date" value="${formattedDate}" class="edit-input">`;
  costCell.innerHTML = `<input type="number" step="0.01" value="${parseCurrency(costCell.textContent)}" class="edit-input">`;

  // Поля с селектами (заполняются асинхронно)
  const propertyAddressCell = cells[2];
  const clientCell = cells[3];
  const realtorCell = cells[4];
  const dealTypeCell = cells[6];

  propertyAddressCell.innerHTML = '<select class="edit-input"><option>Загрузка...</option></select>';
  clientCell.innerHTML = '<select class="edit-input"><option>Загрузка...</option></select>';
  realtorCell.innerHTML = '<select class="edit-input"><option>Загрузка...</option></select>';
  dealTypeCell.innerHTML = '<select class="edit-input"><option>Загрузка...</option></select>';

  const promises = [
    fetch('/api/properties/for-table').then(res => res.json()),
    fetch('/api/clients').then(res => res.json()),
    fetch('/api/realtors').then(res => res.json()),
    fetch('/api/deal-types').then(res => res.json())
  ];

  Promise.all(promises)
    .then(([properties, clients, realtors, dealTypes]) => {
      fillSelect(propertyAddressCell, properties, 'propertyId', 'tableAddress', propertyAddressCell.textContent);
      fillSelect(clientCell, clients, 'idClient', null, clientCell.textContent, i => `${i.lastName} ${i.firstName} ${i.middleName || ''}`.trim());
      fillSelect(realtorCell, realtors, 'idRealtor', null, realtorCell.textContent, i => `${i.lastName} ${i.firstName} ${i.middleName || ''}`.trim());
      fillSelect(dealTypeCell, dealTypes, 'idDealType', 'dealTypeName', dealTypeCell.textContent);
    })
    .catch(err => {
      console.error('Ошибка при загрузке данных сделки:', err);
      propertyAddressCell.innerHTML = '<span>Ошибка</span>';
      clientCell.innerHTML = '<span>Ошибка</span>';
      realtorCell.innerHTML = '<span>Ошибка</span>';
      dealTypeCell.innerHTML = '<span>Ошибка</span>';
    });

  const actionsCell = addActionButtonsUtil(row);
  actionsCell.querySelector('.save-btn').addEventListener('click', () => saveDealRow(row));
  actionsCell.querySelector('.cancel-btn').addEventListener('click', () => cancelDealRow(row));
}

/**
 * Заполнение select
 */
function fillSelect(cell, items, valueField, textField, selectedValue, extractor) {
  const select = cell.querySelector('select');
  select.innerHTML = '<option value="">Выберите...</option>';
  items.forEach(item => {
    const text = extractor ? extractor(item) : item[textField];
    const option = document.createElement('option');
    option.value = item[valueField];
    option.textContent = text;
    if (text.trim() === selectedValue.trim()) {
      option.selected = true;
    }
    select.appendChild(option);
  });
}

/**
 * Сохранение сделки
 */
function saveDealRow(row) {
  const cells = row.querySelectorAll('td');
  const dealId = row.dataset.recordId;
  const updates = {
    dealDate: cells[1].querySelector('input').value,
    dealCost: parseCurrency(cells[5].querySelector('input').value),
    propertyId: parseInt(cells[2].querySelector('select').value) || 0,
    clientId: parseInt(cells[3].querySelector('select').value) || 0,
    realtorId: parseInt(cells[4].querySelector('select').value) || 0,
    dealTypeId: parseInt(cells[6].querySelector('select').value) || 0
  };

  fetch(`/deals/update/${dealId}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(updates)
  })
    .then(r => {
      if (r.ok) {
        cells[1].textContent = formatDateToDDMMYYYYUtil(updates.dealDate);
        cells[2].textContent = cells[2].querySelector('select').selectedOptions[0].textContent;
        cells[3].textContent = cells[3].querySelector('select').selectedOptions[0].textContent;
        cells[4].textContent = cells[4].querySelector('select').selectedOptions[0].textContent;
        cells[5].textContent = formatCurrency(updates.dealCost);
        cells[6].textContent = cells[6].querySelector('select').selectedOptions[0].textContent;
        row.removeChild(row.lastChild);
        row.classList.remove('editing');
        removeActionsHeaderIfNeededUtil(row.closest('table'));
      } else {
        cancelDealRow(row);
        showError('Ошибка при сохранении сделки.');
      }
    })
    .catch(err => {
      console.error('Ошибка сохранения сделки:', err);
      cancelDealRow(row);
      showError('Ошибка сети при сохранении сделки.');
    });
}

/**
 * Отмена редактирования сделки
 */
function cancelDealRow(row) {
  row.innerHTML = row.dataset.originalContent;
  row.classList.remove('editing');
  removeActionsHeaderIfNeededUtil(row.closest('table'));
}