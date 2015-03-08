package com.webpoker.domain.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
/**
 * Created by matt on 17/07/2014.
 */
class PokerUser extends User{

    String id

    PokerUser(String id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.id = id;
    }

    /**
     * Admin check
     * @return
     */
    boolean isAdmin(){
        return authorities.find{
            it.authority == PokerRole.ROLE_ADMINISTRATOR
        }
    }
}
