import com.practice.sharemate.mapper.ItemMapper;
import com.practice.sharemate.mapper.UserMapper;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.model.User;
import com.practice.sharemate.repository.ItemRepository;
import com.practice.sharemate.repository.UserRepository;
import com.practice.sharemate.service.impl.ItemServiceImpl;
import com.practice.sharemate.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Optional;

public class MockitoItemTests {
    @Mock
    ItemMapper mockItemMapper;
    @Mock
    ItemRepository mockItemRepository;

    @Mock
    UserMapper mockUserMapper;
    @Mock
    UserRepository mockUserRepository;
    @Test
    public void createItem() {
        UserServiceImpl userServiceImpl = new UserServiceImpl(mockUserRepository, mockUserMapper);
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(mockItemMapper, mockItemRepository, mockUserRepository);

        User user1 = User.builder()
                .name("User 1")
                .email("somebody0707@mail.com")
                .build();

        User addUser1 = User.builder()
                .id(2L)
                .name("User 1")
                .email("somebody0707@mail.com")
                .build();

        Item item1 = Item.builder()
                .name("Пакет для пакетов")
                .description("Прозрачный пакет для хранения пакетов. " +
                        "Пожалуйста не пытайтесь забронировать на срок больше недели. " +
                        "Это дефицитная вещь и такие заявки будут сразу отменяться.")
                .available(true)
                .build();

        Item addItem1 = Item.builder()
                .id(1L)
                .name("Пакет для пакетов")
                .description("Прозрачный пакет для хранения пакетов. " +
                        "Пожалуйста не пытайтесь забронировать на срок больше недели. " +
                        "Это дефицитная вещь и такие заявки будут сразу отменяться.")
                .available(true)
                .comments(new ArrayList<>())
                .build();

        Mockito
                .when(mockUserRepository.save(user1))
                .thenReturn(addUser1);

        Mockito
                .when(mockUserMapper.dtoToEntity(userServiceImpl.addUser(user1)))
                .thenReturn(addUser1);

        User newUser2 = mockUserMapper.dtoToEntity(userServiceImpl.addUser(user1));

        Mockito
                .when(mockUserRepository.findById(newUser2.getId()))
                .thenReturn(Optional.of(new User()));

        Mockito
                .when(mockItemRepository.save(item1))
                .thenReturn(addItem1);

        Mockito
                .when(mockItemMapper.dtoToEntity(itemServiceImpl.addItem(newUser2.getId(), item1)))
                .thenReturn(addItem1);

        Item newItem = mockItemMapper.dtoToEntity(itemServiceImpl.addItem(newUser2.getId(), item1));
        Assertions.assertEquals(addItem1, newItem);
    }

    @Test
    public void updateItem() {
        UserServiceImpl userServiceImpl = new UserServiceImpl(mockUserRepository, mockUserMapper);
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl(mockItemMapper, mockItemRepository, mockUserRepository);

        User user2 = User.builder()
                .name("User 2")
                .email("nobody0000@mail.com")
                .build();

        User addUser2 = User.builder()
                .id(2L)
                .name("User 2")
                .email("nobody0000@mail.com")
                .build();

        Item item2 = Item.builder()
                .name("Пакет для пакетов")
                .description("Прозрачный пакет для хранения пакетов. " +
                        "Пожалуйста не пытайтесь забронировать на срок больше недели. " +
                        "Это дефицитная вещь и такие заявки будут сразу отменяться.")
                .available(true)
                .build();

        Item addItem2 = Item.builder()
                .id(2L)
                .name("Пакет для пакетов")
                .description("Прозрачный пакет для хранения пакетов. " +
                        "Пожалуйста не пытайтесь забронировать на срок больше недели. " +
                        "Это дефицитная вещь и такие заявки будут сразу отменяться.")
                .available(true)
                .comments(new ArrayList<>())
                .ownerId(addUser2.getId())
                .build();

        Item updateItem2 = Item.builder()
                .name("Левый носок")
                .description("Носок для левой ноги. " +
                        "На месте большого пальца отверстие круглой формы. " +
                        "Дам поносить за небольшое вознаграждение.")
                .available(false)
                .build();

        Item check = Item.builder()
                .id(2L)
                .name("Левый носок")
                .description("Носок для левой ноги. " +
                        "На месте большого пальца отверстие круглой формы. " +
                        "Дам поносить за небольшое вознаграждение.")
                .available(false)
                .comments(new ArrayList<>())
                .ownerId(addUser2.getId())
                .build();

        Mockito
                .when(mockUserRepository.save(user2))
                .thenReturn(addUser2);

        Mockito
                .when(mockUserMapper.dtoToEntity(userServiceImpl.addUser(user2)))
                .thenReturn(addUser2);


        User newUser3 = mockUserMapper.dtoToEntity(userServiceImpl.addUser(user2));

        Mockito
                .when(mockUserRepository.findById(newUser3.getId()))
                .thenReturn(Optional.of(new User()));

        Mockito
                .when(mockItemRepository.save(item2))
                .thenReturn(addItem2);

        Mockito
                .when(mockItemMapper.dtoToEntity(itemServiceImpl.addItem(newUser3.getId(), item2)))
                .thenReturn(addItem2);

        Item newItem2 = mockItemMapper.dtoToEntity(itemServiceImpl.addItem(newUser3.getId(), item2));


        Mockito
                .when(mockItemRepository.save(Mockito.any()))
                .thenReturn(check);

        Mockito
                .when(mockItemRepository.findById(newItem2.getId()))
                .thenReturn(Optional.of(addItem2));

        Mockito
                .when(mockItemMapper.dtoToEntity(itemServiceImpl.updateItem(newUser3.getId(), newItem2.getId(), updateItem2)))
                .thenReturn(check);

        Item checkUpdateItem2 = mockItemMapper.dtoToEntity(itemServiceImpl.updateItem(newUser3.getId(), newItem2.getId(), updateItem2));

        Assertions.assertEquals(check, checkUpdateItem2);
    }
}
