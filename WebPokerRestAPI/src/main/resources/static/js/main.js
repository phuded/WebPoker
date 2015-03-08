var gameId;

var headerName;
var token;

var loggedInPlayer;
var currentPlayer;

var callButton;
var foldButton;
var newRoundButton;

var bettingControls;

//Constants
var CHECK = "Check";

$(document).ready(function() {

    //$("#cards").html(playingCards.card(cardValues["KING"], suits["SPADES"]).getHTML() + playingCards.card(cardValues["KING"], suits["HEARTS"]).getHTML() + playingCards.card(cardValues["KING"], suits["DIAMONDS"]).getHTML()+ playingCards.card(cardValues["KING"], suits["CLUBS"]).getHTML()+ playingCards.card(cardValues["KING"], suits["SPADES"]).getHTML());
    //$("#playerCards").html(playingCards.card(cardValues["KING"], suits["CLUBS"]).getHTML()+ playingCards.card(cardValues["KING"], suits["SPADES"]).getHTML());

    //Connect to Web Socket
    connect();

    //Set header token
    headerName = $("meta[name='_csrf_header']").attr("content");
    token = $("meta[name='_csrf']").attr("content");

    callButton = $("#callButton");
    foldButton = $("#foldButton");
    newRoundButton = $("#newRoundButton");

    bettingControls = $("#bettingControls");
});

//List all games
function listGames(){
    $.ajax({
        type: "GET",
        url: "/games?showAll=true",
        beforeSend: function(xhr) {
            xhr.setRequestHeader(headerName, token);
        },
        //data: JSON.stringify(game),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data){

           var gameList = $("#gameList");

           gameList.html("");

           $.each(data, function(i, item) {
               gameList.append($("<option>", { value : item.id }).text(item.name));
           });

        },
        error: function(error) {
            $("#newGameNotification").val("Error listing games: " + $.parseJSON(error.responseText).message);
        }
    });
}

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
            $("#newGameNotification").val("New game created: " + game.name)

            //List the games
            listGames();
        },
        error: function(error) {
            $("#newGameNotification").val("Error creating game: " + $.parseJSON(error.responseText).message);
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
              $("#notification").val("Problem creating round: " + $.parseJSON(error.responseText).message);
          }
      });
}

//Join a current game
function joinGame(){

    var gameList = $("#gameList");

    //Get the Game ID
    gameId = gameList.val();

    //Clear form
    resetForm();

    $.ajax({
        type: "POST",
        url: "/games/"+ gameId+ "/players",
        beforeSend: function(xhr) {
            xhr.setRequestHeader(headerName, token);
        },
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function(data){
            $("#notification").val("Joined game: " + gameList.find(":selected").text());

            //Collapse panel
            $('.panel-collapse').collapse();

            //Get the round state
            getRoundDetails();
        },
        error: function(error) {
            $("#newGameNotification").val("Error joining game: " + $.parseJSON(error.responseText).message);
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
            //Set logged in player
            loggedInPlayer = data.player.name;

            //Update
            updateDetails(data);
        },
        error: function(errMsg) {
            //Do nothing - no round

            //Finished round
            bettingControls.hide(200);
            newRoundButton.show(200);
        }
    });

}

//Perform and action in a round
function updateRound(betType){

    var roundUpdate;

    if(betType == "bet"){
        roundUpdate = {
                        bet: $("#betAmount").val()
                      }
    }
    else if (betType == "call"){
        roundUpdate = {
                        bettingAction: (callButton.text() == CHECK)?"check":"call"
                      }
    }
    else{
        roundUpdate = {
                        bettingAction: betType
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
              $("#notification").val("Could not update round: " + $.parseJSON(error.responseText).message);
          }
      });
}

//Update the page
function updateDetails(data){

     //Round number
     $("#round").val(data.round.roundNumber);

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
     currentPlayer = data.round.currentPlayerName;
     $("#currentPlayer").val(currentPlayer);

     //Pot
     $("#pot").val(data.round.pot)


     //Current bet for round
     var currentBet;

     //Current details of bet and current
     $.each(data.round.bettingRounds, function(i, item) {
           if(item.isCurrent){
               $("#bettingRound").val(item.bettingRoundNumber)

               currentBet = item.amountBetPerPlayer;
               $("#currentBet").val(currentBet)
           }
     });

     //Amount player has bet so far
     var amountBet = data.player.amountBet;
     $("#amountBet").val(amountBet);

     //Determine buttons
     //TODO: Split out logic
     if(!data.round.hasFinished && loggedInPlayer == currentPlayer){
         //Current player
         newRoundButton.hide(200);
         bettingControls.show(200);

         //Amount needed to match bet
         var toCall = currentBet - amountBet;

         if(toCall > 0){
            //Need to call, raise or fold
            callButton.text("Call (" + toCall + ")");
            foldButton.removeAttr("disabled");
         }
         else{
            //Don't need to match -> So need to check or raise/bet
            callButton.text(CHECK)
            foldButton.attr("disabled","disabled");
         }
     }
     else if (data.round.hasFinished){
        //Finished round
        bettingControls.hide(200);
        newRoundButton.show(200);
     }
     else{
        //Not current player
        newRoundButton.hide(200);
        bettingControls.hide(200);
     }


     //Funds of player...
     $("#funds").val(data.player.funds);

     //If round finished
     //TODO: Tidy logic
     if(data.round.hasFinished){

        //Hand type
        var handType = "";

        if(data.round.winningHand){
            handType = " (" + data.round.winningHand.handType + ")";
        }

        //Winners
        var winners = "";

        $.each(data.round.winningPlayerNames, function(i, winner) {
           winners += winner + handType + ", ";
        });

        winners += "pot: " + data.round.pot;

        $("#notification").val("Round Finished. Winner is: " + winners);
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

//Unused
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

            //Notify
            notificationInput.val("Started new round.");
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

//Card stuff
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