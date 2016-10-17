"use strict";

var Display = {
    init(m, canvasId, width, height, glassCanvasId) {

        this.activeCohort = -1;
        this.last_activeCohort = -1;

        this.matrix = m;
        this.mouseIsDown = false;
        this.canvas = document.getElementById(canvasId);
        this.context = this.canvas.getContext('2d');

        this.w = width;
        this.h = height;
        this.draw();

        this.glass_canvas = document.getElementById(glassCanvasId);
        this.glass_context = this.glass_canvas.getContext('2d');
//        this.glass_context.strokeStyle = "rgba(255,126,0,0.4)";
//        this.glass_context.strokeStyle = "rgba(255,126,0,1)";
        this.glass_context.strokeStyle = "rgba(0,0,0,1)";

        this.glass_context.lineWidth = 6;

        this.activeLayer = -1;
        this.activeCohort = -1;

        // for metro picking
        this.SELECT_MODE = "SELECT_MODE";
        this.INSPECT_MODE = "INSPECT_MODE";
        this.MODE = this.SELECT_MODE;
    },

    showControlPoint( x, y ) {
        this.context.strokeStyle = "rgba(0,0,0,0.4)";
        this.context.fillStyle = "rgba(0,0,0,0.3)";
        this.context.fillRect(x,y,10,10);
        this.context.stroke();
    }, 

    drawCohort(x1, y1, x2, y2, x3, y3, x4, y4, drop, down2) {
        this.context.beginPath();

        this.context.moveTo(x1, y1);
        this.context.bezierCurveTo(x2,y2,x3,y3,x4,y4);

        this.context.strokeStyle = "rgba(0,0,0,0.4)";

        this.context.fillStyle = "rgba(250,105,0,0.7)";
        this.context.stroke();

        this.context.restore();
    },

    getRandColor() {
        //rbga(x,y,z,a)
        var color = "rgba(";
        var r = Math.floor(Math.random() * 255);
        var g = Math.floor(Math.random() * 255);
        var b = Math.floor(Math.random() * 255);
        var a = 0.7;

        color += r + ",";
        color += g + ",";
        color += b + ",";
        color += a + ")";

        return color;
    },


    drawPopulationBar(over, pad, height, key, clr) { 

        var x1 = over ; 
        var x2 = over + (pad * 6 ); 
        var y1 = height;
        var y2 = y1 - this.ratio(height, data[key]['claimlines'], mosts['claimlines']);
        

        this.context.fillStyle = clr; //red
        this.context.beginPath();

        this.context.moveTo(x1,y1);
        this.context.lineTo(x1,y2);
        this.context.lineTo(x2,y2);
        this.context.lineTo(x2,y1);
        this.context.lineTo(x1,y1);
        
        this.context.closePath();
        this.context.fill();
    }, 

    draw() { 

    var middle_height = 200;
    this.context.moveTo(0,middle_height);
    this.context.lineTo(1650,middle_height);
    this.context.stroke(); 

    var group_count = this.len(data);
    var pad = 1600 / (( group_count * 4 ) ) ;
    var half = pad / 2; 
    var over = 0;
    var viewport = 350;
    var i = 0; 
    for ( var key in data ) { 
        var over_under = this.get_over_under(data[key]); 

        var paid = this.ratio(viewport, over_under['patient_paid'], mosts['patient_paid']);
        var cost = this.ratio(viewport, over_under['ingredient_cost'], mosts['ingredient_cost']);
        var days = this.ratio(viewport, over_under['days_supply'], mosts['days_supply']);
        var complexity = this.ratio(viewport, over_under['complexity'], mosts['complexity']);
        var pain = this.ratio(viewport, over_under['pain'], mosts['pain']);
        var velocity = this.ratio(viewport, over_under['velocity'], mosts['velocity']);

        var startingX = over;

        var background_color = "#000000";
        if ( i % 2 == 0 ) {
            background_color = "#e0e0e0";
        }
        this.drawPopulationBar(over, pad, 550, key, background_color); 


        over = this.drawOverUnder(over, pad, middle_height, paid, "#c82124");
        over = this.drawOverUnder(over, pad, middle_height, cost, "#ff6633");
        over = this.drawOverUnder(over, pad, middle_height, days, "#5733aa");
        over = this.drawOverUnder(over, pad, middle_height, complexity, "#00ff00");
        over = this.drawOverUnder(over, pad, middle_height, pain, "#0000ff");
        over = this.drawOverUnder(over, pad, middle_height, velocity, "#3366ff");
        var endingX = over;

        var middleX = ( startingX + endingX ) / 2; 

        data[key]['middleX'] = middleX; 
    
    }




    },

    get_over_under(obj) {    
        var over_under = {};

        over_under['patient_paid'] = obj['patient_paid'] - predicted['patient_paid'];
        over_under['ingredient_cost'] = obj['ingredient_cost'] - predicted['ingredient_cost'];
        over_under['days_supply'] = obj['days_supply'] - predicted['days_supply'];
        over_under['complexity'] = obj['complexity'] - predicted['complexity'];
        over_under['pain'] = obj['pain'] - predicted['pain'];
        over_under['velocity'] = obj['velocity'] - predicted['velocity'];

        return over_under; 
    },



    ratio( vp, given, max ) {
        if ( given == 0 ) { 
            return 0;
        } else { 
            var result = vp * given / max ;
            return result;
        }
    },

    len(obj) {
        var count = 0;
        for ( var key in obj ) { 
            count++;
        }
        return count;
    },

    drawOverUnder(over, pad, middle_height, value, clr) {

        this.context.fillStyle = clr; //red
        this.context.beginPath();

        this.context.moveTo(over,middle_height - 10);
        this.context.lineTo(over + pad, middle_height - 10);
        this.context.lineTo(over + pad, middle_height + 10);
        this.context.lineTo(over, middle_height + 10);
        this.context.lineTo(over,middle_height - 10);

        this.context.closePath();
        this.context.fill();


        if ( value < 0 ) {
            // This is good! e.g. 'less painful'
            var y1 = middle_height + value; // translate up [ Remember: This is a neg num that is being added ]
            var y2 = value * -1;
            this.context.moveTo(over,y1);
            this.context.lineTo(over + pad, y1);
            this.context.lineTo(over + pad, y1 + y2);
            this.context.lineTo(over, y1 + y2);
            this.context.lineTo(over,y1);
        } else {
            // This is bad! e.g., 'more painful'
            var y1 = middle_height;
            var y2 = value;
            this.context.moveTo(over,y1);
            this.context.lineTo(over + pad, y1);
            this.context.lineTo(over + pad, y1 + y2);
            this.context.lineTo(over, y1 + y2);
            this.context.lineTo(over,y1);
        }
        over += pad;

        this.context.closePath();
        this.context.fill();

        return over;
    },



    moving(pos) {
        
        if ( this.MODE === this.SELECT_MODE) {
            // MAYBE SNAPTO
            var horizon = 200;
            var dist = 1000; 
            var x = -1;
            var y = -1;

            for (var key in data ) {
                    var x = data[key]['middleX'];
                    var y = 200; 
                    var d = Math.sqrt(Math.pow((pos.x - x), 2) + Math.pow((pos.y - y), 2))
                    if ( d < dist ) {
                        dist = d;
                        this.activeCohort = key;
                    }
            }      
            if ( dist < horizon ) {
                this.setActiveCohort( pos );
            } else {
                this.glass_context.clearRect(0,0,this.w,this.h);
            }
        }
        
    },

    setActiveCohort( pos ) {

            var cx = data[this.activeCohort]['middleX'];
            var cy = 200;

            this.glass_context.clearRect(0,0,this.w,this.h);
            this.glass_context.beginPath();
            this.glass_context.moveTo(pos.x, pos.y);
            this.glass_context.lineTo(cx,cy);
            this.glass_context.stroke();
            this.glass_context.closePath();
            if ( this.last_activeCohort != this.activeCohort ) { 
                document.getElementById('claim_lines').innerHTML = data[this.activeCohort]['claimlines'];
                document.getElementById('cost').innerHTML = data[this.activeCohort]['ingredient_cost'];
                document.getElementById('paid').innerHTML = data[this.activeCohort]['patient_paid'];
                document.getElementById('velocity').innerHTML = data[this.activeCohort]['velocity'];
                document.getElementById('pain').innerHTML = data[this.activeCohort]['pain'];
                document.getElementById('complexity').innerHTML = data[this.activeCohort]['complexity'];
                document.getElementById('alive').innerHTML = 'tdb';
                document.getElementById('days').innerHTML = data[this.activeCohort]['days_supply'];
                document.getElementById('sex').innerHTML = 'tbd';

                var over_under = this.get_over_under(data[this.activeCohort]); 



                //document.getElementById('ou_claim_lines').innerHTML = over
                document.getElementById('ou_cost').innerHTML = over_under['ingredient_cost'];
                document.getElementById('ou_paid').innerHTML = over_under['patient_paid'];
                document.getElementById('ou_complexity').innerHTML = over_under['complexity'];
                document.getElementById('ou_days').innerHTML = over_under['days_supply'];
                document.getElementById('ou_pain').innerHTML = over_under['pain'];
                document.getElementById('ou_velocity').innerHTML = over_under['velocity'];

                document.getElementById('p_cost').innerHTML = predicted['ingredient_cost'];
                document.getElementById('p_paid').innerHTML = predicted['patient_paid'];
                document.getElementById('p_complexity').innerHTML = predicted['complexity'];
                document.getElementById('p_days').innerHTML = predicted['days_supply'];
                document.getElementById('p_pain').innerHTML = predicted['pain'];
                document.getElementById('p_velocity').innerHTML = predicted['velocity'];

             } else {
                // do nothing.                 
             }
             this.last_activeCohort = this.activeCohort;


    },

    mouseUp(pos) {
        //        console.log("mouseUp! " + pos.x);
    },

    mouseDown(pos) {
         
        if ( this.MODE === this.SELECT_MODE ) {
            this.MODE = this.INSPECT_MODE;
            this.glass_context.clearRect(0,0,this.w,this.h);

            var cx = data[this.activeCohort]['middleX']; 
            var cy = 200;

            this.glass_context.beginPath();
            this.glass_context.moveTo(0,cy);
            this.glass_context.lineTo(this.w,cy);
            this.glass_context.moveTo(cx,0);
            this.glass_context.lineTo(cx,this.h);
            
            this.glass_context.stroke();


            document.getElementById('globally_seen').innerHTML = featured_drugs[this.activeCohort]['seen'];
            document.getElementById('drug_complexity').innerHTML = featured_drugs[this.activeCohort]['complexity'];
            document.getElementById('drug_desc').innerHTML = featured_drugs[this.activeCohort]['drug'];






        } else {
            this.MODE = this.SELECT_MODE;
        }
        
    },

    clicks(pos) {
        //        console.log("clicks! " + pos.x);
    },

    dragging(pos) {
        //        console.log("!!! dragging! " + pos.x);
    },
}