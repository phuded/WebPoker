var gameId;
var roundId;
var playerName;

$(document).ready(function() {
    //Connect
    connect();
});

//Entry Point 1
function createGame(){
    //Set name
    playerName = $("#playerName").val();

    var players = $("#players").val();
    var playerList = players.split(",")

    var game = {name:"Test",
            playerNames:playerList,
            startingPlayerFunds:$("#amount").val()};

    $.ajax({
        type: "POST",
        url: "/games",
        data: JSON.stringify(game),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data){
            //Set Game ID
            gameId = data.id;

            alert("Game created.")
            createRound();
        },
        failure: function(errMsg) {
            alert(errMsg);
        }
    });
}

//New round
function createRound(){

      $.ajax({
          type: "POST",
          url: "/games/"+gameId+"/rounds?playerName="+ playerName,
          contentType: "application/json; charset=utf-8",
          dataType: "json",
          success: function(data){
            //Do nothing
          },
          error: function(errMsg) {
              alert("Round already in progress");
          }
      });
}

//Entry point 2
function connectToGame(){
    //Set Game ID and Player name
    gameId = $("#gameId").val();
    playerName = $("#playerName").val();

    $.ajax({
        type: "GET",
        url: "/games/"+ gameId+"/rounds/current?playerName="+ playerName,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data){
            //Set round ID
            roundId = data.roundNumber

            //Update
            updateDetails(data)

        },
        failure: function(errMsg) {
            alert(errMsg);
        }
    });

}


function refreshGame(){

    $.ajax({
        type: "GET",
        url: "/games/"+ gameId+"/rounds/"+ roundId + "?playerName="+ playerName,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data){
            //Update
            updateDetails(data)

        },
        failure: function(errMsg) {
            alert(errMsg);
        }
    });

}



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
          url: "/games/"+gameId+"/rounds/"+roundId + "?playerName="+ playerName,
          data: JSON.stringify(roundUpdate),
          contentType: "application/json; charset=utf-8",
          dataType: "json",
          success: function(data){
             //Not doing anything...

          },
          failure: function(errMsg) {
              alert(errMsg);
          }
      });
}

function updateDetails(data){
    //Always show Current Player cards
    if($("#playerCards").text() == ""){
        var cards = ""


        cards += data.player.name + "\n"
        cards += "-----------------\n"
        $.each(data.player.initialCards, function(i, card) {
            cards += card.cardValue + " - " + card.suit  + "\n";
        });
        cards += "-----------------\n\n"


        $("#playerCards").text(cards);
    }


     //Set round number
     roundId = data.roundNumber;

     //Remove bet amount
     $("#betAmount").val("")

     //Round cards
     var cards = ""

     if(data.roundCards[0]){

       $.each(data.roundCards, function(i, item) {
           cards += item.cardValue + " - " + item.suit  + "\n";
       });

     }

     //Set cards and current player
     $("#cards").text(cards)

     $("#currentPlayer").val(data.currentPlayerName)

     $("#pot").val(data.pot)

     //Current details of bet and current
     $.each(data.bettingRounds, function(i, item) {
           if(item.isCurrent){
               $("#bettingRound").val(item.bettingRoundNumber)
               $("#currentBet").val(item.amountBetPerPlayer)
           }
     });

     //If round finished
     if(data.hasFinished){
        var winners = ""
        $.each(data.winningPlayerNames, function(i, winner) {
           winners += winner + " - " + data.winningHand.handType + "\n"
        });

        winners += "Round Pot: " + data.pot

        $("#winners").text(winners)

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


function processNotification(notification) {

    if(notification.gameId == gameId){

        if(notification.type == "bet"){
            var response = $("#response");
            response.text(notification.playerName + " bet: " + (notification.bet?notification.bet:notification.bettingAction))
        }

        if(notification.type == "round"){
            //New round
            roundId = notification.roundNumber

            //Clear form
            $("textarea.reset").text("");
            $("input.reset").val("");
            $("#response").html("");
        }

        //Refresh
        refreshGame()
    }
}

