package tests.frontend;

import base.AccountService;
import frontend.AdminServlet;
import base.dataSets.UserDataSet;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminServletTest {
    private AccountService accountService = mock(AccountService.class);
    private AdminServlet admin = new AdminServlet(accountService);

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
        when(accountService.getSessions("1")).thenReturn(null);

        String CorrectResponse = "{\"status\":401,\"body\":{\"messages\":{\"user\":\"not admin\"}}}";

        admin.doGet(request, response);
        assertEquals(CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testNotAdmin() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final HttpSession httpSession = mock(HttpSession.class);

        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest();
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getId()).thenReturn("2");
        when(accountService.getSessions("2")).thenReturn(new UserDataSet("test", "123", "test@test"));

        String CorrectResponse = "{\"status\":401,\"body\":{\"messages\":{\"user\":\"not admin\"}}}";

        admin.doGet(request, response);

        assertEquals(CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testAdmin() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final HttpSession httpSession = mock(HttpSession.class);

        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest();
        when(request.getSession()).thenReturn(httpSession);
        when(httpSession.getId()).thenReturn("3");
        when(accountService.getSessions("3")).thenReturn(new UserDataSet("admin", "123", "admin@admin"));
        when(accountService.getNumberOfOnlineUsers()).thenReturn("4");
        when(accountService.getNumberOfUsers()).thenReturn("20");

        String CorrectResponse = "{\"status\":200,\"body\":{\"number_of_users\":\"20\",\"online\":\"4\"}}";

        admin.doGet(request, response);

        assertEquals(CorrectResponse, stringWriter.toString());
    }

    @Test
    public void testException() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final HttpSession httpSession = mock(HttpSession.class);

        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest();
        when(request.getSession()).thenReturn(httpSession);
        when(request.getParameter("shutdown")).thenReturn("q");
        when(httpSession.getId()).thenReturn("3");
        when(accountService.getSessions("3")).thenReturn(new UserDataSet("admin", "123", "admin@admin"));

        String CorrectResponse = "{\"status\":400,\"body\":{\"messages\":{\"timer\":\"use numbers\"}}}";

        admin.doGet(request, response);

        assertEquals(CorrectResponse, stringWriter.toString());
    }

// TODO
/*    @Test
    public void testShutdown() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final HttpSession httpSession = mock(HttpSession.class);

        HttpServletResponse response = getMockedResponse(stringWriter);
        HttpServletRequest request = getMockedRequest();
        when(request.getSession()).thenReturn(httpSession);
        when(request.getParameter("shutdown")).thenReturn("1000");
        when(httpSession.getId()).thenReturn("3");
        when(accountService.getSessions("3")).thenReturn(new UserProfile("admin", "123", "admin@admin"));

        admin.doGet(request, response);

    }*/
}