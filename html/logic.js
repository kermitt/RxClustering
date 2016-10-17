"use strict";

var Logic = {
    init : function(width, height) {
        this.w = width;
        this.h = height;
        this.matrix = [];
        this.people = 50000; 
        this.populate();
    },
    populate : function() {
    	
    },

    makeBoxCohort : function() { 
    },
}

//////////////////// //////////////////// //////////////////// ////////////////////
try { 
    module.exports.Logic = Logic;
    module.exports.Layer = Layer;
} catch ( ignore ) {
    // This will be tripped only if loaded into a webpage.
}