package cardGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Player {
    private String name;
    private List<Card> hand;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public Card selectCardToPlay(Card topCard) {
        System.out.println("Enter the index of the card you want to play (or -1 to draw a card):");
        displayPlayerHand();
        Scanner scanner = new Scanner(System.in);
        int index = scanner.nextInt();

        if (index == -1) {
            return null;
        } else if (index >= 0 && index < hand.size()) {
            Card selectedCard = hand.get(index);
            if (selectedCard.matches(topCard)) {
                return selectedCard;
            } else {
                System.out.println("Invalid card! It should match the suit or rank of the top card.");
                return selectCardToPlay(topCard);
            }
        } else {
            System.out.println("Invalid index! Please enter a valid index.");
            return selectCardToPlay(topCard);
        }
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public void removeCardFromHand(Card card) {
        hand.remove(card);
    }

    public boolean hasCards() {
        return !hand.isEmpty();
    }

    private void displayPlayerHand() {
        System.out.println("Player: " + name);
        System.out.println("Your hand:");
        for (int i = 0; i < hand.size(); i++) {
            System.out.println(i + ". " + hand.get(i));
        }
    }
}

class Card {
    private Suit suit;
    private Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean matches(Card otherCard) {
        return suit == otherCard.getSuit() || rank == otherCard.getRank();
    }

    public boolean isActionCard() {
        return rank == Rank.ACE || rank == Rank.KING || rank == Rank.QUEEN || rank == Rank.JACK;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

enum Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES
}

enum Rank {
    TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
}

class CardGame {
    private List<Player> players;
    private List<Card> deck;
    private List<Card> discardPile;
    private List<Card> drawPile;
    private int currentPlayerIndex;

    public CardGame() {
        players = new ArrayList<>();
        deck = new ArrayList<>();
        discardPile = new ArrayList<>();
        drawPile = new ArrayList<>();
        currentPlayerIndex = 0;

        initializePlayers();
        initializeDeck();
        shuffleDeck();
        dealCards();
    }

    public void playGame() {
        startGame();

        while (!hasGameEnded()) {
            Player currentPlayer = players.get(currentPlayerIndex);
            displayGameStatus();
            displayPlayerHand(currentPlayer);

            Card cardToPlay = currentPlayer.selectCardToPlay(discardPile.get(discardPile.size() - 1));

            if (cardToPlay != null) {
                currentPlayer.removeCardFromHand(cardToPlay);
                discardPile.add(cardToPlay);

                if (cardToPlay.isActionCard()) {
                    performActionCard(cardToPlay);
                }

                if (!currentPlayer.hasCards()) {
                    declareWinner(currentPlayer);
                    break;
                }
            } else {
                drawCard(currentPlayer);
            }

            changeTurn();
        }

        System.out.println("Game Over!");
    }

    private void initializePlayers() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of players (2-4):");
        int numPlayers = scanner.nextInt();

        for (int i = 1; i <= numPlayers; i++) {
            System.out.println("Enter the name of player " + i + ":");
            String playerName = scanner.next();
            players.add(new Player(playerName));
        }
    }

    private void initializeDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(suit, rank));
            }
        }
    }

    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    private void dealCards() {
        for (Player player : players) {
            for (int i = 0; i < 5; i++) {
                Card card = deck.remove(deck.size() - 1);
                player.addCardToHand(card);
            }
        }

        discardPile.add(deck.remove(deck.size() - 1));
        drawPile.addAll(deck);
    }

    private void startGame() {
        System.out.println("===== Card Game =====");
        System.out.println("Game has started!");
        System.out.println("Top card on discard pile: " + discardPile.get(discardPile.size() - 1));
        System.out.println("Let's begin!");
    }

    private void displayGameStatus() {
        System.out.println("\n--- Game Status ---");
        System.out.println("Top card on discard pile: " + discardPile.get(discardPile.size() - 1));
    }

    private void displayPlayerHand(Player player) {
        System.out.println("\n--- " + player.getName() + "'s Turn ---");
        System.out.println("Your hand:");
        List<Card> hand = player.getHand();
        for (int i = 0; i < hand.size(); i++) {
            System.out.println(i + ". " + hand.get(i));
        }
    }

    private void drawCard(Player player) {
        if (drawPile.isEmpty()) {
            declareDraw();
        } else {
            Card card = drawPile.remove(drawPile.size() - 1);
            player.addCardToHand(card);
            System.out.println("You drew a card: " + card);
        }
    }

    private void performActionCard(Card card) {
        Rank rank = card.getRank();
        Player nextPlayer = getNextPlayer();

        switch (rank) {
            case ACE:
                System.out.println("Next player (" + nextPlayer.getName() + ") is skipped!");
                changeTurn();
                break;
            case KING:
                System.out.println("The sequence of turns is reversed!");
                Collections.reverse(players);
                currentPlayerIndex = players.indexOf(nextPlayer);
                break;
            case QUEEN:
                drawCards(nextPlayer, 2);
                System.out.println(nextPlayer.getName() + " drew 2 cards!");
                break;
            case JACK:
                drawCards(nextPlayer, 4);
                System.out.println(nextPlayer.getName() + " drew 4 cards!");
                break;
        }
    }

    private Player getNextPlayer() {
        int nextPlayerIndex = currentPlayerIndex + 1;
        if (nextPlayerIndex >= players.size()) {
            nextPlayerIndex = 0;
        }
        return players.get(nextPlayerIndex);
    }

    private void drawCards(Player player, int numCards) {
        for (int i = 0; i < numCards; i++) {
            drawCard(player);
        }
    }

    private void changeTurn() {
        currentPlayerIndex++;
        if (currentPlayerIndex >= players.size()) {
            currentPlayerIndex = 0;
        }
    }

    private boolean hasGameEnded() {
        for (Player player : players) {
            if (!player.hasCards()) {
                return true;
            }
        }
        return false;
    }

    private void declareWinner(Player player) {
        System.out.println("Player " + player.getName() + " wins!");
    }

    private void declareDraw() {
        System.out.println("The draw pile is empty! The game ends in a draw.");
    }
}

public class Main {
    public static void main(String[] args) {
        CardGame cardGame = new CardGame();
        cardGame.playGame();
    }
}

