const searchForm = document.querySelector('[data-search-form]');
const searchInput = document.querySelector('[data-search-input]');
const feedback = document.querySelector('[data-search-feedback]');
const cards = [...document.querySelectorAll('.pokemon-card')];
const emptyState = document.querySelector('[data-empty-state]');
let selectedFilter = 'todos';

function normalize(value) {
    return value.toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '');
}

function filterCards() {
    const query = normalize(searchInput.value.trim());
    let visible = 0;
    cards.forEach((card) => {
        const matchesText = normalize(`${card.dataset.name} ${card.dataset.number}`).includes(query);
        const matchesType = selectedFilter === 'todos' || normalize(card.dataset.type) === selectedFilter;
        card.classList.toggle('hidden', !(matchesText && matchesType));
        if (matchesText && matchesType) visible += 1;
    });
    emptyState.classList.toggle('visible', visible === 0);
    feedback.textContent = query && visible === 0 ? 'Tente outro nome ou número.' : '';
}

searchForm.addEventListener('submit', (event) => { event.preventDefault(); filterCards(); });
searchInput.addEventListener('input', filterCards);
document.querySelectorAll('[data-filter]').forEach((button) => {
    button.addEventListener('click', () => {
        document.querySelector('.filter.active').classList.remove('active');
        button.classList.add('active');
        selectedFilter = button.dataset.filter;
        filterCards();
    });
});

document.querySelectorAll('.card-top button').forEach((button) => {
    button.addEventListener('click', () => {
        button.textContent = button.textContent === '♡' ? '♥' : '♡';
        button.classList.toggle('liked');
    });
});