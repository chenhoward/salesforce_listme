function setSection(to, from)
{
  if (!(to.hasClass("active")))
  {
    if (to.is("#one")) {
      $("#one-tab").addClass("active-tab");
      $("#one-tab .on").css("display","initial");
      $("#one-tab .off").css("display","none");
    }
    if (from.is("#one")) {
      $("#one-tab").removeClass("active-tab");
      $("#one-tab .off").css("display","initial");
      $("#one-tab .on").css("display","none");
    }
    if (to.is("#two")) {
      $("#two-tab").addClass("active-tab");
      $("#two-tab .on").css("display","initial");
      $("#two-tab .off").css("display","none");
    }
    if (from.is("#two")) {
      $("#two-tab").removeClass("active-tab");
      $("#two-tab .off").css("display","initial");
      $("#two-tab .on").css("display","none");
    }
    if (to.is("#three")) {
      $("#three-tab").addClass("active-tab");
      $("#three-tab .on").css("display","initial");
      $("#three-tab .off").css("display","none");
    }
    if (from.is("#three")) {
      $("#three-tab").removeClass("active-tab");
      $("#three-tab .off").css("display","initial");
      $("#three-tab .on").css("display","none");
    }
    from.animate({"left":"-100%"},100,'linear')
    to.animate({"left":"0%"},100,'linear',function() {
      from.css("left","100%");
      from.removeClass("active");
      to.addClass("active");
    });
  }
}
$(document).ready(function(){
   FastClick.attach(document.body);
  $('a').on('click', function(event) {
    var sectionId = $(this).attr("data-section"),
    $toSlide= $("#container section#"+sectionId),
    $fromSlide= $('.active');
    setSection($toSlide,$fromSlide);
  });
  $('.img-right').on('click', function(event) {
    $(this).css("background","#2f92d0");
    $(this).parent().animate({right:"-100%"}, 200).animate({height:"0px",padding:"0px",border:"0px"},200);
  });
  $('#one-overall .link').on('click', function(event) {
    fromSlide= $('.active');
    fromSlide.removeClass("active");
    $('#one-overall').animate({left:"-100%",right:"100%"},200);
  });
  $('.arrow-back-button').on('click', function(event) {
    fromSlide= $('.active');
    fromSlide.removeClass("active");
    $('#one-overall').animate({left:"0",right:"0"},200);
  });
  $('#show-add').on('click', function(event) {
    $('#add').animate({"top":"0"}, 200);
  });
  $('#hide-add').on('click', function(event) {
    $('#add').animate({"top":"100%"}, 200);
  });
  $('#show-settings').on('click', function(event) {
    $('#settings').animate({"top":"0"}, 200);
  });
  $('#hide-settings').on('click', function(event) {
    $('#settings').animate({"top":"100%"}, 200);
  });
  $('#expand-list').on('click', function(event) {
    $('#list').animate({"opacity":"1"}, 1);
    $('#list').animate({"top":"0"}, { queue: false, duration: 200 });
    $('#list').animate({"left":"0"}, { queue: false, duration: 200 });
    $('#list').animate({"bottom":"0"}, { queue: false, duration: 200 });
    $('#list').animate({"right":"0"}, { queue: false, duration: 200 });
  });
  $('#hide-list').on('click', function(event) {
    $('#list').animate({"opacity":"0"}, 200);
    $('#list').animate({"top":"50%"}, 1);
    $('#list').animate({"left":"50%"}, 1);
    $('#list').animate({"bottom":"50%"}, 1);
    $('#list').animate({"right":"50%"}, 1);
  });
});