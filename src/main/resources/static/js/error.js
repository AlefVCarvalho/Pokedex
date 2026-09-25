/*
 * Comportamento da tela de erro.
 * O botão volta para a página anterior e usa a Home como alternativa quando não há histórico.
 */
const backButton = document.querySelector('[data-error-back]');

if (backButton) {
    backButton.addEventListener('click', () => {
        if (window.history.length > 1) {
            window.history.back();
            return;
        }

        window.location.href = '/';
    });
}
