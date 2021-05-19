package ru.savin.minicrm.service;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.savin.minicrm.dao.RoleRepository;
import ru.savin.minicrm.dao.UserRepository;
import ru.savin.minicrm.dto.FormUser;
import ru.savin.minicrm.entity.Role;
import ru.savin.minicrm.entity.User;
import ru.savin.minicrm.model.UserTestUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final Long USER_ID = 5L;
    private static final String USER_NAME = "andrey";
    private static final String PASSWORD = "pass123";
    private static final String FIRST_NAME = "Andrey";
    private static final String LAST_NAME = "Savin";
    private static final String EMAIL = "sava@mail.ru";
    private static final List<Role> ROLES = Arrays.asList(new Role("ROLE_EMPLOYEE"));

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void findByUserName() {
        Optional<User> actualUser = Optional.of(UserTestUtil.createUserObject(USER_ID, USER_NAME, PASSWORD,
                FIRST_NAME, LAST_NAME,
                EMAIL,
                ROLES));

        doReturn(actualUser).when(userRepository).findByUserName(USER_NAME);

        Optional<User> expectedUser = userService.findByUserName(USER_NAME);

        assertThat(expectedUser).isEqualTo(actualUser);
    }

    @Test
    void findByEmail() {
        Optional<User> actualUser = Optional.of(UserTestUtil.createUserObject(USER_ID, USER_NAME, PASSWORD,
                FIRST_NAME, LAST_NAME,
                EMAIL,
                ROLES));

        doReturn(actualUser).when(userRepository).findByEmail(EMAIL);

        Optional<User> expectedUser = userService.findByEmail(EMAIL);

        assertThat(expectedUser).isEqualTo(actualUser);
    }

    @Test
    void findAll() {

        User user = UserTestUtil.createUserObject(USER_ID, USER_NAME, PASSWORD, FIRST_NAME, LAST_NAME, EMAIL,
                ROLES);

        List<User> actualUsers = Arrays.asList(user);

        doReturn(actualUsers).when(userRepository).findAll();

        List<User> expectedUsers = userService.findAll();

        assertThat(expectedUsers).isEqualTo(actualUsers);
    }

    @Test
    void delete() {
        userService.delete(USER_ID);

        verify(userRepository, times(1)).deleteById(USER_ID);
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    void saveUserTest() {
        FormUser created = UserTestUtil.createDTO(USER_NAME, PASSWORD, FIRST_NAME, LAST_NAME, EMAIL);
        User persisted = UserTestUtil.createUserObject(USER_ID, USER_NAME, PASSWORD, FIRST_NAME, LAST_NAME, EMAIL,
                ROLES);

        Role role = new Role("ROLE_EMPLOYEE");

        doReturn(role).when(roleRepository).findRoleByName("ROLE_EMPLOYEE");
        doReturn(persisted).when(userRepository).save(any(User.class));

        User returnedByService = userService.save(created);

        ArgumentCaptor<User> userArgument = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userArgument.capture());
        verifyNoMoreInteractions(userRepository);

        assertTrue(CoreMatchers.is(returnedByService.getRoles()).matches(ROLES));

        assertUser(created, userArgument.getValue());
        assertEquals(persisted, returnedByService);

    }

    public void assertUser(FormUser expected, User actual) {

        assertEquals(expected.getUserName(), actual.getUserName());
        assertEquals(passwordEncoder.encode(expected.getPassword()), actual.getPassword());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), expected.getLastName());
        assertEquals(expected.getEmail(), expected.getEmail());
    }

}