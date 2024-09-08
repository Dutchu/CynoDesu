document.body.addEventListener('htmx:configRequest', function (evt) {
    if (evt.detail.xhr.status === 400) {
        evt.detail.headers['HX-Target'] = '#errorContainer'; // Set custom target for validation errors
    }
});

document.body.addEventListener('htmx:afterRequest', function (evt) {
    if (evt.detail.xhr.getResponseHeader("HX-Trigger") === "validationError") {
        const errorContainer = document.querySelector("#errorContainer");
        errorContainer.innerHTML = evt.detail.target.innerHTML; // Replace only part of HTML with errors
    }
});
