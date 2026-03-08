let globalTableScores = [];

const addAreas = async () => {
    const areaSelect = document.querySelector("#area_select");

    if (!areaSelect) return;
    const response = await fetch("http://localhost:8080/api/areas")

    const areasData = await response.json();

    noneOption = document.createElement("option");
    noneOption.value = "none";
    noneOption.textContent = "-None-";
    areaSelect.append(noneOption);

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


const paintTables = (tableScores) => {
    const validScores = tableScores.filter(item => item.currentScore > -900).map(item => item.currentScore);

    const maximumScore = validScores.length > 0 ? Math.max(...validScores) : 0;
    const minimumScore = validScores.length > 0 ? Math.min(...validScores) : 0;
    const scoreRange = maximumScore - minimumScore;

    tableScores.forEach(item => {
        const tableLabel = document.querySelector(`.tableNr[for="table${item.id}"]`)

        if (tableLabel.classList.contains("chosen")) {
            tableLabel.style.backgroundColor = "#2563eb";
            tableLabel.style.color = "white";
        }
        else {
            const normalized = scoreRange === 0 ? 1 : (item.currentScore - minimumScore) / scoreRange;

            //AI soovitas
            const hue = 30 + (normalized * 90);

            tableLabel.style.backgroundColor = `hsl(${hue}, 80%, 55%)`;
            tableLabel.style.color = "black";
        }
    })
}


//Count table scores by size, area and preference
const updateScore = (tableScores) => {
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

    paintTables(tableScores);

}

const createTablesByData = async () => {
    const grid = document.querySelector(".restaurant-grid");
    if (!grid) return;

    const urlParams = new URLSearchParams(window.location.search);

    const bookingResponse = await fetch("http://localhost:8080/api/booking/avaiable_tables?" + urlParams);
    const bookingData = await bookingResponse.json();

    console.log(bookingData);

    //add the table points logic(table_size - people_count)
    for (const booking of bookingData) {
        const divide = document.createElement("div");
        divide.classList.add("table-checkbox")
        const input = document.createElement("input");
        input.type = "radio";
        input.name = "tableId";
        input.id = "table" + booking.table.id;
        input.dataset.id = booking.table.id;
        divide.append(input);

        const label = document.createElement("label");
        label.classList.add("tableNr")
        label.htmlFor = "table" + booking.table.id;
        label.textContent = "Laud " + booking.table.id;
        divide.append(label);

        grid.append(divide);
    }

    //need to add clients registation check if true -1000
    globalTableScores = bookingData.map(booking => ({
        id: String(booking.table.id),
        baseSize: booking.table.size,
        areaName: booking.table.area?.name,
        preferences: booking.table.preferences || [],
        currentScore: 0
    }));

    updateScore(globalTableScores);


    const areaSelect = document.getElementById("area_select");

    if (areaSelect) {
        areaSelect.addEventListener("change", () => updateScore(globalTableScores));
    }

    const preferenceSelect = document.querySelector(".preferences");

    if (preferenceSelect)
        preferenceSelect.addEventListener("change", () => updateScore(globalTableScores));

}

//Fill the filters
addAreas();
addPreferences();
createTablesByData();
addTimes();

document.addEventListener("DOMContentLoaded", () => {
    const filterForm = document.querySelector('form[action="/api/booking/map"]');

    const registerForm = document.querySelector('form[action="/api/booking/book"]')

    const grid = document.querySelector('.restaurant-grid');

    if (grid) {
        grid.addEventListener("click", (event) => {

            const target = event.target.closest(".table-checkbox");

            if (target) {
                const label = target.querySelector(".tableNr");
                const input = target.querySelector("input[type='radio']");

                document.querySelectorAll(".tableNr").forEach(el => el.classList.remove("chosen"));

                label.classList.add("chosen");

                input.checked = true;

                paintTables(globalTableScores)
            }
        })

        grid.addEventListener("mouseenter", async (event) => {
            const target = event.target.closest(".table-checkbox");
            if (target) {

                const tableResponse = await fetch("http://localhost:8080/api/tables");
                const tableData = await tableResponse.json();
                const selectedInput = target.querySelector('input[type="radio"]');
                const selectedTableId = Number(selectedInput.dataset.id);
                const tableInfo = tableData.find(item => item.id === selectedTableId);
                const prefString = tableInfo.preferences ? tableInfo.preferences.map(p => p.preference).join(", ") : "puuduvad";
                target.title = `Laua suurus: ${tableInfo.size}.\nLaua asukoht : ${tableInfo.area.name}.\nLaua eelistused: ${prefString}.`;

            }
        }, true)
    }

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
    if (registerForm) {
        registerForm.addEventListener("submit", async (event) => {

            event.preventDefault();//prevent the page reloading

            const selectedTable = document.querySelector('input[name="tableId"]:checked');
            const selectedTableId = Number(selectedTable.dataset.id);
            const tableScoreInfo = globalTableScores.find(item => Number(item.id) === selectedTableId);

            if (!selectedTable) {
                alert("Palun valige laud!");
                return;
            }
            if (tableScoreInfo.currentScore < -900) {
                alert("Laud on liiga väike. Proovige uuseti.")
                return;
            }

            const urlParams = new URLSearchParams(window.location.search);

            const tableResponse = await fetch("http://localhost:8080/api/tables");
            const tableData = await tableResponse.json();

            const tableInfo = tableData.find(item => item.id === selectedTableId);

            const newBook = {
                restaurantTable: tableInfo,
                client: {
                    name: document.querySelector("#clientName").value.trim(),
                    phoneNumber: document.querySelector("#phonenumber").value.trim()
                },
                date: urlParams.get('date'),
                time: urlParams.get('time')
            }

            const response = await fetch('http://localhost:8080/api/booking/book', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(newBook)
            });

            if (response.ok) {
                alert(`Broneering õnnestus!`);
                window.location.href = "/"
            } else {
                alert("Viga broneerimisel. Proovige uuesti!");
            }

        })
    }
})
