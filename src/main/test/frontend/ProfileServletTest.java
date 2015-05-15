package frontend;

import base.AccountService;
import org.junit.Test;
import classesForTests.AccountServiceStub;
import classesForTests.AccountServiceStubExist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProfileServletTest {
    private AccountService accountService = new AccountServiceStub();
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
    public void testNotAuthorized() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final HttpSession httpSession = mock(HttpSession.class);
        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest();

        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getId()).thenReturn("1");
        String CorrectResponse = "{\"status\":401,\"body\":{\"messages\":{\"user\":\"not authorized\"}}}";

        ProfileServlet profile = new ProfileServlet(accountService);
        profile.doGet(request, response);

        assertEquals(CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testGet() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final HttpSession httpSession = mock(HttpSession.class);

        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest();
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getId()).thenReturn("1");
        String CorrectResponse = "{\"status\":200,\"body\"" +
                ":{\"id\":1,\"name\":\"test\",\"email\":\"test@test\"}}";

        ProfileServlet profile = new ProfileServlet(accountServiceExist);
        profile.doGet(request, response);

        assertEquals(CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testPost() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final HttpSession httpSession = mock(HttpSession.class);

        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest();
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getId()).thenReturn("1");

        String CorrectResponse = "{\"status\":200,\"body\":{}}";

        ProfileServlet profile = new ProfileServlet(accountServiceExist);
        profile.doPost(request, response);

        assertEquals(CorrectResponse, stringWriter.toString());
    }

}