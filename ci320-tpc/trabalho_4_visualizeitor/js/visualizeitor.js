$( document ).ready(function() {
    // Get xml file
  $.ajax({
    type: "GET",
    url: "xmls/alunos.xml",
    dataType: "xml",
    success: function(student) {
      /* JS-only version
      if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
      }
      else {// code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
      }
      xmlhttp.open("GET","alunos.xml",false);
      xmlhttp.send();
      xmlDoc=xmlhttp.responseXML;
      var aluno = xmlDoc.getElementsByTagName("ALUNO");
       */
      $(document).bind("contextmenu",function(e){return false;}); 
      var btGrr = $('#btngrr').val();
      var opt = 0;
      var grr, aux = 0;
      var statusColor = {
        'Aprovado'  :  '#86C543', // Green
        'Matricula' :  '#595DF6', // Blue
        'Equivale'  :  '#E8E340', // Yellow
        'Reprovado' :  '#FF372E', // Red
        'Repr. Freq':  '#FF372E', // Red
        'Cancelado' :  '#DEDEDE', // SILVER
        'Tr. Total' :  '#222',    // LIGHT BLACK BDC3C7        
        'hover'     :  '0.8',
        'default'   :  '#333',    // LIGHT SILVER
        'taken'     :  'white'     
      };

      var legendSpans = $('.label');      
      for(var i in statusColor){
        legendSpans.eq(aux).html(i);
        legendSpans.eq(aux).css('color',statusColor[i]);
        aux++;
      }
  
      getDisciplineId = function(studentTag) {  
        //var desc_est = aluno[studentTag].getElementsByTagName("DESCR_ESTRUTURA")[0].childNodes[0].nodeValue;   
        var desc_est = $(studentTag).find("DESCR_ESTRUTURA").text();
        var cod = $(studentTag).find("COD_ATIV_CURRIC").text();
        if($('#'+cod).length) {
          return '#'+cod;
        } else if(desc_est == 'Obrigatórias') {
            // Disciplina é obrigatória no 
            // xml, mas não está na grade atual, EX: CI066
            // o contrário acontece com CI209
          } else if(desc_est == 'Optativas') {
            if(opt) {
              var valueDisc;
              for (var j = 1; j <= opt; j++) {
                valueDisc = $('#OPT'+j).text();
                // Se a disciplina ja está na lista de optativas    
                if(valueDisc == cod) {
                  return '#OPT'+opt;  
                }
              }
            }
            if(opt < 6){ // Nova optativa - Se não acabou os slots para optativas
                opt++;
                $('#OPT'+opt).html(cod);
                return '#OPT'+opt;
            }        
        } // Discipl. de outros currículos do curso
        return false;
      }

      setStyle = function(cod,sig) {
          //if(cod == '#OPT2') alert("SS: cod:"+cod+", sig:"+sig);
          if(cod && (statusColor[sig] !== undefined)){
            $(cod).css('background-color',statusColor[sig]); 
            if(sig != 'Equivale' && sig != 'Cancelado')
              $(cod).css('color',statusColor['taken']);      
            $(cod).hover(
              function() { // #96B6B7
                $(cod).css('opacity',statusColor['hover']);
                $(cod).css('cursor','pointer');
              },
              function() {
                $(cod).css('opacity','1');
                $(cod).css('cursor','default');                
              }
            );        
          }
      }

      customizeDisciplines = function() { 
        opt = 0;
        //$('.cod_disc').unbind();
        $('.cod_disc').css('background-color','white');
        $('.cod_disc').css('color',statusColor['default']); 
        $('.cod_disc').hover(
          function() {                       
            $('.cod_disc').css("cursor", 'default'); 
            $('.cod_disc').css('opacity','1');
          }
        );             
        grr = $('#grr').val().toUpperCase(); 
        $(student).find("ALUNO").each(function(){
          tmpGrr = $(this).find("MATR_ALUNO").text();
          if(tmpGrr == grr) {
            var sig = $(this).find("SIGLA").text();
            var cod = getDisciplineId(this);
            setStyle(cod,sig);
          }
        });
        /*
        JQuery v1 - with JS-only parse
          $(aluno).each(function(){...}
        JS-only version
          var grr = document.getElementById('grr').value; 
          for (var i=0;i<aluno.length;i++) {    
            if(aluno[i].getElementsByTagName("MATR_ALUNO")[0].childNodes[0].nodeValue == grr) {
              var sig = aluno[i].getElementsByTagName("SIGLA")[0].childNodes[0].nodeValue;
              var cod = getDisciplineCode(i);
              setColor(cod,sig);
            }
          }
        */
      }

      getLastTime = function(discSelected) {
        var dc, tmpGrr, msg;
        dc = tmpGrr = msg = ''; 
        $(student).find("ALUNO").each(function(){
          tmpGrr = $(this).find("MATR_ALUNO").text();
          dc = $(this).find ("COD_ATIV_CURRIC").text();
          if(tmpGrr == grr && dc == discSelected) {  
            msg = getDataInfo(this,discSelected);            
          }
        });
        if(msg)
          $("<div title='"+discSelected+"'>"+msg+"</div>")            
            .on('contextmenu', function(e){return false;})
            .dialog({maxHeight:600});
      } 

      getDataInfo = function(studentTag,discSelected) {
        var disc, semester, year, grade, freq, cod;        
        disc = $(studentTag).find("NOME_ATIV_CURRIC").text(); 
        semester = $(studentTag).find("PERIODO").text();                 
        year = $(studentTag).find("ANO").text();         
        grade = $(studentTag).find("MEDIA_FINAL").text();                                                         
        freq = $(studentTag).find("FREQUENCIA").text(); 
        return "Disciplina: "+disc+"<br>Código: "+discSelected+"<br>Cursado em: "+semester+" / "+year+"<br>Nota: "+grade+"<br>Frequencia: "+freq;         
      }

      getHistory = function(discSelected) {
        var dc, tmpGrr, disc, semester, year, grade, freq, cod, msg;
        dc = tmpGrr = msg = ''; 
        aux = '';
        $(student).find("ALUNO").each(function(){
          tmpGrr = $(this).find("MATR_ALUNO").text();
          dc = $(this).find ("COD_ATIV_CURRIC").text();
          if(tmpGrr == grr && dc == discSelected) {         
            if(msg)
              msg += "<br><br>";
            msg += '<div '+aux+'">'+getDataInfo(this,discSelected)+'</div>';            
            aux = aux ? '' : 'class="histcolor"';
          }
        });
        if(msg)
          $("<div title='"+discSelected+"'>"+msg+"</div>")
            .on('contextmenu', function(e){return false;})
            .dialog({maxHeight:600});
       }

      /*
        EVENTS   
      */
      document.onkeydown = function(event) {
        if (event.keyCode == 13) {
          customizeDisciplines();
        }
      }

      $("#btngrr").click(function(){
        customizeDisciplines();
      });
    
      $('.cod_disc').mousedown(function(event) {
        var disc = $(this).text();
        switch (event.which) {
            case 1: // topleft            
                getLastTime(disc);
                break;
            case 2: // topmiddle
                alert('middle');
                break;
            case 3: // topright
                getHistory(disc);
                break;
        }
      });
    }
  });    
});
//GRR00000007