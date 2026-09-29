'use strict';

/**
 * Represents a search engine for images.
 * @class
 */
class ISearchEngine {
    /**
     * Creates a new instance of the ISearchEngine class.
     * @param {string} dbase - The name of the JSON file with the information related to the images.
     */
    constructor(dbase) {
        //Pool to include all the objects (mainly pictures) drawn in canvas 
        this.allpictures = new Pool(3000);

        //Array of color to be used in image processing algorithms
        this.colors = ["red", "orange", "yellow", "green", "Blue-green", "blue", "purple", "pink", "white", "grey", "black", "brown"];

        // Red component of each color
        this.redColor = [204, 251, 255, 0, 3, 0, 118, 255, 255, 153, 0, 136];
        // Green component of each color
        this.greenColor = [0, 148, 255, 204, 192, 0, 44, 152, 255, 153, 0, 84];
        // Blue component of each color
        this.blueColor = [0, 11, 0, 0, 198, 255, 167, 191, 255, 153, 0, 24];

        //List of categories available in the image database
        this.categories = ["beach", "birthday", "face", "indoor", "manmade/artificial", "manmade/manmade", "manmade/urban", "marriage", "nature", "no_people", "outdoor", "party", "people", "snow"];

        //Name of the JSON file with the information related to the images 
        this.jsonFile = dbase;

        // Instance of the Database class to manage the information in the JSON file 
        this.db = new DatabaseJSON();

        // Instance of the LocalStorageDatabase class to manage the information in the LocalStorage 
        this.lsDb = new LocalStorageDatabaseJSON();

        //Number of images per category for image processing
        this.numImages = 100;
        //Number of images to show in canvas as a search result
        this.numShownPic = 30;

        //Width of image in canvas
        this.imgWidth = 190;
        //Height of image in canvas
        this.imgHeight = 140;

        //lista de imagens
        this.imgCanva = [];

        // Margem entre imagens
        this.xBetweenImg = 20;
        this.yBetweenImg = 40;

        //lista valida para a search 
        this.categoriesValid = ["beach", "birthday", "face", "indoor", "artificial", "manmade", "urban", "marriage", "nature", "no_people", "outdoor", "party", "people", "snow"];
    }

    /**
     * Initializes the search engine and the canvas.
     * First stage it is used to process all the images.
     * @param {Canvas} cnv - The canvas element.
     */
    init(cnv) {
        this.databaseProcessing(cnv);
    }

    /**
     * Performs database processing for image search.
     * @param {CanvasRenderingContext2D} cnv - The canvas rendering context.
     */
    databaseProcessing(cnv) {
        // method to build the database which is composed by all the pictures organized by the JSON database file
        // At this initial stage, in order to evaluate the image algorithms, the method only compute one image.
        // However, after the initial stage the method must compute all the images in the JSON file

        //Images processing classes
        const h12color = new ColorHistogram(this.redColor, this.greenColor, this.blueColor);
        const colmoments = new ColorMoments();

        let listOfImages = [];

        //teste com picture daniel1.jpg
        //const img = new Picture(0, 0, 100, 100, "Images/daniel1.jpg", "test");

        this.db.loadFile(this.jsonFile)
        .then(jsonData => {
            for(let i = 0; i < this.categories.length; i++){

                let procuraCategoria = this.db.search(this.categories[i], jsonData, this.numImages);

                for (let img = 0; img < procuraCategoria.length; img++) {
                    let imagem = new Picture(0, 0, this.imgWidth, this.imgHeight, procuraCategoria[img], this.categories[i]);
                    listOfImages.push(imagem);
                }
            }

            for (let j = 0; j < listOfImages.length; j++) {
                //Creating an event that will be used to understand when image is already processed
                const img = listOfImages[j];
                const eventname = "processed_picture_" + img.impath;
                const eventP = new Event(eventname);
                const self = this;
                document.addEventListener(eventname, function () {
                    self.imageProcessed(img, eventname);
                }, false);
        
                img.computation(cnv, h12color, colmoments, eventP);
            }
        })
        .catch(error => {
            console.error('Error loading JSON file:', error);
        });
    }

    /**
     * Processed the given image and performs necessary operations.
     * @param {Image} img - The image to be processed.
     * @param {string} eventname - The name of the event triggered during image processing.
     */
    imageProcessed(img, eventname) {
        //When the event "processed_picture_" is enabled this method is called to check if all the images are
        //already processed. When all the images are processed, a database organized in JSON is saved in the localStorage
        //to answer the queries related to Color and Image Example

        this.allpictures.insert(img);
        console.log("image processed " + this.allpictures.stuff.length + eventname);

        if (this.allpictures.stuff.length === (this.numImages * this.categories.length)) {
            this.createColorDatabaseLS();
            this.createIExampledatabaseLS();

            const loadingWrapper = document.querySelector(".loading-wrapper");
            this.fadeOut(loadingWrapper);

            //depois de processar as imagens ele vai apresentar algumas na grid
            this.radomBeginImg();
            this.gridView(document.getElementById("canvas"));
        }
    }

    /**
     * Creates a color database using Local Storage.
     */
    createColorDatabaseLS()  {
        // Method to create the JSON database in the localStorage for color queries
        // This method should be completed by the students
    
        for (let idxCat = 0; idxCat < this.categories.length; idxCat++) {
            let colors = [];

            let localStorageData = {
                images: []
            };
    
            for (let idxImg = 0; idxImg < this.allpictures.stuff.length; idxImg++) {
                if (this.allpictures.stuff[idxImg].category === this.categories[idxCat]){
                    colors.push(this.allpictures.stuff[idxImg]);
                } 
            }
    
            for (let idxColor = 0; idxColor < this.colors.length; idxColor++) {
                this.sortbyColor(idxColor, colors);
    
                for (let i = 0; i < this.numShownPic; i++) {
                    let jsonDataBase = {
                        class: this.colors[idxColor],
                        path: colors[i].impath
                    };
    
                    localStorageData.images.push(jsonDataBase);
                }
            }

            // Save to localStorage
            this.lsDb.save(this.categories[idxCat], localStorageData);
        }
    }

    /** 
     * Creates an example database for image search.
     * This method should be completed by the students.
     */
    createIExampledatabaseLS() {
        //Method to create the JSON database in the localStorage for Image Example queries

        const list_images = new Pool(this.allpictures.stuff.length);

        this.zscoreNormalization();

        // this method should be completed by the students

        for(let idxImg = 0; idxImg < list_images.size; idxImg++){
            let imagens = [];
            let localStorageData = {
                images: []
            };

            for (let i = 0; i <  list_images.size; i++) {
				imagens.push({
					manhattanDist: this.calcManhattanDist(this.allpictures.stuff[idxImg], this.allpictures.stuff[i]),
					path: this.allpictures.stuff[i].impath
				});
			}

            this.sortbyManhattanDist(idxImg, imagens);

            for(let j = 0; j < this.numShownPic; j++){
                let jsonDataSearch = {
                    class: "Manhattan",
                    path: imagens[j].path,
                };
                
                localStorageData.images.push(jsonDataSearch);
            }

            this.lsDb.save(this.allpictures.stuff[idxImg].impath, localStorageData);
        }
    }

    /**
     * Performs z-score normalization on the color moments of the pictures.
     */
    zscoreNormalization() {
        //A good normalization of the data is very important to look for similar images. This method applies the
        //zscore normalization to the data

        const overall_mean = [];
        const overall_std = [];

        // Inicialization
        for (let i = 0; i < this.allpictures.stuff[0].color_moments.length; i++) {
            overall_mean.push(0);
            overall_std.push(0);
        }

        // Mean computation I
        for (let i = 0; i < this.allpictures.stuff.length; i++) {
            for (let j = 0; j < this.allpictures.stuff[0].color_moments.length; j++) {
                overall_mean[j] += this.allpictures.stuff[i].color_moments[j];
            }
        }

        // Mean computation II
        for (let i = 0; i < this.allpictures.stuff[0].color_moments.length; i++) {
            overall_mean[i] /= this.allpictures.stuff.length;
        }

        // STD computation I
        for (let i = 0; i < this.allpictures.stuff.length; i++) {
            for (let j = 0; j < this.allpictures.stuff[0].color_moments.length; j++) {
                overall_std[j] += Math.pow((this.allpictures.stuff[i].color_moments[j] - overall_mean[j]), 2);
            }
        }

        // STD computation II
        for (let i = 0; i < this.allpictures.stuff[0].color_moments.length; i++) {
            overall_std[i] = Math.sqrt(overall_std[i] / this.allpictures.stuff.length);
        }

        // zscore normalization
        for (let i = 0; i < this.allpictures.stuff.length; i++) {
            for (let j = 0; j < this.allpictures.stuff[0].color_moments.length; j++) {
                this.allpictures.stuff[i].color_moments[j] = (this.allpictures.stuff[i].color_moments[j] - overall_mean[j]) / overall_std[j];
            }
        }
    }

    /**
     * Searches for images based on the specified category and color.
     * @param {string} category - The category of the images to search for.
     * @param {string} color - The color of the images to search for.
     */
    searchColor(category, color) {
        //Method to search images based on a selected color

        // this method should be completed by the students
        category = category.toLowerCase();

        let idx = this.categoriesValid.indexOf(category);

        if(idx === -1){
            return;
        }

        category = this.categories[idx];

        let readJsonSearch = this.lsDb.read(category);

        let searchColor = readJsonSearch.images.filter(im => im.class === color);
        let paths = searchColor.map(item => item.path);

        this.imgCanva = [];

        for (let i = 0; i < paths.length; i++) {
            for(let j = 0; j < this.allpictures.stuff.length; j++){
                if (this.allpictures.stuff[j].impath === paths[i]) {
                    this.imgCanva.push(this.allpictures.stuff[j]);
                    break;
                }
            }
		}
    }

    /**
     * Searches for images based on the provided category.
     * @param {string} category - The category to search for.
     */
    searchKeywords(category) {
        // Method to search images based on keywords
        
        // this method should be completed by the students
        category = category.toLowerCase();

        let idx = this.categoriesValid.indexOf(category);

        if(idx === -1){
            return;
        }

        category = this.categories[idx];

        this.imgCanva = [];

        for (let i = 0; i < this.allpictures.stuff.length; i++) {
			if (this.allpictures.stuff[i].category === category && this.imgCanva.length < this.numShownPic) {
				this.imgCanva.push(this.allpictures.stuff[i]);
			}
		}
    }

    /**
     * Searches for images with similarity to the given example.
     * @param {ImageData} IExample - The example image to compare against.
     * @param {number} dist - The distance threshold for similarity.
     */
    searchISimilarity(IExample, dist) {
        //Method to search images based on Image similarities
        //this method should be completed by the students

        let idxImg = this.imgCanva.indexOf(IExample);

        if(idxImg === -1){
            return;
        }

        let readJsonImgSimilarity = this.lsDb.read(this.imgCanva[idxImg].impath);

        let paths =  readJsonImgSimilarity.images.map(item => item.path);

        this.imgCanva = [];

        for (let i = 0; i < paths.length; i++) {
            for(let j = 0; j < this.allpictures.stuff.length; j++){
                if (this.allpictures.stuff[j].impath === paths[i]) {
                    this.imgCanva.push(this.allpictures.stuff[j]);
                    break;
                }
            }
		}
    }


    /**
     * Calculates the Manhattan distance between two images based on their color moments.
     * @param {Object} img1 - The first image object.
     * @param {Object} img2 - The second image object.
     * @returns {number} The calculated Manhattan distance.
     */
    calcManhattanDist(img1, img2) {
        //Method to compute the Manhattan difference between 2 images which is one way of measure the similarity
        //between images.
        let manhattan = 0;

        for (let i = 0; i < img1.color_moments.length; i++) {
            manhattan += Math.abs(img1.color_moments[i] - img2.color_moments[i]);
        }
        manhattan /= img1.color_moments.length;
        return manhattan;
    }

    /**
     * Sorts the given list of image by their Manhattan distance based on the provided index-distance mapping.
     * @param {Object} idxdist - The index-distance mapping.
     * @param {Array} list - The list of items to be sorted.
     */
    sortbyManhattanDist(idxdist, list) {
        //Method to sort images according to the Manhattan distance measure
        //this method should be completed by the students

        list.sort(function (a, b) {
            return b.manhattanDist - a.manhattanDist;
        })
    }

    /**
     * Sorts the given list of images by the specified color index.
     * @param {number} idxColor - The index of the color to sort by.
     * @param {Array} list - The list of images to be sorted.
     */
    sortbyColor(idxColor, list) {
        //Method to sort images according to the number of pixels of a selected color
        list.sort(function (a, b) {
            return b.hist[idxColor] - a.hist[idxColor];
        });
    }

    /**
     * Renders the grid view of the search results on the canvas.
     *
     * @param {Canvas} canvas - The canvas element to render the grid view on.
     */
    gridView(canvas) {
        //Method to visualize images in canvas organized in columns and rows
        // this method should be completed by the students

        let ctx = canvas.getContext('2d');
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        let xImg = 20;
        let yImg = 0;

        for(let i=0; i<this.imgCanva.length; i++){

            let lastPosImg = xImg + this.imgCanva[i].w;

            if(lastPosImg > canvas.width){
                yImg += this.imgCanva[i].h + this.yBetweenImg;
                xImg = 20;
            }

            this.imgCanva[i].setPosition(xImg, yImg);
            this.imgCanva[i].draw(canvas);
            
            xImg += this.xBetweenImg + this.imgCanva[i].w;
        }
    }

    /**
     * Finds and returns an image from the canvas based on mouse coordinates.
     * @param {number} mx - The x-coordinate of the mouse.
     * @param {number} my - The y-coordinate of the mouse.
     * @returns {Image|null} - The image object if found, or null if no image is found.
     */
    selectedImg(mx, my) {
        return this.imgCanva.find((img) => {
            if (img.mouseOver(mx, my)) {
                return true;
            }
            return false;
        });
    }

    /**
     * Fades out an HTML element by reducing its opacity gradually.
     * @param {HTMLElement} element - The HTML element to be faded out.
     */
    fadeOut(element) {
        let opacity = 1;
        const interval = setInterval(function () {
            if (opacity > 0) {
                opacity -= 0.1;
                element.style.opacity = opacity;
            } else {
                clearInterval(interval);
                element.style.display = "none";
            }
        }, 100);
    }

    /**
     * Randomly selects a subset of images from the entire collection to display on the canvas.
     */
    radomBeginImg(){
        this.imgCanva = [];

        for (let i = this.allpictures.stuff.length; i > 0; i--) {

            let j = Math.floor(Math.random() * (i+1));

            this.imgCanva.push(this.allpictures.stuff[j]);

            if(this.imgCanva.length === this.numShownPic){
                break;
            }
		}
    }
}

/**
 * Represents a pool of objects with a maximum size.
 */
class Pool {
    /**
     * Creates a new instance of Pool.
     * @param {number} maxSize - The maximum size of the pool.
     */
    constructor(maxSize) {
        this.size = maxSize;
        this.stuff = [];
    }

    /**
     * Inserts an object into the pool.
     * @param {any} obj - The object to be inserted.
     */
    insert(obj) {
        if (this.stuff.length < this.size) {
            this.stuff.push(obj);
        } else {
            alert("The application is full: there isn't more memory space to include objects");
        }
    }

    /**
     * Removes an object from the pool.
     * If there are no objects to delete, an alert message is displayed.
     */
    remove() {
        if (this.stuff.length !== 0) {
            this.stuff.pop();
        } else {
            alert("There aren't objects in the application to delete");
        }
    }

    /**
     * Empties the pool by removing all items.
     */
    empty_Pool() {
        while (this.stuff.length > 0) {
            this.remove();
        }
    }
}