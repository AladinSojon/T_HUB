package net.therap.mealsystem.service;

import net.therap.mealsystem.domain.Item;
import net.therap.mealsystem.domain.User;
import net.therap.mealsystem.exception.CollectionException;
import net.therap.mealsystem.repository.ItemRepository;
import net.therap.mealsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author aladin
 * @since 3/6/22
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    public void save(User user) throws ConstraintViolationException, CollectionException {
        User userDb = findByName(user.getName());

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
        User userWithSameName = findByName(user.getName());

        if (userDB.isPresent()) {
            if (!ObjectUtils.isEmpty(userWithSameName) && userWithSameName.getId() != id) {
                throw new CollectionException(CollectionException.alreadyExists());
            }

            User updatedUser = userDB.get();

            updatedUser.setName(user.getName());
            updatedUser.setUpdated(new Date(System.currentTimeMillis()));
//            updatedUser.setNotifList();
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
            userRepository.delete(user.get());
        }
    }
}
