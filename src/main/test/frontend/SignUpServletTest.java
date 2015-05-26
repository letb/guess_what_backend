package frontend;

import accountService.AccountService;
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
import static org.mockito.Mockito.*;

public class SignUpServletTest {

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
    public void testAllFieldsEmpty() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest();
        when(request.getParameter("name")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");
        when(request.getParameter("email")).thenReturn("");

        String CorrectResponse = "{\"status\":\"403\",\"body\":{\"messages\":" +
                "{\"name\":\"shouldn't be empty\",\"email\":\"shouldn't be empty\"," +
                "\"password\":\"shouldn't be empty\"}}}";

        SignUpServlet signUp = new SignUpServlet(accountService);

        signUp.doPost(request, response);

        assertEquals(CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testSuccessfulRegistration() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final String name = "test";
        final String password = "123";
        final String email = "test@test";
        final HttpSession httpSession = mock(HttpSession.class);

        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest();
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("email")).thenReturn(email);
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getId()).thenReturn("1");

        String CorrectResponse = "{\"status\":\"201\",\"body\":{\"id\":-1,\"name\":" +
            "\"test\",\"email\":\"test@test\"}}";

        SignUpServlet signUp = new SignUpServlet(accountService);

        signUp.doPost(request, response);

        assertEquals(CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testUserExist() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final String name = "test";
        final String password = "123";
        final String email = "test@test";

        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest();
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("email")).thenReturn(email);
        String CorrectResponse = "{\"status\":\"401\",\"body\":{\"messages\":{\"user\":\"already exist\"}}}";

        SignUpServlet signUp = new SignUpServlet(accountServiceExist);

        signUp.doPost(request, response);

        assertEquals(CorrectResponse, stringWriter.toString());
    }
}