// =========================
// API функции
// =========================

import { DEBUG } from './utils.js';

// Безопасный парсинг JSON-ответа
export const safeJson = async (response) => {
  try {
    return await response.json();
  } catch (error) {
    try {
      const text = await response.text();
      return { message: text };
    } catch (innerError) {
      // Возвращаем fallback при сбое разбора ответа
      return {
        message: innerError?.message || error?.message || "Неизвестная ошибка при обработке ответа сервера"
      };
    }
  }
};

// Универсальная обертка над fetch с единообразной обработкой ошибок
// В случае ошибки бросает исключение с сообщением
export const fetchJson = async (url, options = {}) => {
  const response = await fetch(url, {
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options
  });

  if (!response.ok) {
    let errorInfo;
    try {
      errorInfo = await response.json();
    } catch (parseError) {
      // Если не удалось распарсить JSON — используем статус ответа
      errorInfo = { message: parseError?.message || response.statusText };
    }
    if (DEBUG) console.error('[api]', response.status, errorInfo);
    throw new Error(errorInfo.message || `Ошибка запроса: ${response.status}`);
  }

  return await safeJson(response);
};