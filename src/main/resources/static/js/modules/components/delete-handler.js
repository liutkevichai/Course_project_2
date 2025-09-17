// =========================
// Модуль обработчика удаления записей
// =========================

import { showError } from '../utils.js';
import { safeJson } from '../api.js';

/**
 * Инициализация глобального обработчика удаления
 */
export function initDeleteHandler() {
  document.body.addEventListener('click', function (e) {
    if (e.target.classList.contains('delete-btn')) {
      if (!confirm('Вы уверены, что хотите удалить эту запись?')) {
        return;
      }

      const row = e.target.closest('tr');
      if (!row) return;

      const recordId = row.cells[0].textContent;
      if (!recordId) {
        showError('Не удалось получить ID записи для удаления.');
        return;
      }

      fetch(`${window.location.pathname}/delete/${recordId}`, { method: 'DELETE' })
        .then(async response => {
          if (response.ok) {
            row.remove();
          } else {
            const errorData = await safeJson(response);
            showError(errorData.message || 'Ошибка при удалении записи.');
          }
        })
        .catch(error => {
          console.error('[delete-handler] Ошибка при удалении:', error);
          showError('Сетевая ошибка при попытке удаления.');
        });
    }
  });
}