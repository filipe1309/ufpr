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
        
        bx = canvas.width/2;
        by = canvas.height/2;
        ax = 20;
        ay = by;
        mv = 0;
        mx = 0;
        my = 0;
        inLine = 0;
        drawLine=  function() {
            ctx.clearRect(0,0,canvasWidth,canvasHeight);
            ctx.beginPath();
            ctx.arc(ax,ay,10,0,2*Math.PI,true); 
            ctx.fillStyle = "red";
            ctx.fill();
            
            ctx.beginPath();
            ctx.arc(bx,by,10,0,2*Math.PI,true);
            ctx.fillStyle = "red";             
            ctx.fill();

            ctx.beginPath();
            //ctx.arc(ax,ay,10,0,2*Math.PI,true); 
            //ctx.arc(bx,by,10,0,2*Math.PI,true);                                   
            ctx.moveTo(ax,ay);
            ctx.lineTo(bx,by);
            ctx.lineWidth = 3;
            ctx.stroke();
            ctx.fillText("inLine: "+inLine+", a("+ax+","+ay+"), b("+bx+","+by+"), mv: "+mv+", m("+mx+","+my+")",20,10);
            
            mv = (ay-by)*mx + (bx-ax)*my+(ax*by-bx*ay);
            if(ax > bx){
                ma = ax;
                me = bx;            
            } else {
                ma = bx;
                me = ax;
            }
        };

        //drawLine();
        setInterval(drawLine,20);

        updateAll = function(event) {
            if(!up) {
                var diffX = event.pageX - canvas.offsetLeft - oldX;
                oldX = event.pageX - canvas.offsetLeft;
                var diffY = event.pageY - canvas.offsetTop - oldY;
                oldY = event.pageY - canvas.offsetTop;
                document.inLineElementById('lineM').innerHTML = "M: " + diffX + " " + diffY;
                bx += diffX;
                by += diffY;
                ax += diffX;
                ay += diffY;
            }
        }

        canvas.onmousedown=function(event) {
            mx = event.pageX - this.offsetLeft;
            my = event.pageY - this.offsetTop;
            mv = (ay-by)*mx + (bx-ax)*my+(ax*by-bx*ay);            
            range = 20;
            up = false;
            a = false;
            b =false;
            line = false;
            minX = Math.min(ax,bx);
            maxX = Math.max(ax,bx);
            minY = Math.min(ay,by);
            maxY = Math.max(ay,by);

            if((mx > (ax-range)) && (mx < (ax+range)) && (my > (ay-range)) && (my < (ay+range))) {
                a = true;
            } else if((mx > (bx-range)) && (mx < (bx+range)) && (my > (by-range)) && (my < (by+range))) {
                b = true;
            } else if( (mv > -2250) && (mv < 2250) && ((mx >= minX - range)  && (mx <= maxX + range) && (my >= minY - range) && (my <= maxY + range))/*&& ((mx >= me) && (mx <= ma)) /*&& !dot*/) {
                line = true;
                inLine = -1; 
                oldX = event.pageX - canvas.offsetLeft;
                oldY = event.pageY - canvas.offsetTop;
                inLine = 0;
            }

            canvas.onmouseup=function(event) {
                up = true;
            };
        };  

        canvas.onmousemove=function(event) {
            if(a) {
                if(!up) {
                    mx = event.pageX - this.offsetLeft;
                    my = event.pageY - this.offsetTop;
                    ax = mx;
                    ay = my;
                    dot = true;
                }
            } else if(b) {
                if(!up) {
                    mx = event.pageX - this.offsetLeft;
                    my = event.pageY - this.offsetTop;
                    bx = mx;
                    by = my;
                    dot = true;
                }     
            } else if(line) {
                 if(!up) {
                    var diffX = event.pageX - canvas.offsetLeft - oldX;
                    oldX = event.pageX - canvas.offsetLeft;
                    var diffY = event.pageY - canvas.offsetTop - oldY;
                    oldY = event.pageY - canvas.offsetTop;
                    document.getElementById('lineM').innerHTML = "diffX: " + diffX + " diffY: " + diffY;
                    bx += diffX;
                    by += diffY;
                    ax += diffX;
                    ay += diffY;
                }             
            } 
        }

    }
    return this;
}