// Establish an SSE connection to the server
const eventSource = new EventSource('/sse/notifications');

eventSource.onmessage = function (event) {
    // Parse the data received from the server
    const data = JSON.parse(event.data);

    // Determine alert class based on type of data
    let alertClass = 'alert-info'; // default to blue (info)
    if (data.type === 'user') {
        alertClass = 'alert-success'; // green for new user
    } else if (data.type === 'dog') {
        alertClass = 'alert-primary'; // blue for new dog
    }

    // Create a new alert element
    const alertElement = document.createElement('div');
    alertElement.className = `alert ${alertClass} alert-dismissible fade show`;
    alertElement.role = 'alert';
    alertElement.innerHTML = `
            ${data.message}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        `;

    // Append the new alert to the container
    const alertsContainer = document.getElementById('alertsContainer');
    alertsContainer.appendChild(alertElement);
};

eventSource.onerror = function (event) {
    console.error("Error receiving SSE: ", event);
};
