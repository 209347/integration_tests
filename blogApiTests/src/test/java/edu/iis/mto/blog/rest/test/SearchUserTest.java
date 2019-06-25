package edu.iis.mto.blog.rest.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class SearchUserTest extends FunctionalTests {

    private static final String SEARCH_USER_API = "/blog/user/find?searchString=";

    @Test
    public void shouldFindUserByFirstName() {
        String user = "Brian";
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size()", is(1))
                .when()
                .get(SEARCH_USER_API + user);
    }

    @Test
    public void shouldFindUserByPartOfFirstName() {
        String user = "B";
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size()", is(1))
                .when()
                .get(SEARCH_USER_API + user);
    }

    @Test
    public void shouldFindUserByLastName() {
        String user = "Steward";
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size()", is(1))
                .when()
                .get(SEARCH_USER_API + user);
    }

    @Test
    public void shouldFindUserByPartOfLastName() {
        String user = "St";
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size()", is(1))
                .when()
                .get(SEARCH_USER_API + user);
    }

    @Test
    public void shouldFindUserByMail() {
        String user = "john@domain.com";
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size()", is(1))
                .when()
                .get(SEARCH_USER_API + user);
    }

    @Test
    public void shouldFindUserByPartOfMail() {
        String user = "br";
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size()", is(1))
                .when()
                .get(SEARCH_USER_API + user);
    }

    @Test
    public void shouldNotFindRemovedUser() {
        String user = "Removed";
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
                .get(SEARCH_USER_API + user);
    }

}
