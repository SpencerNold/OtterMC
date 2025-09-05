document.addEventListener("DOMContentLoaded", run)

var settings = []

function run() {
    const button = document.getElementById("save-btn")
    button.addEventListener("click", () => {
        saveModule()
    }, { once: true })
    loadModule()
}

function loadModule() {
    const urlParams = new URLSearchParams(window.location.search)
    const moduleName = urlParams.get("name")
    if (!moduleName) {
        console.error("No module name specified")
        return
    }

    const title = document.getElementById("settings-title")
    title.textContent = "Settings: " + moduleName

    const button = document.getElementById("save-btn")

    const xhr = new XMLHttpRequest()
    xhr.open("GET", `/api/modstat?name=${moduleName}`)
    xhr.onload = () => {
        if (xhr.status == 200) {
            const module = JSON.parse(xhr.responseText)
            console.log(xhr.responseText)

            const status = createCheckbox("Enabled", module.enabled)
            document.getElementById("container").insertBefore(status, button)

            module.settings.forEach(s => {
                settings.push({ name: s.name, type: s.type })
                if (s.type == "BOOLEAN") {
                    const checkbox = createCheckbox(s.name, s.value)
                    document.getElementById("container").insertBefore(checkbox, button)
                } else if (s.type == "INT" || s.type == "FLOAT") {
                    const slider = createSlider(s.name, s.value, s.min, s.max, "FLOAT" ? "0.01" : "1")
                    document.getElementById("container").insertBefore(slider, button)
                } else if (s.type == "ENUM") {
                    var components = []
                    for (let c of s.constants) {
                        components.push(c.name)
                    }
                    const dropdown = createDropdown(s.name, s.value, components)
                    document.getElementById("container").insertBefore(dropdown, button)
                } else if (s.type == "STRING" || s.type == "KEYBOARD") {
                    const textbox = createTextbox(s.name, s.value)
                    document.getElementById("container").insertBefore(textbox, button)
                } else if (s.type == "COLOR") {
                    const colorPicker = createColorPicker(s.name, s.value)
                    document.getElementById("container").insertBefore(colorPicker, button)
                }
            })
        } else {
            console.error("Failed to load module: ", xhr.statusText)
        }
    }
    xhr.onerror = () => {
        console.error("Network error")
    }
    xhr.send()
}

function saveModule() {
    const urlParams = new URLSearchParams(window.location.search)
    const moduleName = urlParams.get("name")
    if (!moduleName) {
        console.error("No module name specified")
        return
    }

    const xhr = new XMLHttpRequest()
    xhr.open("POST", `/api/toggle`, true)
    xhr.setRequestHeader("Content-Type", "application/json; charset=UTF-8")
    xhr.onload = () => {
        if (xhr.status >= 200 && xhr.status < 300) {
            console.log("Success: ", xhr.responseText)
        } else {
            console.error("Error: ", xhr.status, xhr.statusText)
        }
        window.location.href = "/"
    }
    xhr.onerror = () => {
        console.error("Network error")
    }
    var settingValues = []
    for (let s of settings) {
        if (s.type == "BOOLEAN") {
            settingValues.push({ name: s.name, value: getCheckboxState(idify(s.name)) })
        } else if (s.type == "INT" || s.type == "FLOAT") {
            settingValues.push({ name: s.name, value: getSliderState(idify(s.name)) })
        } else if (s.type == "ENUM") {
            settingValues.push({ name: s.name, value: getDropdownIndex(idify(s.name)) })
        } else if (s.type == "STRING" || s.type == "KEYBOARD") {
            settingValues.push({ name: s.name, value: getTextValue(idify(s.name)) })
        } else if (s.type == "COLOR") {
            settingValues.push({ name: s.name, value: getColorValue(idify(s.name)) })
        }
    }
    const data = {
        name: moduleName,
        state: getCheckboxState("enabled"),
        settings: settingValues
    }
    xhr.send(JSON.stringify(data))
}

function createCheckbox(name, checked) {
    const div = document.createElement("div")
    div.className = "setting"
    const label = document.createElement("label")
    label.id = idify(name) + "-label"
    const checkbox = document.createElement("input")
    checkbox.type = "checkbox"
    checkbox.id = idify(name)
    checkbox.checked = checked
    label.appendChild(checkbox)
    label.appendChild(document.createTextNode(" " + name))
    div.appendChild(label)
    return div
}

function getCheckboxState(id) {
    return document.getElementById(id).checked
}

function createSlider(name, value, min, max, step) {
    const div = document.createElement("div")
    div.className = "setting"
    const label = document.createElement("label")
    label.id = idify(name) + "-label"
    const input = document.createElement("input")
    input.type = "range"
    input.min = min
    input.max = max
    input.step = step
    input.value = value
    input.id = idify(name)
    input.addEventListener("input", () => {
        document.getElementById(idify(name) + "-label").textContent = name + ": " + document.getElementById(idify(name)).value
    })
    label.textContent = name + ": " + input.value
    label.htmlFor = idify(name)
    div.appendChild(label)
    div.appendChild(input)
    return div
}

function getSliderState(id) {
    return document.getElementById(id).value
}

function createDropdown(name, index, components) {
    const div = document.createElement("div")
    div.className = "setting"
    const label = document.createElement("label")
    label.id = idify(name) + "-label"
    const select = document.createElement("select")
    select.id = idify(name)
    for (let c of components) {
        const option = document.createElement("option")
        option.value = c
        option.textContent = c
        select.appendChild(option)
    }
    select.selectedIndex = index
    label.textContent = name
    label.htmlFor = idify(name)
    div.appendChild(label)
    div.appendChild(select)
    return div
}

function getDropdownIndex(id) {
    return document.getElementById(id).selectedIndex
}

function createTextbox(name, value) {
    const div = document.createElement("div")
    div.className = "setting"
    const label = document.createElement("label")
    label.id = idify(name) + "-label"
    const input = document.createElement("input")
    input.id = idify(name)
    input.type = "text"
    input.value = value
    label.textContent = name
    label.htmlFor = idify(name)
    div.appendChild(label)
    div.appendChild(input)
    return div
}

function getTextValue(id) {
    return document.getElementById(id).value
}

function createColorPicker(name, value) {
    const div = document.createElement("div")
    div.className = "setting"
    const label = document.createElement("label")
    label.id = idify(name) + "-label"
    const input = document.createElement("input")
    input.id = idify(name)
    input.type = "color"
    input.value = value
    label.textContent = name
    label.htmlFor = idify(name)
    div.appendChild(label)
    div.appendChild(input)
    return div
}

function getColorValue(id) {
    return document.getElementById(id).value
}

function idify(id) {
    return id.toLowerCase().replace(" ", "-")
}