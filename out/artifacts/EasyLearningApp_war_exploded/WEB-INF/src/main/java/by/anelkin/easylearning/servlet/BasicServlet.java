package by.anelkin.easylearning.servlet;

import by.anelkin.easylearning.command.CommandFactory;
import by.anelkin.easylearning.command.RequestReceiver;
import by.anelkin.easylearning.connection.ConnectionPool;
import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.exeption.RepositoryException;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.specification.account.SelectAccByLoginSpecification;
import lombok.extern.log4j.Log4j;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static by.anelkin.easylearning.command.CommandFactory.*;

@Log4j
public class BasicServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CommandType commandType = CommandType.valueOf(request.getParameter("command_name").toUpperCase());
        RequestReceiver receiver = new RequestReceiver(commandType, request);
        String redirectPath;
        try {
            redirectPath = receiver.executeCommand();
        } catch (RepositoryException e) {
            // TODO: 7/1/2019 редирект на страницу ошибки
            throw new RuntimeException(e);
        }
        log.debug("Sending redirect: " + redirectPath);
        response.sendRedirect(redirectPath);


        //        System.out.println(request.getContextPath());
//        String login = request.getParameter("login");
//        String password = request.getParameter("password");
//        log.debug("Attempt to enter in account. Login: " + login + ", password: " + password);
//        if (!checkRegistration(login, password)) {
//            request.setAttribute("wrong_login_message", "Username or Password is incorrect! Try again, please!");
//            RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/start_page.jsp");
//            dispatcher.forward(request, response);
//        } else {
//            response.sendRedirect("http://localhost:8080/easyLearning/account?login=" + login);
//        }
    }

//    @Override
//    public void destroy() {
//        ConnectionPool.getInstance().closePool();
//        super.destroy();
//    }
}
