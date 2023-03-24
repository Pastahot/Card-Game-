import java.util.*;

public class BangGame {
    private static final int STARTING_LIVES = 4;

    private final List<String> deck = new ArrayList<>();
    private final List<String> discardedCards = new ArrayList<>();
    private final Map<String, Integer> playerLives = new LinkedHashMap<>();
    private final Map<String, List<String>> playerCards = new LinkedHashMap<>();
    private boolean gameIsOver = false; // initialize gameIsOver to false
    public BangGame() {
        initializeDeck();
        shuffleDeck();
        dealCards();
    }

    public void play() {
        System.out.println("Welcome to the simplified BANG card game!");

        while (playerLives.size() > 1) {
            for (String playerName : playerLives.keySet()) {
                playTurn(playerName);
                if (playerLives.size() == 1) {
                    break;
                }
            }
        }

        String winner = playerLives.keySet().iterator().next();
        System.out.println("Game over! " + winner + " is the winner!");
    }

    private void playTurn(String playerName) {
        System.out.println("It's " + playerName + "'s turn.");
        System.out.println(playerName + " has " + playerLives.get(playerName) + " lives and the following cards:");

        List<String> cards = playerCards.get(playerName);
        for (int i = 0; i < cards.size(); i++) {
            System.out.println((i + 1) + ". " + cards.get(i));
        }

        boolean hasDynamite = cards.contains("Dynamite");
        boolean hasJail = cards.contains("Jail");

        if (hasJail) {
            int chance = new Random().nextInt(4) + 1;
            if (chance == 1) {
                System.out.println("You escaped from jail!");
            } else {
                System.out.println("You are still in jail and lose your turn.");
                return;
            }
        }

        if (hasDynamite) {
            int chance = new Random().nextInt(8) + 1;
            if (chance == 1) {
                System.out.println("Dynamite exploded and you lose 3 lives!");
                playerLives.put(playerName, playerLives.get(playerName) - 3);
                discardedCards.add("Dynamite");
                playerCards.get(playerName).remove("Dynamite");
                return;
            } else {
                System.out.println("Dynamite didn't explode.");
                int index = playerLives.keySet().size() - 1 - new ArrayList<>(playerLives.keySet()).indexOf(playerName);
                String nextPlayer = new ArrayList<>(playerLives.keySet()).get(index);
                playerCards.get(playerName).remove("Dynamite");
                playerCards.get(nextPlayer).add("Dynamite");
                System.out.println("Dynamite has been passed to " + nextPlayer + ".");
            }
        }

        drawCards(playerName, 2);
        playCards(playerName);
        discardExcessCards(playerName);
    }

    private void initializeDeck() {
        deck.addAll(Collections.nCopies(2, "Barrel"));
        deck.add("Dynamite");
        deck.addAll(Collections.nCopies(3, "Jail"));
        deck.addAll(Collections.nCopies(30, "Bang"));
        deck.addAll(Collections.nCopies(15, "Missed"));
        deck.addAll(Collections.nCopies(8, "Beer"));
        deck.addAll(Collections.nCopies(6, "Cat Balou"));
        deck.addAll(Collections.nCopies(4, "Stagecoach"));
        deck.addAll(Collections.nCopies(2, "Indians"));
        Collections.shuffle(deck);
    }

    private void initializePlayers() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("How many players? (2-4) ");
        int numPlayers = scanner.nextInt();

        scanner.nextLine();

        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter name for player " + i + ": ");
            String playerName = scanner.nextLine();
            playerLives.put(playerName, STARTING_LIVES);
            playerCards.put(playerName, new ArrayList<>());
        }
    }

    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    private void dealCards() {
        for (String playerName : playerLives.keySet()) {
            drawCards(playerName, 4);
        }
    }

    private void drawCards(String playerName, int numCards) {
        List<String> cards = playerCards.get(playerName);
        for (int i = 0; i < numCards; i++) {
            if (deck.isEmpty()) {
                deck.addAll(discardedCards);
                discardedCards.clear();
                shuffleDeck();
            }
            cards.add(deck.remove(0));
        }
    }

    private void playCards(String playerName) {
        List<String> cards = playerCards.get(playerName);
        Iterator<String> iter = cards.iterator();
        while (iter.hasNext()) {
            String card = iter.next();
            if (canPlayCard(playerName, card)) {
                playCard(playerName, card);
                iter.remove();
            }
        }
    }

    private boolean canPlayCard(String playerName, String card) {
        List<String> cards = playerCards.get(playerName);

        if (card.equals("Bang")) {
            return true;
        }

        if (card.equals("Missed")) {
            return false;
        }

        if (card.equals("Beer")) {
            return playerLives.get(playerName) < STARTING_LIVES;
        }

        if (card.equals("Cat Balou")) {
            return !playerCards.values().stream().flatMap(List::stream).allMatch(c -> c.equals("Bang"));
        }

        if (card.equals("Stagecoach")) {
            return true;
        }

        if (card.equals("Indians")) {
            return true;
        }

        if (card.equals("Jail")) {
            return true;
        }

        if (card.equals("Dynamite")) {
            return true;
        }

        return card.equals("Barrel");
    }

    private void playCard(String playerName, String card) {
        Scanner scanner = new Scanner(System.in);

        if (card.equals("Bang")) {
            System.out.print("Who do you want to target? ");
            String target = scanner.nextLine();
            if (playerCards.get(target).contains("Barrel")) {
                int chance = new Random().nextInt(4) + 1;
                if (chance == 1) {
                    System.out.println("The barrel protected " + target + "!");
                    return;
                } else {
                    System.out.println("The barrel did not protect " + target + ".");
                }
            }
            if (playerCards.get(target).contains("Missed")) {
                System.out.println(target + " has a missed card and is protected.");
                playerCards.get(target).remove("Missed");
            } else {
                playerLives.put(target, playerLives.get(target) - 1);
                System.out.println("You hit " + target + "!");
                if (playerLives.get(target) == 0) {
                    System.out.println(target + " is dead and discards all cards from their hand and in play.");
                    discardedCards.addAll(playerCards.get(target));
                    playerCards.remove(target);
                    playerLives.remove(target);
                }
            }
        }
        if (card.equals("Missed")) {
            System.out.println("You played a missed card.");
        }

        if (card.equals("Beer")) {
            playerLives.put(playerName, playerLives.get(playerName) + 1);
            System.out.println("You gained a life.");
        }

        if (card.equals("Cat Balou")) {
            System.out.print("Whose card do you want to discard? ");
            String target = scanner.nextLine();
            if (playerCards.get(target).isEmpty()) {
                System.out.println(target + " has no cards to discard.");
            } else {
                String cardToDiscard = playerCards.get(target).get(new Random().nextInt(playerCards.get(target).size()));
                playerCards.get(target).remove(cardToDiscard);
                discardedCards.add(cardToDiscard);
                System.out.println("You discarded a card from " + target + ".");
            }
        }

        if (card.equals("Stagecoach")) {
            drawCards(playerName, 2);
            System.out.println("You drew 2 cards.");
        }

        if (card.equals("Indians")) {
            for (String target : playerCards.keySet()) {
                if (!target.equals(playerName)) {
                    if (playerCards.get(target).contains("Bang")) {
                        System.out.println(target + " discarded a bang.");
                        playerCards.get(target).remove("Bang");
                    } else {
                        playerLives.put(target, playerLives.get(target) - 1);
                        System.out.println(target + " lost a life.");
                        if (playerLives.get(target) == 0) {
                            System.out.println(target + " is dead and discards all cards from their hand and in play.");
                            discardedCards.addAll(playerCards.get(target));
                            playerCards.remove(target);
                            playerLives.remove(target);
                        }
                    }
                }
            }
        }

        if (card.equals("Jail")) {
            System.out.print("Who do you want to put in jail? ");
            String target = scanner.nextLine();
            playerCards.get(target).add("Jail");
            System.out.println(target + " is now in jail.");
        }

        if (card.equals("Dynamite")) {
            int chance = new Random().nextInt(8) + 1;
            if (chance == 1) {
                playerLives.put(playerName, playerLives.get(playerName) - 3);
                System.out.println("The dynamite exploded and you lost 3 lives!");
                if (playerLives.get(playerName) == 0) {
                    System.out.println("You are dead and discard all cards from your hand and in play.");
                    discardedCards.addAll(playerCards.get(playerName));
                    playerCards.remove(playerName);
                    playerLives.remove(playerName);
                }
            } else {
                System.out.println("The dynamite did not explode.");
                discardedCards.add("Dynamite");
            }
        }

        if (card.equals("Barrel")) {
            System.out.println("You played a barrel.");
        }
    }

    private void discardExcessCards(String playerName) {
        List<String> cards = playerCards.get(playerName);
        while (cards.size() > playerLives.get(playerName)) {
            System.out.print("You have too many cards. Which card do you want to discard? ");
            String cardToDiscard = new Scanner(System.in).nextLine();
            if (cards.contains(cardToDiscard)) {
                cards.remove(cardToDiscard);
                discardedCards.add(cardToDiscard);
            } else {
                System.out.println("You do not have that card.");
            }
        }
    }

    private void checkGameOver() {
        if (playerLives.size() == 1) {
            System.out.println("Game over! " + playerLives.keySet().iterator().next() + " is the winner!");
            gameIsOver = true;
        }
    }
    private void executeEffects(String player, CardType cardType) {
        List<String> cards = playerCards.get(player);
        Iterator<String> iterator = cards.iterator();
        while (iterator.hasNext()) {
            String card = iterator.next();
            if (cardType.equals(CardType.getCardType(card))) {
                new Card().getCardEffect(card).execute(this, player);
                iterator.remove();
                break;
            }
        }
    }





    private void playGame() {
        String[] playerOrder = new String[playerLives.size()];
        playerLives.keySet().toArray(playerOrder);

        while (!gameIsOver) {
            for (String player : playerOrder) {
                if (!gameIsOver) {
                    System.out.println("\n" + player + "'s turn.");
                    System.out.println("Your lives: " + playerLives.get(player));
                    System.out.println("Your cards: " + playerCards.get(player));
                    executeEffects(player, CardType.JAIL);
                    executeEffects(player, CardType.DYNAMITE);
                    executeEffects(player, CardType.BARREL);
                    drawCards(player, 2);
                    executeEffects(player, CardType.JAIL);
                    executeEffects(player, CardType.DYNAMITE);
                    executeEffects(player, CardType.BARREL);
                    discardExcessCards(player);
                    while (true) {
                        System.out.println("Your options:");
                        System.out.println("1. Play a card.");
                        System.out.println("2. End turn.");
                        int choice = new Scanner(System.in).nextInt();
                        if (choice == 1) {
                            System.out.print("Which card do you want to play? ");
                            String card = new Scanner(System.in).nextLine();
                            if (playerCards.get(player).contains(card)) {
                                playCard(player, card);
                                if (gameIsOver) {
                                    break;
                                }
                            } else {
                                System.out.println("You do not have that card.");
                            }
                        } else if (choice == 2) {
                            break;
                        }
                    }
                }
            }
        }
    }



    public void start() {
        initializePlayers(); // setup the players
        shuffleDeck(); // shuffle the deck
        dealCards(); // deal initial cards to the players
        play(); // start the game
    }

    public static void main(String[] args) {
        BangGame game = new BangGame();
        game.start();
    }
}
