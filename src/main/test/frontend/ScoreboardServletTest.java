package frontend;

import accountService.AccountService;
import classesForTests.AccountServiceStubExist;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScoreboardServletTest {
    private AccountService accountServiceExist = new AccountServiceStubExist();


    private HttpServletResponse getMockedResponse(StringWriter stringWriter) throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        return response;
    }

    private HttpServletRequest getMockedRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        return request;
    }

    @Test
    public void testGet() throws Exception {
        final StringWriter stringWriter = new StringWriter();

        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest();

        String CorrectResponse = "{\"status\":\"200\",\"body\":{\"users\":[" +
                "{\"login\":\"first\",\"score\":1000}," +
                "{\"login\":\"second\",\"score\":100}," +
                "{\"login\":\"third\",\"score\":10}]}}";

        ScoreboardServlet scoreboardServlet = new ScoreboardServlet(accountServiceExist);
        scoreboardServlet.doGet(request, response);

        assertEquals(CorrectResponse, stringWriter.toString());
    }
}
