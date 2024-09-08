// Initialize the stack to store HTMX requests
const htmxRequestStack = [];
let isNavigatingBack = false; // Flag to prevent stack modification during back navigation

// Event listener for HTMX requests targeting #renderSpace
document.body.addEventListener('htmx:beforeRequest', function (event) {
    // Log the entire event detail object for debugging
    console.log("HTMX Event Detail:", event.detail);

    const url = event.detail.pathInfo.finalRequestPath; // URL of the request
    const method = event.detail.requestConfig.verb; // HTTP method (GET, POST, etc.)
    const target = event.detail.target.id; // Target element ID

    // Check if the target of the request is the renderSpace div
    if (!isNavigatingBack && method === 'get' && target === 'renderSpace') {

        // Log the absolute path being accessed
        console.log(`Request URL: ${url}`);
        console.log(`Request Method: ${method}`);
        console.log(`Request Target: ${target}`);

        // Check if the URL is unique before adding to the stack
        const isUnique = !htmxRequestStack.some(request => request.url === url && request.method === method);

        if (isUnique) {
            // Push the request details onto the stack
            htmxRequestStack.push({
                url: url,
                method: method,
                target: target
            });
            console.log('Request added to stack:', {url, method, target});
        } else {
            console.log('Duplicate request not added to stack:', {url, method});
        }
    }
});

// Function to handle the back button click
document.getElementById('backButton').addEventListener('click', function () {
    isNavigatingBack = true;

    // Check if there are any requests in the stack
    if (htmxRequestStack.length > 1) { // Ensure at least 2 to avoid removing the initial load
        // Remove the last request from the stack
        htmxRequestStack.pop();

        // Get the new last request
        const lastRequest = htmxRequestStack[htmxRequestStack.length - 1];

        if (lastRequest) {
            // Reissue the request using HTMX
            htmx.ajax(lastRequest.method, lastRequest.url, {target: "#" + lastRequest.target});
        } else {
            console.error("No request found to go back to.");
        }
    } else {
        console.log("No previous requests in the stack.");
    }
    isNavigatingBack = false;
});

// Optional: Handle errors
document.body.addEventListener('htmx:responseError', function (event) {
    console.error("Error during HTMX request:", event.detail);
});
