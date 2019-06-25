package edu.iis.mto.blog.rest.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.Matchers.hasItem;

public class LikePostTest extends FunctionalTests {

    private static final String LIKED_POST_API = "/blog/user/{userId}/like/{postId}";

    @Test
    public void likeBlogPostByConfirmedUserReturnsOKCode() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(LIKED_POST_API, 1, 1);
    }

    @Test
    public void likeBlogPostByNewUserReturnsForbiddenCode() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .when()
                .post(LIKED_POST_API, 2, 1);
    }

    @Test
    public void likeBlogPostByRemovedUserReturnsForbiddenCode() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .when()
                .post(LIKED_POST_API, 3, 1);
    }

    @Test
    public void likeBlogPostByOwnerUserReturnsForbiddenCode() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .when()
                .post(LIKED_POST_API, 4, 1);
    }

    @Test
    public void likingTheSameBlogPostTwoTimesDoesNotChangeItsStatus() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(LIKED_POST_API, 1, 1);
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(LIKED_POST_API, 1, 1);
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .when()
                .get("/blog/user/4/post")
                .then()
                .body("likesCount", hasItem(1));
    }
}
