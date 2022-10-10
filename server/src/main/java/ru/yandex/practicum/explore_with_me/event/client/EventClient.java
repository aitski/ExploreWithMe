package ru.yandex.practicum.explore_with_me.event.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.yandex.practicum.explore_with_me.event.model.EndpointHitDto;


@Service
public class EventClient extends BaseClient {
    private static final String API_PREFIX = "/hit";

    @Autowired
    public EventClient(@Value("${stats.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> sendToStatistics (EndpointHitDto endpointHitDto) {
        return post("", endpointHitDto);
    }
}
