document.getElementById('exportButton').addEventListener('click', e => {
    console.log('Exporting LocalStorage contents to a JSON file.');
    exportLocalStorageToJSON('localstorage.json');
});

const inputElement = document.getElementById('fileImport')
inputElement.type = "file";
inputElement.accept = ".json";
inputElement.addEventListener("change", e => {
    console.log('Importing LocalStorage contents from a JSON file.');
    importLocalStorageFromJSON(inputElement);
});