const hasBeenClick = "Click here to hide the card";
const hasntBeenClick = "Click here for a surprise";

$("#action").on("click", function(){
    $("#action").toggleClass("hasBeenClick");
    if($("#action").hasClass("hasBeenClick")){
        document.getElementById("action").innerHTML = hasBeenClick;
    } else{
        document.getElementById("action").innerHTML = hasntBeenClick;
    }
    $(".container").toggleClass("invisible");
    $(".text").toggleClass("invisible");
});