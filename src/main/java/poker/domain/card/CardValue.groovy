package poker.domain.card

/**
 * Created with IntelliJ IDEA.
 * User: matthew.carter
 * Date: 12/08/13
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
 */
public enum CardValue {
    ACE("Ace",14),
    KING ("King",13),
    QUEEN ("Queen",12),
    JACK ("Jack",11),
    TEN ("Ten",10),
    NINE ("Nine",9),
    EIGHT ("Eight",8),
    SEVEN ("Seven",7),
    SIX ("Six",6),
    FIVE ("Five",5),
    FOUR ("Four",4),
    THREE ("Three",3),
    TWO ("Two",2)

    String name;
    int value;

    CardValue(name,value){
        this.name = name
        this.value = value
    }
}
