
$( document ).ready(function() {
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
    var btGrr = document.getElementById('btngrr').value;
    var opt = 0;

    getDisciplineCode = function(alunoTag) {  
      var desc_est = aluno[alunoTag].getElementsByTagName("DESCR_ESTRUTURA")[0].childNodes[0].nodeValue;   
      var cod = aluno[alunoTag].getElementsByTagName("COD_ATIV_CURRIC")[0].childNodes[0].nodeValue;
      
      if(document.getElementById(cod)) {
        return '#'+cod;
      } else if(desc_est == 'Obrigatórias') {
          // Disciplina é obrigatória no 
          // xm, mas não está na grade atual, EX: CI066
          // o contrário acontece com CI209
        } else if(desc_est == 'Optativas') {
          if(opt) {
            var valueDisc;
            for (var j = 1; j <= opt; j++) {
              valueDisc = $('#OPT'+j).textContent;
              // Se a disciplica ja está na lista de optativas    
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
      }
      return false;
    }

    setColor = function(cod,sig) { 
      if(cod) {
        var bg = 'background-color';
        switch(sig) {
          case "Aprovado": // Green
            $(cod).css(bg,'#86C543');
            break;
          case "Reprovado": // Red 
          case "Repr. Freq":
            $(cod).css(bg,'#FF372E');
            break;
          case "Equivale": // Yellow
            $(cod).css(bg,'#E8E340');
            break;
          case "Matricula": // Blue
            $(cod).css(bg,'#595DF6');
            break;
          default: // White - não cursando
            break;
        }
      }
    }

    customizeDisciplines = function() { 
      opt = 0;
      $('.cod_disc').css('background-color','white');
      var grr = document.getElementById('grr').value; 
      for (var i=0;i<aluno.length;i++) {    
        if(aluno[i].getElementsByTagName("MATR_ALUNO")[0].childNodes[0].nodeValue == grr) {
          var sig = aluno[i].getElementsByTagName("SIGLA")[0].childNodes[0].nodeValue;
          var cod = getDisciplineCode(i);
          setColor(cod,sig);
        }
      }
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
});
//GRR00000007