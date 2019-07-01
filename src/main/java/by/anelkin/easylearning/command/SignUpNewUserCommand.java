package by.anelkin.easylearning.command;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.exeption.RepositoryException;
import by.anelkin.easylearning.repository.AccRepository;
import by.anelkin.easylearning.specification.account.SelectAccByLoginSpecification;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpNewUserCommand implements Command {
    private static final String REDIRECT_PATH = "http://localhost:8080/easyLearning/account";
    private static final String WRONG_LOGIN_REDIRECT_PATH = "http://localhost:8080/easyLearning/sign-up";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String execute(HttpServletRequest request) throws RepositoryException {
        AccRepository repository = new AccRepository();
        String login = request.getParameter("login");
        if (repository.query(new SelectAccByLoginSpecification(login)).size() != 0) {
            return WRONG_LOGIN_REDIRECT_PATH;
        }
        Account account = new Account();
        initAccount(account, request);
        HttpSession session = request.getSession();
        session.setAttribute("user", account);
        boolean isInserted = repository.insert(account);
        System.out.println(isInserted);
        System.out.println(account);
        return REDIRECT_PATH;
    }

    private void initAccount(Account account, HttpServletRequest request) {
        account.setLogin(request.getParameter("login"));
        account.setPassword(request.getParameter("password"));
        account.setName(request.getParameter("name"));
        account.setSurname(request.getParameter("surname"));
        account.setEmail(request.getParameter("email"));
        account.setPhoneNumber(request.getParameter("phonenumber"));
        account.setAbout(request.getParameter("about"));
        try {
            account.setBirthDate(dateFormat.parse(request.getParameter("birthdate")));
            account.setRegistrDate(new Date(System.currentTimeMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        account.setType(Account.AccountType.valueOf(request.getParameter("role").toUpperCase()));
    }
}
