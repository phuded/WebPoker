package poker.card

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 12/08/13
 * Time: 15:19
 * To change this template use File | Settings | File Templates.
 */
enum Suit {
    SPADES ("Spades","Black"),
    CLUBS ("Clubs","Black"),
    DIAMONDS("Diamonds","Red"),
    HEARTS("Hearts","Red")

    String name
    String colour

    Suit(name,colour){
        this.name = name
        this.colour = colour
    }
}
