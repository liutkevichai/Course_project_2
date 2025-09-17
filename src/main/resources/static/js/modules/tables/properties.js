// =========================
// Модуль для таблицы недвижимости
// =========================

import { addActionButtonsUtil, removeActionsHeaderIfNeededUtil, showError } from '../utils.js';

/**
 * Инициализация таблицы недвижимости
 */
export function initPropertiesTable() {
  if (document.querySelector('main h1') && document.querySelector('main h1').textContent.includes('Недвижимость')) {
    const propertiesTable = document.querySelector('main table tbody');
    if (propertiesTable) {
      propertiesTable.addEventListener('click', function (e) {
        const row = e.target.closest('tr');
        if (row && !row.classList.contains('editing')) {
          if (e.target.classList.contains('delete-btn')) {
            return;
          }
          editPropertyRow(row);
        }
      });
    }
  }
}

/**
 * Редактирование недвижимости
 */
function editPropertyRow(row) {
  row.dataset.originalContent = row.innerHTML;
  const cells = row.querySelectorAll('td');
  const propertyId = cells[0].textContent;
  row.dataset.recordId = propertyId;
  row.classList.add('editing');

  // Площадь
  const areaCell = cells[3];
  const areaValue = areaCell.textContent.replace(' м²', '');
  areaCell.innerHTML = `<input type="number" step="0.1" value="${areaValue}" class="edit-input">`;

  // Стоимость
  const costCell = cells[4];
  const costValue = costCell.textContent.replace(/[^\d.,]/g, '').replace(',', '.');
  costCell.innerHTML = `<input type="number" step="1000" value="${costValue}" class="edit-input">`;

  // Описание
  const descCell = cells[5];
  const descValue = descCell.textContent;
  descCell.innerHTML = `<textarea class="edit-input" rows="2">${descValue}</textarea>`;

  // Тип
  const typeCell = cells[6];
  typeCell.innerHTML = '<select class="edit-input"><option>Загрузка...</option></select>';
  fetch('/api/property-types')
    .then(r => r.json())
    .then(propertyTypes => {
      const select = typeCell.querySelector('select');
      select.innerHTML = '<option value="">Выберите тип недвижимости</option>';
      propertyTypes.forEach(t => {
        const opt = document.createElement('option');
        opt.value = t.idPropertyType;
        opt.textContent = t.propertyTypeName;
        if (t.propertyTypeName === cells[6].textContent) {
          opt.selected = true;
        }
        select.appendChild(opt);
      });
    })
    .catch(err => {
      console.error('Ошибка типов недвижимости:', err);
      typeCell.innerHTML = '<span>Ошибка загрузки</span>';
    });

  const actionsCell = addActionButtonsUtil(row);
  actionsCell.querySelector('.save-btn').addEventListener('click', () => savePropertyRow(row));
  actionsCell.querySelector('.cancel-btn').addEventListener('click', () => cancelPropertyRow(row));
}

/**
 * Сохранение недвижимости
 */
function savePropertyRow(row) {
  const cells = row.querySelectorAll('td');
  const propertyId = row.dataset.recordId;
  const updates = {
    area: parseFloat(cells[3].querySelector('input').value) || 0,
    price: parseFloat(cells[4].querySelector('input').value) || 0,
    description: cells[5].querySelector('textarea').value,
    propertyTypeId: parseInt(cells[6].querySelector('select').value) || 0
  };

  fetch(`/properties/update/${propertyId}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(updates)
  })
    .then(r => {
      if (r.ok) {
        cells[3].textContent = updates.area + ' м²';
        cells[4].textContent = new Intl.NumberFormat('ru-RU').format(updates.price) + ' ₽';
        cells[5].textContent = updates.description;
        cells[6].textContent = cells[6].querySelector('select').selectedOptions[0].textContent;
        row.removeChild(row.lastChild);
        row.classList.remove('editing');
        removeActionsHeaderIfNeededUtil(row.closest('table'));
      } else {
        cancelPropertyRow(row);
        showError('Ошибка при сохранении недвижимости');
      }
    })
    .catch(err => {
      console.error('Ошибка недвижимости:', err);
      cancelPropertyRow(row);
      showError('Ошибка сети при сохранении недвижимости.');
    });
}

/**
 * Отмена редактирования недвижимости
 */
function cancelPropertyRow(row) {
  row.innerHTML = row.dataset.originalContent;
  row.classList.remove('editing');
  removeActionsHeaderIfNeededUtil(row.closest('table'));
}