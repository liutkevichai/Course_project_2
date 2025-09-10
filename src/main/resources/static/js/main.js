document.addEventListener('DOMContentLoaded', function () {
    const dropdownToggle = document.querySelector('aside .dropdown-toggle');
    const dropdownMenu = document.querySelector('aside .dropdown-menu');
    const menuStateKey = 'dropdownMenuState';

    // Функция для установки состояния меню
    const setMenuState = (state) => {
        if (state === 'open') {
            dropdownMenu.classList.add('show');
            localStorage.setItem(menuStateKey, 'open');
        } else {
            dropdownMenu.classList.remove('show');
            localStorage.setItem(menuStateKey, 'closed');
        }
    };

    // Проверяем состояние меню при загрузке страницы
    if (localStorage.getItem(menuStateKey) === 'open') {
        setMenuState('open');
    }

    // Обработчик для кнопки открытия/закрытия меню
    if (dropdownToggle) {
        dropdownToggle.addEventListener('click', function (e) {
            e.preventDefault();
            const isMenuOpen = dropdownMenu.classList.contains('show');
            setMenuState(isMenuOpen ? 'closed' : 'open');
        });
    }

    // Получаем все ссылки в боковой панели
    const sidebarLinks = document.querySelectorAll('aside nav a');

    sidebarLinks.forEach(link => {
        link.addEventListener('click', function() {
            // Если ссылка находится внутри выпадающего меню, сохраняем его открытым
            if (this.closest('.dropdown-menu')) {
                localStorage.setItem(menuStateKey, 'open');
            } else {
                // Если ссылка вне меню, закрываем его
                localStorage.setItem(menuStateKey, 'closed');
            }
        });
    });

    // Обработчики для формы добавления недвижимости
    // ВАЖНО: атрибуты th:* удаляются на стороне сервера, поэтому селектор по th:object не работает в браузере
    // Используем id формы, заданный в шаблоне
    const propertyForm = document.getElementById('propertyAddForm');
    if (propertyForm) {
        const countrySelect = propertyForm.querySelector('#idCountry');
        const regionSelect = propertyForm.querySelector('#idRegion');
        const citySelect = propertyForm.querySelector('#idCity');
        const districtSelect = propertyForm.querySelector('#idDistrict');
        const streetSelect = propertyForm.querySelector('#idStreet');

        const resetAndDisable = (selectElement, defaultOptionText) => {
            if (selectElement) {
                selectElement.innerHTML = `<option value="">${defaultOptionText}</option>`;
                selectElement.disabled = true;
            }
        };

        // Функция для обновления регионов по ID страны
        const updateRegionsByCountry = (countryId) => {
            if (!countryId) {
                resetAndDisable(regionSelect, 'Выберите регион');
                resetAndDisable(citySelect, 'Выберите город');
                resetAndDisable(districtSelect, 'Выберите район');
                resetAndDisable(streetSelect, 'Выберите улицу');
                return;
            }

            fetch(`/api/geography/countries/${countryId}/regions`)
                .then(response => response.json())
                .then(regions => {
                    regionSelect.innerHTML = '<option value="">Выберите регион</option>';
                    regions.forEach(region => {
                        const option = document.createElement('option');
                        option.value = region.idRegion;
                        option.textContent = region.name;
                        regionSelect.appendChild(option);
                    });
                    regionSelect.disabled = false;
                })
                .catch(error => console.error('Ошибка при получении списка регионов:', error));
        };

        // Функция для обновления городов по ID региона
        const updateCitiesByRegion = (regionId) => {
            if (!regionId) {
                resetAndDisable(citySelect, 'Выберите город');
                resetAndDisable(districtSelect, 'Выберите район');
                resetAndDisable(streetSelect, 'Выберите улицу');
                return;
            }

            fetch(`/api/geography/regions/${regionId}/cities`)
                .then(response => response.json())
                .then(cities => {
                    citySelect.innerHTML = '<option value="">Выберите город</option>';
                    cities.forEach(city => {
                        const option = document.createElement('option');
                        option.value = city.idCity;
                        option.textContent = city.cityName;
                        citySelect.appendChild(option);
                    });
                    citySelect.disabled = false;
                })
                .catch(error => console.error('Ошибка при получении списка городов:', error));
        };

        // Функция для обновления районов и улиц по ID города
        const updateDistrictsAndStreetsByCity = (cityId) => {
            if (!cityId) {
                resetAndDisable(districtSelect, 'Выберите район');
                resetAndDisable(streetSelect, 'Выберите улицу');
                return;
            }

            // Обновление районов
            fetch(`/api/geography/cities/${cityId}/districts`)
                .then(response => response.json())
                .then(districts => {
                    districtSelect.innerHTML = '<option value="">Выберите район</option>';
                    districts.forEach(district => {
                        const option = document.createElement('option');
                        option.value = district.idDistrict;
                        option.textContent = district.districtName;
                        districtSelect.appendChild(option);
                    });
                    districtSelect.disabled = false;
                })
                .catch(error => console.error('Ошибка при получении списка районов:', error));

            // Обновление улиц
            fetch(`/api/geography/cities/${cityId}/streets`)
                .then(response => response.json())
                .then(streets => {
                    streetSelect.innerHTML = '<option value="">Выберите улицу</option>';
                    streets.forEach(street => {
                        const option = document.createElement('option');
                        option.value = street.idStreet;
                        option.textContent = street.streetName;
                        streetSelect.appendChild(option);
                    });
                    streetSelect.disabled = false;
                })
                .catch(error => console.error('Ошибка при получении списка улиц:', error));
        };

        // Обработчики событий
        if (countrySelect) {
            countrySelect.addEventListener('change', () => {
                updateRegionsByCountry(countrySelect.value);
                resetAndDisable(citySelect, 'Выберите город');
                resetAndDisable(districtSelect, 'Выберите район');
                resetAndDisable(streetSelect, 'Выберите улицу');
            });
        }
        if (regionSelect) {
            regionSelect.addEventListener('change', () => {
                updateCitiesByRegion(regionSelect.value);
                resetAndDisable(districtSelect, 'Выберите район');
                resetAndDisable(streetSelect, 'Выберите улицу');
            });
        }
        if (citySelect) {
            citySelect.addEventListener('change', () => updateDistrictsAndStreetsByCity(citySelect.value));
        }

        // Инициализация формы
        const initializeForm = () => {
            resetAndDisable(regionSelect, 'Сначала выберите страну');
            resetAndDisable(citySelect, 'Сначала выберите регион');
            resetAndDisable(districtSelect, 'Сначала выберите город');
            resetAndDisable(streetSelect, 'Сначала выберите город');
        };

        initializeForm();
    // Обработчики для формы поиска недвижимости
    const propertySearchForm = document.getElementById('propertySearchForm');
    if (propertySearchForm) {
        const citySelect = propertySearchForm.querySelector('#searchCityId');
        const districtSelect = propertySearchForm.querySelector('#searchDistrictId');
        const streetSelect = propertySearchForm.querySelector('#searchStreetId');

        const resetAndDisable = (selectElement, defaultOptionText) => {
            if (selectElement) {
                selectElement.innerHTML = `<option value="">${defaultOptionText}</option>`;
                selectElement.disabled = true;
            }
        };

        const updateDistrictsAndStreets = (cityId, selectedDistrictId, selectedStreetId) => {
            resetAndDisable(districtSelect, 'Все районы');
            resetAndDisable(streetSelect, 'Все улицы');

            if (!cityId) return;

            // Загружаем районы
            fetch(`/api/geography/cities/${cityId}/districts`)
                .then(response => response.json())
                .then(districts => {
                    districts.forEach(district => {
                        const option = document.createElement('option');
                        option.value = district.idDistrict;
                        option.textContent = district.districtName;
                        if (district.idDistrict == selectedDistrictId) {
                            option.selected = true;
                        }
                        districtSelect.appendChild(option);
                    });
                    districtSelect.disabled = false;
                });

            // Загружаем улицы
            fetch(`/api/geography/cities/${cityId}/streets`)
                .then(response => response.json())
                .then(streets => {
                    streets.forEach(street => {
                        const option = document.createElement('option');
                        option.value = street.idStreet;
                        option.textContent = street.streetName;
                        if (street.idStreet == selectedStreetId) {
                            option.selected = true;
                        }
                        streetSelect.appendChild(option);
                    });
                    streetSelect.disabled = false;
                });
        };

        citySelect.addEventListener('change', () => {
            updateDistrictsAndStreets(citySelect.value, null, null);
        });

        if (citySelect.value) {
            const selectedDistrictId = new URLSearchParams(window.location.search).get('districtId');
            const selectedStreetId = new URLSearchParams(window.location.search).get('streetId');
            updateDistrictsAndStreets(citySelect.value, selectedDistrictId, selectedStreetId);
        }
    }
    }
    
    // Универсальная функция для добавления кнопок действий
    function addActionButtons(row) {
        // Добавляем заголовок "Действия" если его еще нет
        const table = row.closest('table');
        const headerRow = table.querySelector('thead tr');
        if (!headerRow.querySelector('th[data-actions-header]')) {
            const actionsHeader = document.createElement('th');
            actionsHeader.textContent = 'Действия';
            actionsHeader.setAttribute('data-actions-header', 'true');
            headerRow.appendChild(actionsHeader);
        }
        
        // Создаем ячейку с кнопками
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
    }
    
    // Универсальная функция для удаления заголовка действий если нет редактируемых строк
    function removeActionsHeaderIfNeeded(table) {
        const editingRows = table.querySelectorAll('tbody tr.editing');
        if (editingRows.length === 0) {
            const actionsHeader = table.querySelector('th[data-actions-header]');
            if (actionsHeader) {
                actionsHeader.remove();
            }
        }
    }
    
    // Обработчик для редактирования строк таблицы клиентов
    // Проверяем, что мы на странице клиентов
    if (document.querySelector('main h1') && document.querySelector('main h1').textContent.includes('Клиенты')) {
        const clientsTable = document.querySelector('main table tbody');
        if (clientsTable) {
            clientsTable.addEventListener('click', function(e) {
                const row = e.target.closest('tr');
                if (row && !row.classList.contains('editing')) {
                    // Check if the click was on a delete button inside this row
                    if (e.target.classList.contains('delete-btn')) {
                         // The generic delete handler will take care of it.
                        return;
                    }
                    editClientRow(row);
                }
            });
        }
    }
    
    // Функция для редактирования строки клиента
    function editClientRow(row) {
        // Сохраняем исходное содержимое строки
        row.dataset.originalContent = row.innerHTML;
        
        // Получаем данные из ячеек
        const cells = row.querySelectorAll('td');
        const clientId = cells[0].textContent;
        
        // Добавляем атрибут data-record-id
        row.dataset.recordId = clientId;
        
        // Добавляем класс editing
        row.classList.add('editing');
        
        // Заменяем содержимое ячеек на input поля
        cells.forEach((cell, index) => {
            // Пропускаем первую ячейку (ID) и последнюю (будет для кнопок)
            if (index === 0) return;
            
            const cellValue = cell.textContent;
            let inputType = 'text';
            
            // Определяем тип input в зависимости от столбца
            if (index === 4) { // Телефон
                inputType = 'tel';
            } else if (index === 5) { // Email
                inputType = 'email';
            }
            
            // Создаем input элемент
            const input = document.createElement('input');
            input.type = inputType;
            input.value = cellValue;
            input.className = 'edit-input';
            
            // Заменяем содержимое ячейки на input
            cell.innerHTML = '';
            cell.appendChild(input);
        });
        
        // Добавляем кнопки действий
        const actionsCell = addActionButtons(row);
        
        // Обработчики для кнопок
        const saveBtn = actionsCell.querySelector('.save-btn');
        const cancelBtn = actionsCell.querySelector('.cancel-btn');
        
        saveBtn.addEventListener('click', function() {
            saveClientRow(row);
        });
        
        cancelBtn.addEventListener('click', function() {
            cancelEditClientRow(row);
        });
    }
    
    // Функция для сохранения изменений в строке клиента
    function saveClientRow(row) {
        // Получаем ID записи из data-record-id
        const clientId = row.dataset.recordId;
        
        // Получаем все ячейки строки
        const cells = row.querySelectorAll('td');
        
        // Собираем новые значения из полей input
        // Индексы соответствуют структуре таблицы:
        // 0 - ID (не редактируется), 1 - Имя, 2 - Фамилия, 3 - Отчество, 4 - Телефон, 5 - Email
        const firstName = cells[1].querySelector('input').value;
        const lastName = cells[2].querySelector('input').value;
        const middleName = cells[3].querySelector('input').value;
        const phone = cells[4].querySelector('input').value;
        const email = cells[5].querySelector('input').value;
        
        // Формируем объект updates с ключами, соответствующими полям в Client.java
        const updates = {
            firstName: firstName,
            lastName: lastName,
            middleName: middleName,
            phone: phone,
            email: email
        };
        
        // Отправляем асинхронный POST запрос
        fetch(`/clients/update/${clientId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updates)
        })
        .then(response => {
            if (response.ok) {
                // В случае успешного ответа обновляем UI
                cells[1].textContent = firstName;
                cells[2].textContent = lastName;
                cells[3].textContent = middleName;
                cells[4].textContent = phone;
                cells[5].textContent = email;
                
                // Удаляем последнюю ячейку с кнопками
                row.removeChild(row.lastChild);
                
                // Убираем класс editing
                row.classList.remove('editing');
                
                // Удаляем заголовок действий если нужно
                const table = row.closest('table');
                removeActionsHeaderIfNeeded(table);
            } else {
                // В случае ошибки выводим сообщение в консоль
                console.error('Ошибка при сохранении данных клиента:', response.status, response.statusText);
                // Возвращаем строку в исходное состояние
                cancelEditClientRow(row);
                // Показываем пользователю уведомление об ошибке
                alert('Произошла ошибка при сохранении данных клиента. Пожалуйста, попробуйте еще раз.');
            }
        })
        .catch(error => {
            // В случае ошибки сети или другой исключительной ситуации
            console.error('Ошибка при отправке запроса:', error);
            // Возвращаем строку в исходное состояние
            cancelEditClientRow(row);
            // Показываем пользователю уведомление об ошибке
            alert('Произошла ошибка при сохранении данных клиента. Пожалуйста, проверьте подключение к сети и попробуйте еще раз.');
        });
    }
    
    // Функция для отмены редактирования строки клиента
    function cancelEditClientRow(row) {
        const table = row.closest('table');
        // Восстанавливаем исходное содержимое строки
        row.innerHTML = row.dataset.originalContent;
        // Удаляем класс editing
        row.classList.remove('editing');
        // Удаляем заголовок действий если нужно
        removeActionsHeaderIfNeeded(table);
    }
    
    // Обработчик для редактирования строк таблицы платежей
    // Проверяем, что мы на странице платежей
    if (document.querySelector('main h1') && document.querySelector('main h1').textContent.includes('Платежи')) {
        const paymentsTable = document.querySelector('main table tbody');
        if (paymentsTable) {
            paymentsTable.addEventListener('click', function(e) {
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
    
    // Функция для редактирования строки платежа
    function editPaymentRow(row) {
        // Сохраняем исходное содержимое строки
        row.dataset.originalContent = row.innerHTML;
        
        // Получаем данные из ячеек
        const cells = row.querySelectorAll('td');
        const paymentId = cells[0].textContent;
        
        // Добавляем атрибут data-record-id
        row.dataset.recordId = paymentId;
        
        // Добавляем класс editing
        row.classList.add('editing');
        
        // Заменяем содержимое редактируемых ячеек на input поля
        // Редактируемые поля: Дата платежа (1), Сумма (2), Сделка (ID) (3)
        // Нередактируемые поля: Дата сделки (4), Клиент (5), Адрес объекта (6)
        
        // Дата платежа (ячейка 1)
        const paymentDateCell = cells[1];
        const paymentDateValue = paymentDateCell.textContent;
        let formattedDate = '';
        
        // Проверяем, в каком формате дата: DD.MM.YYYY или YYYY-MM-DD
        if (paymentDateValue.includes('.')) {
            // Формат DD.MM.YYYY
            const [day, month, year] = paymentDateValue.split('.');
            formattedDate = `${year}-${month}-${day}`;
        } else if (paymentDateValue.includes('-')) {
            // Формат YYYY-MM-DD
            formattedDate = paymentDateValue;
        } else {
            // Неизвестный формат, оставляем как есть
            formattedDate = paymentDateValue;
        }
        
        const paymentDateInput = document.createElement('input');
        paymentDateInput.type = 'date';
        paymentDateInput.value = formattedDate;
        paymentDateInput.className = 'edit-input';
        paymentDateCell.innerHTML = '';
        paymentDateCell.appendChild(paymentDateInput);
        
        // Сумма (ячейка 2)
        const amountCell = cells[2];
        // Убираем все нечисловые символы, кроме точки и запятой (для десятичных дробей)
        const amountValue = amountCell.textContent.replace(/[^\d.,]/g, '').replace(',', '.');
        const amountInput = document.createElement('input');
        amountInput.type = 'number';
        amountInput.step = '0.01'; // Разрешаем ввод копеек
        amountInput.value = amountValue;
        amountInput.className = 'edit-input';
        amountCell.innerHTML = '';
        amountCell.appendChild(amountInput);
        
        // Сделка (ID) (ячейка 3)
        const dealIdCell = cells[3];
        // Извлекаем только числовую часть ID, убирая возможные дополнительные элементы
        const dealIdValue = dealIdCell.textContent.match(/\d+/) ? dealIdCell.textContent.match(/\d+/)[0] : dealIdCell.textContent;
        const dealIdInput = document.createElement('input');
        dealIdInput.type = 'number';
        dealIdInput.value = dealIdValue;
        dealIdInput.className = 'edit-input';
        dealIdInput.dataset.field = 'dealId'; // Для идентификации поля
        dealIdCell.innerHTML = '';
        dealIdCell.appendChild(dealIdInput);
        
        // Добавляем обработчик события для изменения ID сделки
        dealIdInput.addEventListener('change', function() {
            updateDealDetails(this.value, row);
        });
        
        // Добавляем кнопки действий
        const actionsCell = addActionButtons(row);
        
        // Обработчики для кнопок
        const saveBtn = actionsCell.querySelector('.save-btn');
        const cancelBtn = actionsCell.querySelector('.cancel-btn');
        
        saveBtn.addEventListener('click', function() {
            savePaymentRow(row);
        });
        
        cancelBtn.addEventListener('click', function() {
            cancelEditPaymentRow(row);
        });
    }
    
    // Функция для сохранения изменений в строке платежа
    function savePaymentRow(row) {
        // Получаем ID записи из data-record-id
        const paymentId = row.dataset.recordId;
        
        // Получаем все ячейки строки
        const cells = row.querySelectorAll('td');
        
        // Собираем новые значения из полей input
        // Индексы соответствуют структуре таблицы:
        // 0 - ID (не редактируется), 1 - Дата платежа, 2 - Сумма, 3 - Сделка (ID)
        const paymentDate = cells[1].querySelector('input').value;
        const amount = cells[2].querySelector('input').value;
        const dealId = cells[3].querySelector('input').value;
        
        // Формируем объект updates
        // Убеждаемся, что amount - это корректное число
        const parsedAmount = parseFloat(amount);
        const parsedDealId = parseInt(dealId);
        
        const updates = {
            paymentDate: paymentDate,
            amount: isNaN(parsedAmount) ? 0 : parsedAmount,
            idDeal: isNaN(parsedDealId) ? 0 : parsedDealId
        };
        
        // Отправляем асинхронный POST запрос
        // Предполагаем, что у нас есть эндпоинт для обновления платежа
        // В данном случае используем тот же путь, что и для добавления, но с методом PUT
        fetch(`/payments/update/${paymentId}`, {
            method: 'POST', // Метод POST, как в форме добавления
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updates)
        })
        .then(response => {
            if (response.ok) {
                // В случае успешного ответа обновляем UI
                // Преобразуем дату из формата YYYY-MM-DD в DD.MM.YYYY
                const dateParts = paymentDate.split('-');
                const formattedDate = `${dateParts[2]}.${dateParts[1]}.${dateParts[0]}`;
                cells[1].textContent = formattedDate;
                cells[2].textContent = new Intl.NumberFormat('ru-RU').format(amount) + ' ₽';
                cells[3].textContent = dealId;
                
                // Удаляем последнюю ячейку с кнопками
                row.removeChild(row.lastChild);
                
                // Убираем класс editing
                row.classList.remove('editing');
                
                // Удаляем заголовок действий если нужно
                const table = row.closest('table');
                removeActionsHeaderIfNeeded(table);
            } else {
                // В случае ошибки выводим сообщение в консоль
                console.error('Ошибка при сохранении данных платежа:', response.status, response.statusText);
                // Возвращаем строку в исходное состояние
                cancelEditPaymentRow(row);
                // Показываем пользователю уведомление об ошибке
                alert('Произошла ошибка при сохранении данных платежа. Пожалуйста, попробуйте еще раз.');
            }
        })
        .catch(error => {
            // В случае ошибки сети или другой исключительной ситуации
            console.error('Ошибка при отправке запроса:', error);
            // Возвращаем строку в исходное состояние
            cancelEditPaymentRow(row);
            // Показываем пользователю уведомление об ошибке
            alert('Произошла ошибка при сохранении данных платежа. Пожалуйста, проверьте подключение к сети и попробуйте еще раз.');
        });
    }
    
    // Функция для отмены редактирования строки платежа
    function cancelEditPaymentRow(row) {
        const table = row.closest('table');
        // Восстанавливаем исходное содержимое строки
        row.innerHTML = row.dataset.originalContent;
        // Удаляем класс editing
        row.classList.remove('editing');
        // Удаляем заголовок действий если нужно
        removeActionsHeaderIfNeeded(table);
    }
    
    // Функция для форматирования даты из YYYY-MM-DD в DD.MM.YYYY
    function formatDateToDDMMYYYY(dateString) {
        if (!dateString) return '';
        
        const parts = dateString.split('-');
        if (parts.length === 3) {
            return `${parts[2]}.${parts[1]}.${parts[0]}`;
        }
        return dateString;
    }
    
    // Функция для форматирования имени клиента в формат "Фамилия И."
    function formatClientNameShort(clientName) {
        if (!clientName) return '';
        
        const parts = clientName.trim().split(/\s+/);
        if (parts.length >= 2) {
            return `${parts[0]} ${parts[1].charAt(0)}.`;
        }
        return clientName;
    }
    
    // Функция для обновления нередактируемых полей при изменении ID сделки
    function updateDealDetails(dealId, row) {
        if (!dealId) return;
        
        // Отправляем запрос к серверу для получения данных сделки
        fetch(`/api/deals/${dealId}/for-table`)
            .then(response => response.json())
            .then(deal => {
                // Получаем ячейки строки
                const cells = row.querySelectorAll('td');
                
                // Обновляем нередактируемые поля:
                // Дата сделки (ячейка 4) - форматируем дату
                cells[4].textContent = formatDateToDDMMYYYY(deal.dealDate);
                
                // Клиент (ячейка 5) - форматируем имя клиента
                cells[5].textContent = formatClientNameShort(deal.clientName);
                
                // Адрес объекта (ячейка 6)
                cells[6].textContent = deal.propertyAddress;
            })
            .catch(error => {
                console.error('Ошибка при получении данных сделки:', error);
                // Показываем пользователю уведомление об ошибке
                alert('Произошла ошибка при получении данных сделки. Пожалуйста, проверьте ID сделки и попробуйте еще раз.');
            });
    }
    
    // Обработчик для редактирования строк таблицы сделок
    // Проверяем, что мы на странице сделок
    if (document.querySelector('main h1') && document.querySelector('main h1').textContent.includes('Сделки')) {
        const dealsTable = document.querySelector('main table tbody');
        if (dealsTable) {
            console.log('Найдена таблица сделок, добавляем обработчик событий');
            dealsTable.addEventListener('click', function(e) {
                const row = e.target.closest('tr');
                if (row && !row.classList.contains('editing')) {
                    if (e.target.classList.contains('delete-btn')) {
                        return;
                    }
                    editDealRow(row);
                }
            });
        } else {
            console.log('Таблица сделок не найдена');
        }
    } else {
        console.log('Не на странице сделок');
    }
    
    // Функция для редактирования строки сделки
    function editDealRow(row) {
        console.log('Вызов функции editDealRow для строки:', row);
        row.dataset.originalContent = row.innerHTML;
        const cells = row.querySelectorAll('td');
        const dealId = cells[0].textContent;
        row.dataset.recordId = dealId;
        row.classList.add('editing');

        // --- Редактируемые поля ---
        const dealDateCell = cells[1];
        const propertyAddressCell = cells[2];
        const clientCell = cells[3];
        const realtorCell = cells[4];
        const costCell = cells[5];
        const dealTypeCell = cells[6];

        // --- Исходные значения ---
        const dealDateValue = dealDateCell.textContent;
        const propertyAddressValue = propertyAddressCell.textContent;
        const clientValue = clientCell.textContent;
        const realtorValue = realtorCell.textContent;
        const costValue = costCell.textContent.replace(/[^\d.,]/g, '').replace(',', '.');
        const dealTypeValue = dealTypeCell.textContent;
        
        console.log("Исходные значения:", { propertyAddressValue, clientValue, realtorValue, dealTypeValue });


        // --- Подготовка полей для редактирования ---

        // Дата сделки
        let formattedDate = '';
        if (dealDateValue.includes('.')) {
            const [day, month, year] = dealDateValue.split('.');
            formattedDate = `${year}-${month}-${day}`;
        } else {
            formattedDate = dealDateValue;
        }
        dealDateCell.innerHTML = `<input type="date" value="${formattedDate}" class="edit-input">`;

        // Стоимость
        costCell.innerHTML = `<input type="number" step="0.01" value="${costValue}" class="edit-input">`;

        // Устанавливаем временное содержимое для select'ов
        propertyAddressCell.innerHTML = '<select class="edit-input"><option>Загрузка...</option></select>';
        clientCell.innerHTML = '<select class="edit-input"><option>Загрузка...</option></select>';
        realtorCell.innerHTML = '<select class="edit-input"><option>Загрузка...</option></select>';
        dealTypeCell.innerHTML = '<select class="edit-input"><option>Загрузка...</option></select>';

        // --- Загрузка данных для select'ов ---
        const promises = [
            fetch('/api/properties/for-table').then(res => res.json()),
            fetch('/api/clients').then(res => res.json()),
            fetch('/api/realtors').then(res => res.json()),
            fetch('/api/deal-types').then(res => res.json())
        ];

        Promise.all(promises)
            .then(([properties, clients, realtors, dealTypes]) => {
                console.log("Данные с сервера:", { properties, clients, realtors, dealTypes });

                // Функция для заполнения select'а
                const populateSelect = (cell, items, valueField, textField, selectedValue, nameExtractor) => {
                    const select = cell.querySelector('select');
                    select.innerHTML = `<option value="">Выберите...</option>`;
                    let matchFound = false;
                    
                    // Нормализуем исходное значение из ячейки
                    const normalizedSelectedValue = selectedValue.trim().replace(/\s+/g, ' ');

                    items.forEach(item => {
                        const text = nameExtractor ? nameExtractor(item) : item[textField];
                        // Нормализуем текст опции
                        const normalizedText = text.trim().replace(/\s+/g, ' ');

                        const option = document.createElement('option');
                        option.value = item[valueField];
                        option.textContent = text;
                        
                        // Сравниваем нормализованные строки
                        if (normalizedText === normalizedSelectedValue) {
                            option.selected = true;
                            matchFound = true;
                        }
                        select.appendChild(option);
                    });

                    // Если совпадение не найдено, добавляем исходное значение как выбранное, но без value
                    if (!matchFound && normalizedSelectedValue !== '') {
                        const option = document.createElement('option');
                        option.value = ''; // Явно указываем пустое значение, чтобы не отправилось на сервер
                        option.textContent = selectedValue; // Сохраняем оригинальный текст для отображения
                        option.selected = true;
                        select.insertBefore(option, select.firstChild);
                    }
                };

                // Заполнение select'ов
                populateSelect(propertyAddressCell, properties, 'propertyId', 'tableAddress', propertyAddressValue);
                populateSelect(clientCell, clients, 'idClient', null, clientValue, item => `${item.lastName} ${item.firstName} ${item.middleName || ''}`.trim());
                populateSelect(realtorCell, realtors, 'idRealtor', null, realtorValue, item => `${item.lastName} ${item.firstName} ${item.middleName || ''}`.trim());
                populateSelect(dealTypeCell, dealTypes, 'idDealType', 'dealTypeName', dealTypeValue);

            })
            .catch(error => {
                console.error("Ошибка при загрузке данных для редактирования:", error);
                propertyAddressCell.innerHTML = '<span>Ошибка загрузки</span>';
                clientCell.innerHTML = '<span>Ошибка загрузки</span>';
                realtorCell.innerHTML = '<span>Ошибка загрузки</span>';
                dealTypeCell.innerHTML = '<span>Ошибка загрузки</span>';
            });

        // --- Кнопки управления ---
        const actionsCell = addActionButtons(row);

        actionsCell.querySelector('.save-btn').addEventListener('click', () => saveDealRow(row));
        actionsCell.querySelector('.cancel-btn').addEventListener('click', () => cancelEditDealRow(row));
    }
    
    // Функция для сохранения изменений в строке сделки
    function saveDealRow(row) {
        // Получаем ID записи из data-record-id
        const dealId = row.dataset.recordId;
        
        // Получаем все ячейки строки
        const cells = row.querySelectorAll('td');
        
        // Собираем новые значения из полей input и select
        // Индексы соответствуют структуре таблицы:
        // 0 - ID (не редактируется), 1 - Дата сделки, 2 - Адрес недвижимости, 3 - Клиент, 4 - Риелтор, 5 - Стоимость, 6 - Тип
        const dealDate = cells[1].querySelector('input').value;
        const propertySelect = cells[2].querySelector('select');
        const clientSelect = cells[3].querySelector('select');
        const realtorSelect = cells[4].querySelector('select');
        const dealCost = cells[5].querySelector('input').value;
        const dealTypeSelect = cells[6].querySelector('select');
        
        // Получаем значения из select'ов
        const propertyId = propertySelect.value;
        const clientId = clientSelect.value;
        const realtorId = realtorSelect.value;
        const dealTypeId = dealTypeSelect.value;
        
        // Получаем текстовые значения для отображения
        const propertyText = propertySelect.options[propertySelect.selectedIndex].text;
        const clientText = clientSelect.options[clientSelect.selectedIndex].text;
        const realtorText = realtorSelect.options[realtorSelect.selectedIndex].text;
        const dealTypeText = dealTypeSelect.options[dealTypeSelect.selectedIndex].text;
        
        // Формируем объект updates
        // Убеждаемся, что dealCost - это корректное число
        const parsedCost = parseFloat(dealCost);
        
        const updates = {
            dealDate: dealDate,
            dealCost: isNaN(parsedCost) ? 0 : parsedCost,
            propertyId: parseInt(propertyId) || 0,
            clientId: parseInt(clientId) || 0,
            realtorId: parseInt(realtorId) || 0,
            dealTypeId: parseInt(dealTypeId) || 0
        };
        
        // Отправляем асинхронный POST запрос
        fetch(`/deals/update/${dealId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updates)
        })
        .then(response => {
            if (response.ok) {
                // В случае успешного ответа обновляем UI
                // Преобразуем дату из формата YYYY-MM-DD в DD.MM.YYYY
                const dateParts = dealDate.split('-');
                const formattedDate = `${dateParts[2]}.${dateParts[1]}.${dateParts[0]}`;
                cells[1].textContent = formattedDate;
                cells[2].textContent = propertyText;
                cells[3].textContent = clientText;
                cells[4].textContent = realtorText;
                cells[5].textContent = new Intl.NumberFormat('ru-RU').format(dealCost) + ' ₽';
                cells[6].textContent = dealTypeText;
                
                // Удаляем последнюю ячейку с кнопками
                row.removeChild(row.lastChild);
                
                // Убираем класс editing
                row.classList.remove('editing');
                
                // Удаляем заголовок действий если нужно
                const table = row.closest('table');
                removeActionsHeaderIfNeeded(table);
            } else {
                // В случае ошибки выводим сообщение в консоль
                console.error('Ошибка при сохранении данных сделки:', response.status, response.statusText);
                // Возвращаем строку в исходное состояние
                cancelEditDealRow(row);
                // Показываем пользователю уведомление об ошибке
                alert('Произошла ошибка при сохранении данных сделки. Пожалуйста, попробуйте еще раз.');
            }
        })
        .catch(error => {
            // В случае ошибки сети или другой исключительной ситуации
            console.error('Ошибка при отправке запроса:', error);
            // Возвращаем строку в исходное состояние
            cancelEditDealRow(row);
            // Показываем пользователю уведомление об ошибке
            alert('Произошла ошибка при сохранении данных сделки. Пожалуйста, проверьте подключение к сети и попробуйте еще раз.');
        });
    }
    
    // Функция для отмены редактирования строки сделки
    function cancelEditDealRow(row) {
        const table = row.closest('table');
        // Восстанавливаем исходное содержимое строки
        row.innerHTML = row.dataset.originalContent;
        // Удаляем класс editing
        row.classList.remove('editing');
        // Удаляем заголовок действий если нужно
        removeActionsHeaderIfNeeded(table);
    }
    
    // Обработчик для редактирования строк таблицы риелторов
    // Проверяем, что мы на странице риелторов
    if (document.querySelector('main h1') && document.querySelector('main h1').textContent.includes('Риелторы')) {
        const realtorsTable = document.querySelector('main table tbody');
        if (realtorsTable) {
            realtorsTable.addEventListener('click', function(e) {
                const row = e.target.closest('tr');
                if (row && !row.classList.contains('editing')) {
                    // Check if the click was on a delete button inside this row
                    if (e.target.classList.contains('delete-btn')) {
                         // The generic delete handler will take care of it.
                        return;
                    }
                    // Получаем данные из ячеек
                    const cells = row.querySelectorAll('td');
                    const realtorId = cells[0].textContent;
                    
                    // Добавляем класс editing
                    row.classList.add('editing');
                    
                    // Сохраняем исходное HTML-содержимое строки
                    row.dataset.originalContent = row.innerHTML;
                    row.dataset.recordId = realtorId;
                    
                    // Заменяем содержимое ячеек на поля ввода, сохраняя исходные значения
                    for (let i = 1; i < cells.length; i++) { // Пропускаем первую ячейку (ID)
                        const cellValue = cells[i].textContent;
                        const input = document.createElement('input');
                        input.type = 'text';
                        input.value = cellValue;
                        input.className = 'edit-input';
                        cells[i].innerHTML = '';
                        cells[i].appendChild(input);
                    }
                    
                    // Добавляем кнопки действий
                    const actionsCell = addActionButtons(row);
                    
                    // Добавляем обработчики для кнопок
                    const saveBtn = actionsCell.querySelector('.save-btn');
                    const cancelBtn = actionsCell.querySelector('.cancel-btn');
                    
                    saveBtn.addEventListener('click', function() {
                        // Собираем данные из полей ввода
                        const inputs = row.querySelectorAll('input.edit-input');
                        
                        // Формируем объект updates с ключами, соответствующими полям в Realtor.java
                        const updates = {
                            firstName: inputs[0].value, // Имя
                            lastName: inputs[1].value,  // Фамилия
                            phone: inputs[2].value,     // Телефон
                            email: inputs[3].value,     // Email
                            experienceYears: inputs[4].value // Опыт работы
                        };
                        
                        // Получаем id записи из data-record-id атрибута строки
                        const id = row.dataset.recordId;
                        
                        // Формируем POST запрос на эндпоинт /realtors/update/{id}
                        fetch(`/realtors/update/${id}`, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify(updates)
                        })
                        .then(response => {
                            if (response.ok) {
                                // После успешного сохранения обновляем ячейки новыми значениями
                                // и возвращаем строку в исходное состояние
                                cells[1].textContent = inputs[0].value;
                                cells[2].textContent = inputs[1].value;
                                cells[3].textContent = inputs[2].value;
                                cells[4].textContent = inputs[3].value;
                                cells[5].textContent = inputs[4].value;
                                
                                // Удаляем последнюю ячейку с кнопками
                                row.removeChild(row.lastChild);
                                
                                // Убираем класс editing
                                row.classList.remove('editing');
                                
                                // Удаляем заголовок действий если нужно
                                const table = row.closest('table');
                                removeActionsHeaderIfNeeded(table);
                            } else {
                                console.error('Ошибка при сохранении данных риелтора:', response.status, response.statusText);
                                // Возвращаем строку в исходное состояние
                                cancelEditRealtorRow(row);
                                alert('Произошла ошибка при сохранении данных риелтора. Пожалуйста, попробуйте еще раз.');
                            }
                        })
                        .catch(error => {
                            console.error('Ошибка при отправке запроса:', error);
                            // Возвращаем строку в исходное состояние
                            cancelEditRealtorRow(row);
                            alert('Произошла ошибка при сохранении данных риелтора. Пожалуйста, проверьте подключение к сети и попробуйте еще раз.');
                        });
                    });
                    
                    cancelBtn.addEventListener('click', function() {
                        // Возвращаем строке ее исходное HTML-содержимое
                        cancelEditRealtorRow(row);
                    });
                }
            });
        }
    }
    
    // Функция для отмены редактирования строки риелтора
    function cancelEditRealtorRow(row) {
        const table = row.closest('table');
        // Восстанавливаем исходное содержимое строки
        row.innerHTML = row.dataset.originalContent;
        // Удаляем класс editing
        row.classList.remove('editing');
        // Удаляем заголовок действий если нужно
        removeActionsHeaderIfNeeded(table);
    }
    
    // Обработчик для редактирования строк таблицы недвижимости
    // Проверяем, что мы на странице недвижимости
    if (document.querySelector('main h1') && document.querySelector('main h1').textContent.includes('Недвижимость')) {
        const propertiesTable = document.querySelector('main table tbody');
        if (propertiesTable) {
            propertiesTable.addEventListener('click', function(e) {
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
    
    // Функция для редактирования строки недвижимости
    function editPropertyRow(row) {
        // Сохраняем исходное содержимое строки
        row.dataset.originalContent = row.innerHTML;
        
        // Получаем данные из ячеек
        const cells = row.querySelectorAll('td');
        const propertyId = cells[0].textContent;
        
        // Добавляем атрибут data-record-id
        row.dataset.recordId = propertyId;
        
        // Добавляем класс editing
        row.classList.add('editing');
        
        // Заменяем содержимое редактируемых ячеек на input поля
        // Редактируемые поля: Площадь (3), Стоимость (4), Описание (5), Тип (6)
        
        // Площадь (ячейка 3)
        const areaCell = cells[3];
        const areaValue = areaCell.textContent.replace(' м²', ''); // Убираем " м²"
        const areaInput = document.createElement('input');
        areaInput.type = 'number';
        areaInput.step = '0.1';
        areaInput.value = areaValue;
        areaInput.className = 'edit-input';
        areaCell.innerHTML = '';
        areaCell.appendChild(areaInput);
        
        // Стоимость (ячейка 4)
        const costCell = cells[4];
        const costValue = costCell.textContent.replace(/[^\d.,]/g, '').replace(',', '.'); // Оставляем только цифры и десятичные разделители
        const costInput = document.createElement('input');
        costInput.type = 'number';
        costInput.step = '1000';
        costInput.value = costValue;
        costInput.className = 'edit-input';
        costCell.innerHTML = '';
        costCell.appendChild(costInput);

        // Описание (ячейка 5) - используем textarea
        const descriptionCell = cells[5];
        const descriptionValue = descriptionCell.textContent;
        const descriptionTextarea = document.createElement('textarea');
        descriptionTextarea.value = descriptionValue;
        descriptionTextarea.className = 'edit-input';
        descriptionTextarea.rows = 2; // Начальное количество строк
        descriptionCell.innerHTML = '';
        descriptionCell.appendChild(descriptionTextarea);
        
        // Тип недвижимости (ячейка 6)
        const typeCell = cells[6];
        const typeValue = typeCell.textContent;
        // Создаем выпадающий список для типа недвижимости
        createPropertyTypeSelect(typeValue, typeCell);
        
        // Добавляем кнопки действий
        const actionsCell = addActionButtons(row);
        
        // Обработчики для кнопок
        const saveBtn = actionsCell.querySelector('.save-btn');
        const cancelBtn = actionsCell.querySelector('.cancel-btn');
        
        saveBtn.addEventListener('click', function() {
            savePropertyRow(row);
        });
        
        cancelBtn.addEventListener('click', function() {
            cancelEditPropertyRow(row);
        });
    }
    
    // Функция для создания выпадающего списка типов недвижимости
    function createPropertyTypeSelect(currentValue, cell) {
        // Устанавливаем временное содержимое
        cell.innerHTML = '<select class="edit-input"><option>Загрузка...</option></select>';
        
        // Загружаем данные с сервера
        fetch('/api/property-types')
            .then(response => response.json())
            .then(propertyTypes => {
                const select = cell.querySelector('select');
                select.innerHTML = '<option value="">Выберите тип недвижимости</option>';
                let matchFound = false;
                
                propertyTypes.forEach(type => {
                    const option = document.createElement('option');
                    option.value = type.idPropertyType;
                    option.textContent = type.propertyTypeName;
                    
                    // Проверяем, совпадает ли значение
                    if (type.propertyTypeName === currentValue) {
                        option.selected = true;
                        matchFound = true;
                    }
                    select.appendChild(option);
                });
                
                // Если совпадение не найдено, добавляем исходное значение как выбранное, но без value
                if (!matchFound && currentValue !== '') {
                    const option = document.createElement('option');
                    option.value = ''; // Явно указываем пустое значение, чтобы не отправилось на сервер
                    option.textContent = currentValue; // Сохраняем оригинальный текст для отображения
                    option.selected = true;
                    select.insertBefore(option, select.firstChild);
                }
            })
            .catch(error => {
                console.error("Ошибка при загрузке типов недвижимости:", error);
                cell.innerHTML = '<span>Ошибка загрузки</span>';
            });
    }
    
    // Функция для сохранения изменений в строке недвижимости
    function savePropertyRow(row) {
        // Получаем ID записи из data-record-id
        const propertyId = row.dataset.recordId;
        
        // Получаем все ячейки строки
        const cells = row.querySelectorAll('td');
        
        // Собираем новые значения из полей input и select
        // Индексы соответствуют структуре таблицы:
        // 0 - ID (не редактируется), 1 - Адрес, 2 - Город, 3 - Площадь, 4 - Стоимость, 5 - Описание, 6 - Тип
        const area = cells[3].querySelector('input').value;
        const price = cells[4].querySelector('input').value;
        const description = cells[5].querySelector('textarea').value; // Изменено на textarea
        const propertyTypeSelect = cells[6].querySelector('select');
        const propertyTypeId = propertyTypeSelect.value;
        
        // Получаем текстовое значение типа недвижимости для отображения
        const propertyTypeText = propertyTypeSelect.options[propertyTypeSelect.selectedIndex].text;
        
        // Формируем объект updates
        const updates = {
            area: parseFloat(area) || 0,
            price: parseFloat(price) || 0,
            description: description,
            propertyTypeId: parseInt(propertyTypeId) || 0
        };
        
        // Отправляем асинхронный POST запрос
        fetch(`/properties/update/${propertyId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updates)
        })
        .then(response => {
            if (response.ok) {
                // В случае успешного ответа обновляем UI
                cells[3].textContent = area + ' м²';
                cells[4].textContent = new Intl.NumberFormat('ru-RU').format(price) + ' ₽';
                cells[5].textContent = description;
                cells[6].textContent = propertyTypeText;
                
                // Удаляем последнюю ячейку с кнопками
                row.removeChild(row.lastChild);
                
                // Убираем класс editing
                row.classList.remove('editing');
                
                // Удаляем заголовок действий если нужно
                const table = row.closest('table');
                removeActionsHeaderIfNeeded(table);
            } else {
                // В случае ошибки выводим сообщение в консоль
                console.error('Ошибка при сохранении данных недвижимости:', response.status, response.statusText);
                // Возвращаем строку в исходное состояние
                cancelEditPropertyRow(row);
                // Показываем пользователю уведомление об ошибке
                alert('Произошла ошибка при сохранении данных недвижимости. Пожалуйста, попробуйте еще раз.');
            }
        })
        .catch(error => {
            // В случае ошибки сети или другой исключительной ситуации
            console.error('Ошибка при отправке запроса:', error);
            // Возвращаем строку в исходное состояние
            cancelEditPropertyRow(row);
            // Показываем пользователю уведомление об ошибке
            alert('Произошла ошибка при сохранении данных недвижимости. Пожалуйста, проверьте подключение к сети и попробуйте еще раз.');
        });
    }
    
    // Функция для отмены редактирования строки недвижимости
    function cancelEditPropertyRow(row) {
        const table = row.closest('table');
        // Восстанавливаем исходное содержимое строки
        row.innerHTML = row.dataset.originalContent;
        // Удаляем класс editing
        row.classList.remove('editing');
        // Удаляем заголовок действий если нужно
        removeActionsHeaderIfNeeded(table);
    }
    
    // Обработчик для кнопок удаления с использованием делегирования событий
    document.body.addEventListener('click', function(e) {
        if (e.target.classList.contains('delete-btn')) {
            // Показываем диалоговое окно подтверждения
            if (!confirm('Вы уверены, что хотите удалить эту запись?')) {
                return; // Если пользователь не подтверждает удаление, ничего не делаем
            }
            
            // Получаем родительскую строку (tr)
            const row = e.target.closest('tr');
            if (!row) return;
            
            // Получаем recordId из текстового содержимого первой ячейки строки
            const recordId = row.cells[0].textContent;
            if (!recordId) {
                alert('Не удалось получить ID записи для удаления.');
                return;
            }
            
            // Отправляем DELETE запрос
            fetch(`${window.location.pathname}/delete/${recordId}`, {
                method: 'DELETE'
            })
            .then(response => {
                if (response.ok) {
                    // В случае успешного ответа удаляем строку из DOM
                    row.remove();
                } else {
                    // Парсим JSON из тела ответа
                    response.json().then(errorData => {
                        // Показываем поле message, если оно есть, иначе - общее сообщение
                        alert(errorData.message || 'Ошибка при удалении записи.');
                    }).catch(() => {
                        // Если тело ответа - не JSON, показываем как текст
                        response.text().then(errorMessage => {
                             alert(errorMessage || 'Ошибка при удалении записи.');
                        });
                    });
                }
            })
            .catch(error => {
                // Этот блок сработает при сетевых ошибках
                console.error('Fetch error:', error);
                alert('Сетевая ошибка при попытке удаления.');
            });
        }
    });
    
});