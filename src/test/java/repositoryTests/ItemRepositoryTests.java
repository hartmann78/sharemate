package repositoryTests;

import generators.ItemGenerator;
import generators.UserGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import repositoryTests.model.Item;
import repositoryTests.model.User;
import repositoryTests.repository.ItemRepository;
import repositoryTests.repository.UserRepository;

import java.util.Optional;

@DataJpaTest
public class ItemRepositoryTests {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;

    UserGenerator userGenerator = new UserGenerator();
    ItemGenerator itemGenerator = new ItemGenerator();

    @Test
    void createItem() {
        User user = userGenerator.generateUser();
        User newUser = userRepository.save(user);

        Item item = itemGenerator.generateItem(newUser.getId());
        Item newItem = itemRepository.save(item);

        Optional<User> checkUser = userRepository.findById(newUser.getId());

        Assertions.assertTrue(checkUser.isPresent());
        Assertions.assertEquals(checkUser.get(), newUser);

        Optional<Item> checkItem = itemRepository.findById(newItem.getId());

        Assertions.assertTrue(checkItem.isPresent());
        Assertions.assertEquals(checkItem.get(), newItem);
    }
}
