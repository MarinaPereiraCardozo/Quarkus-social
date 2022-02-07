package my.groupId.quarkussocial.domain.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import my.groupId.quarkussocial.domain.model.Follower;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {
}
