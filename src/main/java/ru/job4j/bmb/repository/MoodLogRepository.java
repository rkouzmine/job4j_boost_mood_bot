package ru.job4j.bmb.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.util.List;
import java.util.stream.Stream;

public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {

    List<MoodLog> findAll();

    List<MoodLog> findByUserId(Long userId);

    Stream<MoodLog> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("""
       select u
       from User u
       where u.id not in (
           select m.user.id
           from MoodLog m
           where m.createdAt between :startOfDay and :endOfDay
       )
       """)
    List<User> findUsersWhoDidNotVoteToday(
            @Param("startOfDay") long startOfDay,
            @Param("endOfDay") long endOfDay
    );

    @Query("""
           select m
           from MoodLog m
           where m.user.id = :userId
           and m.createdAt >= :weekStart
           """)
    List<MoodLog> findMoodLogsForWeek(
            @Param("userId") Long userId,
            @Param("weekStart") long weekStart
    );

    @Query("""
           select m
           from MoodLog m
           where m.user.id = :userId
           and m.createdAt >= :monthStart
           """)
    List<MoodLog> findMoodLogsForMonth(
            @Param("userId") Long userId,
            @Param("monthStart") long monthStart
    );
}