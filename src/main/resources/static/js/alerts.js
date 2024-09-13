// Establish an SSE connection to the server
const eventSource = new EventSource('/sse/notifications');

const notificationHandlers = {
    'newUser': (data) => createAlert('alert-success', `New user created: ${data.message}`),
    'newDog': (data) => createAlert('alert-primary', `New dog created: ${data.message}`),
    // Add more handlers here as needed for other event types
};

eventSource.onmessage = function (event) {
    // Parse the data received from the server
    console.log(event)
    const data = JSON.parse(event.data);
    console.log(data)

    const notificationType = event.type;

    // Call the appropriate handler based on the event type
    if (notificationHandlers[notificationType]) {
        notificationHandlers[notificationType](data);
    } else {
        console.warn(`No handler found for notification type: ${notificationType}`);
    }
};

eventSource.onerror = function (event) {
    console.error("Error receiving SSE: ", event);
};

// Function to create an alert based on the alert class and message
function createAlert(alertClass, message) {
    // Create a new alert element
    const alertElement = document.createElement('div');
    alertElement.className = `alert ${alertClass} alert-dismissible fade show`;
    alertElement.role = 'alert';
    alertElement.innerHTML = `
        ${message}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    `;

    // Append the new alert to the container
    const alertsContainer = document.getElementById('alertsContainer');
    alertsContainer.appendChild(alertElement);
}