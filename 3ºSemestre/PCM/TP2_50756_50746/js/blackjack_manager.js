//Blackjack oop

let game = null;
let indDealer = 0;
let indPlayer = 0;

/*
function debug(an_object) {
    document.getElementById("debug").innerHTML = JSON.stringify(an_object);
}*/

function buttons_initialization() {
    document.getElementById("card").disabled = false;
    document.getElementById("stand").disabled = false;
    document.getElementById("new_game").disabled = true;
}

function finalize_buttons() {
    document.getElementById("card").disabled = true;
    document.getElementById("stand").disabled = true;
    document.getElementById("new_game").disabled = false;
}


//FUNÇÕES QUE DEVEM SER IMPLEMENTADAS PELOS ALUNOS
function new_game() {
    game = new BlackJack();

    document.getElementById("Winner").innerHTML = null;

    game.player_move();
    game.player_move();
    game.dealer_move();
    game.dealer_move();
    indDealer=0;
    indPlayer=0;

    document.getElementById("dealer").innerHTML="";
    document.getElementById("player").innerHTML="";
    document.getElementById("dealer-Score").innerHTML="";
    document.getElementById("player-Score").innerHTML="";

    buttons_initialization();

    //document.getElementById("dealer").innerHTML = game.get_dealer_cards()[0]+ ", X";
    //document.getElementById("player").innerHTML = game.get_player_cards();
    
    create_image(game.get_dealer_cards()[indDealer], "dealer", true); 
    create_image(game.get_dealer_cards()[indDealer], "dealer", false); 

    create_image(game.get_player_cards()[indPlayer], "player", true); 
    create_image(game.get_player_cards()[indPlayer], "player", true);

    update_player(game.get_game_state());
    update_dealer(game.get_game_state())

    //debug(game);
}

function update_dealer(state) {

    update_dealer_score(false);

    if (state.gameEnded) {
        // Mostrar as cartas do dealer em string
        //document.getElementById("dealer").innerHTML = game.get_dealer_cards().join(", ");

        //"virar" segunda carta
        let str = srcImage(game.get_dealer_cards()[1], true);
        document.getElementsByClassName("hiddenCard")[0].src = str;

        update_dealer_score(true);

        // Verificar se o dealer ganhou ou perdeu
        if (state.dealerWon) {
            document.getElementById("Winner").innerHTML = "Dealer ganhou!";
        } else {
            document.getElementById("Winner").innerHTML = "Player ganhou!";
        }
        
        finalize_buttons();
    }
}

function update_player(state) {
    // Mostrar as cartas do player em string
    //document.getElementById("player").innerHTML = game.get_player_cards().join(", "); 

    update_player_score();
    update_dealer(state);

    if (state.gameEnded) {
        // Verificar se o player ganhou ou perdeu
        if (!state.dealerWon) {
            document.getElementById("Winner").innerHTML = "Player Ganhou!";
        } 
        if(state.playerBusted){
            document.getElementById("Winner").innerHTML = "Player Rebentou!";
        }
        
        finalize_buttons();
    }
}

function dealer_new_card() {
    game.dealer_move();
    create_image(game.get_dealer_cards()[indDealer], "dealer", true);
    update_dealer(game.get_game_state());
    return game.state;
}

function player_new_card() {
    game.player_move();
    create_image(game.get_player_cards()[indPlayer], "player", true);
    update_player(game.get_game_state());
    return game.state;
}

function dealer_finish() {
    game.setDealerTurn(true);
    while(!game.state.gameEnded){
        dealer_new_card();
    }
}

function create_image (card, id, show){
    let imgCarta = document.createElement("img");
    if(show){
        imgCarta.src = srcImage(card, show);
        imgCarta.className = "showCard";
    }else{
        imgCarta.src = srcImage(card, show);
        imgCarta.className = "hiddenCard";
    }
    
    document.getElementById(id).append(imgCarta);

    if(id === "dealer"){
        indDealer++;
    }else if(id === "player"){
        indPlayer++;
    }
}

function srcImage (card, show){
    if(show){
        let separarInfo = card.split("_"); // numero_naipe
        let valorCarta = separarInfo[0]; 
        let naipe = separarInfo[1];
        src = "img/png/"; //img\png\2_of_clubs.png

        if (valorCarta == 1) { //A
            src += "ace";
        }else if (valorCarta == 11){ //K
            src += "king";
        }else if (valorCarta == 12){ //J
            src += "jack";
        }else if (valorCarta == 13){ //Q
            src += "queen";
        } else{
            src += valorCarta;
        }

        if(naipe == "clubs"){
            src += "_of_clubs.png";
        }else if(naipe == "diamonds"){
            src += "_of_diamonds.png";
        }else if(naipe == "hearts"){
            src += "_of_hearts.png";
        }else{
            src += "_of_spades.png";
        }
    } else{
        src = "img/png/card_back.png";
    }
    return src;
}

function update_player_score(){
    const playerScore = game.get_cards_value(game.get_player_cards());
    document.getElementById("player-Score").innerHTML = "Score: " + playerScore;
}

function update_dealer_score(show){
    let dealerCards = game.get_dealer_cards();
    if(!show){
        dealerCards.splice(1,1); //remove a segunda carta (carta virada para baixo)
    }
    dealerScore = game.get_cards_value(dealerCards);
    document.getElementById("dealer-Score").innerHTML = "Score: " + dealerScore;
}