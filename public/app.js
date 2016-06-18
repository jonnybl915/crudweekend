$(document).ready(function(){
  logInPage.events();
})

var logInPage = {

  events: function() {
  $('.signIn').on("click", function(event){
    event.preventDefault();
    $.ajax({
      url:"/login",
      method: "POST",
      data: {
          user:$("#Username").val(),
          password:$('#Password').val(),
        },
      success: function(data) {
      var success =  console.log("This worked", data)
      },
      error: function(err) {
        console.error("OH CRAP", err);
      }
    })
  });
},
}
var mainPage = {
  
}
