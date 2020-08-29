package macyBlackJack;

import macyBlackJack.model.*;

import java.util.ArrayList;

public class GameInteractor {

    private Game game;

    public GameInteractor(Game game) {
        this.game = game;

        game.getCurrentTurnProperty().addListener((observable, oldValue, newValue) -> {
            if(game.getCurrentTurnProperty().get() == 0) {
                performDealersTurn();
                determineWinners();
                checkBets();
                shuffleIfNeeded();
                game.getGameInProgressProperty().setValue(false);
            }
        });
    }

    public void addPlayer() {
        if(game.getPlayersProperty().size() < GameConstants.MAX_PLAYERS_INCLUDING_DEALER) {
            String playerName = "Player " + game.getPlayersProperty().size();
            game.addPlayer(new Player(playerName, false));
        }
    }

    public void removePlayer(Player player) {
        if(!player.isDealer()) {
            game.removePlayer(player);
        }

        renamePlayers();
    }

    public void dealNewHand() {
        resetPlayersHands();

        Player firstPlayer = firstPlayablePlayer();
        if(firstPlayer != null) {
            game.getCurrentTurnProperty().set(game.getPlayersProperty().indexOf(firstPlayer));
        } else {
            game.getCurrentTurnProperty().set(0);
        }
        game.getGameInProgressProperty().set(true);

        //Create temp list and move dealer to the end (dealer deals to himself after all other players)
        ArrayList<Player> orderedPlayers = new ArrayList<>(game.getPlayersProperty());
        Player dealer = orderedPlayers.remove(0);
        orderedPlayers.add(dealer);

        for(int i = 0; i < GameConstants.NUM_STARTING_CARDS; i++) {
            for (Player player : orderedPlayers) {
                if(player.canAffordToPlay()) {
                    dealCard(player);
                }
            }
        }
    }

    public void dealCard(Player player) {
        PlayingCard nextCard = getNextCard();
        if(nextCard != null) {
            player.getCurrentHand().addCard(nextCard);
        }

        if(player.getCurrentHand().isBust()) {
            nextTurn();
        }
    }

    public void stand() {
        nextTurn();
    }

    public void decrementBet(Player player) {
        if(player.getPlayerBet() > GameConstants.MIN_BET) {
            player.setPlayerBet(player.getPlayerBet() - GameConstants.BET_INCREMENT);
        }
    }

    public void incrementBet(Player player) {
        if(player.getPlayerBet() < GameConstants.MAX_BET && player.getPlayerBet() < player.getPlayerBank()) {
            player.setPlayerBet(player.getPlayerBet() + GameConstants.BET_INCREMENT);
        }
    }

    public void nextTurn() {
        if(game.getCurrentTurnProperty().get() == game.getPlayersProperty().size() - 1) {
            game.getCurrentTurnProperty().setValue(0);
        } else {
            game.getCurrentTurnProperty().setValue(game.getCurrentTurnProperty().get() + 1);

            //Skip players who are out of money
            if(!game.getPlayersProperty().get(game.getCurrentTurnProperty().get()).canAffordToPlay()) {
                nextTurn();
            }
        }
    }

    private Player firstPlayablePlayer() {
        for(Player player : game.getPlayersProperty()) {
            if(player.isDealer()) {
                continue;
            }

            if(player.canAffordToPlay()) {
                return player;
            }
        }

        return null;
    }

    private void renamePlayers() {
        for(int i = 0; i < game.getPlayersProperty().size(); i++) {
            if(i == 0) {
                continue;
            }

            String playerName = "Player " + i;
            game.getPlayersProperty().get(i).setPlayerName(playerName);
        }
    }

    private void resetPlayersHands() {
        for(Player player : game.getPlayersProperty()) {
            player.getCurrentHand().reset();
        }
    }

    private void checkBets() {
        for(Player player : game.getPlayersProperty()) {
            if(player.getPlayerBet() > player.getPlayerBank()) {
                player.setPlayerBet(player.getPlayerBank());
            }
        }
    }

    private PlayingCard getNextCard() {
        if(this.game.getShoe().getCards().size() == 0) {
            return null;
        }

        return this.game.getShoe().getCards().remove(0);
    }

    private void performDealersTurn() {
        Player dealer = game.getPlayersProperty().get(0);
        while(dealer.getCurrentHand().getCurrentScoreProperty().get() < GameConstants.DEALER_STANDS_AT) {
            dealCard(dealer);
        }
    }

    private void determineWinners() {
        Player dealer = game.getPlayersProperty().get(0);

        for(int i = 1; i < game.getPlayersProperty().size(); i++) {
            Player player = game.getPlayersProperty().get(i);
            player.getCurrentHand().setHandResult(determineHandResult(player, dealer));
            handlePayout(player);
        }
    }

    private HandResult determineHandResult(Player player, Player dealer) {
        int playerScore = player.getCurrentHand().getCurrentScoreProperty().get();
        int dealerScore = dealer.getCurrentHand().getCurrentScoreProperty().get();

        if(player.getCurrentHand().isBust()) {
            return HandResult.LOSS;
        }

        //player has blackjack and dealer does not
        if(player.getCurrentHand().isBlackJack() && !dealer.getCurrentHand().isBlackJack()) {
            return HandResult.WIN;
        }

        //dealer has blackjack and player does not
        if(dealer.getCurrentHand().isBlackJack() && !player.getCurrentHand().isBlackJack()) {
            return HandResult.LOSS;
        }

        //dealer busts but player does not
        if(dealer.getCurrentHand().isBust() && !player.getCurrentHand().isBust()) {
            return HandResult.WIN;
        }

        if(playerScore > dealerScore) {
            return HandResult.WIN;
        }

        if(playerScore == dealerScore) {
            return HandResult.TIE;
        }

        return HandResult.LOSS;
    }

    private void handlePayout(Player player) {
        int playerBet = player.getPlayerBet();
        int playerBank = player.getPlayerBank();

        switch (player.getCurrentHand().getHandResult()) {
            case TIE:
                //No payout
                break;

            case WIN:
                if(player.getCurrentHand().isBlackJack()) {
                    //Blackjack pays bet * multiplier
                    player.setPlayerBank(playerBank + (GameConstants.BLACKJACK_WIN_MULTIPLIER * playerBet));
                } else {
                    //Win pays bet
                    player.setPlayerBank(playerBank + playerBet);
                }
                break;

            case LOSS:
                //Loss subtracts bet from bank
                player.setPlayerBank(playerBank - playerBet);
                break;
        }
    }

    private void shuffleIfNeeded() {
        int cardsInDeck = PlayingCardType.stream().toArray().length;
        int cardsInShoe = cardsInDeck * GameConstants.NUM_DECKS;
        boolean shouldShuffle = game.getShoe().getCards().size() <= cardsInShoe * (GameConstants.REDEAL_THRESHOLD_PERCENTAGE / 100.0);

        if(shouldShuffle) {
            game.resetShoe();
        }
    }
}