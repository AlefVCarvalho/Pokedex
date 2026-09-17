const cards = [...document.querySelectorAll('.catalog-card')];
const queryInput = document.querySelector('[data-query]');
const countLabel = document.querySelector('[data-count]');
const emptyState = document.querySelector('[data-empty]');
const catalogGrid = document.querySelector('[data-grid]');
let activeType = 'todos';

function normalize(value) {
    return value.toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '');
}

function renderCatalog() {
    const query = normalize(queryInput.value.trim());
    let visible = 0;
    cards.forEach((card) => {
        const matchesQuery = normalize(`${card.dataset.name} ${card.dataset.number}`).includes(query);
        const types = card.dataset.types.split(',').map(normalize);
        const matchesType = activeType === 'todos' || types.includes(normalize(activeType));
        const visibleCard = matchesQuery && matchesType;
        card.classList.toggle('hidden', !visibleCard);
        if (visibleCard) visible += 1;
    });
    countLabel.textContent = `${visible} Pokémon encontrado(s)`;
    emptyState.classList.toggle('visible', visible === 0);
}

function openModal(card) {
    const modal = document.querySelector('[data-modal]');
    document.querySelector('[data-modal-number]').textContent = `#${String(card.dataset.number).padStart(3, '0')}`;
    document.querySelector('[data-modal-name]').textContent = card.dataset.name;
    document.querySelector('[data-modal-image]').src = card.dataset.image;
    document.querySelector('[data-modal-image]').alt = card.dataset.name;
    document.querySelector('[data-modal-types]').innerHTML = card.dataset.types
        .split(',').map((type) => `<span>${type}</span>`).join('');
    document.querySelector('[data-modal-stats]').innerHTML = card.dataset.stats.split('|').map((stat) => {
        const [name, value] = stat.split('=');
        const percentage = Math.min(Number(value) / 160 * 100, 100);
        return `<div class="stat"><span>${name}</span><div class="stat-bar"><span style="width:${percentage}%"></span></div><strong>${value}</strong></div>`;
    }).join('');
    modal.classList.add('open');
}

function closeModal() { document.querySelector('[data-modal]').classList.remove('open'); }

queryInput.addEventListener('input', renderCatalog);
document.querySelectorAll('[data-type]').forEach((button) => {
    button.addEventListener('click', () => {
        document.querySelector('.filter.active').classList.remove('active');
        button.classList.add('active');
        activeType = button.dataset.type;
        renderCatalog();
    });
});

document.querySelectorAll('[data-view]').forEach((button) => {
    button.addEventListener('click', () => {
        document.querySelector('.view-button.active').classList.remove('active');
        button.classList.add('active');
        catalogGrid.classList.toggle('list-view', button.dataset.view === 'list');
    });
});
cards.forEach((card) => {
    card.addEventListener('click', (event) => {
        if (!event.target.closest('.favorite-button')) openModal(card);
    });
});
document.querySelectorAll('.details-button').forEach((button, index) => button.addEventListener('click', () => openModal(cards[index])));
document.querySelectorAll('.favorite-button').forEach((button) => button.addEventListener('click', (event) => {
    event.stopPropagation();
    button.textContent = button.textContent === '♡' ? '♥' : '♡';
    button.classList.toggle('liked');
}));
document.querySelector('[data-random]').addEventListener('click', () => openModal(cards[Math.floor(Math.random() * cards.length)]));
document.querySelector('[data-close]').addEventListener('click', closeModal);
document.querySelector('[data-modal]').addEventListener('click', (event) => { if (event.target === event.currentTarget) closeModal(); });
document.addEventListener('keydown', (event) => { if (event.key === 'Escape') closeModal(); });
renderCatalog();