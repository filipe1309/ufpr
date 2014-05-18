/*
 * jQuery resize event - v1.1 - 3/14/2010
 * http://benalman.com/projects/jquery-resize-plugin/
 * 
 * Copyright (c) 2010 "Cowboy" Ben Alman
 * Dual licensed under the MIT and GPL licenses.
 * http://benalman.com/about/license/
 */
(function($,h,c){var a=$([]),e=$.resize=$.extend($.resize,{}),i,k="setTimeout",j="resize",d=j+"-special-event",b="delay",f="throttleWindow";e[b]=250;e[f]=true;$.event.special[j]={setup:function(){if(!e[f]&&this[k]){return false}var l=$(this);a=a.add(l);$.data(this,d,{w:l.width(),h:l.height()});if(a.length===1){g()}},teardown:function(){if(!e[f]&&this[k]){return false}var l=$(this);a=a.not(l);l.removeData(d);if(!a.length){clearTimeout(i)}},add:function(l){if(!e[f]&&this[k]){return false}var n;function m(s,o,p){var q=$(this),r=$.data(this,d);r.w=o!==c?o:q.width();r.h=p!==c?p:q.height();n.apply(this,arguments)}if($.isFunction(l)){n=l;return m}else{n=l.handler;l.handler=m}}};function g(){i=h[k](function(){a.each(function(){var n=$(this),m=n.width(),l=n.height(),o=$.data(this,d);if(m!==o.w||l!==o.h){n.trigger(j,[o.w=m,o.h=l])}});g()},e[b])}})(jQuery,this);

/* 
	jAlert v.2
	Made with love by Versatility Werks (http://flwebsites.biz)
	MIT Licensed
*/
(function($,h,c){var a=$([]),e=$.resize=$.extend($.resize,{}),i,k="setTimeout",j="resize",d=j+"-special-event",b="delay",f="throttleWindow";e[b]=250;e[f]=true;$.event.special[j]={setup:function(){if(!e[f]&&this[k]){return false}var l=$(this);a=a.add(l);$.data(this,d,{w:l.width(),h:l.height()});if(a.length===1){g()}},teardown:function(){if(!e[f]&&this[k]){return false}var l=$(this);a=a.not(l);l.removeData(d);if(!a.length){clearTimeout(i)}},add:function(l){if(!e[f]&&this[k]){return false}var n;function m(s,o,p){var q=$(this),r=$.data(this,d);r.w=o!==c?o:q.width();r.h=p!==c?p:q.height();n.apply(this,arguments)}if($.isFunction(l)){n=l;return m}else{n=l.handler;l.handler=m}}};function g(){i=h[k](function(){a.each(function(){var n=$(this),m=n.width(),l=n.height(),o=$.data(this,d);if(m!==o.w||l!==o.h){n.trigger(j,[o.w=m,o.h=l])}});g()},e[b])}})(jQuery,this);$(function(){var positionAlert=function(thisAlert){thisAlert=thisAlert.find('.jAlert');var divHeight=thisAlert.innerHeight();var winHeight=$(window).height();if(divHeight<winHeight){var margin=winHeight-divHeight;margin=margin/2;}else{if(winHeight<500){var margin=20;}else{var margin=100;}}
winHeight=winHeight-margin;thisAlert.css('margin-top',margin+'px');if(divHeight>winHeight){$('html, body').animate({scrollTop:0},'fast');thisAlert.closest('.jAlertWrap').css('position','absolute');}else{thisAlert.closest('.jAlertWrap').css('position','fixed');}};(function($){$('body').append("<div id='jAlertBack'></div>");$jAlertBack=$('#jAlertBack');$.fn.jAlert=function(options){var alert=this;if(alert.length>1){alert.each(function(){$(this).jAlert(options)});return this;}
var defaultOptions={title:false,message:false,imgUrl:false,ajaxUrl:false,iframeUrl:false,iframeHeight:false,cssClass:'',id:false,theme:false,size:false,clickAnywhere:false,hideOnEsc:true,closeBtn:true,btn:false,autofocus:false,onClose:false,onOpen:false}
options=$.extend({},defaultOptions,options);if(options.theme){if(options.theme=='dark'){options.cssClass+=' jDark';}else if(options.theme=='error'){options.cssClass+=' jError';}else if(options.theme=='success'){options.cssClass+=' jSuccess';}else if(options.theme=='info'){options.cssClass+=' jInfo';}}
if(options.size){if(options.size=='small'||options.size=='sm'){options.cssClass+=' sm';}else if(options.size=='medium'||options.size=='md'){options.cssClass+=' md';}else if(options.size=='large'||options.size=='lg'){options.cssClass+=' lg';}else if(options.size=='full'){options.cssClass+=' full';}}
if(typeof options.btn=='object'||typeof options.onClose=='function'||options.autofocus){options.clickAnywhere=false;}else{options.btn=false;}
if(options.autofocus=='btn:first'){options.autofocus='.jBtn:first';}
if(options.autofocus=='btn:last'){options.autofocus='.jBtn:last';}
var anyAlertsVisible=function(){var visible=false;$('.jAlertWrap').each(function(){if($(this).is(":visible")){visible=true;return;}});return visible;}
alert.closeAlert=function(remove,onClose){var instance=$(this);if(instance.length){instance.hide('200',function(){if(remove){instance.remove();}
if(!anyAlertsVisible()){$jAlertBack.fadeOut('fast');}
if(typeof options.onClose=='function'){options.onClose(instance);}
if(typeof onClose=='function'){onClose(instance);}});}}
alert.showAlert=function(overlap,remove,onOpen){if(overlap!=false){overlap=true;}
if(remove!=false){remove=true;}
var instance=$(this);if(!overlap){$('.jAlertWrap:visible').closeAlert(remove);}
$jAlertBack.fadeIn('fast');instance.show('fast');if(typeof onOpen=='function'){onOpen(instance);}}
var showJAlert=function(content){$jAlertBack.fadeIn('fast');var div="<div class='jAlertWrap'";var topMost=$('.jAlertWrap:last')[0];if(typeof topMost!=='undefined'){var zIndex=parseInt(topMost.style.zIndex,10)+1;}else{zIndex=99999;}
div+=" style='z-index: "+zIndex+"'><div class='jAlert";div+=' '+options.cssClass;if(!options.title){div+=' noTitle';}
div+="'";if(options.id){div+=" id='"+options.id+"'";}
div+="><div>";if(options.closeBtn){div+="<div class='closeAlert jClose'>X</div>";}
if(options.title){div+="<div class='jTitle'><div>"+options.title+"</div></div>";}
div+="<div class='jContent'>"+content;if(options.btn){div+="<div style='clear: both;'></div><div class='jBtnWrap'>";$.each(options.btn,function(index,value){var thisBtn=options.btn[index];if(typeof thisBtn['href']=='undefined'){thisBtn['href']='';}
if(typeof thisBtn['class']=='undefined'){thisBtn['class']='';}
if(typeof thisBtn['label']=='undefined'){thisBtn['label']='';}
if(typeof thisBtn['id']=='undefined'){thisBtn['id']='';}
if(typeof thisBtn['target']=='undefined'){thisBtn['target']='self';}
div+="<a href='"+thisBtn['href']+"' id='"+thisBtn['id']+"' target='"+thisBtn['target']+"' class='jBtn "+thisBtn['cssClass']+"'>"+thisBtn['label']+"</a> ";});}
div+="</div></div></div></div>";var div=$(div);div.appendTo('body').show('fast',function(){positionAlert(div);div.find('.jAlert').resize(function(){positionAlert(div);});});if(options.closeBtn){div.find('.closeAlert').on('click',function(e){e.preventDefault();div.closeAlert(true);});}
if(options.clickAnywhere){$(document).on('mouseup',function(e){div.closeAlert(true);});}
if(options.btn){$.each(div.find('.jBtn'),function(index,value){var thisBtn=options.btn[index];var onClick=thisBtn['onClick'];var href=thisBtn['href'];var closeOnClick=thisBtn['closeOnClick'];$(this).on('click',function(e){if(typeof href=='undefined'||href==''||href=='#'){e.preventDefault();}
if(typeof onClick=='function'){onClick(div);}
if(closeOnClick!=false){div.closeAlert(true);}});});}
if(options.hideOnEsc){$(document).on('keydown',function(e){if(e.keyCode===27){$($('.jAlertWrap').get().reverse()).each(function(i,obj){var thisAlert=$(this);if(thisAlert.is(':visible')){if(thisAlert.index()==div.index()){div.closeAlert(true);}
return false;}});}});}
if(typeof options.onOpen=='function'){options.onOpen(div);}
div.find(options.autofocus).focus();return div;}
alert.initialize=function(){if(!options.message&&!options.ajaxUrl&&!options.iframeUrl&&!options.imgUrl){return showJAlert('Error! Content not defined.');}else if(options.ajaxUrl){$.get(options.ajaxUrl,function(content){if(options.message){content+=options.message;}
return showJAlert(content);});}else if(options.iframeUrl){content="<iframe style='border: 0px; height: "+options.iframeHeight+"; width: 100%;' src='"+options.iframeUrl+"'></iframe>";if(options.message){content+=options.message;}
return showJAlert(content);}else if(options.imgUrl){content="<div style='text-align: center;'><img style='border: 0px; max-width: 100%;' src='"+options.imgUrl+"'></div>";if(options.message){content+=options.message;}
return showJAlert(content);}else if(options.message){return showJAlert(options.message);}}
return alert.initialize();}})(jQuery);});function alert(msg){$.fn.jAlert({'message':msg});}