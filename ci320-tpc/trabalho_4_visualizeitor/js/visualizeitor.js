
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

    $(".table").on('contextmenu', function(e){return false;});
    var aluno = xmlDoc.getElementsByTagName("ALUNO");
    var btGrr = document.getElementById('btngrr').value;
    var opt = 0;
    var grr;
    

    getDisciplineId = function(alunoTag) {  
      //var desc_est = aluno[alunoTag].getElementsByTagName("DESCR_ESTRUTURA")[0].childNodes[0].nodeValue;   
      var desc_est = $(alunoTag).find("DESCR_ESTRUTURA").text();
      var cod = $(alunoTag).find("COD_ATIV_CURRIC").text();
      if(document.getElementById(cod)) {
        return '#'+cod;
      } else if(desc_est == 'Obrigatórias') {
          // Disciplina é obrigatória no 
          // xml, mas não está na grade atual, EX: CI066
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
      grr = document.getElementById('grr').value; 
      $(aluno).each(function(){
        tmpGrr = $(this).find("MATR_ALUNO").text();
        if(tmpGrr == grr) {
          var sig = $(this).find("SIGLA").text();
          var cod = getDisciplineId(this);
          setColor(cod,sig);
        }
      });
    }

    getLastTime = function(discSelected) {
      var dc, tmpGrr, disc, semester, year, grade, freq, cod;
      dc = tmpGrr = disc = semester = year = grade = freq = cod = ''; 
      $(aluno).each(function(){
        tmpGrr = $(this).find("MATR_ALUNO").text();
        dc = $(this).find ("COD_ATIV_CURRIC").text();
        if(tmpGrr == grr && dc == discSelected) { 
          disc = $(this).find("NOME_ATIV_CURRIC").text(); 
          semester = $(this).find("PERIODO").text();                 
          year = $(this).find("ANO").text();         
          grade = $(this).find("MEDIA_FINAL").text();                                                         
          freq = $(this).find("FREQUENCIA").text(); 
        }
      });
      if(disc != '')
        alert("Disciplina: "+disc+"\nCódigo: "+discSelected+"\nCursado em: "+semester+" / "+year+"\nNota: "+grade+"\nFrequencia: "+freq);
    } 

    getHistory = function(discSelected) {
      var dc, tmpGrr, disc, semester, year, grade, freq, cod, hist;
      dc = tmpGrr = disc = semester = year = grade = freq = cod = hist = ''; 
      $(aluno).each(function(){
        tmpGrr = $(this).find("MATR_ALUNO").text();
        dc = $(this).find ("COD_ATIV_CURRIC").text();
        if(tmpGrr == grr && dc == discSelected) {
          disc = $(this).find("NOME_ATIV_CURRIC").text(); 
          semester = $(this).find("PERIODO").text();                 
          year = $(this).find("ANO").text();         
          grade = $(this).find("MEDIA_FINAL").text();                                                         
          freq = $(this).find("FREQUENCIA").text(); 
          hist += "Disciplina: "+disc+"\nCódigo: "+discSelected+"\nCursado em: "+semester+" / "+year+"\nNota: "+grade+"\nFrequencia: "+freq;
          hist += "\n-------------\n";
        }
      });
      if(disc != '')
        alert(hist);
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
});
//GRR00000007