package my.groupId.quarkussocial.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import my.groupId.quarkussocial.domain.model.User;
import my.groupId.quarkussocial.rest.dto.CreateUserResquest;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    @Transactional
    public Response createUser(CreateUserResquest userRequest ){

        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        user.persist(); //usado para salvar no db
//        outros exemplos
//        user.delete();
//        User.count();
//        User.delete(query:"dele from User where age < 18")

        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers(){
        PanacheQuery<User> query = User.findAll();
        return Response.ok(query.list()).build();
    }
}
