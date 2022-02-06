package my.groupId.quarkussocial.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import my.groupId.quarkussocial.domain.model.User;
import my.groupId.quarkussocial.domain.repository.UserRepository;
import my.groupId.quarkussocial.rest.dto.CreateUserResquest;
import my.groupId.quarkussocial.rest.dto.ResponseError;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserRepository repository;
    private Validator validator;

    @Inject
    public UserResource(UserRepository repository, Validator validator){
        this.repository = repository;
        this.validator = validator;
    }

    @POST
    @Transactional
    public Response createUser(CreateUserResquest userRequest){

        Set<ConstraintViolation<CreateUserResquest>> violations = validator.validate(userRequest);
        if(!violations.isEmpty()){
            return ResponseError.createFromValidation(violations).withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }

        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        repository.persist(user); //usado para salvar no db
//        outros exemplos
//        user.delete();
//        User.count();
//        User.delete(query:"delete from User where age < 18")

        return Response.status(Response.Status.CREATED.getStatusCode()).entity(user).build();
    }

    @GET
    public Response listAllUsers(){
        PanacheQuery<User> query = repository.findAll();
        return Response.ok(query.list()).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response deleteUser(@PathParam("id") Long id){

        User user = repository.findById(id);

        if(user != null) {
            repository.delete(user);
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Transactional
    @Path("{id}")
    public Response putUser(@PathParam("id") Long id, CreateUserResquest userData){

        User user = repository.findById(id);

        if(user != null) {
            user.setAge(userData.getAge());
            user.setName(userData.getName());
            return Response.status(Response.Status.OK).entity(user).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
