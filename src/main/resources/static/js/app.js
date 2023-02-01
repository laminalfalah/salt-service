const showLoading = () => {
    const loading = document.querySelector("#loading");
    const span = document.createElement("span");
    span.className = "bi bi-arrow-repeat";
    loading.style.display = "block";
    loading.appendChild(span);
}

const hideLoading = () => {
    const loading = document.querySelector("#loading");
    loading.style.display = "none";
    while (loading.firstChild) {
        loading.removeChild(loading.firstChild);
    }
}

const onReady = (callback) => {
    let interval = window.setInterval(() => {
        if (document.getElementsByTagName("html") !== undefined) {
            window.clearInterval(interval);
            callback.call(this);
        }
    }, 1000);
}

document.addEventListener('DOMContentLoaded', () => {
    showLoading();

    onReady(() => hideLoading());

});