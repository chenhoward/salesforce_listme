function setSection(to, from) {
    $('#collab').animate({"top":"100%"}, 100);
    if (!(to.hasClass("active")))
    {
      from.animate({"left":"-100%"}, 100)
      to.animate({"left":"0%"}, 100, function()
      {
        from.css("left","100%");
        from.removeClass("active");
        to.addClass("active");
      });
      if (to.is("#one-overall")) {
        $("#newbut").animate({"left":"100%"}, 0);
        $("#newbut").animate({"left":"0"}, 100);
        $("#header").animate({"left":"100%"}, 0);
        $("#header").animate({"left":"0"}, 100);
      } else {
        $("#newbut").animate({"left":"-100%"}, 100);
        $("#header").animate({"left":"-100%"}, 100);
      }
      if ($(window).width() < 1000) {
        if (to.is("#one")) {
          $("#pending-tab").addClass("active-tab");
          $("#pending-alt").css("display", "inline");
          $("#pending").css("display", "none");
        }
        if (from.is("#one")) {
          $("#pending-tab").removeClass("active-tab");
          $("#pending").css("display", "inline");
          $("#pending-alt").css("display", "none");
        }
        if (to.is("#two")) {
          $("#doing-tab").addClass("active-tab");
          $("#doing-alt").css("display", "inline");
          $("#doing").css("display", "none");
        }
        if (from.is("#two")) {
          $("#doing-tab").removeClass("active-tab");
          $("#doing").css("display", "inline");
          $("#doing-alt").css("display", "none");
        }
        if (to.is("#three")) {
          $("#complete-tab").addClass("active-tab");
          $("#complete-alt").css("display", "inline");
          $("#complete").css("display", "none");
        }
        if (from.is("#three")) {
          $("#complete-tab").removeClass("active-tab");
          $("#complete").css("display", "inline");
          $("#complete-alt").css("display", "none");
        }
      }
    }
  }
  function switchCollab() {
    $('#switch-collab').animate({"opacity":"1"}, 1);
    $('#switch-collab').animate({"top":"0%"}, { queue: false, duration: 100 });
    $('#switch-collab').animate({"left":"0%"}, { queue: false, duration: 100 });
    $('#switch-collab').animate({"bottom":"0%"}, { queue: false, duration: 100 });
    $('#switch-collab').animate({"right":"0%"}, { queue: false, duration: 100 });
  }
  $(document).ready(function(){
    $('a').on('click', function(event)
    {
      var sectionId = $(this).attr("data-section"),
      $toSlide= $("#container section#"+sectionId),
      $fromSlide= $('.active');
      setSection($toSlide, $fromSlide);
    });
    $('#collab-button').on('click', function(event) {
      $('#collab').animate({"top":"0%"},200);
    });
    $('#hide-collab').on('click', function(event) {
      $('#collab').animate({"top":"100%"},200);
    });
    $('#hide-switch-collab').on('click', function(event) {
      $('#switch-collab').animate({"opacity":"0"}, 100);
      $('#switch-collab').animate({"top":"50%"}, 1);
      $('#switch-collab').animate({"left":"50%"}, 1);
      $('#switch-collab').animate({"bottom":"50%"}, 1);
      $('#switch-collab').animate({"right":"50%"}, 1);
    });
  });
  window.addEventListener('load', function() {
    FastClick.attach(document.body);
  }, false);