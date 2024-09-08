document.body.addEventListener("htmx:afterOnLoad", function (evt) {
    console.log("htmx:afterOnLoad event triggered.");

    // Get the response body
    const responseBody = evt.detail.xhr.responseText;
    // Retrieve the element that triggered the HTMX request
    const triggeringElement = evt.detail.elt;
    // Check for success or error markers in the response
    const isSuccess = responseBody.includes('text-bg-success');
    const isError = responseBody.includes('text-bg-danger');

    // Logic to decide where to target the swap
    if (isSuccess) {

        // Close the modal using jQuery and Bootstrap
        $('#registerModal').modal('toggle');

        // Optionally close the navbar collapse if it's open
        $('#navbarNav').collapse('hide'); // Adjust with your correct ID or class to target the navbar

        // Update the message container with the response
        $('#message-container').html(responseBody);

        showToast();
    } else {
        console.log("Validation errors detected.");

        // If neither success nor error is found, replace modal body content with form
        $('#modalBody').html(responseBody);
    }
});

function showToast() {
    const toastElement = document.getElementById('serverToast');
    if (toastElement) {
        const toast = new bootstrap.Toast(toastElement);
        toast.show();
        console.log("Toast shown.");
    } else {
        console.warn("Toast element not found.");
    }
}

