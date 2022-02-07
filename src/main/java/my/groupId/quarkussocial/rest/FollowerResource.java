package my.groupId.quarkussocial.rest;

import my.groupId.quarkussocial.domain.repository.FollowerRepository;
import my.groupId.quarkussocial.domain.repository.UserRepository;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/users/{idUser}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private FollowerRepository followerRepository;
    private UserRepository userRepository;

    private FollowerResource(FollowerRepository followerRepository, UserRepository userRepository){

        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }
}
