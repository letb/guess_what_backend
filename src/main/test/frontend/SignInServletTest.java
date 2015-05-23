package frontend;

import base.AccountService;
import base.dataSets.UserDataSet;
import org.junit.Test;
import classesForTests.AccountServiceStub;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//TODO в стабе менять id пользователей
public class SignInServletTest {
    AccountService accountService = mock(AccountService.class);
    AccountService accountServiceStub = new AccountServiceStub();


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
    public void testWrongLogin() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest();
        when(request.getParameter("name")).thenReturn("test");
        when(request.getParameter("password")).thenReturn("123");
        when(accountService.getUser("test")).thenReturn(null);

        String CorrectResponse = "{\"status\":\"401\",\"body\":" +
                "{\"messages\":{\"user\":\"wrong login or password\"}}}";

        SignInServlet signIn = new SignInServlet(accountService);
        signIn.doPost(request, response);

        assertEquals(CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testWrongPassword() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final String name = "test";
        final String password = "123";
        final String email = "test@test";

        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest();
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("password")).thenReturn(password);
        when(accountService.getUser("test")).thenReturn(new UserDataSet(name, "456", email));
        String CorrectResponse = "{\"status\":\"401\",\"body\":" +
                "{\"messages\":{\"user\":\"wrong login or password\"}}}";

        SignInServlet signIn = new SignInServlet(accountService);
        signIn.doPost(request, response);

        assertEquals(CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testAuthorization() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final String name = "test";
        final String password = "123";
        final HttpSession httpSession = mock(HttpSession.class);

        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest();
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getId()).thenReturn("1");
        String CorrectResponse = "{\"status\":\"200\",\"body\":" +
                "{\"id\":-1,\"name\":\"test\",\"email\":\"test@test\"}}";

        SignInServlet signIn = new SignInServlet(accountServiceStub);
        signIn.doPost(request, response);

        assertEquals(CorrectResponse, stringWriter.toString());
    }
}