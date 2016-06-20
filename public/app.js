
var RatingData=0;
var clean = $('#isClean option')
var map = null;

function initMap() {
  var mapDiv = document.getElementById('map');
  map = new google.maps.Map(mapDiv, {
    center: {lat: 44.540, lng: -78.546},
    zoom: 8
  });
}

$(document).ready(function(){
  skipToMyLou.events();
  $('.mainPage').addClass("hidden");
})

var skipToMyLou = {
  events: function() {
  /* USER NAME AND PASSWORD */
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
      console.log("This worked", data);
      $('.logInPage').fadeToggle(1000);
      $(".mainPage").removeClass("hidden").toggle();
      $(".mainPage").fadeToggle(3000)
  },
      error: function(err) {
      console.error("OH CRAP", err);
      alert("HOLD IT!");
      }
    })
    skipToMyLou.Read();
  });
  /*SUBMIT TOILET INFORMATION */
  $('.btn-primary').on("click",function(event){
  event.preventDefault();
    skipToMyLou.Read();
   if ($('#isClean').val() === "Yep!") {
      clean = true;
    }
    else{clean = false}
   $.ajax({
        url:"/skipToTheLoo",
        method:"POST",
        contentType:"application/json; charset=utf-8",
        data: JSON.stringify({
        description:$("#exampleTextarea").val(),
        latitude:$("#Latitude").val(),
        longitude:$("#Longitude").val(),
        visitDate:$("#When").val(),
        isClean:clean,
        rating:RatingData,
        userId:1
      }),
    success: function(data){
      console.log("DATA SENT",data);
    },
      error:function(err) {
      console.error("OOOPS!!!",err)
      }
})});
/* LOG RATING */
$('.logo').on("click",function(){
  DataFields = $(this).data();
  RatingData =DataFields.id
  $(this).css("border-color","red")
});

},
Read: function() {
  $.ajax({
    method:"GET",
    url:"/skipToTheLoo",
  success:function(data) {
    console.log(data.latitude, data.longitude);
    data = JSON.parse(data)
    data.forEach(function(item){
      console.log('item:', item);
      var mark = new google.maps.Marker({
        position: {lat:item.latitude, lng:item.longitude },
        map: map,
        title:item.description
      });
    })
  },
  error:function(err) {
    console.log("Oh SHit!!!",err)
  }
})}}
