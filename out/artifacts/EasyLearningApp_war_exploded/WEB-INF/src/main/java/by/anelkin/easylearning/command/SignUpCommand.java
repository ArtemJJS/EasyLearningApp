package by.anelkin.easylearning.command;

import javax.servlet.http.HttpServletRequest;

public class SignUpCommand implements Command {
    private static final String REDIRECT_PATH = "http://localhost:8080/easyLearning/sign-up";

    @Override
    public String execute(HttpServletRequest request) {
        return REDIRECT_PATH;
    }
}
