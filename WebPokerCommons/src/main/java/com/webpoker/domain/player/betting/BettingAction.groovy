package com.webpoker.domain.player.betting


/**
 * Created by matt on 04/06/2014.
 */
public enum BettingAction {
    CHECK("check"),
    CALL("call"),
    FOLD("fold")

    String name

   // BettingAction(){}

    BettingAction(String name){
       this.name = name
    }

    /**
     * Check if the supplied action is valid
     * @param actionName
     * @return
     */
    static boolean isValidAction(String actionName){
        for(BettingAction bettingAction : BettingAction.values()){
            if(bettingAction.name.equals(actionName)){
                return true
            }
        }

        return false
    }

    static BettingAction getBettingActionByName(String actionName){
        for(BettingAction bettingAction : BettingAction.values()){
            if(bettingAction.name.equals(actionName)){
                return bettingAction
            }
        }

        return null
    }
}
