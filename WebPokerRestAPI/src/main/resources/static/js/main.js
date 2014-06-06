var gameId;
var roundId;

$(document).ready(function() {

});

function createGame(){

    //Clear
    $("textarea.reset").text("");
    $("input.reset").val("");

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

              $.each(data.bettingRounds, function(i, item) {
                  if(item.isCurrent){
                      $("#bettingRound").val(item.bettingRoundNumber)
                  }
              });

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
                cards += item.name + "\n"
                cards += "-----------------\n"
                $.each(item.initialCards, function(i, card) {
                    cards += card.suit + " " + card.cardValue  + "\n";
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

              //Remove bet amount
              $("#betAmount").val("")

              var cards = ""

              if(data.roundCards[0]){

                $.each(data.roundCards, function(i, item) {
                    cards += item.suit + " " + item.cardValue  + "\n";
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
          },
          failure: function(errMsg) {
              alert(errMsg);
          }
      });
}

