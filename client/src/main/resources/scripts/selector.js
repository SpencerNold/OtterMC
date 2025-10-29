document.addEventListener("DOMContentLoaded", run)

const moduleMap = {}

function run() {
    document.getElementById("settings-button").addEventListener("click", () => {
        savePersistentCategoryAndModule()
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
            loadPersistentCategory()
            updateSelection()

        } else {
            console.error("Failed to fetch modules: ", xhr.statusText)
        }
    }
    xhr.onerror = () => {
        console.error("Error in GET from /api/modules")
        console.error("Status:", xhr.status)
        console.error("Status text:", xhr.statusText)
        console.error("Response URL:", xhr.responseURL)
    }
    xhr.send()
}

function loadPersistentCategory() {
    let selectedCategory = getSessionCookie("selected-category")
    if (selectedCategory == null) return
    let selectedCategoryIndex = Number(selectedCategory)
    if (selectedCategoryIndex == NaN) return
    document.getElementById("category").selectedIndex = selectedCategoryIndex
}

function loadPersistentModule() {
    let selectedModule = getSessionCookie("category-" + document.getElementById("category").selectedIndex)
    if (selectedModule == null)
        selectedModule = "0"
    let selectedModuleIndex = Number(selectedModule)
    if (selectedModuleIndex == NaN) return
    document.getElementById("module").selectedIndex = selectedModuleIndex
}

function savePersistentCategoryAndModule() {
    let selectedCategoryIndex = document.getElementById("category").selectedIndex
    let selectedModuleIndex = document.getElementById("module").selectedIndex
    setSessionCookie("selected-category", selectedCategoryIndex)
    setSessionCookie("category-" + selectedCategoryIndex, selectedModuleIndex)
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
    loadPersistentModule()
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

function setSessionCookie(name, value) {
    document.cookie = `${name}=${encodeURIComponent(value)}; path=/; SameSite=Lax`;
}

function getSessionCookie(name) {
    const cookies = document.cookie.split("; ");
    for (const cookie of cookies) {
        const [key, value] = cookie.split("=");
        if (key == name) {
            return decodeURIComponent(value);
        }
    }
  return null;
}