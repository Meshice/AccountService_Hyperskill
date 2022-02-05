package account.entity;

import account.userDAO.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userTable = userDAO.findByEmailIgnoreCase(username);

        if (userTable == null) {
            throw new UsernameNotFoundException("Not found" + username);
        }
        return new UserDetailsImpl(userTable);
    }
}
