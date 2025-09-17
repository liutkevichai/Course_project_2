// =========================
// Модуль выпадающего меню в боковой панели
// =========================

/**
 * Инициализация выпадающего меню в aside
 */
import { STORAGE_KEYS } from '../utils.js';

export function initDropdownMenu() {
  const dropdownToggle = document.querySelector('aside .dropdown-toggle');
  const dropdownMenu = document.querySelector('aside .dropdown-menu');
  const menuStateKey = STORAGE_KEYS.dropdownMenuState;

  const setMenuState = (state) => {
    if (state === 'open') {
      dropdownMenu?.classList.add('show');
      localStorage.setItem(menuStateKey, 'open');
    } else {
      dropdownMenu?.classList.remove('show');
      localStorage.setItem(menuStateKey, 'closed');
    }
  };

  // Проверяем сохранённое состояние при загрузке
  if (localStorage.getItem(menuStateKey) === 'open') {
    setMenuState('open');
  }

  // Обработчик на клик
  dropdownToggle?.addEventListener('click', (e) => {
    e.preventDefault();
    const isMenuOpen = dropdownMenu?.classList.contains('show');
    setMenuState(isMenuOpen ? 'closed' : 'open');
  });

  // Следим за кликами на ссылки
  const sidebarLinks = document.querySelectorAll('aside nav a');
  sidebarLinks.forEach(link => {
    link.addEventListener('click', function () {
      if (this.closest('.dropdown-menu')) {
        localStorage.setItem(menuStateKey, 'open');
      } else {
        localStorage.setItem(menuStateKey, 'closed');
      }
    });
  });
}