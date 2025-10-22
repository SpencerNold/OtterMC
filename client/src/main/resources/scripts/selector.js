document.addEventListener("DOMContentLoaded", run)

const moduleMap = {}

function run() {
    document.getElementById("settings-button").addEventListener("click", () => {
        const selectedModule = document.getElementById("module").value
        window.location.href = `module?name=${selectedModule}`
    })
    loadModules()
}

function loadModules() {
    const xhr = new XMLHttpRequest()
    xhr.open("GET", "/api/modules")
    xhr.onload = () => {
        if (xhr.status == 200) {
            const modules = JSON.parse(xhr.responseText)
            for (let module of modules) {
                insertModule(module)
            }
            initSelection()
        } else {
            console.error("Failed to fetch modules: ", xhr.statusText)
        }
    }
    xhr.onerror = () => {
        console.error("Error in GET from /api/modules")
        console.error("XHR error:", event)
        console.error("Status:", xhr.status)
        console.error("Status text:", xhr.statusText)
        console.error("Ready state:", xhr.readyState)
        console.error("Response URL:", xhr.responseURL)
    }
    xhr.send()
}

function insertModule(module) {
    if (!moduleMap[module.category]) {
        moduleMap[module.category] = []
    }
    moduleMap[module.category].push(module.name)
}

function initSelection() {
    const categorySelect = document.getElementById("category")
    for (let [category, modules] of Object.entries(moduleMap)) {
        const option = document.createElement("option")
        option.value = category
        option.text = category
        categorySelect.appendChild(option)
    }
    updateSelection()
    categorySelect.addEventListener("change", updateSelection)
}

function updateSelection() {
    const categorySelect = document.getElementById("category")
    const category = getCategoryByIndex(categorySelect.selectedIndex)
    const modules = moduleMap[category] || []
    const moduleSelect = document.getElementById("module")
    moduleSelect.innerHTML = ""
    for (let module of modules) {
        const option = document.createElement("option")
        option.value = module
        option.text = module
        moduleSelect.appendChild(option)
    }
}

function getCategoryByIndex(target) {
    let index = 0
    for (let category of Object.keys(moduleMap)) {
        if (index == target) {
            return category
        }
        index++
    }
    return null
}