$("#form-signin").submit(function(event) {
  event.preventDefault();
	logini = $("#inputLoginid").val();
	pass  = $("#inputPassword").val();
	login(logini, pass, function(){
  	console.log("change");
  	window.location.replace('calendar.html');
  });
});
