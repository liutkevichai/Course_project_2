document.addEventListener('DOMContentLoaded', function () {
    // Найдем все выпадающие меню в боковой панели
    const dropdownToggles = document.querySelectorAll('aside .dropdown-toggle');
    
    dropdownToggles.forEach(function(toggle) {
        toggle.addEventListener('click', function (e) {
            e.preventDefault(); // Предотвратим стандартное поведение кнопки
            const menu = this.nextElementSibling;
            if (menu) {
                menu.classList.toggle('show');
            }
        });
    });
});