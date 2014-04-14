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
        /* Hello World
        x = canvas.width/2;
        y = canvas.height/2;

        ctx = canvas.getContext("2d");
        ctx.font = "48pt Arial";
        ctx.textAlign = "center";
        ctx.textBaseline = "middle";
        ctx.fillStyle = "#FF0000";
        ctx.fillText("Hello World",x,y);

        ctx.font = "49pt Arial";
        ctx.textAlign = "center";
        ctx.textBaseline = "middle";
        ctx.strokeStyle = "#0000FF";
        ctx.strokeText("Hello World",x-1,y-1);
        */
        
        bx = canvas.width/2;
        by = canvas.height/2;
        ax = 20;
        ay = by;
        mv = 0;
        mx = 0;
        my = 0;
        drawLine=  function() {
            ctx.clearRect(0,0,canvasWidth,canvasHeight);
            ctx.beginPath();
            ctx.moveTo(ax,ay);
            ctx.lineTo(bx,by);
            ctx.lineWidth = 3;
            ctx.stroke();

            //mx = event.clientX - this.offsetLeft;
            //my = event.clientY - this.offsetTop;
            //mv = (ay-by)*mx + (bx-ax)*my+(ax*by-bx*ay);
            ctx.fillText("a("+ax+","+ay+"), b("+bx+","+by+"), mv: "+mv+", m("+mx+","+my+")",20,10);
            //(ay-by)*mx + (bx-ax)*my+(ax*by-bx*ay) = 0 --> ax+by+c
        };

        setInterval(drawLine,20);


        canvas.onmousedown=function(event) {
            mx = event.clientX - this.offsetLeft;
            my = event.clientY - this.offsetTop;
            mv = (ay-by)*mx + (bx-ax)*my+(ax*by-bx*ay);
            range = 20;
            up = false;
            // Define range para o clique do mouse
            /*if((mx > (ax-range)) && (mx < (ax+range)) && (my > (ay-range)) && (my < (ay+range))) {
                //alert("Clickou no a!!");
                up = false;
                canvas.onmousemove=function(event) {            
                    if(!up) {
                        ax = event.clientX - this.offsetLeft;
                        ay = event.clientY - this.offsetTop;
                    }
                };
            } else if((mx > (bx-range)) && (mx < (bx+range)) && (my > (by-range)) && (my < (by+range))) {
                //alert("Clickou no a!!");
                up = false;
                canvas.onmousemove=function(event) {                    
                    if(!up) {
                        bx = event.clientX - this.offsetLeft;
                        by = event.clientY - this.offsetTop;
                    }
                };
            } else {

            }*/
            if(!up) {
                //mv = (ay-by)*mx + (bx-ax)*my+(ax*by-bx*ay);                    
                if((mv > 0) && (mv < 2250)){
                    //mx = event.clientX - this.offsetLeft;
                    //my = event.clientY - this.offsetTop;
                    //mv = (ay-by)*mx + (bx-ax)*my+(ax*by-bx*ay);

                    //(ay-by)*mx + (bx-ax)*my+(ax*by-bx*ay) = 0 --> ax+by+c
                    //alert("Ponto na reta!!!");
                    //mv = (ay-by)*mx + (bx-ax)*my+(ax*by-bx*ay);
                    ctx.fillText("--> mv: "+mv+", m("+mx+","+my+")",30,30);      
                }
            }

            canvas.onmousemove=function(event) {
                mv = (ay-by)*mx + (bx-ax)*my+(ax*by-bx*ay);

                if((mx > (ax-range)) && (mx < (ax+range)) && (my > (ay-range)) && (my < (ay+range))) {
                    if(!up) {
                        mx = event.clientX - this.offsetLeft;
                        my = event.clientY - this.offsetTop;
                        ax = mx;
                        ay = my;
                    }
                } else if((mx > (bx-range)) && (mx < (bx+range)) && (my > (by-range)) && (my < (by+range))) {
                    if(!up) {
                        mx = event.clientX - this.offsetLeft;
                        my = event.clientY - this.offsetTop;
                        bx = mx;
                        by = my;

                    }                    
                } else {
                    /*if(!up) {
                        //mv = (ay-by)*mx + (bx-ax)*my+(ax*by-bx*ay);                    
                        if((mv > 0) && (mv < 2250)){
                            mx = event.clientX - this.offsetLeft;
                            my = event.clientY - this.offsetTop;
                            //mv = (ay-by)*mx + (bx-ax)*my+(ax*by-bx*ay);

                            //(ay-by)*mx + (bx-ax)*my+(ax*by-bx*ay) = 0 --> ax+by+c
                            alert("Ponto na reta!!!");
                            //mv = (ay-by)*mx + (bx-ax)*my+(ax*by-bx*ay);
                        }
                    }*/
                }
            }    

            canvas.onmouseup=function(event) {
                up = true;
                mv = -1;
            };
        };

       

    }
    return this;
}