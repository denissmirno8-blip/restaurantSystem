
const addAreas = async () => {
    const areaSelect = document.querySelector("#area_select");

    if (!areaSelect) return;
    const response = await fetch("http://localhost:8080/api/areas")

    const areasData = await response.json();

    for (const area of areasData) {
        option = document.createElement("option");
        option.value = area.name;
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
        option.value = timesData[i];
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
        input.value = preference.preference;
        input.name = "preferences";
        input.classList.add("filter");
        choice.append(input);

        const label = document.createElement("label");
        label.htmlFor = preference.id;
        label.textContent = preference.preference;
        choice.append(label);
    }
}


const createTablesMap = async () => {
    const grid = document.querySelector(".restaurant-grid");
    if (!grid) return;

    const urlParams = new URLSearchParams(window.location.search);

    const bookingResponse = await fetch("http://localhost:8080/api/booking/avaiable_tables?" + urlParams);
    const bookingData = await bookingResponse.json();

    console.log(bookingData);



    //add the table points logic(table_size - people_count)
    for (const booking of bookingData) {


        const input = document.createElement("input");
        input.type = "checkbox";
        input.name = "tableId";
        input.id = "table" + booking.table.id;
        input.classList.add("table-checkbox");
        grid.append(input);

        const label = document.createElement("label");
        label.classList.add("tableNr")
        label.htmlFor = "table" + booking.table.id;
        label.textContent = "Laud " + booking.table.id;
        grid.append(label);

    }

    //need to add clients registation check if true -1000
    let tableScores = bookingData.map(booking => ({
        id: String(booking.table.id),
        baseSize: booking.table.size,
        areaName: booking.table.area?.name,
        preferences: booking.table.preferences || [],
        currentScore: 0
    }));


    //Count table scores
    const updateScore = () => {
        const selectedArea = document.getElementById("area_select")?.value;
        const urlParams = new URLSearchParams(window.location.search);
        const requestedSize = Number(urlParams.get('size'));

        const selectedPreferences = Array.from(document.querySelectorAll('fieldset input[type="checkbox"]:checked')).map(el => el.value);

        tableScores.forEach(item => {
            let score = 0;

            const sizeDiff = item.baseSize - requestedSize;
            if (sizeDiff < 0)
                score -= 1000;
            else
                score -= sizeDiff * 2;

            if (selectedArea && selectedArea === item.areaName)
                score += 10;

            const matches = selectedPreferences.filter(preference => item.preferences.some(p => String(p.preference) === preference));

            score += (matches.length * 5);

            item.currentScore = score;

        });

        console.log(tableScores)

    }

    const areaSelect = document.getElementById("area_select");

    if (areaSelect) {
        areaSelect.addEventListener("change", updateScore);
    }

    const preferenceSelect = document.querySelector(".preferences");

    if (preferenceSelect)
        preferenceSelect.addEventListener("change", updateScore);

}

addAreas();
addPreferences();
createTablesMap();
addTimes();

document.addEventListener("DOMContentLoaded", () => {
    const filterForm = document.querySelector('form[action="/api/booking/map"]');

    if (filterForm) {
        filterForm.addEventListener("submit", (event) => {
            const dateValue = document.getElementById("date").value;
            const timeValue = document.getElementById("time").value;
            const sizeValue = document.getElementById("table_size").value;


            if (!dateValue || !timeValue || !sizeValue) {
                alert("Palun valige kuupäeva, aega, suuruse.")

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
