package ru.job4j.bmb.repository;

import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.Achievement;

import java.util.*;

public class AchievementFakeRepository
        extends CrudRepositoryFake<Achievement, Long>
        implements AchievementRepository {

    @Override
    public List<Achievement> findAll() {
        return new ArrayList<>(memory.values());
    }

    @Override
    public <S extends Achievement> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId((long) (memory.size() + 1));
        }
        memory.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public boolean existsByUserIdAndAwardId(Long userId, Long awardId) {
        return memory.values().stream()
                .anyMatch(a -> a.getUser() != null
                        && a.getAward() != null
                        && a.getUser().getId().equals(userId)
                        && a.getAward().getId().equals(awardId));
    }
}