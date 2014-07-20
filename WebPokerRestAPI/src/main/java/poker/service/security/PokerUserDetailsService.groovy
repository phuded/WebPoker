package poker.service.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import poker.domain.security.PokerRole
import poker.domain.security.PokerUser
import poker.domain.player.Player
import poker.repository.PlayerRepository

/**
 * Created by matt on 10/07/2014.
 */
@Service
class PokerUserDetailsService implements UserDetailsService {

    static final Logger logger = LoggerFactory.getLogger(PokerUserDetailsService.class)

    PlayerRepository playerRepository;

    @Autowired
    public PokerUserDetailsService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    PokerUser loadUserByUsername(String name) throws UsernameNotFoundException {

        Player player = playerRepository.findByName(name)

        if(!player){
            throw new UsernameNotFoundException("Incorrect username: " + name)
        }

        PokerRole pokerRole = PokerRole.getRole(player.role)

        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>()

        if(pokerRole) {

            String roleEnum = pokerRole.toString()

            logger.info("Player: " + player.name + " has role: " + pokerRole.name + "/" + roleEnum)
            authorities << new SimpleGrantedAuthority(roleEnum)
        }

        PokerUser user = new PokerUser(player.id, player.name, player.password, authorities);

        return user
    }

    /**
     * Get the current user
     * @return
     */
    static PokerUser getCurrentUser(){
        return (PokerUser)SecurityContextHolder.context.authentication.principal

    }
}
