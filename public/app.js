
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
  skipToMyLou.ReadUpdate()
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
      $(".mainPage").fadeToggle(1000)
  },
      error: function(err) {
      console.error("OH CRAP", err);
      alert("HOLD IT!");
      }
    })
    skipToMyLou.Read();
  });
  //LOGOUT BUTTON
  $('.logout').on('click', function(event){
    event.preventDefault();
    console.log('You logged out');
    $('.mainPage').addClass('hidden');
    $('.logInPage').fadeToggle(1000);
    location.reload();
})
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
      skipToMyLou.Read()
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
$(document).on('click', 'li',function(event){
  event.preventDefault();
      var msgId =$(this).children().text();
      console.log(msgId);
      skipToMyLou.deleteChat(msgId);
      $(this).remove();
  })

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
  },
})},
ReadUpdate: function() {
  $.ajax({
    method:"GET",
    url:"/skipToTheLoo",
  success:function(data) {
    data = JSON.parse(data)
    console.log(data)
    data.forEach(function(item){
    $('ul').append(`<li class=${item.restroomId}><p>${item.restroomId}</p>: ${item.description}</li>`)
    })
  },
  error:function(err) {
    console.log(err)
  }
  })
},
deleteChat: function (msgId) {
  var deleteUrl = "/skipToTheLoo"+ "/" + msgId;
  $.ajax({
    url: deleteUrl,
    method:"DELETE",
    success: function(data) {
      console.log("IT IS GONE",data);
      skipToMyLou.Read()
    },
    error: function(err) {
      console.error("you blew it", err);
      }
    })}
}
