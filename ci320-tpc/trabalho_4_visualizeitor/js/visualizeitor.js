
$( document ).ready(function() {
  
    if (window.XMLHttpRequest)
      {// code for IE7+, Firefox, Chrome, Opera, Safari
      xmlhttp=new XMLHttpRequest();
      }
    else
      {// code for IE6, IE5
      xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
      }
    xmlhttp.open("GET","alunos.xml",false);
    xmlhttp.send();
    xmlDoc=xmlhttp.responseXML;

    var aluno = xmlDoc.getElementsByTagName("ALUNO");
    var btGrr = document.getElementById('btngrr').value;

    setColor = function(cod,sig) {      
      var disc = document.getElementById(cod);
      if(disc) {
        //alert("Foi color");
        var bg = 'background-color';
        switch(sig) {
          case "Aprovado":
              $('#'+cod).css(bg,'#86C543');
            break;
          case "Reprovado":         
          case "Repr. Freq":
            $('#'+cod).css(bg,'#FF372E');
            break;
          case "Equivale":
            $('#'+cod).css(bg,'#E8E340');
            break;
          case "Matricula":
            $('#'+cod).css(bg,'#484CF5');
            break;
          default:
            break;
        }
      }
    }

     /*
        EVENTS   
     */
      
      $("#btngrr").click(function(){
        $('.cod_disc').css('background-color','white');
        var grr = document.getElementById('grr').value; 
        //aprovado em verde, reprovado em vermelho, matriculado em azul, 
        // equivalência em amarelo e não cursado em branco
        for (i=0;i<aluno.length;i++) {    
          if(aluno[i].getElementsByTagName("MATR_ALUNO")[0].childNodes[0].nodeValue == grr) {
            var cod = aluno[i].getElementsByTagName("COD_ATIV_CURRIC")[0].childNodes[0].nodeValue;
            var sig = aluno[i].getElementsByTagName("SIGLA")[0].childNodes[0].nodeValue;
            //alert(cod+" i: "+i);
            setColor(cod,sig);
          }
        }
      });
});
//GRR00000007