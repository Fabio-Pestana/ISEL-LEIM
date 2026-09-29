function convertLocalStorageToObject() {
    const localStorageData = {};

    // Loop through all items in LocalStorage
    for (let i = 0; i < localStorage.length; i++) {
        const key = localStorage.key(i);
        const value = localStorage.getItem(key);

        // Parse the value if it's valid JSON
        try {
            localStorageData[key] = JSON.parse(value);
        } catch (e) {
            localStorageData[key] = value;
        }
    }

    return localStorageData;
}

function exportLocalStorageToJSON(filename) {
    // Convert the data to JSON
    const jsonData = JSON.stringify(convertLocalStorageToObject(), null, 2);

    // Create a Blob containing the JSON data
    const blob = new Blob([jsonData], { type: "application/json" });

    // Create a download link
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = filename || "localStorageData.json";

    // Trigger a click event to download the file
    link.click();
}

function importLocalStorageFromJSON(inputElement) {
    const file = inputElement.files[0];
    if (!file) {
        alert("Please select a JSON file.");
        return;
    }

    const reader = new FileReader();

    reader.onload = function (event) {
        try {
            const jsonString = event.target.result;
            const data = JSON.parse(jsonString);

            // Clear existing LocalStorage data
            localStorage.clear();

            // Restore data from the JSON
            for (const key in data) {
                const value = data[key];
                if (typeof value === 'object' && value !== null) {
                    // If the value is an object, store it as a string
                    localStorage.setItem(key, JSON.stringify(value));
                } else {
                    // Otherwise, store it as-is
                    localStorage.setItem(key, value);
                }
            }
            alert("LocalStorage data restored successfully.");
        } catch (error) {
            alert("Error restoring LocalStorage data. Make sure the selected file is a valid JSON file.");
        }
    };

    reader.readAsText(file);
}