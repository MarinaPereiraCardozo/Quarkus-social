package my.groupId.quarkussocial.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import my.groupId.quarkussocial.domain.model.Follower;
import my.groupId.quarkussocial.domain.model.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {

    public boolean follows(User follower, User user){
//        Map<String, Object> paramns = new HashMap<>();
//        params.put("follower", follower);
//        params.put("user", user);
//        outra forma de fazer:
        var params = Parameters.with("follower", follower)
                .and("user", user)
                .map();
        PanacheQuery<Follower> query = find("follower = :follower and user = :user", params);
        Optional<Follower> result = query.firstResultOptional();

        return result.isPresent();
    }
}
