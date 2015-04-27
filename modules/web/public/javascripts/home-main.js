$(function() {
    $(".main").onepage_scroll({
        updateURL: false,
        keyboard: true,
        easing: "ease",
        sectionContainer: "section",
        responsiveFallback: 600,
        loop: true
    });

    $(".sidemenu").fly_sidemenu();

    $('#signupButton').click(function(){
        $(".main").moveTo(3);
    });
});
