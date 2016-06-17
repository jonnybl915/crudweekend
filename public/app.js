$(document).ready(function(){
  logInPage.events();
})

var logInPage = {
  user: user.input,
  password:password.input,
  init: function() {
    logInPage.styling();
    logInPage.events();
  },
  events: function() {
  $('.signIn').on("click", function(event){
    event.preventDefault();

  });
},
}
var mainPage = {

}
