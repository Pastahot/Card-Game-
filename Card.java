import java.util.Map;
import java.util.HashMap;

public class Card {

    private static final Map<String, CardType> cardTypes = new HashMap<>();
    private static final Map<String, CardEffect> cardEffects = new HashMap<>();

    static {
        cardTypes.put("Bang", CardType.BANG);
        cardTypes.put("Missed", CardType.MISSED);
        cardTypes.put("Beer", CardType.BEER);
        cardTypes.put("Cat Balou", CardType.CAT_BALOU);
        cardTypes.put("Stagecoach", CardType.STAGECOACH);
        cardTypes.put("Indians", CardType.INDIANS);
        cardTypes.put("Jail", CardType.JAIL);
        cardTypes.put("Dynamite", CardType.DYNAMITE);
        cardTypes.put("Barrel", CardType.BARREL);

        cardEffects.put("Bang", (game, player) -> {
            // implementation of the Bang card effect
        });

        cardEffects.put("Missed", (game, player) -> {
            // implementation of the Missed card effect
        });

        cardEffects.put("Beer", (game, player) -> {
            // implementation of the Beer card effect
        });

        cardEffects.put("Cat Balou", (game, player) -> {
            // implementation of the Cat Balou card effect
        });

        cardEffects.put("Stagecoach", (game, player) -> {
            // implementation of the Stagecoach card effect
        });

        cardEffects.put("Indians", (game, player) -> {
            // implementation of the Indians card effect
        });

        cardEffects.put("Jail", (game, player) -> {
            // implementation of the Jail card effect
        });

        cardEffects.put("Dynamite", (game, player) -> {
            // implementation of the Dynamite card effect
        });

        cardEffects.put("Barrel", (game, player) -> {
            // implementation of the Barrel card effect
        });
    }

    public static CardType getCardType(String card) {
        return cardTypes.get(card);
    }

    public static CardEffect getCardEffect(String card) {
        return cardEffects.get(card);
    }

    public interface CardEffect {
        void execute(BangGame game, String player);
    }
}
