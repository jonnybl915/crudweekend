var clean = $('#isClean option')
$(document).ready(function(){
  skipToMyLou.events();
  $('.mainPage').addClass("hidden");
})

var skipToMyLou = {
  events: function() {
    var RatingData=0;
 // if (clean.val() === "Clean?") {
 //   clean = true;
 // }
 // else{clean = false}
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
    });
  });
  /*SUBMIT TOILET INFORMATION */
  $('.btn-primary').on("click",function(event){
  event.preventDefault();
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
    console.log(data);
    data = JSON.parse(data)
    data.forEach(function(item){
      var mark = new google.maps.Marker({
        position: {latitude:item.Latitude, longitude:item.Longitude },
        map:$('.map'),
        title:item.description
      });
    })
  },
  error:function(err) {
    console.err("Oh SHit!!!",data)
  }
})}}
