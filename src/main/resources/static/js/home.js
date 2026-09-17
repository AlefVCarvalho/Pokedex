const cards = [...document.querySelectorAll('.pokemon-card')];
const heroBall = document.querySelector('.hero-pokeball');
const carouselTrack = document.querySelector('[data-carousel] .carousel-track');
const carouselCards = [...document.querySelectorAll('[data-carousel] .pokemon-card')];
const carouselDots = document.querySelector('[data-carousel-dots]');
let carouselIndex = 0;

document.querySelectorAll('.card-top button').forEach((button) => {
    button.addEventListener('click', () => {
        button.textContent = button.textContent === '♡' ? '♥' : '♡';
        button.classList.toggle('liked');
    });
});

function updateCarousel() {
    if (!carouselCards.length) return;
    const offset = carouselCards[0].getBoundingClientRect().width + 16;
    carouselTrack.style.transform = `translateX(-${carouselIndex * offset}px)`;
    document.querySelectorAll('.carousel-dot').forEach((dot, index) => {
        dot.classList.toggle('active', index === carouselIndex);
    });
}

carouselCards.forEach((_, index) => {
    const dot = document.createElement('button');
    dot.className = `carousel-dot${index === 0 ? ' active' : ''}`;
    dot.type = 'button';
    dot.setAttribute('aria-label', `Ver destaque ${index + 1}`);
    dot.addEventListener('click', () => { carouselIndex = index; updateCarousel(); });
    carouselDots.appendChild(dot);
});

document.querySelector('[data-carousel-previous]').addEventListener('click', () => {
    carouselIndex = carouselIndex === 0 ? carouselCards.length - 1 : carouselIndex - 1;
    updateCarousel();
});

document.querySelector('[data-carousel-next]').addEventListener('click', () => {
    carouselIndex = carouselIndex === carouselCards.length - 1 ? 0 : carouselIndex + 1;
    updateCarousel();
});

window.addEventListener('resize', updateCarousel);

window.addEventListener('mousemove', (event) => {
    const x = event.clientX / window.innerWidth - 0.5;
    const y = event.clientY / window.innerHeight - 0.5;
    heroBall.style.setProperty('--ball-x', `${x * 22}px`);
    heroBall.style.setProperty('--ball-y', `${y * 15}px`);
    heroBall.style.setProperty('--ball-tilt', `${x * 14}deg`);
});

window.addEventListener('mouseleave', () => {
    heroBall.style.setProperty('--ball-x', '0px');
    heroBall.style.setProperty('--ball-y', '0px');
    heroBall.style.setProperty('--ball-tilt', '0deg');
});