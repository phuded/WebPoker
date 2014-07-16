package poker.service.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import poker.domain.security.PokerUser
import poker.domain.player.Player
import poker.repository.PlayerRepository

/**
 * Created by matt on 10/07/2014.
 */
@Service
class PokerUserDetailsService implements UserDetailsService {

    PlayerRepository playerRepository;

    @Autowired
    public PokerUserDetailsService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    PokerUser loadUserByUsername(String name) throws UsernameNotFoundException {

        Player player = playerRepository.findByName(name)

        if(!player){
            throw new UsernameNotFoundException("Incorrect user name: " + name)
        }

        PokerUser user = new PokerUser(player.id, player.name, player.password, true, true, true, true, new TreeSet<GrantedAuthority>());

        return user
    }
}
