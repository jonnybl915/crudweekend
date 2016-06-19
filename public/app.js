$(document).ready(function(){
  logInPage.events();
})

var logInPage = {

  events: function() {
  // $(".mainPage").toggle();
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
      success: function(data) { if(1){
      console.log("This worked", data);
      $('.logInPage').toggle();
      $(".mainPage").toggle();
    }
      },
      error: function(err) { if(-1){
        console.error("OH CRAP", err);
      alert("HOLD IT!");
    }
      }
    })
  });
},
Read: function() {
  $.ajax({
    method:"GET",
    url:"/login",
  success:function(data) {
    console.log(data);
    data = JSON.parse(data)
  },
  error:function(err) {
    console.err("Oh SHit!!!",data)
  }
  })
}
}
var mainPage = {

}
