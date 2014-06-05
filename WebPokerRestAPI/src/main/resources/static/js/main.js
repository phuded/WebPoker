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

            $.each(data.players, function(i, item) {
                $.each(item.initialCards, function(i, card) {
                    cards += card.suit + " " + card.cardValue  + "\n";
                });
                cards += "\n"
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

              var cards = ""

              if(data.roundCards[0]){

                $.each(data.roundCards, function(i, item) {
                    cards += item.suit + " " + item.cardValue  + "\n";
                });

              }

              $("#cards").text(cards)

              $("#currentPlayer").val(data.currentPlayer)

              if(data.hasFinished){
                 var winners = ""
                 $.each(data.winningPlayerNames, function(i, item) {
                   winners += item + " "
                 });

                 $("#winners").text(winners)
              }
          },
          failure: function(errMsg) {
              alert(errMsg);
          }
      });
}

