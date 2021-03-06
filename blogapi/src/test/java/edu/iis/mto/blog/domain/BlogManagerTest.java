package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.BlogDataMapper;
import edu.iis.mto.blog.services.BlogService;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    BlogPostRepository blogPostRepository;

    @MockBean
    LikePostRepository likedPostRepository;

    @Autowired
    BlogDataMapper dataMapper;

    @Autowired
    BlogService blogService;

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test(expected = DomainError.class)
    public void shouldThrownDomainErrorWhenNewUserLikesThePost() {
        User blogPostUser = new User();
        blogPostUser.setEmail("blogPostUser@mail.com");
        blogPostUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(blogPostUser));

        User user = new User();
        user.setEmail("user@mail.com");
        user.setId(2L);
        user.setAccountStatus(AccountStatus.NEW);
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setUser(blogPostUser);
        when(blogPostRepository.findById(1L)).thenReturn(Optional.of(blogPost));

        when(likedPostRepository.findByUserAndPost(user, blogPost)).thenReturn(Optional.empty());
        blogService.addLikeToPost(user.getId(), blogPost.getId());
    }

    public void shouldLikePostIfUserIsConfirmed() {
        User blogPostUser = new User();
        blogPostUser.setEmail("blogPostUser@mail.com");
        blogPostUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(blogPostUser));

        User user = new User();
        user.setEmail("user@mail.com");
        user.setId(2L);
        user.setAccountStatus(AccountStatus.NEW);
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setUser(blogPostUser);
        when(blogPostRepository.findById(1L)).thenReturn(Optional.of(blogPost));

        when(likedPostRepository.findByUserAndPost(user, blogPost)).thenReturn(Optional.empty());
        blogService.addLikeToPost(user.getId(), blogPost.getId());

        ArgumentCaptor<LikePost> likePostParam = ArgumentCaptor.forClass(LikePost.class);
        Mockito.verify(likedPostRepository).save(likePostParam.capture());
        LikePost likePost = likePostParam.getValue();
        Assert.assertThat(likePost.getUser(), Matchers.is(user));
        Assert.assertThat(likePost.getPost(), Matchers.is(blogPost));
    }

}
