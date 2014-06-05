var gameId;
var roundId;

$(document).ready(function() {

});

function createGame(){

var game = {name:"Test",
            playerNames:[$("#player1").val(),$("#player2").val()],
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
              roundId = data.roundNumber;

              $("#currentPlayer").val(data.currentPlayer)

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

            var cards = ""
            cards += data.players[0].initialCards[0].suit + " " + data.players[0].initialCards[0].cardValue  + "\n"
            cards += data.players[0].initialCards[1].suit + " " + data.players[0].initialCards[1].cardValue  + "\n\n"

            cards += data.players[1].initialCards[0].suit + " " + data.players[1].initialCards[0].cardValue  + "\n"
            cards += data.players[1].initialCards[1].suit + " " + data.players[1].initialCards[1].cardValue  + "\n"

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

              var cards = ""

              if(data.roundCards[0]){
                 cards += data.roundCards[0].suit + " " + data.roundCards[0].cardValue  + "\n"
                 cards += data.roundCards[1].suit + " " + data.roundCards[1].cardValue  + "\n"
                 cards += data.roundCards[2].suit + " " + data.roundCards[2].cardValue  + "\n"
              }

              $("#cards").text(cards)

              $("#currentPlayer").val(data.currentPlayer)
          },
          failure: function(errMsg) {
              alert(errMsg);
          }
      });
}

