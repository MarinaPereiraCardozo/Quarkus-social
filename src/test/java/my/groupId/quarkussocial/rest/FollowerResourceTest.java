package my.groupId.quarkussocial.rest;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import my.groupId.quarkussocial.domain.model.Follower;
import my.groupId.quarkussocial.domain.model.User;
import my.groupId.quarkussocial.domain.repository.FollowerRepository;
import my.groupId.quarkussocial.domain.repository.UserRepository;
import my.groupId.quarkussocial.rest.dto.FollowerRequest;
import my.groupId.quarkussocial.rest.dto.ResponseError;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;

    Long userId;
    Long followerId;

    @BeforeEach
    @Transactional
    public void setUP() {

        //usuário padrão dos testes
        var user = new User();
        user.setAge(30);
        user.setName("Fulano");
        userRepository.persist(user);
        userId = user.getId();

        //usuário seguidor
        var follower = new User();
        follower.setAge(20);
        follower.setName("Fulano2");
        userRepository.persist(follower);
        followerId = follower.getId();

        //criando um follower
        var followerEntity = new Follower();
        followerEntity.setFollower(follower);
        followerEntity.setUser(user);
        followerRepository.persist(followerEntity);
    }

    @Test
    @DisplayName("should return 409 when followerId is equal to userId")
    public void sameUserAsFollowerTest(){

        var body = new FollowerRequest();
        body.setFollowerId(userId);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userId)
        .when()
                .put()
        .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode())
                .body(Matchers.is("You can't follow youself"));
    }

    @Test
    @DisplayName("should return 404 on follow when userId doesn't exist")
    public void userNotFoundWhenTryindToFollowTest(){

        var body = new FollowerRequest();
        body.setFollowerId(userId);

        var inexistentUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", inexistentUserId)
        .when()
                .put()
        .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("should follow a user")
    public void followUserTest(){

        var body = new FollowerRequest();
        body.setFollowerId(followerId);

        var inexistentUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userId)
        .when()
                .put()
        .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @DisplayName("should return 404 on list users followers and userId doesn't exist")
    public void userNotFoundWhenListingFollowersTest(){

        var inexistentUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", inexistentUserId)
        .when()
                .get()
        .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("should list a users followers")
    public void listFollowersTest(){

        var response =
        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
        .when()
                .get()
        .then()
                .extract().response();

        var followersCount = response.jsonPath().get("followersCount");
        var followersCountent = response.jsonPath().getList("content");
        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());
        assertEquals(1, followersCount);
        assertEquals(1, followersCountent.size());
    }

    @Test
    @DisplayName("should return 404 on unfollow user and userId doesn't exist")
    public void userNotFoundWhenUnfollowingAnUserTest(){

        var inexistentUserId = 999;

        given()
                .pathParam("userId", inexistentUserId)
                .queryParam("followerId", followerId)
        .when()
                .delete()
        .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("should unfollow an user")
    public void unfollowingAnUserTest(){

        var inexistentUserId = 999;

        given()
                .pathParam("userId", userId)
                .queryParam("followerId", followerId)
        .when()
                .delete()
        .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }
}