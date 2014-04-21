function createObj() {
    canvas = document.getElementById("canvasBox");
    btAddPolygon = document.getElementById("add"); 
    inNumSides = document.getElementById("sides");
    btClear = document.getElementById("clear");    

    if(canvas &&  canvas.getContext) { 
        canvasWidth = 600;
        canvasHeight = 400;
        
        with(canvas) {
            style.border = "5px dashed #34302D";
            width = canvasWidth;
            height = canvasHeight;
        }
        ctx = canvas.getContext("2d");
        canvas.oncontextmenu = function(){return false}

        /*
            INIT
         */
        initCanvas = function(clear){
            objects = new Array();
            if(!clear)
                objects[0] = [[20,(canvas.height/2)],[(canvas.width/4),(canvas.height/2)]];
            pointSelected = line = up = false;
            object = position = firstPoint = lastPoint = d_ab = endPointy = endPointx = my = mx = lineDist = 0;
        }

        /*
            DRAWS
         */
        drawObjects = function() {
            // Clear canvas area
            ctx.clearRect(0,0,canvasWidth,canvasHeight);
            // Through all Objects (Poligons, lines)
            for (var i = 0; i < objects.length ; i++) {
                // Through all points
                for (var j = 0; j < objects[i].length; j++) {
                    // Draw circle                
                    ctx.beginPath();
                    ctx.arc(objects[i][j][0],objects[i][j][1],10,0,2*Math.PI,true); 
                    ctx.fillStyle = "#98E765";
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
            /*ctx.fillText("mouse position("+mx+","+my+")",20,10);
            ctx.fillText("object selected: "+object+", firstPoint("+firstPoint+"), selected/lastPoint("+lastPoint+")",20,20);               
            ctx.fillText("point selected: "+pointSelected+", line selected: "+line,20,30); 
            //ctx.fillText("List of objects: "+JSON.stringify(objects),20,40);             
            */                                              
        };  

        initCanvas(0);
        setInterval(drawObjects,20);

        /*
            PHYSICS
         */        
        restoreParams = function() {
            range = 12;
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
                    var diffX = event.pageX - canvas.offsetLeft - linex;
                    linex = mx;
                    var diffY = event.pageY - canvas.offsetTop - liney;
                    liney = my;
                    objects[object][firstPoint][0] += diffX;
                    objects[object][firstPoint][1] += diffY;
                    objects[object][lastPoint][0] += diffX;
                    objects[object][lastPoint][1] += diffY;
                    canvas.style.cursor = 'move';                  
                } 
            }
        };

        inNumSides.onclick=function(event) { 
            inNumSides.value = "";
        }; 

        btAddPolygon.onclick=function(event) {  
                numSides = parseInt(inNumSides.value);

                if(numSides < 3 || numSides > 8 || isNaN(numSides)) {
                    alert("Valor incorreto!!!\nDigite um valor entre 3 e 8");
                } else { 
                    var points = new Array();
                    var angle = 0.0;
                    var step = 2 * Math.PI / numSides;
                    var radius = numSides * 10;
                    for (var i = 0; i < numSides; i++) {
                        var x = radius * Math.cos(angle) + canvas.width/2;
                        var y = radius * Math.sin(angle) + canvas.height/2;
                        angle += step;
                        points.push([x,y]);
                    };
                    points.push(points[0]);
                    objects.push(points);
                }
        };

        btClear.onclick=function(event) {                
            initCanvas(1);
        };
    }
    return this;
}