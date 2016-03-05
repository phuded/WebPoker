package com.webpoker.service.security

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import com.webpoker.domain.player.Player
import com.webpoker.domain.security.PokerUser
import com.webpoker.repository.PlayerRepository

/**
 * Created by matt on 10/07/2014.
 */
@Slf4j
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
            throw new UsernameNotFoundException("Incorrect username: " + name)
        }

        return buildPokerUser(player)
    }

    /**
     * Build a PokerUser from the accessed player
     * @param player
     * @return
     */
    private PokerUser buildPokerUser(Player player){

        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>()

        if(player.role) {

            String roleEnum = player.role.toString()

            log.info("Player: " + player.name + " has role: " + player.role.name + "/" + roleEnum)
            authorities << new SimpleGrantedAuthority(roleEnum)
        }

        return new PokerUser(player.id, player.name, player.password, authorities)
    }

    /**
     * Get the current user
     * @return
     */
    static PokerUser getCurrentUser(){
        return (PokerUser)SecurityContextHolder.context.authentication.principal

    }

    /**
     * Get the current user's user name
     * @return
     */
    static String getCurrentUserName(){
        return currentUser.username
    }
}
