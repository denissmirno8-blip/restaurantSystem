
const addAreas = async () => {
    const areaSelect = document.querySelector("#area_select");

    if (!areaSelect) return;
    const response = await fetch("http://localhost:8080/api/areas")

    const areasData = await response.json();

    for (const area of areasData) {
        option = document.createElement("option");
        option.value = area.id;
        option.textContent = area.name;
        areaSelect.append(option);
    }

}

const addTimes = async () => {
    const timeSelect = document.querySelector("#time");

    if (!timeSelect) return;
    const response = await fetch("http://localhost:8080/api/booking/times")

    const timesData = await response.json();

    for (let i = 0; i < timesData.length; i++) {
        option = document.createElement("option");
        option.value = i;
        option.textContent = timesData[i];
        timeSelect.append(option);
    }

}

//Add to map.html
const addPreferences = async () => {
    const fieldSet = document.querySelector(".preferences");

    if (!fieldSet) return;
    const response = await fetch("http://localhost:8080/api/preferences")

    const preferenceData = await response.json();

    for (const preference of preferenceData) {
        const choice = document.createElement("div");
        fieldSet.append(choice);

        const input = document.createElement("input");
        input.type = "checkbox";
        input.id = preference.id;
        input.classList.add("filter");
        choice.append(input);

        const label = document.createElement("label");
        label.htmlFor = preference.id;
        label.textContent = preference.preference;
        choice.append(label);
    }
}

const addTables = async () => {
    //Put tables and after filters setting change it to select distinct restauranttable from booking where ...
    const grid = document.querySelector(".restaurant-grid");
    if(!grid) return;

    const response = await fetch("http://localhost:8080/api/tables");
    const tableData = await response.json();



    for (const table of tableData) {
        const input = document.createElement("input");
        input.type = "checkbox";
        input.name = "tableId";
        input.id = table.id;
        input.classList.add("table-checkbox");
        grid.append(input);

        const label = document.createElement("label");
        label.htmlFor = table.id;
        label.textContent = "Laud " + table.id;
        grid.append(label);
    }
}

addAreas();
addPreferences();
addTables();
addTimes();

document.addEventListener("DOMContentLoaded", () => {
    const filterForm = document.querySelector('form[action="/api/booking/map"]');

    if (filterForm) {
        filterForm.addEventListener("submit", (event) => {
            const dateValue = document.getElementById("date").value;
            const timeSelect = document.getElementById("time");
            const sizeValue = document.getElementById("table_size").value;

            const timeValue = timeSelect.options[timeSelect.selectedIndex]?.textContent;

            if (!dateValue || !timeValue || !sizeValue || !areaValue) {
                alert("Palun valige kuupäeva, aega ja suuruse.")

                event.preventDefault();
            }

            const selectedDateAndTime = new Date(`${dateValue}T${timeValue}`);
            const now = new Date();

            if (selectedDateAndTime < now) {

                alert("Laua ei saa broneerida minevikku!.")
                event.preventDefault();
            }
        })
    }
})
