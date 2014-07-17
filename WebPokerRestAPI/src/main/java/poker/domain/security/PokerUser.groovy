package poker.domain.security

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
}
