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
      //$(document).bind("contextmenu",function(e){return false;});  
      $('#container').bind("contextmenu",function(e){return false;}); 
      $('#jAlertBack').bind("contextmenu",function(e){return false;}); 
      $('.jAlertWrap').bind("contextmenu",function(e){return false;});
              
      var btGrr = $('#btngrr').val();
      var opt = 0;
      var grr, found, aux = 0;
      var statusColor = {
        'Aprovado'  :  '#86C543', // GREEN
        'Matricula' :  '#595DF6', // BLUE
        'Equivale'  :  '#E8E340', // YELLOW
        'Reprovado' :  '#FF372E', // RED
        'Repr. Freq':  '#FF483F', // LIGHT Red
        'Cancelado' :  '#BCBCBC', // SILVER
        'Tr. Total' :  '#222',    // LIGHT BLACK 
        'TrancAdm'  :  '#466',    // LIGHT BLACK         
        'Disp. c/nt':  '#E67E22'  // CARROT                        
      };

      var statusChange = {
        'hover'     :  '0.8',
        'default'   :  '#333',    // LIGHT SILVER
        'taken'     :  'white'     
      };

      /*
      var legendSpans = $('.label');      
      for(var i in statusColor){
        legendSpans.eq(aux).html(i);
        legendSpans.eq(aux).css('color',statusColor[i]);
        aux++;
      }
      */
      for(var i in statusColor){
        $("#cation").append('<span class="label" id="status'+aux+'">' + i + '</span>');
        $('#status'+aux).css('color',statusColor[i]);
        aux++;
      }
      
      setStyle = function(cod,sig) {
          //if(cod == '#OPT2') alert("SS: cod:"+cod+", sig:"+sig);
          if(cod && (statusColor[sig] !== undefined)){
            $(cod).css('background-color',statusColor[sig]); 
            if(sig != 'Equivale' && sig != 'Cancelado')
              $(cod).css('color',statusChange['taken']);      
            $(cod).hover(
              function() { // #96B6B7
                $(cod).css('opacity',statusChange['hover']);
                $(cod).css('cursor','pointer');
              },
              function() {
                $(cod).css('opacity','1');
                $(cod).css('cursor','default');                
              }
            ); 
            /*$('#'+cod).mousedown(function(event) {
                func[event.which](code);
            });*/       
          }
      }

      getDisciplineId = function(studentTag) {  
        //var desc_est = aluno[studentTag].getElementsByTagName("DESCR_ESTRUTURA")[0].childNodes[0].nodeValue;   
        var desc_est = $(studentTag).find("DESCR_ESTRUTURA").text();
        var cod = $(studentTag).find("COD_ATIV_CURRIC").text();
        if($('#'+cod).length) {
          return '#'+cod;
        } else  {
            /*
            if(desc_est == 'Obrigatórias')
            Disciplina é obrigatória no 
            xml, mas não está na grade atual, EX: CI066
            o contrário acontece com CI209
            
            if(desc_est == 'Optativas')
             
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
            */            
            var index = 0;
            if (found == 0) {
                $('#more').append('<br id="br"/>')
                row = {};
                table = $('<table class="table"></table>').addClass('table_grade').attr('id', 'opt_table').attr('width', '100%');
                index = 0;
                row[index] = $('<tr></tr>').attr('id', 'tr'+index);
                var column = $('<td></td>').addClass('titulos_table').attr('colspan', 8).text('Outras Disciplinas (optativas, eletivas, que não estão na nova grade...)');
                row[index].append(column);
                table.append(row[index]);
                found += 8;
            }
            index = Math.floor(found/8)
            if (!row[index]) {
                row[index] = $('<tr></tr>').attr('id', 'tr'+index);
                column = $('<td></td>').addClass('cod_disc').attr('id', cod).text(cod);
                row[index].append(column);
                table.append(row[index]);
            } else {
                column = $('<td></td>').addClass('cod_disc').attr('id', cod).text(cod);
                row[index].append(column);
                $('#tr' + index).replaceWith(row[index]);
            }
            found++;
            $('#more').append(table);
          
          return '#'+cod;
        } // Discipl. de outros currículos do curso
      }      

      customizeDisciplines = function() { 
        opt = 0;
        $('.cod_disc').unbind();
        found = 0;
        $('#opt_table').remove();
        $('#br').remove();
        $('.cod_disc').css('background-color','white');
        $('.cod_disc').css('color',statusChange['default']); 
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
        }

        );

        $('.cod_disc').mousedown(function(event) {
          if ($(this).css("background-color") != 'white') {
            var disc = $(this).text();
            switch (event.which) {
                case 1: // topleft 
                  // if not white           
                  getLastTime(disc);
                  break;
                case 2: // topmiddle
                  alert('middle');
                  break;
                case 3: // topright
                  getHistory(disc);
                  break;
            }
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
      showMessage = function(discSelected,msg) {
        if(msg)
          $.fn.jAlert({
                'title': discSelected,
                'message': msg,
              }).bind("contextmenu",function(e){return false;});
            //$("<div title='"+discSelected+"'>"+msg+"</div>")            
            //  .on('contextmenu', function(e){return false;})
            //  .dialog({maxHeight:600});
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
        showMessage(discSelected,msg);
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
        showMessage(discSelected,msg);
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
    
      
    }
  });    
});
//GRR00000007