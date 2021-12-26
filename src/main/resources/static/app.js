var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#inference").show();
    }
    else {
        $("#inference").hide();
    }
    $("#predictions").html("");
}

function connect() {
    var socket = new SockJS('/pima-inference-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/prediction/response', function (prediction) {
            showPrediction(
                 JSON.parse(prediction.body).pregnancies,
                 JSON.parse(prediction.body).glucose,
                 JSON.parse(prediction.body).bloodPressure,
                 JSON.parse(prediction.body).skinThickness,
                 JSON.parse(prediction.body).insulin,
                 JSON.parse(prediction.body).bmi,
                 JSON.parse(prediction.body).diabetesPedigreeFunction,
                 JSON.parse(prediction.body).age,
                 JSON.parse(prediction.body).outcome
             );
        });
        //enable predictor_modal, disconnect and upload buttons
        $("#disconnect").prop('disabled', false);
        $("#upload").prop('disabled', false);
        $("#predictor_modal").prop('disabled', false);
        //disable connect button
        $("#connect").prop('disabled', true);
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");

    //disable predictor_modal, disconnect and upload buttons
    $("#disconnect").prop('disabled', true);
    $("#upload").prop('disabled', true);
    $("#predictor_modal").prop('disabled', true);
    //enable connect button
    $("#connect").prop('disabled', false);
}

function sendPrediction() {
    stompClient.send("/app/prediction/socket", {}, JSON.stringify({
    'pregnancies': $("#pregnancies").val(),
    'glucose': $("#glucose").val(),
    'bloodPressure': $("#bloodPressure").val(),
    'skinThickness': $("#skinThickness").val(),
    'insulin': $("#insulin").val(),
    'bmi': $("#bmi").val(),
    'diabetesPedigreeFunction': $("#diabetesPedigreeFunction").val(),
    'age': $("#age").val()
    }));
}

function showPrediction(pregnancies,glucose,bloodPressure,skinThickness,insulin,bmi,diabetesPedigreeFunction,age,outcome) {
    if(outcome < 0.5)
    {
        $("#predictions").append("<tr class='table-success'><td>" + pregnancies+ "</td><td>" + glucose+ "</td><td>" + bloodPressure+ "</td><td>" + skinThickness+ "</td><td>" + insulin+ "</td><td>" + bmi+ "</td><td>" + diabetesPedigreeFunction+ "</td><td>" + age+ "</td><td>" +outcome+ "</td></tr>");
    }
    else
    {
        $("#predictions").append("<tr class='table-danger'><td>" + pregnancies+ "</td><td>" + glucose+ "</td><td>" + bloodPressure+ "</td><td>" + skinThickness+ "</td><td>" + insulin+ "</td><td>" + bmi+ "</td><td>" + diabetesPedigreeFunction+ "</td><td>" + age+ "</td><td>" +outcome+ "</td></tr>");
    }
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#upload" ).click(function(){
        //alert("Upload button clicked!");
        var fd = new FormData();
        var files = $('#file')[0].files[0];
        fd.append('file', files);
        $.ajax({
            url: '/api/v1/prediction/socket/batch',
            type: 'post',
            data: fd,
            contentType: false,
            processData: false,
            success: function(response){
                if(response.success === true){
                    //TODO
                   //alert('file uploaded');
                }
                else{
                    //TODO
                    //alert('file not uploaded');
                }
            },
        });

    });
});
