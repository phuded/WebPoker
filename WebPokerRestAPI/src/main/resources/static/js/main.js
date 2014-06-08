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

    //Clear
    $("textarea.reset").text("");
    $("input.reset").val("");
    $("#response").html();

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

            createRound();
        },
        failure: function(errMsg) {
            alert(errMsg);
        }
    });
}

function createRound(){
      $.ajax({
          type: "POST",
          url: "/games/"+gameId+"/rounds?playerName="+ playerName,
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
        $.each(data.winningPlayerNames, function(i, item) {
           winners += item + ", "
        });

        winners += "Round Pot: " + data.pot

        $("#winners").val(winners)

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
        stompClient.subscribe('/topic/betNotifications', function(betNotification){
            showBet(JSON.parse(betNotification.body));
            //Refresh
            refreshGame()
        });
    });
}

function disconnect() {
    stompClient.disconnect();
    setConnected(false);
    console.log("Disconnected");
}


function showBet(betNotification) {
    var response = $("#response");
    response.text(betNotification.playerName + " bet: " + (betNotification.bet?betNotification.bet:betNotification.bettingAction))

}

