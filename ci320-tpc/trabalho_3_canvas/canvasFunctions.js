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
        pointSelected = line = up = false;
        object = position = firstPoint = lastPoint = d_ab = endPointy = endPointx = my = mx = lineDist = 0;

        /*
            DRAWS
         */
        drawObjects=  function() {
            // Clear canvas area
            ctx.clearRect(0,0,canvasWidth,canvasHeight);

            // Through all Objects (Poligons, lines)
            for (var i = 0; i < objects.length ; i++) {
                //objects[i]
                
                // Through all points
                for (var j = 0; j < objects[i].length; j++) {
                    //objects[j][k]
                    // Draw circle                
                    ctx.beginPath();
                    ctx.arc(objects[i][j][0],objects[i][j][1],10,0,2*Math.PI,true); 
                    ctx.fillStyle = "red";
                    ctx.fill();
                    if(j) {
                        // Draw line
                        ctx.beginPath();                                   
                        ctx.moveTo(objects[i][j-1][0],objects[i][j-1][1]);                                    
                        ctx.lineTo(objects[i][j][0],objects[i][j][1]);
                        ctx.lineWidth = 3;            
                        ctx.stroke();
                    }
                };
            };
            ctx.fillText("a("+objects[0][0][0]+","+objects[0][0][1]+"), b("+objects[0][1][0]+","+objects[0][1][1]+"), lineDist: "+lineDist+", m("+mx+","+my+"), p("+endPointx+","+endPointy+")",20,10);
            ctx.fillText("object: "+object+", firstPoint("+firstPoint+"), selected/lastPoint("+lastPoint+"), ->"+JSON.stringify(objects),20,20);               
            ctx.fillText("pointSelected: "+pointSelected+", line: "+line,20,30);                           
        };  

        //drawObjects();
        setInterval(drawObjects,20);

        /*
            PHYSICS
         */        
        restoreParams = function() {
            range = 20;
            lineRange = 1200;
            up = pointSelected = line = false; 
        }

        defineLineRange = function() {
            minX = Math.min(objects[object][lastPoint-1][0],objects[object][lastPoint][0]);
            maxX = Math.max(objects[object][lastPoint-1][0],objects[object][lastPoint][0]);
            minY = Math.min(objects[object][lastPoint-1][1],objects[object][lastPoint][1]);
            maxY = Math.max(objects[object][lastPoint-1][1],objects[object][lastPoint][1]);
        }

        selectClickedObject = function(event) {
            // Define the first line of first object as selected for init
            // Dist a(x,y) m(x,y) + Dist b(x,y) m(x,y) 
            //minDistLine = Math.sqrt( Math.pow(mx-objects[0][0][0],2) + Math.pow(my-objects[0][0][1],2) ) + Math.sqrt( Math.pow(mx-objects[0][1][0],2) + Math.pow(my-objects[0][0][1],2) );
            lastPoint = 1;
            firstPoint = out = object = 0;

            for (var i = 0; i < objects.length && !out; i++) {
                for (var j = 0; j < objects[i].length && !out; j++) {
                    endPointx = objects[i][j][0];
                    endPointy = objects[i][j][1];
                    object = i;
                    lastPoint = j;
                    // Verify if a point is clicked
                    if((mx > (endPointx-range)) && (mx < (endPointx+range)) && (my > (endPointy-range)) && (my < (endPointy+range))) {
                        pointSelected = true;
                        out = 1;
                    } else if(j && !out) { // May have clicked on a line
                        newEndPointx = endPointx;
                        newEndPointy = endPointy;
                        startPointx = objects[object][j-1][0];
                        startPointy = objects[object][j-1][1];
                        // Ditance point - point
                        d_ab = Math.sqrt( Math.pow(endPointx-startPointx,2) + Math.pow(endPointy-startPointy,2) );
                        while(d_ab >= 100) {
                            newEndPointx = ((startPointx+newEndPointx)/2);
                            newEndPointy = ((startPointy+newEndPointy)/2);
                            d_ab = Math.sqrt( Math.pow(newEndPointx-startPointx,2) + Math.pow(newEndPointy-startPointy,2) );
                        }
                        // Line Equation, for distance of mouse click and the line
                        lineDist = (startPointy-newEndPointy)*mx + (newEndPointx-startPointx)*my+(startPointx*newEndPointy-newEndPointx*startPointy);                    
                        defineLineRange();
                        if( (lineDist > -lineRange) && (lineDist < lineRange) && ((mx >= minX - range)  && (mx <= maxX + range) && (my >= minY - range) && (my <= maxY + range))/*&& ((mx >= me) && (mx <= ma)) /*&& !dot*/) {
                            line = true;
                            out = 1;    
                            firstPoint = lastPoint-1; 
                            linex = mx;
                            liney = my;                      
                            if(event.button) {                    
                                brokeLine();
                            }         
                        }                         
                    }
                }                
            }
        }    
        
        brokeLine = function() {
            objects[object].splice(lastPoint, 0, [mx,my]);
        }

        /*
            EVENTS
         */
        canvas.onmousedown=function(event) {
            mx = event.pageX - this.offsetLeft;
            my = event.pageY - this.offsetTop;
            restoreParams();
            selectClickedObject(event);
        };  

        canvas.onmouseup=function(event) {
            up = true;
            canvas.style.cursor = 'default';
        };

        canvas.onmousemove=function(event) {
            if(!up) {
                mx = event.pageX - this.offsetLeft;
                my = event.pageY - this.offsetTop;
                if(pointSelected) {                             
                    objects[object][lastPoint][0] = mx;
                    objects[object][lastPoint][1] = my;
                    canvas.style.cursor = 'move';
                } else if(line) {
                    document.getElementById('lineM').innerHTML = "Foi<br>";
                    diffX = event.pageX - canvas.offsetLeft - linex;
                    linex = mx;
                    document.getElementById('lineM').innerHTML = "Foi2<br>";                    
                    diffY = event.pageY - canvas.offsetTop - liney;
                    liney = my;
                    document.getElementById('lineM').innerHTML = "diffX: " + diffX + " diffY: " + diffY;
                    objects[object][firstPoint][0] += diffX;
                    objects[object][firstPoint][1] += diffY;
                    objects[object][lastPoint][0] += diffX;
                    objects[object][lastPoint][1] += diffY;
                    canvas.style.cursor = 'move';                  
                } 
            }
        }
    }
    return this;
}