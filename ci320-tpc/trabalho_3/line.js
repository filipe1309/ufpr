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
        dots = new Array();
        lines = new Array();
        
        bx = canvas.width/2;
        by = canvas.height/2;
        ax = 20;
        ay = by;
        d_ab = pmy = pmx = inLine = my = mx = mv = 0;

        /*
            DRAWS
         */
        drawLine=  function() {
            // Clear canvas area
            ctx.clearRect(0,0,canvasWidth,canvasHeight);

            // Draw circle
            ctx.beginPath();
            ctx.arc(ax,ay,10,0,2*Math.PI,true); 
            ctx.fillStyle = "red";
            ctx.fill();
            
            // Draw circle
            ctx.beginPath();
            ctx.arc(bx,by,10,0,2*Math.PI,true);
            ctx.fillStyle = "red";             
            ctx.fill();

            // Draw line
            ctx.beginPath();                                   
            ctx.moveTo(ax,ay);
            ctx.lineTo(bx,by);
            ctx.lineWidth = 3;
            ctx.stroke();
            ctx.fillText("inLine: "+inLine+", a("+ax+","+ay+"), b("+bx+","+by+"), mv: "+mv+", m("+mx+","+my+"), p("+pmx+","+pmy+"), d_ab: "+d_ab,20,10);
        };

        //drawLine();
        setInterval(drawLine,20);

        /*
            PHYSICS
         */
        clickOnLine = function() {
            pmx = bx;
            pmy = by;
            d_ab = Math.sqrt( Math.pow(bx-ax,2) + Math.pow(by-ay,2) );
            while(d_ab >= 100) {
                pmx = ((ax+pmx)/2);
                pmy = ((ay+pmy)/2);
                d_ab = Math.sqrt( Math.pow(pmx-ax,2) + Math.pow(pmy-ay,2) );
            }
            mv = (ay-pmy)*mx + (pmx-ax)*my+(ax*pmy-pmx*ay); 
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
            minX = Math.min(ax,bx);
            maxX = Math.max(ax,bx);
            minY = Math.min(ay,by);
            maxY = Math.max(ay,by);
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

            if((mx > (ax-range)) && (mx < (ax+range)) && (my > (ay-range)) && (my < (ay+range))) {
                a = true; 
            } else if((mx > (bx-range)) && (mx < (bx+range)) && (my > (by-range)) && (my < (by+range))) {
                b = true;
            } else if( (mv > -lineRange) && (mv < lineRange) && ((mx >= minX - range)  && (mx <= maxX + range) && (my >= minY - range) && (my <= maxY + range))/*&& ((mx >= me) && (mx <= ma)) /*&& !dot*/) {
                if(event.button) {
                    alert("Bd");
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
                    ax = mx;
                    ay = my;
                    dot = true;
                    canvas.style.cursor = 'move';
                } else if(b) {
                    bx = mx;
                    by = my;
                    dot = true;
                    canvas.style.cursor = 'move';
                } else if(line) {
                    diffX = event.pageX - canvas.offsetLeft - oldX;
                    oldX = mx;
                    diffY = event.pageY - canvas.offsetTop - oldY;
                    oldY = my;
                    document.getElementById('lineM').innerHTML = "diffX: " + diffX + " diffY: " + diffY;
                    bx += diffX;
                    by += diffY;
                    ax += diffX;
                    ay += diffY;
                    canvas.style.cursor = 'move';                  
                } 
            }
        }

    }
    return this;
}