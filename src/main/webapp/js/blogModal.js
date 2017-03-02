//// Get the modal
var modal; // = document.getElementById('myModal');

$(document)
    .ready(
        function () {
            modal = document.getElementById('myModal');
            //btn = document.getElementById("myBtn");
            var span = document.getElementsByClassName("close")[0];

            $("#showSignUp").click(
                function () {
                    getUserIds();
                    //console.log("Open form");
                    modal.style.display = "block";
                });
            $("#close-signup-form").click(
                function () {
                    modal.style.display = "none";
                });

        });


window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
