//Blackjack object

//constante com o número máximo de pontos para blackJack
const MAX_POINTS = 21;

// Classe BlackJack - construtor
class BlackJack {
    constructor() {

        // array com as cartas do dealer
        this.dealer_cards = [];
        // array com as cartas do player
        this.player_cards = [];
        // variável booleana que indica a vez do dealer jogar até ao fim
        this.dealerTurn = false;

        this.deck = [];

        // objeto na forma literal com o estado do jogo
        this.state = {
            'gameEnded': false,
            'dealerWon': false,
            'playerBusted': false
        };

        //métodos utilizados no construtor (DEVEM SER IMPLEMENTADOS PELOS ALUNOS)
        this.new_deck = function () {
            const valores = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"];
            const naipes = ["clubs", "diamonds", "hearts", "spades"];
            let deck = [];

            for (let i = 0; i < naipes.length; i++) {
                for (let j = 0; j < valores.length; j++) {
                    deck.push(valores[j] + "_" + naipes[i]);
                }
            }
            //console.log(deck);
            return deck
        };

        this.shuffle = function (deck) {
            for (let i = deck.length-1; i > 0; i--) {
                let j = Math.floor(Math.random() * (i+1));
                let aux = deck[i];
                deck[i] = deck[j];
                deck[j] = aux;
            }
        };

        this.deck = this.new_deck();
        this.shuffle(this.deck); 
    }

    // métodos
    // devolve as cartas do dealer num novo array (splice)
    get_dealer_cards() {
        return this.dealer_cards.slice();
    }

    // devolve as cartas do player num novo array (splice)
    get_player_cards() {
        return this.player_cards.slice();
    }

    // Ativa a variável booleana "dealerTurn"
    setDealerTurn(val) {
        this.dealerTurn = val;
    }

    //MÉTODOS QUE DEVEM SER IMPLEMENTADOS PELOS ALUNOS
    get_cards_value(cards) {
        let score = 0;
        let aceCount = 0;

        for (const card of cards) {
            let separarInfo = card.split("_");
            let valorCarta = separarInfo[0]; // numero_naipe
            
            if (valorCarta == 1) { //A
                score += 11;
                aceCount++;
            }else if (valorCarta == 11 || valorCarta == 12 || valorCarta == 13){ //J Q K
                score += 10;
            } else{
                score += parseInt(valorCarta);
            }
            
            // Se o valor da mão for superior a 21 e tiver A´s
            while (aceCount > 0 && score > MAX_POINTS) {
                score -= 10;
                aceCount--;
            }
        }
    
        return score;
    }

    dealer_move() {
        let newCardDealer = this.deck.pop();
        this.dealer_cards.push(newCardDealer);
        return this.get_game_state();
    }

    player_move() {
        let newCardPlayer = this.deck.pop();
        this.player_cards.push(newCardPlayer);
        return this.get_game_state();;
    }

    get_game_state() {
        const dealerScore = this.get_cards_value(this.get_dealer_cards());
        //console.log('Dealer: ' + dealerScore);
        const playerScore = this.get_cards_value(this.get_player_cards());
        //console.log('Player: ' + playerScore);
    
        const playerWon = playerScore === MAX_POINTS;
        const playerBusted = playerScore > MAX_POINTS;

        const dealerWon = this.dealerTurn && dealerScore > playerScore && dealerScore <= MAX_POINTS;
        const dealerBusted = this.dealerTurn && dealerScore > MAX_POINTS;

        this.state.gameEnded = playerWon || playerBusted || dealerWon || dealerBusted;
        this.state.playerBusted = playerBusted;
        this.state.dealerWon = dealerWon;

        return this.state;
    }
}