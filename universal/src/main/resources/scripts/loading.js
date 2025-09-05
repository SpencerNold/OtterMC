document.addEventListener("DOMContentLoaded", run)

function run() {
    const timer = setInterval(() => {
        sendStateRequest()
    }, 1000)
}

function sendStateRequest() {
    const xhr = new XMLHttpRequest()
    xhr.open("GET", "/state")
    xhr.onload = () => {
        if (xhr.status == 200) {
            const state = JSON.parse(xhr.responseText)
            if (state == "RUNNING") {
                location.reload()
            }
        } else {
            console.error("Failed to fetch state: ", xhr.statusText)
        }
    }
    xhr.onerror = () => {
        console.error("Network error")
    }
    xhr.send()
}