package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikePostRepository likePostRepository;

    private User user;

    private LikePost likePost;

    private BlogPost blogPost;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Nowak");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        userRepository.save(user);

        blogPost = new BlogPost();
        blogPost.setEntry("Post");
        blogPost.setUser(user);
        entityManager.persist(blogPost);

        likePost = new LikePost();
        likePost.setPost(blogPost);
        likePost.setUser(user);
    }

    @Test
    public void shouldFindNoLikesIfRepositoryIsEmpty() {
        List<LikePost> likePosts = likePostRepository.findAll();
        Assert.assertThat(likePosts, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneLikedPostIfRepositoryContainsOneLikedPostEntity() {
        likePostRepository.save(likePost);
        List<LikePost> likePosts = likePostRepository.findAll();
        Assert.assertThat(likePosts, Matchers.hasSize(1));
        Assert.assertThat(likePosts.get(0), Matchers.is(likePost));
    }

    @Test
    public void shouldStoreANewLikePost() {
        entityManager.persist(user);
        entityManager.persist(likePost);
        LikePost persistedLikePost = likePostRepository.save(likePost);
        Assert.assertThat(persistedLikePost.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldFindLikePostWithCorrectUserAndBlogPost() {
        likePostRepository.save(likePost);
        Optional<LikePost> likePosts = likePostRepository.findByUserAndPost(user, blogPost);
        Assert.assertThat(likePosts.get(), Matchers.is(likePost));
    }

    @Test
    public void shouldNotFindLikePostWithIncorrectUser() {
        likePostRepository.save(likePost);

        User incorrectUser = new User();
        incorrectUser.setFirstName("Mikolaj");
        incorrectUser.setLastName("Kalinowski");
        incorrectUser.setEmail("test@mail.com");
        incorrectUser.setAccountStatus(AccountStatus.NEW);
        entityManager.persist(incorrectUser);

        Optional<LikePost> likePosts = likePostRepository.findByUserAndPost(incorrectUser, blogPost);
        Assert.assertThat(likePosts, Matchers.is(Optional.empty()));
    }

    @Test
    public void shouldNotFindLikePostWithIncorrectBlogPost() {
        likePostRepository.save(likePost);

        BlogPost incorrectBlogPost = new BlogPost();
        incorrectBlogPost.setEntry("Incorrect Post");
        incorrectBlogPost.setUser(user);
        entityManager.persist(incorrectBlogPost);

        Optional<LikePost> likePosts = likePostRepository.findByUserAndPost(user, incorrectBlogPost);
        Assert.assertThat(likePosts, Matchers.is(Optional.empty()));
    }

    @Test
    public void shouldNotFindLikePostWithIncorrectData() {
        likePostRepository.save(likePost);

        User incorrectUser = new User();
        incorrectUser.setFirstName("Mikolaj");
        incorrectUser.setLastName("Kalinowski");
        incorrectUser.setEmail("test@mail.com");
        incorrectUser.setAccountStatus(AccountStatus.NEW);
        entityManager.persist(incorrectUser);

        BlogPost incorrectBlogPost = new BlogPost();
        incorrectBlogPost.setEntry("Incorrect Post");
        incorrectBlogPost.setUser(user);
        entityManager.persist(incorrectBlogPost);

        Optional<LikePost> likePosts = likePostRepository.findByUserAndPost(incorrectUser, incorrectBlogPost);
        Assert.assertThat(likePosts, Matchers.is(Optional.empty()));
    }
}