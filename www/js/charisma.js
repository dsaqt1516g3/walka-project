var authToken = JSON.parse(sessionStorage["auth-token"]);

$(document).ready(function () {
    //themes, change CSS with JS
    //default theme(CSS) is cerulean, change it if needed
    var defaultTheme = 'cerulean';
/*
    if(sessionStorage["auth-token"]){
	var testTo = sessionStorage["auth-token"];
	console.log(testTo);
	} else {
	console.log("No agafa token");
	}
	//var authToken = JSON.parse(sessionStorage["auth-token"]);
	*/
    var currentTheme = $.cookie('currentTheme') == null ? defaultTheme : $.cookie('currentTheme');
    var msie = navigator.userAgent.match(/msie/i);
    $.browser = {};
    $.browser.msie = {};
    switchTheme(currentTheme);

    $('.navbar-toggle').click(function (e) {
        e.preventDefault();
        $('.nav-sm').html($('.navbar-collapse').html());
        $('.sidebar-nav').toggleClass('active');
        $(this).toggleClass('active');
    });

    var $sidebarNav = $('.sidebar-nav');

    // Hide responsive navbar on clicking outside
    $(document).mouseup(function (e) {
        if (!$sidebarNav.is(e.target) // if the target of the click isn't the container...
            && $sidebarNav.has(e.target).length === 0
            && !$('.navbar-toggle').is(e.target)
            && $('.navbar-toggle').has(e.target).length === 0
            && $sidebarNav.hasClass('active')
            )// ... nor a descendant of the container
        {
            e.stopPropagation();
            $('.navbar-toggle').click();
        }
    });


    $('#themes a').click(function (e) {
        e.preventDefault();
        currentTheme = $(this).attr('data-value');
        $.cookie('currentTheme', currentTheme, {expires: 365});
        switchTheme(currentTheme);
    });


    function switchTheme(themeName) {
        if (themeName == 'classic') {
            $('#bs-css').attr('href', 'bower_components/bootstrap/dist/css/bootstrap.min.css');
        } else {
            $('#bs-css').attr('href', 'css/bootstrap-' + themeName + '.min.css');
        }

        $('#themes i').removeClass('glyphicon glyphicon-ok whitespace').addClass('whitespace');
        $('#themes a[data-value=' + themeName + ']').find('i').removeClass('whitespace').addClass('glyphicon glyphicon-ok');
    }

    //disbaling some functions for Internet Explorer
    if (msie) {
        $('#is-ajax').prop('checked', false);
        $('#for-is-ajax').hide();
        $('#toggle-fullscreen').hide();
        $('.login-box').find('.input-large').removeClass('span10');

    }


    //highlight current / active link
    $('ul.main-menu li a').each(function () {
        if ($($(this))[0].href == String(window.location))
            $(this).parent().addClass('active');
    });

    //establish history variables
    var
        History = window.History, // Note: We are using a capital H instead of a lower h
        State = History.getState(),
        $log = $('#log');

    //bind to State Change
    History.Adapter.bind(window, 'statechange', function () { // Note: We are using statechange instead of popstate
        var State = History.getState(); // Note: We are using History.getState() instead of event.state
        $.ajax({
            url: State.url,
            success: function (msg) {
                $('#content').html($(msg).find('#content').html());
                $('#loading').remove();
                $('#content').fadeIn();
                var newTitle = $(msg).filter('title').text();
                $('title').text(newTitle);
                docReady();
            }
        });
    });


    //other things to do on document ready, separated for ajax calls
    docReady();
});

$(function(){
   getCurrentUserProfile(function(user){ 
   		//hidden-sm hidden-xs
   	  $("#sname").text('  '+user.fullname);
      //$("#prof").attr("href", "http://www.google.com/");
      $("#prof").attr("rel", user.id);
   });

});


//
$('#next-button').click(function() {
    $('#calendar').fullCalendar('next');
	var moment = $('#calendar').fullCalendar('getDate');
		alert(moment.format());
});
$('#prev-button').click(function() {
    $('#calendar').fullCalendar('prev');
	var moment = $('#calendar').fullCalendar('getDate');
		alert(moment.format());
});
/*
$('#calendar').fullCalendar('prev').click(function (){
var moment = $('#calendar').fullCalendar('getDate');
		alert(moment.format());
});
*/
		

function docReady() {

    //prevent # links from moving to top
    $('a[href="#"][data-top!=true]').click(function (e) {
        e.preventDefault();
    });

    //notifications
    $('.noty').click(function (e) {
        e.preventDefault();
        var options = $.parseJSON($(this).attr('data-noty-options'));
        noty(options);
    });

    //chosen - improves select
    $('[data-rel="chosen"],[rel="chosen"]').chosen();

    //tabs
    $('#myTab a:first').tab('show');
    $('#myTab a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });


    //tooltip
    $('[data-toggle="tooltip"]').tooltip();

    //auto grow textarea
    $('textarea.autogrow').autogrow();

    //popover
    $('[data-toggle="popover"]').popover();

    //iOS / iPhone style toggle switch
    $('.iphone-toggle').iphoneStyle();

    //star rating
    $('.raty').raty({
        score: 4 //default stars
    });

    //uploadify - multiple uploads
    $('#file_upload').uploadify({
        'swf': 'misc/uploadify.swf',
        'uploader': 'misc/uploadify.php'
        // Put your options here
    });

    //gallery controls container animation
    $('ul.gallery li').hover(function () {
        $('img', this).fadeToggle(1000);
        $(this).find('.gallery-controls').remove();
        $(this).append('<div class="well gallery-controls">' +
            '<p><a href="#" class="gallery-edit btn"><i class="glyphicon glyphicon-edit"></i></a> <a href="#" class="gallery-delete btn"><i class="glyphicon glyphicon-remove"></i></a></p>' +
            '</div>');
        $(this).find('.gallery-controls').stop().animate({'margin-top': '-1'}, 400);
    }, function () {
        $('img', this).fadeToggle(1000);
        $(this).find('.gallery-controls').stop().animate({'margin-top': '-30'}, 200, function () {
            $(this).remove();
        });
    });


    //gallery image controls example
    //gallery delete
    $('.thumbnails').on('click', '.gallery-delete', function (e) {
        e.preventDefault();
        //get image id
        //alert($(this).parents('.thumbnail').attr('id'));
        $(this).parents('.thumbnail').fadeOut();
    });
    //gallery edit
    $('.thumbnails').on('click', '.gallery-edit', function (e) {
        e.preventDefault();
        //get image id
        //alert($(this).parents('.thumbnail').attr('id'));
    });

    //gallery colorbox
    $('.thumbnail a').colorbox({
        rel: 'thumbnail a',
        transition: "elastic",
        maxWidth: "95%",
        maxHeight: "95%",
        slideshow: true
    });

    //gallery fullscreen
    $('#toggle-fullscreen').button().click(function () {
        var button = $(this), root = document.documentElement;
        if (!button.hasClass('active')) {
            $('#thumbnails').addClass('modal-fullscreen');
            if (root.webkitRequestFullScreen) {
                root.webkitRequestFullScreen(
                    window.Element.ALLOW_KEYBOARD_INPUT
                );
            } else if (root.mozRequestFullScreen) {
                root.mozRequestFullScreen();
            }
        } else {
            $('#thumbnails').removeClass('modal-fullscreen');
            (document.webkitCancelFullScreen ||
                document.mozCancelFullScreen ||
                $.noop).apply(document);
        }
    });

    //tour
    if ($('.tour').length && typeof(tour) == 'undefined') {
        var tour = new Tour();
        tour.addStep({
            element: "#content", /* html element next to which the step popover should be shown */
            placement: "top",
            title: "Custom Tour", /* title of the popover */
            content: "You can create tour like this. Click Next." /* content of the popover */
        });
        tour.addStep({
            element: ".theme-container",
            placement: "left",
            title: "Themes",
            content: "You change your theme from here."
        });
        tour.addStep({
            element: "ul.main-menu a:first",
            title: "Dashboard",
            content: "This is your dashboard from here you will find highlights."
        });
        tour.addStep({
            element: "#for-is-ajax",
            title: "Ajax",
            content: "You can change if pages load with Ajax or not."
        });
        tour.addStep({
            element: ".top-nav a:first",
            placement: "bottom",
            title: "Visit Site",
            content: "Visit your front end from here."
        });

        tour.restart();
    }

    //datatable
    $('.datatable').dataTable({
        "sDom": "<'row'<'col-md-6'l><'col-md-6'f>r>t<'row'<'col-md-12'i><'col-md-12 center-block'p>>",
        "sPaginationType": "bootstrap",
        "oLanguage": {
            "sLengthMenu": "_MENU_ records per page"
        }
    });
    $('.btn-close').click(function (e) {
        e.preventDefault();
        $(this).parent().parent().parent().fadeOut();
    });
    $('.btn-minimize').click(function (e) {
        e.preventDefault();
        var $target = $(this).parent().parent().next('.box-content');
        if ($target.is(':visible')) $('i', $(this)).removeClass('glyphicon-chevron-up').addClass('glyphicon-chevron-down');
        else                       $('i', $(this)).removeClass('glyphicon-chevron-down').addClass('glyphicon-chevron-up');
        $target.slideToggle();
    });
    $('.btn-setting').click(function (e) {
        e.preventDefault();
        $('#myModal').modal('show');
    });

 /*   
$("editarButton").click(function(event) {
  event.preventDefault();
  console.log("Muestra el autributo?");
  console.log($('#popEditLink').attr('rel'));
});
*/
    var uri = authToken["links"]["fill-calendar"].uri+'/between';
    $('#calendar').fullCalendar({
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month'
        },
        events: {
        url: uri,
        type: 'GET',
		cache: true,
		headers: {'X-Auth-Token':authToken.token},
        error: function() {
            alert('Problem');
        },
		success: function(data) {
		console.log(data);
			return data.events; 
		},
        color: 'blue',   // a non-ajax option
        textColor: 'black' // a non-ajax option

    },
	 eventClick: function(event) {
        /* if (event.url) {
            alert(event.url);
            return false; */
            console.log(event);
            var inicio = new Date();
            inicio = JSON.stringify(event.start);
            end = JSON.stringify(event.end);
			inicio = inicio.replace("T"," ");
			inicio = inicio.replace("Z"," ");
			inicio = inicio.replace(".000"," ");
			inicio = inicio.replace(/['"]+/g, '');
			end = end.replace("T"," ");
			end = end.replace("Z"," ");
			end = end.replace(".000"," ");
			end = end.replace(/['"]+/g, '');
			
			/*$("#btn_mod_enable").click(function(e) {
			e.preventDefault();
			console.log("Ha funcionado el click en el boton edit");
			});*/
			
            $("#popTitle").text(event.title);
            $("#popCreator").html('<h6 align="left"><b style="color:black">Creado por: </b>'+event.creatorName+'</h6>');
            $("#popLocation").html('<h6 align="right"><b style="color:black">Ubicación: </b><a href="https://www.google.es/maps/search/'+event.location+'">'+event.location+'</h6>');
            $("#popDate").html('<h6 align="left"><b style="color:black">Empieza: &nbsp&nbsp</b>'+inicio+'<b style="color:black">&nbsp&nbspAcaba: </b>'+end+'</h6>');
            $("#popEvent").html(event.notes+'<hr>');
            $("#popParticipants").text('');
            $("#popParticipants").append('<h6>Participantes:</h6><ul>');
            //$("#popParticipants").append('<h6>Participantes:</h6><ul>');	
			$.each(event.participants.users,function(i,user){
				$("#popParticipants").append('<li class="perf" rel="'+user.id+'">'+user.fullname+'</li>');
			});
			$("#popParticipants").append('</ul>');
			$("#footerEdit").text('');
			//$("#footerEdit").html('<a href="editevent.html" class="btn btn-primary" id="popDeleteLink">Eliminar</a>');
			$("#footerEdit").html('<form id="editarEvento"><button type="submit" class="btn btn-success">Editar</button></form>');
			if(authToken.userid === event.creator){
				$("#footer").text('');
				$("#footer").html('<form id="eliminarEvento"><button type="submit" class="btn btn-danger">Eliminar</button></form>');
			}else{
			if(authToken.userid != event.creator)
			$("#footer").text('');
			$("#footer").html('<form id="leaveEvent"><button type="submit" class="btn btn-danger">Abandonar</button></form>');
			}
			$("#popParticipants").on("click", ".perf", function(e){
				e.preventDefault();
				console.log("esto pilla del rel de cada partiicpante");
				console.log("http://147.83.7.204:8087/walka/users/"+$(this).attr('rel'));	  	
  				var uri = "http://147.83.7.204:8087/walka/users/"+$(this).attr('rel');
  				sessionStorage["tes"] = uri;
  				window.location.replace('perfil.html');
			});
			event.links = linksToMap(event.links);
			$("#eliminarEvento").submit(function(e) {
				e.preventDefault();
				deleteEvent(event.id,function(){
  				console.log("que si que se ha borrado");	
				});
			});
			
			$("#leaveEvent").submit(function(e) {
				e.preventDefault();
				var uri = event.links.leaveEvent.uri;
				leaveEvent(uri,function(){
  				console.log("que si que se ha borrado");	
				});
			});
			
			//event.links = linksToMap(event.links);
			
			$("#editarEvento").submit(function(e) {
				e.preventDefault();
				//var uri = event["links"]["edit-event"].uri;
				//console.log("esta es la uri del evento a editarasdfasfd");
				//console.log(uri);
				sessionStorage["tesi"] = event.id;
				window.location.replace('editevent.html');		
			});
			
			//$("#popParticipants").append('</ul>');	
            $('#myModal').modal('show');
        //<a href="#" class="btn btn-info btn-setting">Click for dialog</a>     
        //}
    }
    });

}
