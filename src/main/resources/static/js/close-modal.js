function toggleModal(modalId) {
    // Close the modal using jQuery and Bootstrap
    $('#' + modalId).modal('toggle');
}

function closeBackDrop() {
    // Close the modal using jQuery and Bootstrap
    // Manually remove the modal backdrop
    $('.modal-backdrop').remove();
}