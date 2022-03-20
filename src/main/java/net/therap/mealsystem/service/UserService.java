package net.therap.mealsystem.service;

import net.therap.mealsystem.domain.User;
import net.therap.mealsystem.domain.UserAccountStatus;
import net.therap.mealsystem.exception.CollectionException;
import net.therap.mealsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author aladin
 * @author sheikh.ishrak
 * @since 3/6/22
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return username.contains("@") ? findByEmail(username) : findByUsername(username);
    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void save(User user) throws ConstraintViolationException, CollectionException {
        User userDb = findByUsername(user.getUsername());

        if (!ObjectUtils.isEmpty(userDb)) {
            throw new CollectionException((CollectionException.alreadyExists()));
        } else {
            user.setCreated(new Date((System.currentTimeMillis())));
            userRepository.save(user);
        }
    }

    public List<User> findAll() {
        List<User> userList = userRepository.findAll();

        if (userList.size() > 0) {
            return userList;
        } else {
            return new ArrayList<>();
        }
    }

    public void update(int id, User user) throws CollectionException {
        Optional<User> userDB = findById(id);
        User userWithSameName = findByUsername(user.getUsername());

        if (userDB.isPresent()) {
            if (!ObjectUtils.isEmpty(userWithSameName) && userWithSameName.getId() != id) {
                throw new CollectionException(CollectionException.alreadyExists());
            }

            User updatedUser = userDB.get();

            updatedUser.setUsername(user.getUsername());
            updatedUser.setFirstName(user.getFirstName());
            updatedUser.setLastName(user.getLastName());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setCreated(user.getCreated());
            updatedUser.setUpdated(new Date(System.currentTimeMillis()));
            updatedUser.setAccountStatus(user.getAccountStatus());
            updatedUser.setAccountNonExpired(user.isAccountNonExpired());
            updatedUser.setAccountNonLocked(user.isAccountNonLocked());
            updatedUser.setCredentialsNonExpired(user.isCredentialsNonExpired());
            updatedUser.setEnabled(user.isEnabled());

            userRepository.save(updatedUser);
        } else {
            throw new CollectionException(CollectionException.notFoundException(id));
        }
    }

    public void delete(Integer id) throws CollectionException {
        Optional<User> user = findById(id);
        if (!user.isPresent()) {
            throw new CollectionException(CollectionException.notFoundException(id));
        } else {
            User deletedUser = user.get();
            deletedUser.setAccountStatus(UserAccountStatus.DELETED);
            deletedUser.setEnabled(false);
            userRepository.save(deletedUser);
        }
    }
}
