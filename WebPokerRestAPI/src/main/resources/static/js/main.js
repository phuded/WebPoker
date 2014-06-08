var gameId;
var roundId;

$(document).ready(function() {
    //Connect
    connect();
});

function createGame(){

    //Clear
    $("textarea.reset").text("");
    $("input.reset").val("");

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
          url: "/games/"+gameId+"/rounds",
          contentType: "application/json; charset=utf-8",
          dataType: "json",
          success: function(data){
              getGame()
          },
          failure: function(errMsg) {
              alert(errMsg);
          }
      });
}


function getGame(){

    $.ajax({
        type: "GET",
        url: "/games/"+ gameId,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data){

            //Get current round
            $.each(data.rounds, function(i,round){

              if(round.isCurrent || i == (data.rounds.length - 1)){
                  updateDetails(round);
              }
            });



            //Player cards
            var cards = ""

            $.each(data.players, function(i, player) {
                cards += player.name + "\n"
                cards += "-----------------\n"
                $.each(player.initialCards, function(i, card) {
                    cards += card.cardValue + " - " + card.suit  + "\n";
                });
                cards += "-----------------\n\n"
            });

            $("#playerCards").text(cards);

        },
        failure: function(errMsg) {
            alert(errMsg);
        }
    });

}

function updateRound(betType){

    var playerName = $("#currentPlayer").val()

    var roundUpdate

    if(betType == "bet"){
        roundUpdate = {
                        player:playerName,
                        bet:$("#betAmount").val()
                      }
    }
    else{
        roundUpdate = {
                        player:playerName,
                        bettingAction:betType
                      }
    }

      $.ajax({
          type: "PUT",
          url: "/games/"+gameId+"/rounds/"+roundId,
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

     roundId = data.roundNumber;

     //Remove bet amount
     $("#betAmount").val("")

     var cards = ""

     if(data.roundCards[0]){

       $.each(data.roundCards, function(i, item) {
           cards += item.cardValue + " - " + item.suit  + "\n";
       });

     }

     //Set cards and current player
     $("#cards").text(cards)

     $("#currentPlayer").val(data.currentPlayer)

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

function connectToGame(){
    gameId = $("#gameId").val();

    getGame();
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
            getGame()
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

