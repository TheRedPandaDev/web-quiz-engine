package tests;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import engine.WebQuizEngine;
import org.hyperskill.hstest.dynamic.input.DynamicTesting;
import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.exception.outcomes.UnexpectedError;
import org.hyperskill.hstest.exception.outcomes.WrongAnswer;
import org.hyperskill.hstest.mocks.web.request.HttpRequest;
import org.hyperskill.hstest.mocks.web.response.HttpResponse;
import org.hyperskill.hstest.stage.SpringTest;
import org.hyperskill.hstest.testcase.CheckResult;

import static org.hyperskill.hstest.common.JsonUtils.getJson;
import static org.hyperskill.hstest.common.JsonUtils.getPrettyJson;
import static org.hyperskill.hstest.testing.expect.Expectation.expect;
import static org.hyperskill.hstest.testing.expect.json.JsonChecker.*;

public class WebQuizEngineTest extends SpringTest {
    public WebQuizEngineTest() {
        super(WebQuizEngine.class, "../quizdb.mv.db");
    }

    static void checkStatusCode(HttpResponse resp, int status) {
        if (resp.getStatusCode() != status) {
            throw new WrongAnswer(
                resp.getRequest().getMethod() + " " +
                    resp.getRequest().getLocalUri() +
                    " should respond with status code " + status + ", " +
                    "responded: " + resp.getStatusCode() + "\n\n" +
                    "Response body:\n\n" + resp.getContent()
            );
        }
    }

    private static String quiz1 =
        "{\n" +
            "  \"title\": \"The Java Logo\", \n" +
            "  \"text\": \"What is depicted on the Java logo?\",\n" +
            "  \"options\": [\"Robot\",\"Tea leaf\",\"Cup of coffee\",\"Bug\"],\n" +
            "  \"answer\": [2]\n" +
            "}";

    private static String quiz2 =
        "{\n" +
            "  \"title\": \"The Ultimate Question\",\n" +
            "  \"text\": \"What is the answer to the Ultimate Question of Life, the Universe and Everything?\",\n" +
            "  \"options\": [\"Everything goes right\",\"42\",\"2+2=4\",\"11011100\"],\n" +
            "  \"answer\": [1]\n" +
            "}";

    private static String quiz3 =
        "{\n" +
            "  \"title\": \"Math1\",\n" +
            "  \"text\": \"Which of the following is equal to 4?\",\n" +
            "  \"options\": [\"1+3\",\"2+2\",\"8-1\",\"1+5\"],\n" +
            "  \"answer\": [0,1]\n" +
            "}";

    private static String quiz4 =
        "{\n" +
            "  \"title\": \"Math2\",\n" +
            "  \"text\": \"Which of the following is equal to 4?\",\n" +
            "  \"options\": [\"1+1\",\"2+2\",\"8-1\",\"5-1\"],\n" +
            "  \"answer\": [1,3]\n" +
            "}";

    private static String quiz5 =
        "{\n" +
            "  \"title\": \"Math3\",\n" +
            "  \"text\": \"Which of the following is equal to 4?\",\n" +
            "  \"options\": [\"2*3\",\"5*8\",\"8*0\",\"1*5\"],\n" +
            "  \"answer\": []\n" +
            "}";

    private static String quiz6 =
        "{\n" +
            "  \"title\": \"Math4\",\n" +
            "  \"text\": \"Which of the following is equal to 4?\",\n" +
            "  \"options\": [\"2*3\",\"5*8\",\"8*0\",\"1*5\"]\n" +
            "}";

    private static String quiz7 =
        "{\n" +
            "  \"title\": \"Math5\",\n" +
            "  \"text\": \"Which of the following is equal to 4?\",\n" +
            "  \"options\": [\"2^2\",\"2+2\",\"2-2\",\"2*2\"],\n" +
            "  \"answer\": [0,1,3]\n" +
            "}";

    private static String[] quizzes = new String[] {
        quiz1, quiz2, quiz3, quiz4, quiz5, quiz6, quiz7
    };

    private static int[] quizIds = new int[] {
        0, 0, 0, 0, 0, 0, 0
    };

    private static String error400noTitle =
        "{\n" +
            "  \"text\": \"What is the answer to the Ultimate Question of Life, the Universe and Everything?\",\n" +
            "  \"options\": [\"Everything goes right\",\"42\",\"2+2=4\",\"11011100\"]\n" +
            "  \"answer\": [1]\n" +
            "}";

    private static String error400emptyTitle =
        "{\n" +
            "  \"title\": \"\",\n" +
            "  \"text\": \"What is the answer to the Ultimate Question of Life, the Universe and Everything?\",\n" +
            "  \"options\": [\"Everything goes right\",\"42\",\"2+2=4\",\"11011100\"]\n" +
            "  \"answer\": [1]\n" +
            "}";

    private static String error400noText =
        "{\n" +
            "  \"title\": \"123123123\",\n" +
            "  \"options\": [\"Everything goes right\",\"42\",\"2+2=4\",\"11011100\"]\n" +
            "  \"answer\": [1]\n" +
            "}";

    private static String error400emptyText =
        "{\n" +
            "  \"title\": \"The Ultimate Question\",\n" +
            "  \"text\": \"\",\n" +
            "  \"options\": [\"Everything goes right\",\"42\",\"2+2=4\",\"11011100\"]\n" +
            "  \"answer\": [1]\n" +
            "}";

    private static String error400noOptions =
        "{\n" +
            "  \"title\": \"The Ultimate Question\",\n" +
            "  \"text\": \"123123123\",\n" +
            "  \"answer\": [1]\n" +
            "}";

    private static String error400emptyOptions =
        "{\n" +
            "  \"title\": \"The Ultimate Question\",\n" +
            "  \"text\": \"What is the answer to the Ultimate Question of Life, the Universe and Everything?\",\n" +
            "  \"options\": [ ]\n" +
            "  \"answer\": [ ]\n" +
            "}";

    private static String error400oneOption =
        "{\n" +
            "  \"title\": \"The Ultimate Question\",\n" +
            "  \"text\": \"What is the answer to the Ultimate Question of Life, the Universe and Everything?\",\n" +
            "  \"options\": [\"Everything goes right\"]\n" +
            "  \"answer\": [0]\n" +
            "}";

    @DynamicTestingMethod
    DynamicTesting[] dt = new DynamicTesting[] {
        () -> testAllQuizzes(0),

        () -> testCreateQuiz(0),
        () -> testQuizExists(0),
        () -> testQuizNotExists(0),

        () -> testAllQuizzes(1),

        () -> testCreateQuiz(1),
        () -> testQuizExists(1),
        () -> testQuizNotExists(1),

        () -> testAllQuizzes(2),

        () -> checkQuizSuccess(quizIds[0], "[0]", false),
        () -> checkQuizSuccess(quizIds[0], "[1]", false),
        () -> checkQuizSuccess(quizIds[0], "[2]", true),
        () -> checkQuizSuccess(quizIds[0], "[3]", false),

        () -> checkQuizSuccess(quizIds[1], "[0]", false),
        () -> checkQuizSuccess(quizIds[1], "[1]", true),
        () -> checkQuizSuccess(quizIds[1], "[2]", false),
        () -> checkQuizSuccess(quizIds[1], "[3]", false),

        () -> testAllQuizzes(2),
        this::reloadServer,
        () -> testAllQuizzes(2),
        () -> checkQuizSuccess(quizIds[0], "[2]", true),
        () -> checkQuizSuccess(quizIds[0], "[3]", false),
        () -> checkQuizSuccess(quizIds[1], "[0]", false),
        () -> checkQuizSuccess(quizIds[1], "[1]", true),

        () -> addIncorrectQuiz(error400noTitle),
        () -> addIncorrectQuiz(error400emptyTitle),
        () -> addIncorrectQuiz(error400noText),
        () -> addIncorrectQuiz(error400emptyText),
        () -> addIncorrectQuiz(error400noOptions),
        () -> addIncorrectQuiz(error400emptyOptions),
        () -> addIncorrectQuiz(error400oneOption),

        () -> testCreateQuiz(2),
        () -> testQuizExists(2),
        () -> testQuizNotExists(2),
        () -> checkQuizSuccess(quizIds[2], "[]", false),
        () -> checkQuizSuccess(quizIds[2], "[0]", false),
        () -> checkQuizSuccess(quizIds[2], "[1]", false),
        () -> checkQuizSuccess(quizIds[2], "[2]", false),
        () -> checkQuizSuccess(quizIds[2], "[3]", false),
        () -> checkQuizSuccess(quizIds[2], "[0,1]", true),
        () -> checkQuizSuccess(quizIds[2], "[0,2]", false),
        () -> checkQuizSuccess(quizIds[2], "[0,3]", false),
        () -> checkQuizSuccess(quizIds[2], "[1,2]", false),
        () -> checkQuizSuccess(quizIds[2], "[1,3]", false),
        () -> checkQuizSuccess(quizIds[2], "[2,3]", false),
        () -> checkQuizSuccess(quizIds[2], "[0,1,2]", false),
        () -> checkQuizSuccess(quizIds[2], "[0,1,3]", false),
        () -> checkQuizSuccess(quizIds[2], "[1,2,3]", false),
        () -> checkQuizSuccess(quizIds[2], "[0,1,2,3]", false),

        () -> testCreateQuiz(3),
        () -> testQuizExists(3),
        () -> testQuizNotExists(3),
        () -> checkQuizSuccess(quizIds[3], "[]", false),
        () -> checkQuizSuccess(quizIds[3], "[0]", false),
        () -> checkQuizSuccess(quizIds[3], "[1]", false),
        () -> checkQuizSuccess(quizIds[3], "[2]", false),
        () -> checkQuizSuccess(quizIds[3], "[3]", false),
        () -> checkQuizSuccess(quizIds[3], "[0,1]", false),
        () -> checkQuizSuccess(quizIds[3], "[0,2]", false),
        () -> checkQuizSuccess(quizIds[3], "[0,3]", false),
        () -> checkQuizSuccess(quizIds[3], "[1,2]", false),
        () -> checkQuizSuccess(quizIds[3], "[1,3]", true),
        () -> checkQuizSuccess(quizIds[3], "[2,3]", false),
        () -> checkQuizSuccess(quizIds[3], "[0,1,2]", false),
        () -> checkQuizSuccess(quizIds[3], "[0,1,3]", false),
        () -> checkQuizSuccess(quizIds[3], "[1,2,3]", false),
        () -> checkQuizSuccess(quizIds[3], "[0,1,2,3]", false),

        () -> testCreateQuiz(4),
        () -> testQuizExists(4),
        () -> testQuizNotExists(4),
        () -> checkQuizSuccess(quizIds[4], "[]", true),
        () -> checkQuizSuccess(quizIds[4], "[0]", false),
        () -> checkQuizSuccess(quizIds[4], "[1]", false),
        () -> checkQuizSuccess(quizIds[4], "[2]", false),
        () -> checkQuizSuccess(quizIds[4], "[3]", false),
        () -> checkQuizSuccess(quizIds[4], "[0,1]", false),
        () -> checkQuizSuccess(quizIds[4], "[0,2]", false),
        () -> checkQuizSuccess(quizIds[4], "[0,3]", false),
        () -> checkQuizSuccess(quizIds[4], "[1,2]", false),
        () -> checkQuizSuccess(quizIds[4], "[1,3]", false),
        () -> checkQuizSuccess(quizIds[4], "[2,3]", false),
        () -> checkQuizSuccess(quizIds[4], "[0,1,2]", false),
        () -> checkQuizSuccess(quizIds[4], "[0,1,3]", false),
        () -> checkQuizSuccess(quizIds[4], "[1,2,3]", false),
        () -> checkQuizSuccess(quizIds[4], "[0,1,2,3]", false),

        () -> testCreateQuiz(5),
        () -> testQuizExists(5),
        () -> testQuizNotExists(5),
        () -> checkQuizSuccess(quizIds[5], "[]", true),
        () -> checkQuizSuccess(quizIds[5], "[0]", false),
        () -> checkQuizSuccess(quizIds[5], "[1]", false),
        () -> checkQuizSuccess(quizIds[5], "[2]", false),
        () -> checkQuizSuccess(quizIds[5], "[3]", false),
        () -> checkQuizSuccess(quizIds[5], "[0,1]", false),
        () -> checkQuizSuccess(quizIds[5], "[0,2]", false),
        () -> checkQuizSuccess(quizIds[5], "[0,3]", false),
        () -> checkQuizSuccess(quizIds[5], "[1,2]", false),
        () -> checkQuizSuccess(quizIds[5], "[1,3]", false),
        () -> checkQuizSuccess(quizIds[5], "[2,3]", false),
        () -> checkQuizSuccess(quizIds[5], "[0,1,2]", false),
        () -> checkQuizSuccess(quizIds[5], "[0,1,3]", false),
        () -> checkQuizSuccess(quizIds[5], "[1,2,3]", false),
        () -> checkQuizSuccess(quizIds[5], "[0,1,2,3]", false),

        () -> testCreateQuiz(6),
        () -> testQuizExists(6),
        () -> testQuizNotExists(6),
        () -> checkQuizSuccess(quizIds[6], "[]", false),
        () -> checkQuizSuccess(quizIds[6], "[0]", false),
        () -> checkQuizSuccess(quizIds[6], "[1]", false),
        () -> checkQuizSuccess(quizIds[6], "[2]", false),
        () -> checkQuizSuccess(quizIds[6], "[3]", false),
        () -> checkQuizSuccess(quizIds[6], "[0,1]", false),
        () -> checkQuizSuccess(quizIds[6], "[0,2]", false),
        () -> checkQuizSuccess(quizIds[6], "[0,3]", false),
        () -> checkQuizSuccess(quizIds[6], "[1,2]", false),
        () -> checkQuizSuccess(quizIds[6], "[1,3]", false),
        () -> checkQuizSuccess(quizIds[6], "[2,3]", false),
        () -> checkQuizSuccess(quizIds[6], "[0,1,2]", false),
        () -> checkQuizSuccess(quizIds[6], "[0,1,3]", true),
        () -> checkQuizSuccess(quizIds[6], "[1,2,3]", false),
        () -> checkQuizSuccess(quizIds[6], "[0,1,2,3]", false),

        () -> testAllQuizzes(7),
        this::reloadServer,
        () -> testAllQuizzes(7),
        () -> checkQuizSuccess(quizIds[5], "[]", true),
        () -> checkQuizSuccess(quizIds[5], "[0]", false),
        () -> checkQuizSuccess(quizIds[6], "[0,1,2]", false),
        () -> checkQuizSuccess(quizIds[6], "[0,1,3]", true),
    };

    private CheckResult testCreateQuiz(int quizNum) {
        String url = "/api/quizzes";
        HttpResponse resp = post(url, quizzes[quizNum]).send();
        checkStatusCode(resp, 200);

        expect(resp.getContent()).asJson().check(
            isObject()
                .value("id", isInteger(i -> {
                    quizIds[quizNum] = i;
                    return true;
                }))
                .anyOtherValues()
        );

        return CheckResult.correct();
    }

    private CheckResult testQuizExists(int quizNum) {
        int quizId = quizIds[quizNum];
        String quiz = quizzes[quizNum];

        String url = "/api/quizzes/" + quizId;
        HttpResponse resp = get(url).send();
        checkStatusCode(resp, 200);

        JsonObject rightQuiz = getJson(quiz).getAsJsonObject();
        rightQuiz.remove("answer");
        rightQuiz.addProperty("id", quizId);

        expect(getPrettyJson(rightQuiz)).asJson().check(
            isObject()
                .value("id", quizId)
                .value("title", isString())
                .value("text", isString())
                .value("options", isArray(any()))
        );

        JsonElement json = resp.getJson();

        CheckResult wrongResponse = CheckResult.wrong(
            "The quiz sent to the program looked like this:\n" +
                getPrettyJson(rightQuiz) + "\n\n" +
                "But the received quiz looks like that:\n" +
                getPrettyJson(json)
        );

        if (!json.isJsonObject()) {
            return wrongResponse;
        }

        JsonObject obj = json.getAsJsonObject();

        if (!rightQuiz.equals(obj)) {
            return wrongResponse;
        }

        return CheckResult.correct();
    }

    private CheckResult testQuizNotExists(int quizNum) {
        int quizId = quizIds[quizNum];

        String url = "/api/quizzes/" + (quizId + 125);
        HttpResponse resp = get(url).send();
        checkStatusCode(resp, 404);

        return CheckResult.correct();
    }

    private CheckResult testAllQuizzes(int count) {
        String url = "/api/quizzes";
        HttpResponse resp = get(url).send();
        checkStatusCode(resp, 200);

        expect(resp.getContent()).asJson().check(
            isArray(count, isObject().anyOtherValues())
        );

        return CheckResult.correct();
    }

    private CheckResult checkQuizSuccess(int quizNum, String answerSent, boolean shouldResponse) {
        String url = "/api/quizzes/" + quizNum + "/solve";

        HttpRequest req = post(url, "{" + " \"answer\" : " + answerSent + "}");
        HttpResponse resp = req.send();
        checkStatusCode(resp, 200);

        expect(resp.getContent()).asJson().check(
            isObject()
                .value("success", shouldResponse)
                .value("feedback", isString())
        );

        return CheckResult.correct();
    }

    private CheckResult addIncorrectQuiz(String quiz) {
        String url = "/api/quizzes";
        HttpResponse resp = post(url, quiz).send();
        checkStatusCode(resp, 400);
        return CheckResult.correct();
    }

    private CheckResult reloadServer() {
        try {
            reloadSpring();
        } catch (Exception ex) {
            throw new UnexpectedError(ex.getMessage());
        }
        return CheckResult.correct();
    }
}
