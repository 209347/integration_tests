package edu.iis.mto.blog.rest.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

public class CreatePostTest extends FunctionalTests {

    private static final String POST_API = "/blog/user/{id}/post";

    @Test
    public void postBlogPostByConfirmedUserReturnsCreatedCode() {
        JSONObject jsonObj = new JSONObject().put("entry", "blog post");
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CREATED)
                .when()
                .post(POST_API, 1);
    }

    @Test
    public void postBlogPostByNewUserReturnsForbiddenCode() {
        JSONObject jsonObj = new JSONObject().put("entry", "blog post");
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .when()
                .post(POST_API, 2);
    }

    @Test
    public void postBlogPostByRemovedUserReturnsForbiddenCode() {
        JSONObject jsonObj = new JSONObject().put("entry", "blog post");
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .when()
                .post(POST_API, 3);
    }
}
