package com.webpoker.domain.security

import com.webpoker.domain.player.PlayerRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

/**
 * Spring security User
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
            it.authority == PlayerRole.ROLE_ADMINISTRATOR
        }
    }
}
