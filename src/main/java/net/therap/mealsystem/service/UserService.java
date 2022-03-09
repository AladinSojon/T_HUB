package net.therap.mealsystem.service;

import net.therap.mealsystem.domain.Item;
import net.therap.mealsystem.domain.User;
import net.therap.mealsystem.exception.CollectionException;
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
    private MongoTemplate mongoTemplate;

    public User findById(String id) {
        return mongoTemplate.findById(id, User.class);
    }

    public User findByName(String name) {
        return mongoTemplate.findById(name, User.class);
    }

    public void save(User user) throws ConstraintViolationException, CollectionException {
        User userDb = findByName(user.getName());

        if (!ObjectUtils.isEmpty(userDb)) {
            throw new CollectionException((CollectionException.alreadyExists()));
        } else {
            user.setCreated(new Date((System.currentTimeMillis())));
            mongoTemplate.save(user);
        }
    }

    public List<User> findAll() {
        List<User> userList = mongoTemplate.findAll(User.class);

        if (userList.size() > 0) {
            return userList;
        } else {
            return new ArrayList<>();
        }
    }

    public void updateUser(String id, User user) throws CollectionException {
        User userDb = findById(id);
        User userWithSameName = findByName(user.getName());

        if (!ObjectUtils.isEmpty(userDb)) {
            if (!ObjectUtils.isEmpty(userWithSameName) && !userWithSameName.getId().equals(id)) {
                throw new CollectionException(CollectionException.alreadyExists());
            }

            userDb.setName(user.getName());
            userDb.setUpdated(new Date(System.currentTimeMillis()));
            mongoTemplate.save(userDb);
        } else {
            throw new CollectionException(CollectionException.notFoundException(id));
        }
    }

    public void deleteById(String id) throws CollectionException {
        User user = findById(id);
        if (ObjectUtils.isEmpty(user)) {
            throw new CollectionException(CollectionException.notFoundException(id));
        } else {
            mongoTemplate.remove(user);
        }
    }
}
