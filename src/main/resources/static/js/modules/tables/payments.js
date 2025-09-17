// =========================
// Модуль для таблицы платежей
// =========================

import { setRowEditing, addActionButtonsUtil, removeActionsHeaderIfNeededUtil, showError, parseCurrency, formatCurrency, formatDateToDDMMYYYYUtil, formatClientNameShortUtil } from '../utils.js';
import { fetchJson } from '../api.js';

/**
 * Инициализация таблицы платежей
 */
export function initPaymentsTable() {
  if (document.querySelector('main h1') && document.querySelector('main h1').textContent.includes('Платежи')) {
    const paymentsTable = document.querySelector('main table tbody');
    if (paymentsTable) {
      paymentsTable.addEventListener('click', function (e) {
        const row = e.target.closest('tr');
        if (row && !row.classList.contains('editing')) {
          if (e.target.classList.contains('delete-btn')) {
            return;
          }
          editPaymentRow(row);
        }
      });
    }
  }
}

/**
 * Редактирование строки платежа
 */
function editPaymentRow(row) {
  setRowEditing(row, true);
  const cells = row.querySelectorAll('td');
  const paymentId = cells[0].textContent;
  row.dataset.recordId = paymentId;

  // Дата платежа
  const paymentDateCell = cells[1];
  const paymentDateValue = paymentDateCell.textContent;
  let formattedDate = '';
  if (paymentDateValue.includes('.')) {
    const [day, month, year] = paymentDateValue.split('.');
    formattedDate = `${year}-${month}-${day}`;
  } else {
    formattedDate = paymentDateValue;
  }
  const paymentDateInput = document.createElement('input');
  paymentDateInput.type = 'date';
  paymentDateInput.value = formattedDate;
  paymentDateInput.className = 'edit-input';
  paymentDateCell.innerHTML = '';
  paymentDateCell.appendChild(paymentDateInput);

  // Сумма
  const amountCell = cells[2];
  const amountValue = String(parseCurrency(amountCell.textContent));
  const amountInput = document.createElement('input');
  amountInput.type = 'number';
  amountInput.step = '0.01';
  amountInput.value = amountValue;
  amountInput.className = 'edit-input';
  amountCell.innerHTML = '';
  amountCell.appendChild(amountInput);

  // Сделка (ID)
  const dealIdCell = cells[3];
  const dealIdValue = dealIdCell.textContent.match(/\d+/) ? dealIdCell.textContent.match(/\d+/)[0] : dealIdCell.textContent;
  const dealIdInput = document.createElement('input');
  dealIdInput.type = 'number';
  dealIdInput.value = dealIdValue;
  dealIdInput.className = 'edit-input';
  dealIdInput.dataset.field = 'dealId';
  dealIdCell.innerHTML = '';
  dealIdCell.appendChild(dealIdInput);
  dealIdInput.addEventListener('change', function () {
    updateDealDetails(this.value, row);
  });

  const actionsCell = addActionButtonsUtil(row);
  actionsCell.querySelector('.save-btn').addEventListener('click', () => savePaymentRow(row));
  actionsCell.querySelector('.cancel-btn').addEventListener('click', () => cancelEditPaymentRow(row));
}

/**
 * Сохранение строки платежа
 */
function savePaymentRow(row) {
  const paymentId = row.dataset.recordId;
  const cells = row.querySelectorAll('td');
  const paymentDate = cells[1].querySelector('input').value;
  const amount = cells[2].querySelector('input').value;
  const dealId = cells[3].querySelector('input').value;

  const parsedAmount = parseCurrency(amount);
  const parsedDealId = parseInt(dealId);

  const updates = {
    paymentDate: paymentDate,
    amount: isNaN(parsedAmount) ? 0 : parsedAmount,
    idDeal: isNaN(parsedDealId) ? 0 : parsedDealId
  };

  fetchJson(`/payments/update/${paymentId}`, {
    method: 'POST',
    body: JSON.stringify(updates)
  })
    .then(() => {
      cells[1].textContent = formatDateToDDMMYYYYUtil(paymentDate);
      cells[2].textContent = formatCurrency(parsedAmount);
      cells[3].textContent = dealId;
      row.removeChild(row.lastChild);
      setRowEditing(row, false);
      const table = row.closest('table');
      removeActionsHeaderIfNeededUtil(table);
    })
    .catch(error => {
      console.error('Ошибка платежа:', error);
      cancelEditPaymentRow(row);
      showError('Ошибка при сохранении данных платежа. Попробуйте ещё раз.');
    });
}

/**
 * Отмена редактирования
 */
function cancelEditPaymentRow(row) {
  const table = row.closest('table');
  row.innerHTML = row.dataset.originalContent;
  row.classList.remove('editing');
  removeActionsHeaderIfNeededUtil(table);
}

/**
 * Обновление информации по сделке
 */
async function updateDealDetails(dealId, row) {
  if (!dealId || !row) return;
  try {
    const deal = await fetchJson(`/api/deals/${dealId}/for-table`);
    if (!deal) return;
    const cells = row.querySelectorAll('td');
    if (!cells || cells.length < 7) return;
    cells[4].textContent = deal.dealDate ? formatDateToDDMMYYYYUtil(deal.dealDate) : '';
    cells[5].textContent = deal.clientName ? formatClientNameShortUtil(deal.clientName) : '';
    cells[6].textContent = deal.propertyAddress ?? '';
  } catch (error) {
    console.error('Ошибка updateDealDetails:', error);
    showError('Ошибка получения данных сделки. Проверьте ID и попробуйте снова.');
  }
}