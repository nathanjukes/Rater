package RaterTests.Models.API;

import Rater.Models.API.*;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;
import java.util.stream.Stream;

import static Rater.Models.API.HttpMethod.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RuleSearchQueryTest {

    @ParameterizedTest
    @MethodSource("searchRequestDataProvider")
    public void testApiParsing(String apiPath, HttpMethod methodOut, String apiNameOut, String fullApiOut) {
        UUID serviceId = UUID.randomUUID();
        String userId = UUID.randomUUID().toString();
        RuleSearchQuery searchQuery = new RuleSearchQuery(userId, methodOut, apiNameOut, serviceId, fullApiOut);

        RuleSearchRequest ruleSearchRequest = new RuleSearchRequest(
                userId,
                RuleType.id,
                serviceId,
                apiPath);
        RuleSearchQuery testQuery = RuleSearchQuery.from(ruleSearchRequest);

        assertEquals(searchQuery.getHttpMethod(), testQuery.getHttpMethod());
        assertEquals(searchQuery.getApiName(), testQuery.getApiName());
        assertEquals(searchQuery.getServiceId(), testQuery.getServiceId());
        assertEquals(searchQuery.getFullApi(), testQuery.getFullApi());
    }

    public static Stream<Arguments> searchRequestDataProvider() {
        return Stream.of(
                Arguments.of("GET:/users", GET, "users", "users"),
                Arguments.of("POST:/phones", POST, "phones", "phones"),
                Arguments.of("PUT:/users/testing", PUT, "users", "users/testing")
        );
    }
}
