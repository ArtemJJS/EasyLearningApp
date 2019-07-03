package by.anelkin.easylearning.servlet;

import by.anelkin.easylearning.command.receiver.RequestReceiver;
import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.exception.RepositoryException;
import lombok.extern.log4j.Log4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static by.anelkin.easylearning.command.factory.CommandFactory.*;
import static by.anelkin.easylearning.entity.Account.AccountType.*;

@Log4j
@WebServlet(name = "BasicServlet", urlPatterns = "/basic_servlet")
public class BasicServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(request.getRequestURL());

       if (request.getSession().getAttribute("user") == null){
           Account guest = new Account();
           guest.setType(GUEST);
           request.getSession().setAttribute("user", guest);
       }
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
