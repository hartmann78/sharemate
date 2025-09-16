import com.practice.sharemate.dto.ItemDTO;
import com.practice.sharemate.mapper.ItemMapper;
import com.practice.sharemate.model.Item;
import com.practice.sharemate.repository.ItemRepository;
import com.practice.sharemate.repository.UserRepository;
import com.practice.sharemate.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class TestApplication {
    @Mock
    ItemMapper mockItemMapper;
    @Mock
    ItemRepository mockItemRepository;
    @Mock
    UserRepository mockUserRepository;

    @Test
    public void createItem() throws SQLException {
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl();
        itemServiceImpl.setItemMapper(mockItemMapper);
        itemServiceImpl.setItemRepository(mockItemRepository);
        itemServiceImpl.setUserRepository(mockUserRepository);

        Mockito
                .when(itemServiceImpl.addItem(Mockito.anyLong(), Mockito.any()))
                .thenReturn(Mockito);

        Long userId = 1L;

        Item addItem = Item.builder()
                .id(null)
                .name("Пакет для пакетов")
                .description("Прозрачный пакет для хранения пакетов. " +
                        "Пожалуйста не пытайтесь забронировать на срок больше недели. " +
                        "Это дефицитная вещь и такие заявки будут сразу отменяться.")
                .available(true)
                .ownerId(null)
                .requestId(null)
                .bookings(null)
                .comments(null)
                .build();

        ItemDTO newItem = ItemDTO.builder()
                .id(1L)
                .name("Пакет для пакетов")
                .description("Прозрачный пакет для хранения пакетов. " +
                        "Пожалуйста не пытайтесь забронировать на срок больше недели. " +
                        "Это дефицитная вещь и такие заявки будут сразу отменяться.")
                .available(true)
                .comments(new ArrayList<>())
                .build();

        ItemDTO itemDTO = itemServiceImpl.addItem(userId, addItem);
        Assertions.assertEquals(itemDTO, newItem);

    }
}
