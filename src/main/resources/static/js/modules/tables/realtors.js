// =========================
// Модуль для таблицы риелторов
// =========================

import { addActionButtonsUtil, removeActionsHeaderIfNeededUtil, showError } from '../utils.js';

/**
 * Инициализация таблицы риелторов
 */
export function initRealtorsTable() {
  if (document.querySelector('main h1') && document.querySelector('main h1').textContent.includes('Риелторы')) {
    const realtorsTable = document.querySelector('main table tbody');
    if (realtorsTable) {
      realtorsTable.addEventListener('click', function (e) {
        const row = e.target.closest('tr');
        if (row && !row.classList.contains('editing')) {
          if (e.target.classList.contains('delete-btn')) {
            return;
          }
          editRealtorRow(row);
        }
      });
    }
  }
}

/**
 * Редактирование строки риелтора
 */
function editRealtorRow(row) {
  const cells = row.querySelectorAll('td');
  const realtorId = cells[0].textContent;

  row.dataset.originalContent = row.innerHTML;
  row.dataset.recordId = realtorId;
  row.classList.add('editing');

  // Заменяем содержимое ячеек на поля ввода
  for (let i = 1; i < cells.length; i++) {
    const input = document.createElement('input');
    input.type = 'text';
    input.value = cells[i].textContent;
    input.className = 'edit-input';
    cells[i].innerHTML = '';
    cells[i].appendChild(input);
  }

  const actionsCell = addActionButtonsUtil(row);
  actionsCell.querySelector('.save-btn').addEventListener('click', () => saveRealtorRow(row));
  actionsCell.querySelector('.cancel-btn').addEventListener('click', () => cancelEditRealtorRow(row));
}

/**
 * Сохранение изменений риелтора
 */
function saveRealtorRow(row) {
  const cells = row.querySelectorAll('td');
  const inputs = row.querySelectorAll('input.edit-input');

  const updates = {
    firstName: inputs[0].value,
    lastName: inputs[1].value,
    phone: inputs[2].value,
    email: inputs[3].value,
    experienceYears: inputs[4].value
  };

  const id = row.dataset.recordId;
  fetch(`/realtors/update/${id}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(updates)
  })
    .then(response => {
      if (response.ok) {
        cells[1].textContent = updates.firstName;
        cells[2].textContent = updates.lastName;
        cells[3].textContent = updates.phone;
        cells[4].textContent = updates.email;
        cells[5].textContent = updates.experienceYears;
        row.removeChild(row.lastChild);
        row.classList.remove('editing');
        removeActionsHeaderIfNeededUtil(row.closest('table'));
      } else {
        cancelEditRealtorRow(row);
        showError('Ошибка при сохранении риелтора. Попробуйте снова.');
      }
    })
    .catch(error => {
      console.error('Ошибка при сохранении риелтора:', error);
      cancelEditRealtorRow(row);
      showError('Ошибка сети при сохранении риелтора.');
    });
}

/**
 * Отмена редактирования риелтора
 */
function cancelEditRealtorRow(row) {
  row.innerHTML = row.dataset.originalContent;
  row.classList.remove('editing');
  removeActionsHeaderIfNeededUtil(row.closest('table'));
}