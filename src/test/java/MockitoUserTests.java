import com.practice.sharemate.dto.UserDTO;
import com.practice.sharemate.mapper.UserMapper;
import com.practice.sharemate.model.User;
import com.practice.sharemate.repository.UserRepository;
import com.practice.sharemate.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class MockitoUserTests {
    private Long globalUserId = 0L;

    @Mock
    UserMapper mockUserMapper;
    @Mock
    UserRepository mockUserRepository;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Test
    public void createUser() {
        User user1 = generateUser();

        User addUser1 = User.builder()
                .id(++globalUserId)
                .name(user1.getName())
                .email(user1.getEmail())
                .build();

        Mockito
                .when(mockUserRepository.save(user1))
                .thenReturn(addUser1);

        Mockito
                .when(mockUserMapper.dtoToEntity(userServiceImpl.addUser(user1)))
                .thenReturn(addUser1);

        User newUser = mockUserMapper.dtoToEntity(userServiceImpl.addUser(user1));
        Assertions.assertEquals(addUser1, newUser);
    }

    @Test
    public void getUsers() {
        UserServiceImpl userServiceImpl = new UserServiceImpl(mockUserRepository, mockUserMapper);

        User user2 = generateUser();

        User addUser2 = User.builder()
                .id(++globalUserId)
                .name(user2.getName())
                .email(user2.getEmail())
                .build();

        List<UserDTO> addUs = List.of(mockUserMapper.entityToDto(addUser2));

        Mockito
                .when(mockUserRepository.save(user2))
                .thenReturn(addUser2);

        Mockito
                .when(mockUserRepository.findAll())
                .thenReturn(List.of(addUser2));

        userServiceImpl.addUser(user2);

        List<UserDTO> getUsers = userServiceImpl.findAllUsers();
        Assertions.assertEquals(addUser2, getUsers);
    }

    private User generateUser() {
        return User.builder()
                .name(generate())
                .email(generate() + "@mail.com")
                .build();
    }

    private String generate() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
