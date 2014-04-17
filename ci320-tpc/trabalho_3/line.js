function createObj() {
    canvas = document.getElementById("canvasBox");
    if(canvas &&  canvas.getContext) { 
        canvasWidth = 600;
        canvasHeight = 400;
        with(canvas) {
            style.border = "5px solid black";
            width = canvasWidth;
            height = canvasHeight;
        }
        ctx = canvas.getContext("2d");
        canvas.oncontextmenu = function(){return false}

        /*
            INIT
         */
        points = new Array();
        objects = new Array();
        points[0] = [20,(canvas.height/2)];
        points[1] = [(canvas.width/2),(canvas.height/2)];
        objects[0] = [points[0],points[1]];
        up = false;
        dpa = dpb = distLine = d_ab = pmy = pmx = inLine = my = mx = mv = 0;

        /*
            DRAWS
         */
        drawLine=  function() {
            // Clear canvas area
            ctx.clearRect(0,0,canvasWidth,canvasHeight);

            // Draw circle
            ctx.beginPath();
            ctx.arc(objects[0][0][0],objects[0][0][1],10,0,2*Math.PI,true); 
            ctx.fillStyle = "red";
            ctx.fill();
            
            // Draw circle
            ctx.beginPath();
            ctx.arc(objects[0][1][0],objects[0][1][1],10,0,2*Math.PI,true);
            ctx.fillStyle = "red";             
            ctx.fill();

            // Draw line
            ctx.beginPath();                                   
            ctx.moveTo(objects[0][0][0],objects[0][0][1]);
            ctx.lineTo(objects[0][1][0],objects[0][1][1]);
            ctx.lineWidth = 3;
            ctx.stroke();
            ctx.fillText("inLine: "+inLine+", a("+objects[0][0][0]+","+objects[0][0][1]+"), b("+objects[0][1][0]+","+objects[0][1][1]+"), mv: "+mv+", m("+mx+","+my+"), p("+pmx+","+pmy+"), d_ab: "+d_ab+", distLine: "+distLine+", dpa: "+dpa+", dpb: "+dpb,20,10);
        };

        //drawLine();
        setInterval(drawLine,20);

        /*
            PHYSICS
         */
        clickOnLine = function() {
            pmx = objects[0][1][0];
            pmy = objects[0][1][1];
            // Ditance point - point
            d_ab = Math.sqrt( Math.pow(objects[0][1][0]-objects[0][0][0],2) + Math.pow(objects[0][1][1]-objects[0][0][1],2) );
            while(d_ab >= 100) {
                pmx = ((objects[0][0][0]+pmx)/2);
                pmy = ((objects[0][0][1]+pmy)/2);
                d_ab = Math.sqrt( Math.pow(pmx-objects[0][0][0],2) + Math.pow(pmy-objects[0][0][1],2) );
            }
            // Line Equation
            mv = (objects[0][0][1]-pmy)*mx + (pmx-objects[0][0][0])*my+(objects[0][0][0]*pmy-pmx*objects[0][0][1]); 
            selectedObj = 0;
        }

        restoreParams = function() {
            range = 20;
            lineRange = 1200;
            up = false;
            a = false;
            b =false;
            line = false; 
        }

        defineLineRange = function() {
            minX = Math.min(objects[0][0][0],objects[0][1][0]);
            maxX = Math.max(objects[0][0][0],objects[0][1][0]);
            minY = Math.min(objects[0][0][1],objects[0][1][1]);
            maxY = Math.max(objects[0][0][1],objects[0][1][1]);
        }

        findCloserPointsOfObj = function(object) {
            alert("ax: "+objects[object][0][0]+", ay: "+objects[object][0][1]);

            dpa = Math.sqrt( Math.pow(mx-object[0][0],2) + Math.pow(my-object[0][1],2) );
            //for (var i = 1; i < object.length; i++) {
                // d_ab = sqrt( pow(bx-ax,2) + pow(by-ay,2) )
                dpb = Math.sqrt( Math.pow(mx-object[0][0],2) + Math.pow(my-object[0][1],2) );
                distLine = dpa + dpb;
            //};
        }
        
        brokeLine = function(object) {
            //alert("Bd");
            //points[] = [mx,my];
            findCloserPointsOfObj(object);
        }

        /*
            EVENTS
         */
        canvas.onmousedown=function(event) {
            mx = event.pageX - this.offsetLeft;
            my = event.pageY - this.offsetTop;
            clickOnLine();
            restoreParams();
            defineLineRange();

            if((mx > (objects[0][0][0]-range)) && (mx < (objects[0][0][0]+range)) && (my > (objects[0][0][1]-range)) && (my < (objects[0][0][1]+range))) {
                a = true; 
            } else if((mx > (objects[0][1][0]-range)) && (mx < (objects[0][1][0]+range)) && (my > (objects[0][1][1]-range)) && (my < (objects[0][1][1]+range))) {
                b = true;
            } else if( (mv > -lineRange) && (mv < lineRange) && ((mx >= minX - range)  && (mx <= maxX + range) && (my >= minY - range) && (my <= maxY + range))/*&& ((mx >= me) && (mx <= ma)) /*&& !dot*/) {
                if(event.button) {                    
                    brokeLine(selectedObj);
                } else {
                    line = true;
                    inLine = -1; 
                    oldX = mx;
                    oldY = my;
                    inLine = 0;
                }                
            }      
        };  

        canvas.onmouseup=function(event) {
            up = true;
            canvas.style.cursor = 'default';
        };

        canvas.onmousemove=function(event) {
            if(!up) {
                mx = event.pageX - this.offsetLeft;
                my = event.pageY - this.offsetTop;
                if(a) {                             
                    objects[0][0][0] = mx;
                    objects[0][0][1] = my;
                    dot = true;
                    canvas.style.cursor = 'move';
                } else if(b) {
                    objects[0][1][0] = mx;
                    objects[0][1][1] = my;
                    dot = true;
                    canvas.style.cursor = 'move';
                } else if(line) {
                    diffX = event.pageX - canvas.offsetLeft - oldX;
                    oldX = mx;
                    diffY = event.pageY - canvas.offsetTop - oldY;
                    oldY = my;
                    document.getElementById('lineM').innerHTML = "diffX: " + diffX + " diffY: " + diffY;
                    objects[0][1][0] += diffX;
                    objects[0][1][1] += diffY;
                    objects[0][0][0] += diffX;
                    objects[0][0][1] += diffY;
                    canvas.style.cursor = 'move';                  
                } 
            }
        }

    }
    return this;
}