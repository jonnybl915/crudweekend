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
      var success =  console.log("This worked", data);
      if(success){
        $.ajax({
          url:"",
          method:"GET",
      success: function(data){
        console.log("SUCCESS!!!",data)
      }
      error: function(err) {
        console.log("data not recieved fuckboi!!!!!")
      }
        })
      }
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
