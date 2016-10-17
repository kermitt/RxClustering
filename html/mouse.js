"use strict";

/*
Essentially, this is a Mouse interface - it needs a canvasId and 
and ref to an obj w/ the proper methods to which this will bubble up 
mouse event and position information...

TODO: setup for touchscreens
*/

var MouseController = {

    init: function(canvasId, main_logic) {

        this.mouseIsDown = false;
        setState( "INIT " + MouseController.mouseIsDown);
        this.canvas = document.getElementById(canvasId);
        this.context = this.canvas.getContext('2d');

        //this.canvas.addEventListener('touchstart',function(evt){
        this.canvas.addEventListener('click', function(evt) {
            //touchstart
            main_logic.clicks(MouseController.getMousePos(evt));
        }, false);

        this.canvas.addEventListener('mouseup', function(evt) {
            MouseController.mouseIsDown = false;
            setState( "MOUSEUP! " + MouseController.mouseIsDown);
            main_logic.mouseUp(MouseController.getMousePos(evt));
        }, false);

        this.canvas.addEventListener('mousedown', function(evt) {
            MouseController.mouseIsDown = true;
            setState( "DOWN! "  + MouseController.mouseIsDown);
            main_logic.mouseDown(MouseController.getMousePos(evt));
        }, false);

        this.canvas.addEventListener('mousemove', function(evt) {
            if (MouseController.mouseIsDown) {
                main_logic.dragging(MouseController.getMousePos(evt));
            } else {
                main_logic.moving(MouseController.getMousePos(evt));
            }
        }, false);

//        this.canvas.addEventListener('mouseup', function(evt) {
 //           main_logic.mouseIsDown = false;
  //          setState( "MOUSEUP " + this.mouseIsDown);
   //     }, false);
    },

    getMousePos: function(evt) {
        var rect = this.canvas.getBoundingClientRect();
        return {
            x: evt.clientX - rect.left,
            y: evt.clientY - rect.top
        };
    },
};