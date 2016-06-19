/* Initialize map at the beginning of the page */
function initMap() {
  var mapDiv = document.getElementById('map');
  var map = new google.maps.Map(mapDiv, {
      center: {lat: 44.540, lng: -78.546},
      zoom: 8
  });
}

$(document).ready(function(){
  skipToMyLou.events();
})

var skipToMyLou = {

  events: function() {

  // $(".mainPage").toggle();
  /* USER NAME AND PASSWORD */
  $('.signIn').on("click", function(event){
    if(($("#Username").val()||$('#Password').val())===null){
      return false;
    }
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
      console.log("This worked", data);
      $('.logInPage').fadeToggle(3000);
      $(".mainPage").fadeToggle(3000);
  },
      error: function(err) {
      console.error("OH CRAP", err);
      alert("HOLD IT!");
      }
    })
  });

},
Read: function() {
  $.ajax({
    method:"GET",
    url:"login",
  success:function(data) {
    console.log(data);
    data = JSON.parse(data)
  },
  error:function(err) {
    console.err("Oh SHit!!!",data)
  }
})}}
