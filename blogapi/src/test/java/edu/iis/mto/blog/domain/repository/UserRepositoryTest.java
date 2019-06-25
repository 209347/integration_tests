package edu.iis.mto.blog.domain.repository;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(1));
        Assert.assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldFindUserContainingFirstNameIgnoreCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("jan", "test", "test");
        Assert.assertThat(users.contains(user), Matchers.is(true));
    }

    @Test
    public void shouldNotFindUserWithWrongFirstName() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("janusz", "test", "test");
        Assert.assertThat(users.contains(user), Matchers.is(false));
    }

    @Test
    public void shouldFindUserContainingPartOfFirstNameIgnoreCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("j", "test", "test");
        Assert.assertThat(users.contains(user), Matchers.is(true));
    }

    @Test
    public void shouldFindUserContainingLastNameIgnoreCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("test", "nowak", "test");
        Assert.assertThat(users.contains(user), Matchers.is(true));
    }

    @Test
    public void shouldNotFindUserWithWrongLastName() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("test", "kowalski", "test");
        Assert.assertThat(users.contains(user), Matchers.is(false));
    }

    @Test
    public void shouldFindUserContainingPartOfLastNameIgnoreCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("test", "n", "test");
        Assert.assertThat(users.contains(user), Matchers.is(true));
    }

    @Test
    public void shouldFindUserContainingEmailIgnoreCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("test", "test", "john@domain.com");
        Assert.assertThat(users.contains(user), Matchers.is(true));
    }

    @Test
    public void shouldNotFindUserWithWrongEmail() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("test", "test", "wrong@email.com");
        Assert.assertThat(users.contains(user), Matchers.is(false));
    }

    @Test
    public void shouldFindUserContainingPartOfEmailIgnoreCase() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("test", "test", "john");
        Assert.assertThat(users.contains(user), Matchers.is(true));
    }
}
