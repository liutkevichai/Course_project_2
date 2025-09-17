// =========================
// Модуль для таблицы клиентов
// =========================

import { setRowEditing, addActionButtonsUtil, removeActionsHeaderIfNeededUtil, showError } from '../utils.js';
import { fetchJson } from '../api.js';

/**
 * Инициализация таблицы клиентов
 */
export function initClientsTable() {
  if (document.querySelector('main h1') && document.querySelector('main h1').textContent.includes('Клиенты')) {
    const clientsTable = document.querySelector('main table tbody');
    if (clientsTable) {
      clientsTable.addEventListener('click', function (e) {
        const row = e.target.closest('tr');
        if (row && !row.classList.contains('editing')) {
          if (e.target.classList.contains('delete-btn')) {
            return;
          }
          editClientRow(row);
        }
      });
    }
  }
}

/**
 * Редактирование строки клиента
 */
function editClientRow(row) {
  setRowEditing(row, true);

  const cells = row.querySelectorAll('td');
  const clientId = cells[0].textContent;
  row.dataset.recordId = clientId;

  cells.forEach((cell, index) => {
    if (index === 0) return;
    const cellValue = cell.textContent;
    let inputType = 'text';
    if (index === 4) {
      inputType = 'tel';
    } else if (index === 5) {
      inputType = 'email';
    }
    const input = document.createElement('input');
    input.type = inputType;
    input.value = cellValue;
    input.className = 'edit-input';
    cell.innerHTML = '';
    cell.appendChild(input);
  });

  const actionsCell = addActionButtonsUtil(row);

  const saveBtn = actionsCell.querySelector('.save-btn');
  const cancelBtn = actionsCell.querySelector('.cancel-btn');

  saveBtn.addEventListener('click', function () {
    saveClientRow(row);
  });

  cancelBtn.addEventListener('click', function () {
    cancelEditClientRow(row);
  });
}

/**
 * Сохранение изменений клиента
 */
function saveClientRow(row) {
  const clientId = row.dataset.recordId;
  const cells = row.querySelectorAll('td');
  const updates = {
    firstName: cells[1].querySelector('input').value,
    lastName: cells[2].querySelector('input').value,
    middleName: cells[3].querySelector('input').value,
    phone: cells[4].querySelector('input').value,
    email: cells[5].querySelector('input').value
  };

  fetchJson(`/clients/update/${clientId}`, {
    method: 'POST',
    body: JSON.stringify(updates)
  })
    .then(() => {
      cells[1].textContent = updates.firstName;
      cells[2].textContent = updates.lastName;
      cells[3].textContent = updates.middleName;
      cells[4].textContent = updates.phone;
      cells[5].textContent = updates.email;
      row.removeChild(row.lastChild);
      setRowEditing(row, false);
      const table = row.closest('table');
      removeActionsHeaderIfNeededUtil(table);
    })
    .catch(error => {
      console.error('Ошибка при сохранении клиента:', error);
      cancelEditClientRow(row);
      showError('Произошла ошибка при сохранении данных клиента. Пожалуйста, попробуйте еще раз.');
    });
}

/**
 * Отмена редактирования клиента
 */
function cancelEditClientRow(row) {
  const table = row.closest('table');
  setRowEditing(row, false);
  removeActionsHeaderIfNeededUtil(table);
}