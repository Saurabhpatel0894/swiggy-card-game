# swiggy-card-game
This is a multiplayer card game implementation in Java. The game supports up to 4 players and different types of cards, including number cards and action cards (Ace, King, Queen, Jack). The objective of the game is to be the first player to run out of cards.

Rules of the Game
Each player starts with a hand of 5 cards.
The game starts with a deck of 52 cards (a standard deck of playing cards).
Players take turns playing cards from their hand, following a set of rules that define what cards can be played when.
A player can only play a card if it matches either the suit or the rank of the top card on the discard pile.
If a player cannot play a card, they must draw a card from the draw pile. If the draw pile is empty, the game ends in a draw.
The game ends when one player runs out of cards and is declared the winner.
Action Cards
Ace (A): Skip the next player in turn.
King (K): Reverse the sequence of who plays next.
Queen (Q): The next player draws 2 cards.
Jack (J): The next player draws 4 cards.
Note: Actions are not stackable. For example, if a Queen is played, the next player must draw 2 cards and cannot play another Queen on their turn, even if they have it in their hand.

Getting Started
Prerequisites
Java Development Kit (JDK) installed on your machine
Running the Game
Clone or download the repository to your local machine.
Open the project in your preferred Java IDE.
Build the project to compile the code.
Run the Main class to start the game.
Usage
When prompted, enter the number of players (2-4) who will be playing the game.
Enter the names of the players one by one.
The game will start, and the top card on the discard pile will be displayed.
Each player, in their turn, will be prompted with their hand of cards.
Players should enter the number corresponding to the card they want to play or enter -1 to draw a card from the draw pile.
The game will continue until one player runs out of cards, and they will be declared the winner.
Unit Tests
Unit tests have been provided for the Card, Player, and CardGame classes to ensure the correctness of their functionalities. These tests can be found in the src/test directory.

