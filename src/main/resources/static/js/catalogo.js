/*
 * Cuida das interações do catálogo no navegador.
 * Faz busca, filtros, troca de visualização e abre o modal com os dados do Pokémon selecionado.
 */
const cards = [...document.querySelectorAll('.catalog-card')];
const searchForm = document.querySelector('[data-catalog-search]');
const queryInput = document.querySelector('[data-query]');
const countLabel = document.querySelector('[data-count]');
const emptyState = document.querySelector('[data-empty]');
const catalogGrid = document.querySelector('[data-grid]');
const modal = document.querySelector('[data-modal]');

let activeType = 'todos';

function normalize(value) {
    return value.toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, '');
}

function getTypes(card) {
    return [card.dataset.primaryType, card.dataset.secondaryType].filter(Boolean);
}

function renderCatalog() {
    const query = normalize(queryInput.value.trim());
    let visible = 0;

    cards.forEach((card) => {
        const matchesQuery = normalize(`${card.dataset.name} ${card.dataset.number}`).includes(query);
        const types = getTypes(card).map(normalize);
        const matchesType = activeType === 'todos' || types.includes(normalize(activeType));
        const shouldShow = matchesQuery && matchesType;

        card.classList.toggle('hidden', !shouldShow);

        if (shouldShow) {
            visible += 1;
        }
    });

    countLabel.textContent = `${visible} Pokémon exibido(s)`;
    emptyState.classList.toggle('visible', visible === 0);
}

function openModal(card) {
    document.querySelector('[data-modal-number]').textContent =
        `#${String(card.dataset.number).padStart(3, '0')}`;
    document.querySelector('[data-modal-name]').textContent = card.dataset.name;
    document.querySelector('[data-modal-description]').textContent =
        card.dataset.description || 'Sem descrição cadastrada.';

    const image = document.querySelector('[data-modal-image]');
    image.src = card.dataset.image;
    image.alt = card.dataset.name;

    document.querySelector('[data-modal-types]').innerHTML = getTypes(card)
        .map((type) => `<span>${type}</span>`)
        .join('');

    modal.classList.add('open');
}

function closeModal() {
    modal.classList.remove('open');
}

searchForm.addEventListener('submit', (event) => event.preventDefault());
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
        if (!event.target.closest('.favorite-button, [data-card-action]')) {
            openModal(card);
        }
    });
});

document.querySelectorAll('.favorite-button').forEach((button) => {
    button.addEventListener('click', (event) => {
        event.stopPropagation();
        button.textContent = button.textContent === '♡' ? '♥' : '♡';
        button.classList.toggle('liked');
    });
});

document.querySelector('[data-random]').addEventListener('click', () => {
    if (cards.length > 0) {
        openModal(cards[Math.floor(Math.random() * cards.length)]);
    }
});

document.querySelector('[data-close]').addEventListener('click', closeModal);
modal.addEventListener('click', (event) => {
    if (event.target === event.currentTarget) {
        closeModal();
    }
});

document.addEventListener('keydown', (event) => {
    if (event.key === 'Escape') {
        closeModal();
    }
});

renderCatalog();
