var BASE_URI="http://192.168.1.33:8080/walka";

function linksToMap(links){
	var map = {};
	$.each(links, function(i, link){
		$.each(link.rels, function(j, rel){
			map[rel] = link;
		});
	});

	return map;
}

function loadAPI(complete){
	$.get(BASE_URI)
		.done(function(data){
			var api = linksToMap(data.links);
			sessionStorage["api"] = JSON.stringify(api);
			complete();
		})
		.fail(function(data){
		});
}

$("#form-signin").submit(function(event) {
  event.preventDefault();
	logini = $("#inputLoginid").val();
	pass  = $("#inputPassword").val();
	login(logini, pass, function(){
  	console.log("change");
  	window.location.replace('calendar.html');
  });
});

$("#form-register").submit(function(event) {
  event.preventDefault();
	logini = $("#inputLoginid2").val();
	pass  = $("#inputPassword2").val(); 
	fulln  = $("#inputFullname").val();
	email  = $("#inputEmail").val();
	repass  = $("#inputRePassword").val();
	country = $("#inputCountry option:selected" ).text();
	city  = $("#inputCity").val();
	phone  = $("#inputPhone").val();
	//bday = $("#inputBDay").val();
	if((logini.length == 0) || (pass.length == 0) || (fulln.length == 0) || (email.length == 0) || (repass.length == 0)|| (country.length == 0)|| (city.length == 0) || (phone.length == 0)){
	alert("Rellena todos los campos para poder registrarte."); 
	} else if (pass != repass){
	alert("Las contraseñas no coinciden"); 
	}else{
	register(logini, fulln, email, pass, country, city, phone, function(){
  	//console.log("change");
  	window.location.replace('calendar.html');
  });}
});

$("#form-create-event").submit(function(event) {
  event.preventDefault();
  
   var event = new Object();

	event.title = $("#ceTitle").val();
	event.location  = $("#ceLocation").val(); 
	event.notes  = $("textarea#ceDescription").val();
	event.start  = $("#datetime").val();
	event.end  = $("#datetime2").val();
	event.colour  = $("#color-picker1").val();
	event.tag  = $("#ceTag").val();
	if((event.title.length == 0) || (event.location.length == 0) || (event.start.length == 0) || (event.end.length == 0) || (event.colour.length == 0) || (event.tag.length == 0)){
	alert("Rellena todos los campos para poder crear el evento."); 
	} else {
	createEvent(event, function(event){ 
		console.log("Primer paso: done.");
		$("#eventid").val(event.id);
		console.log("He puesto el hidden:");
		console.log($("#eventid").val());
   });
   }
});

$("#form-addPar").submit(function(event) {
 	event.preventDefault();
 	//$('#trParticipants').append($("<td></td>");
	eventid = $("#eventid").val();
	getEvent(eventid, function(){
  	console.log("el complete del get event");

  });
});


$("#prof").click(function(event) {
 	event.preventDefault();
  	console.log("Has clicado a hashtag profile");
  	console.log($('#prof').attr('rel'));
  	window.location.replace('perfil.html');
  	var uri = "http://localhost:8080/walka/users/"+$("#prof").attr("rel");
  	sessionStorage["tes"] = uri;
  	console.log("console log del session sotrage");
  	console.log(sessionStorage["tes"]);

});
/*
getProfile(uri,function(user){ 
   	 console.log("complete done...");
	$("#suname").text('  '+user.fullname);
   });*/




function login(loginid, password, complete){
	loadAPI(function(){
		var api = JSON.parse(sessionStorage.api);
		var uri = api.login.uri;
		$.post(uri,
			{
				login: loginid,
				password: password
			}).done(function(authToken){
				authToken.links = linksToMap(authToken.links);
				sessionStorage["auth-token"] = JSON.stringify(authToken);
				complete();
			}).fail(function(jqXHR, textStatus, errorThrown){
				var error = jqXHR.responseJSON;
				alert(error.reason);
			}
		);
	});
}

function register(logini, fullname, email, password, country, city, phone, complete){
//monti pasame country y bday
	loadAPI(function(){
		var api = JSON.parse(sessionStorage.api);
		var uri = api["create-user"].uri;
		//var uri = "http://localhost:8080/walka/users";
		console.log(uri);
		$.post(uri,
			{
				loginid: logini,
				password: password,
				email: email,
				fullname: fullname,
				country: country,
				city: city,
				phonenumber: phone
			}).done(function(authToken){
				authToken.links = linksToMap(authToken.links);
				sessionStorage["auth-token"] = JSON.stringify(authToken);
				complete();
			}).fail(function(jqXHR, textStatus, errorThrown){
				var error = jqXHR.responseJSON;
				alert(error.reason);
			}
		);
	});
}

function createEvent(event, complete){
		var uri = "http://localhost:8080/walka/events";
		var authToken = JSON.parse(sessionStorage["auth-token"]);
		//console.log(authToken.token);
		//var data = JSON.stringify(event);
		$.ajax(
			{
				type: 'POST',
				url: uri,
				headers: {'X-Auth-Token':authToken.token},
				crossDomain : true,
				data: event,
				//success: function(data){getEvent(data.id,function(){console.log("Sale mi nombre?");})}
			}).done(function(event){
			/*event.links = linksToMap(event.links);
			window.location.replace('tables.html');*/
			complete(event);
		})
		.fail(function(jqXHR, textStatus, errorThrown){
				var error = jqXHR.responseJSON;
				alert(error.reason);
			}
	);
	}

function logout(complete){
	var authToken = JSON.parse(sessionStorage["auth-token"]);
	var uri = authToken["links"]["logout"].uri;
	console.log(authToken.token);
	$.ajax({
    	type: 'DELETE',
   		url: uri,
    	headers: {
        	"X-Auth-Token":authToken.token
    	}
    }).done(function(data) { 
    	sessionStorage.removeItem("api");
    	sessionStorage.removeItem("auth-token");
    	complete();
  	}).fail(function(){});
}


function getCurrentUserProfile(complete){
	var authToken = JSON.parse(sessionStorage["auth-token"]);
	var uri = authToken["links"]["user-profile"].uri;
	$.get(uri)
		.done(function(user){
			user.links = linksToMap(user.links);
			complete(user);
		})
		.fail(function(){});
}

function getProfile(uri, complete){
	$.get(uri)
		.done(function(user){
			user.links = linksToMap(user.links);
			console.log("el done funciona correctamente");
			complete(user);
		})
		.fail(function(){});
}

function loadEventos(uri, complete){
	// var authToken = JSON.parse(sessionStorage["auth-token"]);
	// var uri = authToken["links"]["current-stings"].uri;
	$.get(uri)
		.done(function(eventos){
			eventos.links = linksToMap(eventos.links);
			complete(eventos);
		})
		.fail(function(){});
}

function getEvent(eventid, complete){
	var uri = "http://localhost:8080/walka/events/"+eventid;
	console.log(uri);
	var authToken = JSON.parse(sessionStorage["auth-token"]);
	$.ajax({
    	type: 'GET',
   		url: uri,
    	headers: {
        	"X-Auth-Token":authToken.token
    	}
    }).done(function(event){
      		console.log("me voy a tables, tirará el get?");
    		console.log("pinto el objeto evento desde getEvent");
    		console.log(event);
			event.links = linksToMap(event.links);
		 	     
			$.each(event.participants.users,function(i,user){
				//$('#trParticipants tbody').append($("<td></td>")
				$('#participantes tr:last').after('<tr><td>'+user.fullname+'</td><td>'+user.loginid+'</td><td class="center">'+user.email+'</td><td class="center"><a class="btn btn-danger" href="#"><i class="glyphicon glyphicon-trash icon-white"></i> Eliminar</a></td></tr>');
				//$("#trParticipants tbody").html('<tr><td> NO me pinta nada esta cosa'+user.fullname+'</td><td class="center">'+user.email+'</td><td class="center"><a class="btn btn-danger" href="#"><i class="glyphicon glyphicon-minus-sign"></i>Eliminar</a></td></tr>');
				//$("#spanPart").append('uno más');
				console.log(user.fullname);
				console.log(user.email);
			});

			console.log("estoy en la funcion getevent:");
			console.log(event.title);
			complete();
			
		}).fail(function(jqXHR, textStatus, errorThrown){
				var error = jqXHR.responseJSON;
				console.log("AQUI EL ERROR");
				alert(error.reason);
			});
}

function addUserToEvent(loginid, complete){
		var authToken = JSON.parse(sessionStorage["auth-token"]);
		var api = JSON.parse(sessionStorage.api);
		var uri = api.login.uri;
		$.post(uri,
			{
				login: loginid,
				password: password
			}).done(function(authToken){
				authToken.links = linksToMap(authToken.links);
				sessionStorage["auth-token"] = JSON.stringify(authToken);
				complete();
			}).fail(function(jqXHR, textStatus, errorThrown){
				var error = jqXHR.responseJSON;
				alert(error.reason);
			}
		);
}

function deleteEvent(idev, complete){
	var uri = "http://localhost:8080/walka/events/"+idev;
	var authToken = JSON.parse(sessionStorage["auth-token"]);
	$.ajax({
    	type: 'DELETE',
    	headers: {
        	"X-Auth-Token":authToken.token
    	},
   		url: uri
    }).done(function(data) { 
  		window.location.replace('calendar.html');
  		console.log("se ha borrado correctamente");
    	complete();
  	}).fail(function(){});
}

function updateUser(uri, data, complete){
		var data = JSON.stringify(data);
		var authToken = JSON.parse(sessionStorage["auth-token"]);
		console.log(uri);
		$.ajax(
			{
				url: uri,
				type : 'PUT',
				headers: {
        		"X-Auth-Token":authToken.token
    			},
				crossDomain : true,
				dataType : 'json',
				contentType : 'application/vnd.dsa.walka.user+json',
				data: data
			}).done(function(authToken){
				complete();
			}).fail(function(jqXHR, textStatus, errorThrown){
				var error = jqXHR.responseJSON;
				alert(error.reason);
			}
		);

}
