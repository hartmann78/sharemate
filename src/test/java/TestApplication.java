import com.practice.sharemate.repository.ItemRepository;
import com.practice.sharemate.service.ItemService;
import com.practice.sharemate.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.SQLException;

public class TestApplication {
    @Test
    public void createItem() throws SQLException {
        ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
        ItemService mockItemService = Mockito.mock(ItemService.class);
        ItemServiceImpl mockItemServiceImpl = Mockito.mock(ItemServiceImpl.class);
    }
}
