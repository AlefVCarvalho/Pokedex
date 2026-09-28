const pageSizeTeams = 10;
const pageSizePlayers = 25;

function setupDirectory({ search, items, empty, pagination, previous, next, page, size, value }) {
    let currentPage = 0;
    let filtered = items;

    function render() {
        const start = currentPage * size;
        items.forEach((item) => item.classList.add('directory-hidden'));
        filtered.slice(start, start + size).forEach((item) => item.classList.remove('directory-hidden'));
        const pages = Math.max(1, Math.ceil(filtered.length / size));
        page.textContent = `${currentPage + 1} / ${pages}`;
        previous.disabled = currentPage === 0;
        next.disabled = currentPage >= pages - 1;
        if (empty) {
            empty.classList.toggle('visible', filtered.length === 0);
        }
        pagination.classList.toggle('visible', filtered.length > size);
    }

    search.addEventListener('input', () => {
        const query = value(search).trim().toLowerCase();
        filtered = items.filter((item) => value(item).toLowerCase().includes(query));
        currentPage = 0;
        render();
    });
    previous.addEventListener('click', () => { currentPage -= 1; render(); });
    next.addEventListener('click', () => { currentPage += 1; render(); });
    render();
}

const teamItems = [...document.querySelectorAll('[data-team-list] .team-card')];
const playerItems = [...document.querySelectorAll('[data-player-list] .person-row')];

setupDirectory({
    search: document.querySelector('[data-team-search] input'),
    items: teamItems,
    empty: document.querySelector('[data-team-empty]'),
    pagination: document.querySelector('[data-team-pagination]'),
    previous: document.querySelector('[data-team-prev]'),
    next: document.querySelector('[data-team-next]'),
    page: document.querySelector('[data-team-page]'),
    size: pageSizeTeams,
    value: (element) => element.dataset.teamName || ''
});

setupDirectory({
    search: document.querySelector('[data-player-search] input'),
    items: playerItems,
    empty: document.querySelector('[data-player-empty]'),
    pagination: document.querySelector('[data-player-pagination]'),
    previous: document.querySelector('[data-player-prev]'),
    next: document.querySelector('[data-player-next]'),
    page: document.querySelector('[data-player-page]'),
    size: pageSizePlayers,
    value: (element) => element.dataset.playerSearchable || ''
});
