'use strict';

/**
 * Represents a drawing application that manages a pool of drawing objects.
 * @class
 */
class FotoPrint {
    /**
     * Creates a new instance of the FotoPrint class.
     * @constructor
     */
    constructor() {
        // Keeps track of the object being moved.
        this.thingInMotion = null;
        // Mouse offset in the x-direction during drag.
        this.offsetx = null;
        // Mouse offset in the y-direction during drag.
        this.offsety = null;
        // Creates a pool to manage drawing objects.
        this.shpinDrawing = new Pool(100);

        this.shpinSelect = new Pool(6);

        this.selectIndx = null;

        // Default color for drawing objects.
        this.color = '#add8e6';
    }

    /**
     * Initializes the application with various drawing objects.
     */
    initSelect() {
        // Initialize the application with various drawing objects.
        const r = new Rect(10, 20, 50, 50, this.color);
        this.shpinSelect.insert(r);

        const o = new Oval(100, 45, 25, 1, 1, this.color);
        this.shpinSelect.insert(o);

        const h = new Heart(175, 30, 60, this.color);
        this.shpinSelect.insert(h);

        /* TO DO */
        const b = new Bear(250,45, 50, this.color);
        this.shpinSelect.insert(b);

        const g = new Ghost(300, 15, 60, 50, this.color);
        this.shpinSelect.insert(g);

        const s = new Star(375, 10, 90, 70, this.color);
        this.shpinSelect.insert(s);
    }

    /**
     * Initializes and inserts a new TextObject into the drawing pool with the provided color and user-supplied text.
     * @param {string} color - The color of the text object.
     * @param {string} userText - The text provided by the user.
     */
    initText(color, userText){
        if (userText === null || userText === '') {
            // Usuário pressionou "Cancelar" (no prompt) ou não forneceu texto
            return;
        }
        // Cria e insere um novo objeto de texto no pool
        const t = new TextObject(150, 150, color, userText);
        app.shpinDrawing.insert(t);
    }

    /**
     * Draws all objects in the pool on the canvas.
     * @param {CanvasRenderingContext2D} cnv - The canvas context to draw on.
     */
    drawObj(cnv) {
        // Draw all objects in the pool on the canvas.
        this.shpinDrawing.stuff.forEach(o => o.draw(cnv));
    }

    /**
     * Checks if an object is being dragged based on mouse coordinates.
     * @param {number} mx - The x coordinate of the mouse.
     * @param {number} my - The y coordinate of the mouse.
     * @returns {boolean} - True if an object is being dragged, false otherwise.
     */
    dragObj(mx, my) {
        // Check if an object is being dragged based on mouse coordinates.
        return this.shpinDrawing.stuff.find((o, i, arr) => {
            if(o.mouseOver(mx, my)) {
                this.offsetx = mx - o.posx;
                this.offsety = my - o.posy;
                this.thingInMotion = arr.length - 1;
                arr.splice(i, 1);
                arr.push(o);
                return true;
            }
            return false;
        });
    }

    /**
     * Move the currently dragged object based on mouse coordinates.
     * @param {number} mx - The x-coordinate of the mouse.
     * @param {number} my - The y-coordinate of the mouse.
     */
    moveObj(mx, my) {
        // Move the currently dragged object based on mouse coordinates.
        this.shpinDrawing.stuff[this.thingInMotion].setPos(mx - this.offsetx,my - this.offsety);
    }

    /**
     * Removes the last object from the pool.
     */
    removeObj() {
        // Remove the last object from the pool.
        this.shpinDrawing.remove();
    }

    /**
     * Inserts a cloned object into the pool based on mouse coordinates.
     * @param {number} mx - The x-coordinate of the mouse.
     * @param {number} my - The y-coordinate of the mouse.
     * @returns {boolean} - Returns true if an object was inserted, false otherwise.
     */
    insertObj(mx, my) {
        const clone =  this.shpinDrawing.stuff.find((o, i, arr) => {
            if (o.mouseOver(mx, my)) {
                const clonedObject = this.cloneObj(o);
                this.shpinDrawing.insert(clonedObject);
                return true;
            }
            return false;
        });

        if(!clone){
            try{
                const newObjetc = this.cloneObj(this.shpinSelect.stuff[this.selectIndx]);
                newObjetc.setPos(mx, my);
                newObjetc.setColor(this.color);
                this.shpinDrawing.insert(newObjetc);
                return newObjetc;
            }catch(error){
                return false;
            }
        }else{
            return clone;
        }
    }

    /**
     * Clones a drawing object based on its type.
     * @param {Object} obj - The object to be cloned.
     * @returns {Object} - The cloned object.
     * @throws {TypeError} - If the object type cannot be cloned.
     */
    cloneObj(obj) {
        // Clone a drawing object based on its type.
        let item = {};

        switch (obj.name) {
            case 'R':
                item = new Rect(obj.posx + 20, obj.posy + 20, obj.w, obj.h, obj.color);
                break;
            case 'P':
                item = new Picture(obj.posx + 20, obj.posy + 20, obj.w, obj.h, obj.impath);
                break;
            case 'O':
                item = new Oval(obj.posx + 20, obj.posy + 20, obj.r, obj.hor, obj.ver, obj.color);
                break;
            case 'H':
                item = new Heart(obj.posx + 20, obj.posy + 20, obj.drx * 4, obj.color);
                break;
            case 'B':
                item = new Bear(obj.posx + 20, obj.posy + 20, obj.r, obj.color);
                break;
            case 'G':
                item = new Ghost(obj.posx + 20, obj.posy + 20, obj.w, obj.h, obj.color);
                break;
            case 'S':
                item = new Star(obj.posx + 20, obj.posy + 20, obj.w, obj.h, obj.color);
                break;
            case 'T':
                item = new TextObject(obj.posx + 20, obj.posy + 20, obj.color, obj.t);
                break;
            default:
                throw new TypeError('Cannot clone this type of object');
        }
        return item;
    }

    /**
     * Inserts a cloned object with a new color into the canvas based on mouse coordinates.
     * @param {number} mx - The x-coordinate of the mouse.
     * @param {number} my - The y-coordinate of the mouse.
     * @param {string} color - The new color of the object.
     * @returns {boolean} - True if an object is inserted, false otherwise.
     */
    insertInCanvasObj(mx, my, color) {
        return this.shpinSelect.stuff.find((o, i, arr) => {
            if (o.mouseOver(mx, my)) {
                const clonedObject = this.cloneObj(o);
                clonedObject.setColor(color);
                this.shpinDrawing.insert(clonedObject);
                return true;
            }
            return false;
        });
    }

    /**
     * Checks if an object is being selected based on mouse coordinates.
     * @param {number} mx - The x-coordinate of the mouse.
     * @param {number} my - The y-coordinate of the mouse.
     * @param {string} color - The new color of the object.
     */
    selectObj(mx, my, color){
        let cont = 0;
        this.shpinSelect.stuff.find((o, i, arr) => {
            if(o.mouseOver(mx, my)) {
                cont = 1;
                o.setColor(color);
                this.selectIndx = i;
            } else{
                o.setColor(this.color);
            }
        });

        if(cont===0){
            this.selectIndx = null;
        }
    }

    /**
     * Updates the color of the selected object.
     * @param {string} color - The new color of the selected object.
     */
    objSelected(color) {
        if(this.selectIndx !== null){
            const objSel = this.shpinSelect.stuff[this.selectIndx];
            objSel.setColor(color);
        }
    }

    /**
     * Draws all objects in the pool on the toolbar.
     * @param {CanvasRenderingContext2D} tlb - The toolbar context to draw on.
     */
    drawObjTLB(tlb) {
        this.shpinSelect.stuff.forEach(o => {
                o.draw(tlb);
        });
    }

    /**
     * Changes the color of all objects in the toolbar, except the selected one.
     * @param {string} color - The new color for the objects.
     */
    changeColorObjTLB(color) {
        this.shpinSelect.stuff.forEach((o, i) => {
            if(i !== this.selectIndx){
                o.setColor(color);
            } 
        });
    }

    /**
     * Inserts a new image object into the canvas.
     * @param {string} file - The file URL of the image.
     */
    insertImage(file){
        const p = new Picture(10, 300, 100, 100, file);
        this.shpinDrawing.insert(p);
    }

    /**
     * Sets the color.
     * @param {string} color - The new color.
     */
    setColor(color){
        this.color = color;
    }
}


/**
 * Represents a pool of drawing objects.
 * @class
 */
class Pool {
    /**
     * Creates a new pool object with a specified maximum size.
     * @constructor
     * @param {number} maxSize - The maximum size of the pool.
     */
    constructor(maxSize) {
        // Maximum size of the pool.
        this.size = maxSize;
        // Array to store drawing objects.
        this.stuff = [];
    }

    /**
     * Inserts an object into the pool if it's not full.
     * @param {Object} obj - The object to be inserted into the pool.
     */
    insert(obj) {
        // Insert an object into the pool if it's not full.
        if (this.stuff.length < this.size) {
            this.stuff.push(obj);
        } else {
            // Display an alert if the pool is full.
            alert('The pool is full: there isn\'t more memory space to include objects');
            /* TO DO: Consider alternative actions when the pool is full, such as removing the oldest object or expanding the pool size. */
        }
    }

    /**
     * Removes the last object from the pool if it's not empty. Displays an alert if there are no objects to delete.
     */
    remove() {
        // Remove the last object from the pool if it's not empty.
        if (this.stuff.length !== 0) {
            this.stuff.pop();
        } else {
            // Display an alert if there are no objects to delete.
            alert('There are no objects in the pool to delete');
            /* TO DO: Consider alternative actions when attempting to remove an object from an empty pool, such as ignoring the request or prompting the user for additional input. */
        }
    }
}
