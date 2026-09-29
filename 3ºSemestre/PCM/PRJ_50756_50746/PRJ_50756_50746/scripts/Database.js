'use strict';

/**
 * Represents a JSON database.
 */
class DatabaseJSON {
    /**
     * Represents a Database object.
     * @constructor
     */
    constructor() { }

    /**
     * Loads a JSON file asynchronously and returns its contents as a JavaScript object.
     * @param {string} filename - The path to the JSON file to be loaded.
     * @returns {Promise<object>} - A Promise that resolves with the loaded JSON data (or an empty object in case of an error).
     */
    async loadFile(filename) {
        // Initialize an empty object to store JSON data
        let jsonData = {};
        try {
            // Use the fetch API to retrieve the JSON file asynchronously
            const response = await fetch(filename);

            // Extract the text content from the response
            const jsonString = await response.text();

            // Parse the JSON string into a JavaScript object
            jsonData = JSON.parse(jsonString);
        } catch (error) {
            // Handle errors, log the error to the console
            console.error('Error loading JSON file:', error);
        }

        // Return the loaded JSON data (or an empty object in case of an error)
        return jsonData;
    }

    // Method to search for images in the JSON data based on a given query
    /**
     * Searches for images that match the given query in the provided JSON data and returns an array of paths for the matched images, limited by maxResults.
     * @param {string} query - The query to search for.
     * @param {Object} jsonData - The JSON data to search in.
     * @param {number} maxResults - The maximum number of results to return.
     * @returns {string[]} An array of paths for the matched images, limited by maxResults.
     */
    search(query, jsonData, maxResults) {
        // Filter images that match the given query
        const imagesMatched = jsonData.images.filter(im => im.class === query);

        // Determine the number of results to return, respecting the maxResults parameter
        maxResults = maxResults > imagesMatched.length ? imagesMatched.length : maxResults;

        // Return an array of paths for the matched images, limited by maxResults
        return imagesMatched.slice(0, maxResults).map(im => im.path);
    }
}


/**
 * A class representing a local storage database that stores JSON objects.
 */
class LocalStorageDatabaseJSON {
    constructor() { }

    /**
     * Saves a JavaScript object to localStorage with the specified keyname.
     * @param {string} keyname - The keyname under which the object will be saved.
     * @param {object} jsonObject - The JavaScript object to be saved.
     */
    save(keyname, jsonObject) {
        try {
            // Convert the JavaScript object to a JSON string and store it in localStorage
            localStorage.setItem(keyname, JSON.stringify(jsonObject));
        } catch (e) {
            // Handle errors that may occur during the save process
            alert('Save failed!');
            if (e == 'QUOTA_EXCEEDED_ERR') {
                // Display a specific alert if the storage quota is exceeded
                alert('Quota exceeded!');
            }
        }
    }

    /**
     * Retrieves data from localStorage based on the provided keyname.
     * @param {string} keyname - The keyname used to store the data in localStorage.
     * @returns {object} - The parsed JavaScript object retrieved from localStorage.
     * @throws {Error} - If the data is not found in localStorage.
     */
    read(keyname) {
        // Retrieve the JSON string from localStorage based on the provided key
        let localStorageJson = localStorage.getItem(keyname);
        let jsonData = null;

        // Check if the data is not found in localStorage
        if (localStorageJson === null) {
            // Handle the case where the data is not in localStorage by throwing an error
            throw new Error('Data not found in localStorage');
        }

        // Parse the JSON string into a JavaScript object
        jsonData = JSON.parse(localStorageJson);

        // Return the parsed JavaScript object
        return jsonData;
    }
}
