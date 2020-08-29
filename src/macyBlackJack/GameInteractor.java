package macyBlackJack;

import macyBlackJack.model.*;

import java.util.ArrayList;

public class GameInteractor {

    private Game game;
    private GamePresenter presenter;

    public GameInteractor(GamePresenter presenter) {
        this.presenter = presenter;
        this.game = presenter.getGameViewModel();

        game.getCurrentTurnProperty().addListener((observable, oldValue, newValue) -> {
            if(presenter.getGameViewModel().getCurrentTurnProperty().get() == 0) {
                performDealersTurn();
                determineWinners();
                checkBets();
                shuffleIfNeeded();
                game.getGameInProgressProperty().setValue(false);
            }
        });
    }

    public void addPlayer() {
        if(game.getPlayersProperty().size() < RuleSet.MAX_PLAYERS_INCLUDING_DEALER) {
            game.addPlayer(new Player("New Player", false));
        }
    }

    public void removePlayer(Player player) {
        if(!player.isDealer()) {
            game.removePlayer(player);
        }
    }

    public void dealNewHand() {
        resetPlayersHands();
        game.getCurrentTurnProperty().set(1);
        game.getGameInProgressProperty().set(true);

        //Create temp list and move dealer to the end (dealer deals to himself after all other players)
        ArrayList<Player> orderedPlayers = new ArrayList<>(game.getPlayersProperty());
        Player dealer = orderedPlayers.remove(0);
        orderedPlayers.add(dealer);

        for(int i = 0; i < RuleSet.NUM_STARTING_CARDS; i++) {
            for (Player player : orderedPlayers) {
                if(player.canAffordToPlay() || player.isDealer()) {
                    player.getCurrentHand().addCard(getNextCard());
                }
            }
        }
    }

    public void dealCard(Player player) {
        player.getCurrentHand().addCard(getNextCard());

        if(player.getCurrentHand().isBust()) {
            nextTurn();
        }
    }

    public void stand() {
        nextTurn();
    }

    public void decrementBet(Player player) {
        if(player.getCurrentBetProperty().get() > RuleSet.MIN_BET) {
            player.getCurrentBetProperty().set(player.getCurrentBetProperty().get() - RuleSet.BET_INCREMENT);
        }
    }

    public void incrementBet(Player player) {
        if(player.getCurrentBetProperty().get() < RuleSet.MAX_BET && player.getCurrentBetProperty().get() < player.getBankProperty().get()) {
            player.getCurrentBetProperty().set(player.getCurrentBetProperty().get() + RuleSet.BET_INCREMENT);
        }
    }

    public void nextTurn() {
        if(game.getCurrentTurnProperty().get() == game.getPlayersProperty().size() - 1) {
            game.getCurrentTurnProperty().setValue(0);
        } else {
            game.getCurrentTurnProperty().setValue(game.getCurrentTurnProperty().get() + 1);
        }
    }

    private void resetPlayersHands() {
        for(Player player : game.getPlayersProperty()) {
            player.getCurrentHand().reset();
        }
    }

    private void checkBets() {
        for(Player player : game.getPlayersProperty()) {
            if(player.getCurrentBetProperty().get() > player.getBankProperty().get()) {
                player.getCurrentBetProperty().set(player.getBankProperty().get());
            }
        }
    }

    private PlayingCard getNextCard() {
        if(this.game.getShoe().getCards().size() == 0) {
            //TODO: handle error
            return null;
        }

        return this.game.getShoe().getCards().remove(0);
    }

    private void performDealersTurn() {
        Player dealer = game.getPlayersProperty().get(0);
        while(dealer.getCurrentHand().getCurrentScoreProperty().get() < RuleSet.DEALER_STANDS_AT) {
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
        int playerBet = player.getCurrentBetProperty().get();
        int playerBank = player.getBankProperty().get();

        switch (player.getCurrentHand().getHandResult()) {
            case TIE:
                //No payout
                break;

            case WIN:
                if(player.getCurrentHand().isBlackJack()) {
                    //Blackjack pays bet * multiplier
                    player.getBankProperty().set(playerBank + (RuleSet.BLACKJACK_WIN_MULTIPLIER * playerBet));
                } else {
                    //Win pays bet
                    player.getBankProperty().set(playerBank + playerBet);
                }
                break;

            case LOSS:
                //Loss subtracts bet from bank
                player.getBankProperty().set(playerBank - playerBet);
                break;
        }
    }

    private void shuffleIfNeeded() {
        int cardsInDeck = PlayingCardType.stream().toArray().length;
        int cardsInShoe = cardsInDeck * RuleSet.NUM_DECKS;
        boolean shouldShuffle = game.getShoe().getCards().size() <= cardsInShoe * (RuleSet.REDEAL_THRESHOLD_PERCENTAGE / 100.0);

        if(shouldShuffle) {
            game.resetShoe();
        }
    }
}
