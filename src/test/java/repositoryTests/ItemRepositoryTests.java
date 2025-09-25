package repositoryTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import generators.ItemGenerator;
import generators.UserGenerator;
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
        userRepository.save(user);

        Item item = itemGenerator.generateItem(user.getId());
        itemRepository.save(item);

        Optional<User> checkUser = userRepository.findById(user.getId());
        assertTrue(checkUser.isPresent());
        assertEquals(checkUser.get(), user);

        Optional<Item> checkItem = itemRepository.findById(item.getId());
        assertTrue(checkItem.isPresent());
        assertEquals(checkItem.get(), item);
    }
}
