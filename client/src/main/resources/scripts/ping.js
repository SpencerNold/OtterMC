document.addEventListener("DOMContentLoaded", run)

function run() {
    const timer = setInterval(() => {
        sendStateRequest()
    }, 1000)
}

function sendStateRequest() {
    fetch("/api/state", { method: "GET" })
    .catch(() => {
        location.reload()
    });
}

