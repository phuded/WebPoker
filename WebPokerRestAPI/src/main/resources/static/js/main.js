var gameId;

var headerName;
var token;

$(document).ready(function() {
    //Connect to Web Socket
    connect();

    //Set header token
    headerName = $("meta[name='_csrf_header']").attr("content");
    token = $("meta[name='_csrf']").attr("content");
});

//Create a new game
function createGame(){
    var game = {name:$("#gameName").val(), startingPlayerFunds:$("#amount").val()};

    $.ajax({
        type: "POST",
        url: "/games",
        beforeSend: function(xhr) {
            xhr.setRequestHeader(headerName, token);
        },
        data: JSON.stringify(game),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data){
            //Set Game ID
            $("#gameId").val(data.id);

            alert("Game created.")
        },
        error: function(error) {
            alert("Error creating game: " + $.parseJSON(error.responseText).message);
        }
    });
}

//Create a new round
function createRound(){

      $.ajax({
          type: "POST",
          url: "/games/"+gameId+"/rounds",
          beforeSend: function(xhr) {
              xhr.setRequestHeader(headerName, token);
          },
          contentType: "application/json; charset=utf-8",
          dataType: "json",
          success: function(data){
            //Do nothing
          },
          error: function(error) {
              alert("Problem creating round: " + $.parseJSON(error.responseText).message);
          }
      });
}

//Join a current game
function joinGame(){
    //Set Game ID and Player name
    gameId = $("#gameId").val();

    //Clear form
    resetForm();

    $.ajax({
        type: "POST",
        url: "/games/"+ gameId+"/players",
        beforeSend: function(xhr) {
            xhr.setRequestHeader(headerName, token);
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data){
            getRoundDetails()
        },
        error: function(error) {
            alert("Error joining game: " + $.parseJSON(error.responseText).message);
        }
    });

}

//Get the details of the current round
function getRoundDetails(){

    $.ajax({
        type: "GET",
        url: "/games/"+ gameId+"/rounds/current",
        beforeSend: function(xhr) {
            xhr.setRequestHeader(headerName, token);
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data){
            //Update
            updateDetails(data)

        },
        error: function(errMsg) {
            //Do nothing
        }
    });

}

//Perform and action in a round
function updateRound(betType){

    var roundUpdate

    if(betType == "bet"){
        roundUpdate = {
                        bet:$("#betAmount").val()
                      }
    }
    else{
        roundUpdate = {
                        bettingAction:betType
                      }
    }

      $.ajax({
          type: "PUT",
          url: "/games/"+gameId+"/rounds/current",
          beforeSend: function(xhr) {
              xhr.setRequestHeader(headerName, token);
          },
          data: JSON.stringify(roundUpdate),
          contentType: "application/json; charset=utf-8",
          dataType: "json",
          success: function(data){
             //Not doing anything...

          },
          error: function(error) {
              alert("Could not update round:" + $.parseJSON(error.responseText).message);
          }
      });
}

//Update the page
function updateDetails(data){
     //Remove bet amount
     $("#betAmount").val("")

     //Cards
     var cards = ""

    //Always show Current Player cards
    if($("#playerCards").html() == ""){

        $.each(data.player.initialCards, function(i, card) {
            cards += playingCards.card(cardValues[card.cardValue], suits[card.suit]).getHTML();
        });

        $("#playerCards").html(cards);
    }

     //Reset cards
     cards = ""

     if(data.round.roundCards[0]){

       $.each(data.round.roundCards, function(i, card) {
           cards += playingCards.card(cardValues[card.cardValue], suits[card.suit]).getHTML();
       });

       //Set cards and current player
       $("#cards").html(cards)

     }

     //Current player
     $("#currentPlayer").val(data.round.currentPlayerName)

     //Pot
     $("#pot").val(data.round.pot)

     //Current details of bet and current
     $.each(data.round.bettingRounds, function(i, item) {
           if(item.isCurrent){
               $("#bettingRound").val(item.bettingRoundNumber)
               $("#currentBet").val(item.amountBetPerPlayer)
           }
     });

     //If round finished
     if(data.round.hasFinished){

        var handType = "";

        if(data.round.winningHand){
            handType = " - " + data.round.winningHand.handType;
        }

        var winners = ""
        $.each(data.round.winningPlayerNames, function(i, winner) {

           winners += winner + handType + "\n"
        });

        winners += "Round Pot: " + data.round.pot

        //$("#winners").text(winners)

        alert("Finished! Winner is: " + winners)
     }
}


//Web Socket code
var stompClient = null;

/*function setConnected(connected) {
    document.getElementById('connect').disabled = connected;
    document.getElementById('disconnect').disabled = !connected;
    document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
    document.getElementById('response').innerHTML = '';
}*/

function connect() {
    var socket = new SockJS('/poker',null,{rtt:5000});
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
       // setConnected(true);
        console.log('Connected: ' + frame);

        //When subscribing
        stompClient.subscribe('/topic/notifications', function(notification){
            processNotification(JSON.parse(notification.body));
        });
    });
}

function disconnect() {
    stompClient.disconnect();
    setConnected(false);
    console.log("Disconnected");
}

//On receiving a web socket notification
function processNotification(notification) {

    if(notification.gameId == gameId){

        var notificationInput = $("#notification");

        if(notification.type == "bet"){
            notificationInput.val(notification.playerName + " bet: " + (notification.bet?notification.bet:notification.bettingAction))
        }

        if(notification.type == "round"){
            //New round
            resetForm();
            notificationInput.val("Started next round...");
        }

        //Refresh game
        getRoundDetails()
    }
}

//Reset form method
function resetForm(){
   //Clear form
   $("textarea.reset").text("");
   $("input.reset").val("");
   $("div.reset").html("");
}

 var cardValues = {
            "TWO": "2",
            "THREE": "3",
            "FOUR": "4",
            "FIVE": "5",
            "SIX": "6",
            "SEVEN": "7",
            "EIGHT": "8",
            "NINE": "9",
            "TEN": "10",
            "JACK": "J",
            "QUEEN": "Q",
            "KING": "K",
            "ACE": "A"
        };


var suits = {
    "SPADES": "S",
    "CLUBS": "C",
    "DIAMONDS": "D",
    "HEARTS": "H"
};