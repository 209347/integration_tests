package edu.iis.mto.blog.rest.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

public class SearchPostsTest extends FunctionalTests {

    private static final String SEARCH_API = "/blog/user/{id}/post/";

    @Test
    public void searchPostsCreatedByRemovedUserShouldReturnBadRequest() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(SEARCH_API, 3);
    }

    @Test
    public void searchPostsCreatedByUserWithNoPostsShouldReturnNothing() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size()", is(0))
                .when()
                .get(SEARCH_API, 2);
    }

    @Test
    public void searchPostsCreatedByUserWithMultiplePostShouldReturnExpectedValue() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size()", is(2))
                .when()
                .get(SEARCH_API, 5);
    }

    @Test
    public void searchPostsCreatedByUserShouldReturnPostWithProperAmountOfLikes() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post("/blog/user/4/like/3");
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size()", is(1))
                .and()
                .body("likesCount", hasItems(1))
                .when()
                .get(SEARCH_API, 1);
    }

    @Test
    public void searchPostsCreatedByUserShouldReturnPostWithProperAmountOfLikesWhenPostHasNoLikes() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size()", is(1))
                .and()
                .body("likesCount", hasItems(0))
                .when()
                .get(SEARCH_API, 5);
    }
}
