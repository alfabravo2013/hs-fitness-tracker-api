import com.google.gson.Gson;
import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.any;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.isArray;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.isObject;

public class FitnessTrackerTest extends SpringTest {
    private final Gson gson = new Gson();
    private final String trackerUrl = "/api/tracker";
    private final String registerUrl = "/api/developers/register";

    public FitnessTrackerTest() {
        super("../fitness_db.mv.db");
    }

    CheckResult testPostTracker(DataRecord[] data, DevProfile devProfile, int expectedCode) {
        for (var item : data) {
            HttpResponse response = post(trackerUrl, gson.toJson(item))
                    .basicAuth(devProfile.getEmail(), devProfile.getPassword())
                    .send();

            checkStatusCode(
                    "POST",
                    response.getRequest().getEndpoint(),
                    response.getStatusCode(),
                    expectedCode
            );
        }
        return CheckResult.correct();
    }

    CheckResult testGetTracker(DataRecord[] data, DevProfile devProfile, int expectedCode) {
        HttpResponse response = get(trackerUrl)
                .basicAuth(devProfile.getEmail(), devProfile.getPassword())
                .send();

        checkStatusCode(
                "GET",
                response.getRequest().getEndpoint(),
                response.getStatusCode(),
                expectedCode
        );

        checkDataJson(
                response,
                "GET",
                response.getRequest().getEndpoint(),
                data
        );

        return CheckResult.correct();
    }

    CheckResult testGetTrackerUnauthorized(DevProfile devProfile) {
        HttpResponse response = get(trackerUrl)
                .basicAuth(devProfile.getEmail(), devProfile.getPassword())
                .send();

        checkStatusCode(
                "GET",
                response.getRequest().getEndpoint(),
                response.getStatusCode(),
                401
        );

        return CheckResult.correct();
    }

    CheckResult testRegisterValidDev(DevProfile devProfile) {
        HttpResponse response = post(registerUrl, gson.toJson(devProfile)).send();

        checkStatusCode(
                "POST",
                response.getRequest().getEndpoint(),
                response.getStatusCode(),
                201
        );

        String location = response.getHeaders().get("Location");
        if (location == null || !location.matches("/api/developers/.+")) {
            return CheckResult.wrong(
                    "User registration response should contain the 'Location' header" +
                            " with the value '/developers/<id>'"
            );
        }

        return CheckResult.correct();
    }

    CheckResult testRegisterInvalidDev(DevProfile devProfile) {
        HttpResponse response = post(registerUrl, gson.toJson(devProfile)).send();

        checkStatusCode(
                "POST",
                response.getRequest().getEndpoint(),
                response.getStatusCode(),
                400
        );

        return CheckResult.correct();
    }

    CheckResult testGetProfile(DevProfile devProfile,
                               DevProfile unauthorized,
                               DevProfile unauthenticated) {
        HttpResponse response = post(registerUrl, gson.toJson(devProfile)).send();

        checkStatusCode(
                "POST",
                response.getRequest().getEndpoint(),
                response.getStatusCode(),
                201
        );

        var location = response.getHeaders().get("Location");

        response = get(location).basicAuth(unauthenticated.getEmail(), unauthenticated.getPassword()).send();
        checkStatusCode(
                "GET",
                response.getRequest().getEndpoint(),
                response.getStatusCode(),
                401
        );

        response = get(location).basicAuth(unauthorized.getEmail(), unauthorized.getPassword()).send();
        checkStatusCode(
                "GET",
                response.getRequest().getEndpoint(),
                response.getStatusCode(),
                403
        );

        response = get(location).basicAuth(devProfile.getEmail(), devProfile.getPassword()).send();
        checkStatusCode(
                "GET",
                response.getRequest().getEndpoint(),
                response.getStatusCode(),
                200
        );

        checkProfileJson(
                response,
                "GET",
                response.getRequest().getEndpoint(),
                devProfile
        );

        return CheckResult.correct();
    }

    private void checkStatusCode(String method,
                                 String endpoint,
                                 int actual,
                                 int expected) {
        if (actual != expected) {
            throw new WrongAnswer("""
                    %s %s should respond with status code %d, responded with %d
                    \r
                    """.formatted(method, endpoint, expected, actual));
        }
    }

    private void checkDataJson(HttpResponse response,
                               String method,
                               String endpoint,
                               DataRecord[] expectedData) {
        try {
            response.getJson();
        } catch (Exception e) {
            throw new WrongAnswer("%s %s should return a valid JSON".formatted(method, endpoint));
        }

        expect(response.getContent()).asJson().check(
                isArray(expectedData.length)
                        .item(isObject()
                                .value("id", any())
                                .value("username", expectedData[3].getUsername())
                                .value("activity", expectedData[3].getActivity())
                                .value("duration", expectedData[3].getDuration())
                                .value("calories", expectedData[3].getCalories())
                        )
                        .item(isObject()
                                .value("id", any())
                                .value("username", expectedData[2].getUsername())
                                .value("activity", expectedData[2].getActivity())
                                .value("duration", expectedData[2].getDuration())
                                .value("calories", expectedData[2].getCalories())
                        )
                        .item(isObject()
                                .value("id", any())
                                .value("username", expectedData[1].getUsername())
                                .value("activity", expectedData[1].getActivity())
                                .value("duration", expectedData[1].getDuration())
                                .value("calories", expectedData[1].getCalories())
                        )
                        .item(isObject()
                                .value("id", any())
                                .value("username", expectedData[0].getUsername())
                                .value("activity", expectedData[0].getActivity())
                                .value("duration", expectedData[0].getDuration())
                                .value("calories", expectedData[0].getCalories())
                        )
        );
    }

    private void checkProfileJson(HttpResponse response,
                                  String method,
                                  String endpoint,
                                  DevProfile expectedData) {
        try {
            response.getJson();
        } catch (Exception e) {
            throw new WrongAnswer("%s %s should return a valid JSON".formatted(method, endpoint));
        }

        expect(response.getContent()).asJson().check(
                isObject()
                        .value("id", any())
                        .value("email", Pattern.compile(expectedData.getEmail(), Pattern.CASE_INSENSITIVE))
        );
    }

    CheckResult reloadServer() {
        try {
            reloadSpring();
        } catch (Exception ex) {
            throw new WrongAnswer("Failed to restart application: " + ex.getMessage());
        }
        return CheckResult.correct();
    }

    DataRecord[] records = Stream
            .generate(DataRecordMother::createRecord)
            .limit(4)
            .toArray(DataRecord[]::new);

    DevProfile alice = DevProfileMother.alice();
    DevProfile dave = DevProfileMother.dave();
    DevProfile bob = DevProfileMother.bob();

    @DynamicTest
    DynamicTesting[] dt = new DynamicTesting[]{
            () -> testRegisterValidDev(alice),
            () -> testRegisterInvalidDev(DevProfileMother.withBadEmail(null)),
            () -> testRegisterInvalidDev(DevProfileMother.withBadEmail("")),
            () -> testRegisterInvalidDev(DevProfileMother.withBadEmail("email")),
            () -> testRegisterInvalidDev(DevProfileMother.withBadPassword(null)),
            () -> testRegisterInvalidDev(DevProfileMother.withBadPassword("")),
            () -> testRegisterInvalidDev(alice),
            () -> testPostTracker(records, alice, 201),
            () -> testPostTracker(records, dave, 401),
            () -> testGetTracker(records, alice, 200),
            () -> testGetTrackerUnauthorized(dave),
            () -> testGetProfile(bob, alice, dave),
            this::reloadServer,
            () -> testGetTracker(records, alice, 200),
    };
}
