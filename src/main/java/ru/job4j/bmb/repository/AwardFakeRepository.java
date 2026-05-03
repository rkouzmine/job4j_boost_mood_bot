package ru.job4j.bmb.repository;

import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.Award;

import java.util.*;

public class AwardFakeRepository
        extends CrudRepositoryFake<Award, Long>
        implements AwardRepository {

    private final List<Award> store = new ArrayList<>();
    private long id = 1;

    @Override
    public <S extends Award> S save(S entity) {
        entity.setId(id++);
        store.add(entity);
        return entity;
    }

    @Override
    public List<Award> findAll() {
        return new ArrayList<>(store);
    }

    @Override
    public long count() {
        return store.size();
    }

    @Override
    public Optional<Award> findById(Long id) {
        return store.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }
}