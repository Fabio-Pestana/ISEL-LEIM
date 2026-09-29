'use strict';

//Declaring a global variable which will be created in main function 
let app = null;

/**
 * The main function that initializes the application.
 */
function main() {

    const canvas = document.querySelector("canvas");

    //Creating the instance of the application
    app = new ISearchEngine("database.json");

    // Initializing the app
    app.init(canvas);

    document.getElementById('changeMode').addEventListener('click', changeMode);
    document.getElementById('musicOnOff').addEventListener('click', changeOnOff);
    document.getElementById('searchButton').addEventListener('click', searchKeyWord);
    document.getElementById('searchInput').addEventListener('change', searchKeyWord);

    canvas.addEventListener('click', selectImg);

    for(let i = 0 ; i<app.colors.length;i++){
        document.getElementById(app.colors[i]).addEventListener('click',  function() {
            searchColor(app.colors[i]);
        });
    }

    updateCanvasSize();
    window.addEventListener('resize', function () {
        updateCanvasSize();  // Function to update the canvas size on window resize
    });
}

/**
 * Searches for images with a specific color and updates the canvas.
 * @param {string} color - The color to search for.
 */
function searchColor(color){
    const inputKey =  document.getElementById('searchInput').value;

    const cnv = document.getElementById('canvas');

    app.searchColor(inputKey, color);
    app.gridView(cnv);
}

/**
 * Updates the size of the canvas based on the window size.
 */
function updateCanvasSize() {
    const cnv = document.getElementById('canvas');
    // Set the width of the canvas and toolbar to match the body's width

    let heightCnv = app.imgHeight + app.yBetweenImg;
    let wImg = app.imgWidth + app.xBetweenImg;
    
    let canvasW = document.documentElement.clientWidth;

    let numPorLinha = Math.floor(canvasW/wImg);
    let canvasH =  Math.ceil(app.numShownPic/numPorLinha);
    
    cnv.width = canvasW;
    cnv.height = canvasH * heightCnv;

    app.gridView(cnv);
}

/**
 * Searches for images based on a keyword and updates the canvas.
 */
function searchKeyWord(){
    const inputKey =  document.getElementById('searchInput').value;
    const cnv = document.getElementById('canvas');

    app.searchKeywords(inputKey);
    app.gridView(cnv);
}

/**
 * Toggles the dark mode on the body element.
 */
function changeMode(){
    document.body.classList.toggle('dark-mode');
}

/**
 * Toggles the background music on and off.
 */
function changeOnOff(){
    let audio = document.getElementById('backgroundMusic');
    document.getElementById('musicOnOff').classList.toggle("change");

    if (audio.paused) {
        audio.play();
    } else {
        audio.pause();
    }
}

/**
 * Calculates the coordinates of the mouse relative to the top-left corner of the specified element.
 * @param {HTMLElement} el - The element to calculate the coordinates relative to.
 * @returns {Array<number>} An array containing the x and y coordinates of the mouse.
 */
function getMouseCoord(el) {
    let xPos = 0;  // Initialize the x-coordinate
    let yPos = 0;  // Initialize the y-coordinate

    // Traverse the DOM hierarchy to calculate the coordinates
    while (el) {
        if (el.tagName === 'BODY') {
            // Deal with browser quirks with body/window/document and page scroll
            let xScroll = el.scrollLeft || document.documentElement.scrollLeft;
            let yScroll = el.scrollTop || document.documentElement.scrollTop;

            xPos += (el.offsetLeft - xScroll + el.clientLeft);
            yPos += (el.offsetTop - yScroll + el.clientTop);
        } else {
            // For all other non-BODY elements
            xPos += (el.offsetLeft - el.scrollLeft + el.clientLeft);
            yPos += (el.offsetTop - el.scrollTop + el.clientTop);
        }

        el = el.offsetParent;
    }
    return [xPos, yPos];
}


/**
 * Selects an image based on the mouse click event, searches for similarity, and updates the canvas.
 * @param {MouseEvent} ev - The mouse click event.
 */
function selectImg(ev){
    const cnv = document.getElementById('canvas');
    
    let check = document.getElementById('similarImagesCheckbox').checked;
    if(check){
        const [xPos, yPos] = getMouseCoord(cnv); 
        const mx = ev.x - xPos;
        const my = ev.y - yPos;

        let img = app.selectedImg(mx, my);
        app.searchISimilarity(img , 0);
        app.gridView(cnv);
    }
}

/**
 * Function that generates an artificial image and draw it in canvas.
 * Useful to test the image processing algorithms.
 * 
 * @param {HTMLCanvasElement} canvas - The canvas element to draw the image on.
 * @returns {ImageData} - The generated image data.
 */
function Generate_Image(canvas) {
    const ctx = canvas.getContext("2d");
    const imgData = ctx.createImageData(100, 100);

    for (let i = 0; i < imgData.data.length; i += 4) {
        imgData.data[i + 0] = 204;
        imgData.data[i + 1] = 0;
        imgData.data[i + 2] = 0;
        imgData.data[i + 3] = 255;
        if ((i >= 8000 && i < 8400) || (i >= 16000 && i < 16400) || (i >= 24000 && i < 24400) || (i >= 32000 && i < 32400))
            imgData.data[i + 1] = 200;
    }
    ctx.putImageData(imgData, 150, 0);
    return imgData;
}