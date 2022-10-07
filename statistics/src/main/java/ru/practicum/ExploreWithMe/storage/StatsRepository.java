package ru.practicum.ExploreWithMe.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ExploreWithMe.model.*;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query(value="SELECT MAX(e.app) AS app, e.uri AS uri, COUNT(e.uri) AS hits " +
            "FROM endpoint_hit AS e " +
            "WHERE e.time_stamp between ?1 AND ?2 " +
            "GROUP BY e.uri", nativeQuery = true)
    List<ViewStats> countTotalperUri(LocalDateTime start, LocalDateTime end);

    @Query(value="SELECT MAX(e.app) AS app, e.uri AS uri, COUNT(e.uri) AS hits " +
            "FROM (SELECT DISTINCT ip AS i, * " +
            "FROM endpoint_hit) AS e " +
            "WHERE e.time_stamp between ?1 AND ?2 " +
            "GROUP BY e.uri", nativeQuery = true)
            List<ViewStats>countTotalperUriUniqueIp(LocalDateTime start, LocalDateTime end);
}
