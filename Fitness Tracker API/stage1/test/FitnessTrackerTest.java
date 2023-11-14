import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

public class FitnessTrackerTest extends SpringTest {
    @DynamicTest
    CheckResult test() {
        return CheckResult.correct();
    }
}
