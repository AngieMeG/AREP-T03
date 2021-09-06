function testServer(){
    const resource = $(".inputField").val();
    window.location.replace(resource);
}

$("#submit").on("click", testServer);