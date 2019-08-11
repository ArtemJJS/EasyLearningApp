package by.anelkin.easylearning.servlet;

import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.exception.ServiceException;
import by.anelkin.easylearning.filter.JspAccessFilter;
import by.anelkin.easylearning.receiver.RequestReceiver;
import by.anelkin.easylearning.receiver.SessionRequestContent;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static by.anelkin.easylearning.command.CommandFactory.*;
import static by.anelkin.easylearning.entity.Account.AccountType.*;
import static by.anelkin.easylearning.receiver.SessionRequestContent.*;
import static by.anelkin.easylearning.receiver.SessionRequestContent.ResponseType.*;
import static by.anelkin.easylearning.util.GlobalConstant.*;

@Log4j
@WebServlet(name = "BasicServlet", urlPatterns = {"/basic_servlet", "/search", "/change-lang"
        , "/admin/*", "/user/deposit", "/author/cash-out", "/author/add-course", "/account/change-pass", "/restore-password"
        , "/change-forgotten-password", "/edit_comment", "/edit_author_comment"})
public class BasicServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public void destroy() {
        ConnectionPool.getInstance().closePool();
    }


    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute(ATTR_ROLE) == null) {
            session.setAttribute(ATTR_ROLE, GUEST);
            session.setAttribute(ATTR_LOCALE, Locale.US);
        }
        String[] localeParts = session.getAttribute(ATTR_LOCALE).toString().split(LOCALE_SPLITTER);
        Locale locale = new Locale(localeParts[0], localeParts[1]);
        ResourceBundle rb = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale);
        CommandType commandType = null;
        try {
            commandType = CommandType.valueOf(request.getParameter(ATTR_COMMAND_NAME).toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error(e);
            request.setAttribute(ATTR_MESSAGE, rb.getString(BUNDLE_ETERNAL_SERVER_ERROR));
            response.sendError(ERROR_500);
            return;
        } catch (NullPointerException e) {
            log.error(e);
            response.sendError(ERROR_404);
            return;
        }

        Account.AccountType accType = (Account.AccountType) session.getAttribute(ATTR_ROLE);
        if (!commandType.getAccessTypes().contains(accType)) {
            log.warn("Access denied to command: " + commandType);
            request.setAttribute(ATTR_MESSAGE, rb.getString(BUNDLE_ACCESS_DENIED));
            response.sendError(ERROR_403, rb.getString(BUNDLE_ACCESS_DENIED));
            return;
        }
        log.debug("Server received command: " + commandType);
        SessionRequestContent requestContent = new SessionRequestContent();
        requestContent.extractValues(request);
        RequestReceiver receiver = new RequestReceiver(commandType, requestContent);
        ResponseType responseType;
        try {
            responseType = receiver.executeCommand();
        } catch (ServiceException e) {
            log.error("Command: " + commandType + ". " + e);
            request.setAttribute(ATTR_MESSAGE, rb.getString(BUNDLE_ETERNAL_SERVER_ERROR));
            response.sendError(ERROR_500);
            return;
        }

        requestContent.insertAttributes(request);
        String path = requestContent.getPath();
        if (responseType == FORWARD) {
            request.setAttribute(JspAccessFilter.ATTR_JSP_PERMITTED, true);
            request.getRequestDispatcher(path).forward(request, response);
        } else {
            String url = request.getContextPath() + path;
            response.sendRedirect(url);
        }
    }
}


