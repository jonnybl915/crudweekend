$(document).ready(function(){
  logInPage.events();
})

var logInPage = {

  events: function() {
  $(".mainPage").toggle();
  $('.signIn').on("click", function(event){
    event.preventDefault();
    $.ajax({
      url:"/login",
      method: "POST",
      contentType: "application/json; charset=utf-8",
      data: JSON.stringify({
          username:$("#Username").val(),
          password:$('#Password').val(),
        }),
      success: function(data) {
      var success =  console.log("This worked", data)
      $('.mainPage').toggle();
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
