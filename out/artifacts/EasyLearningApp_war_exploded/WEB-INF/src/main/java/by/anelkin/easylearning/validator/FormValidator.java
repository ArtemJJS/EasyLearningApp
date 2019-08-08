package by.anelkin.easylearning.validator;

import lombok.extern.log4j.Log4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Log4j
public class FormValidator {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final long FIVE_YEARS = 1000 * 60 * 60 * 24 * 365 * 5;
    private static final String LOGIN_PATTERN = "^[A-Za-zА-я0-9_ -]{3,30}$";
    private static final String PASSWORD_PATTERN = "^.{5,50}$";
    private static final String NAME_PATTERN = "^[A-Za-zА-я]{2,30}$";
    private static final String SURNAME_PATTERN = "^[A-Za-zА-я]{2,30}$";
    private static final String EMAIL_PATTERN = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
    private static final String PHONE_PATTERN = "^\\+[0-9]{2,19}$";
    private static final String PRICE_PATTERN = "[0-9]{1,8}(\\.[0-9]{1,2})?";
    private static final String CARD_PATTERN = "[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}";
    private static final String COURSE_NAME_PATTERN = "[A-Za-zА-я0-9_ ,.:!?-]{5,200}";
    private static final String CHAPTER_NAME_PATTERN = "[A-Za-zА-я0-9_ ,.:!?-]{5,200}";
    private static final String LESSON_NAME_PATTERN = "[A-Za-zА-я0-9_ ,.:!?-]{5,200}";
    private static final int COURSE_DESCRIPTION_LENGTH = 1000;
    private static final int MAX_LESSON_DURATION = 999_999;
    private static final int ACC_ABOUT_MAX_LENGTH = 500;
    public static final int MARK_COMMENT_MAX_LENGTH = 700;

    public boolean validateLogin(String test) {
        return (test.matches(LOGIN_PATTERN));
    }

    public boolean validatePassword(String test) {
        return test.matches(PASSWORD_PATTERN);
    }

    public boolean validateName(String test) {
        return test.matches(NAME_PATTERN);
    }

    public boolean validateSurName(String test) {
        return test.matches(SURNAME_PATTERN);
    }

    public boolean validateEmail(String test) {
        return test.matches(EMAIL_PATTERN);
    }


    public boolean validateBirthDate(String test) {
        try {
            Date date = dateFormat.parse(test);
            return (System.currentTimeMillis() - date.getTime()) > FIVE_YEARS;
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean validatePhone(String test) {
        return test.matches(PHONE_PATTERN);
    }

    public boolean validateAccAboutLength(String test) {
        return test.length() <= ACC_ABOUT_MAX_LENGTH;
    }


    public boolean validateMarkCommentLength(String test) {
        return test.length() <= MARK_COMMENT_MAX_LENGTH;
    }

    public boolean validatePrice(String test) {
        return test.matches(PRICE_PATTERN);
    }

    public boolean validateCard(String test) {
        return test.matches(CARD_PATTERN);
    }

    public boolean validateCourseName(String test) {
        return test.matches(COURSE_NAME_PATTERN);
    }

    public boolean validateCourseDescription(String test) {
        return test.length() <= COURSE_DESCRIPTION_LENGTH;
    }

    public boolean validateChapterName(String test) {
        return test.matches(CHAPTER_NAME_PATTERN);
    }

    public boolean validateLessonName(String test) {
        return test.matches(LESSON_NAME_PATTERN);
    }

    public boolean validateLessonDuration(String test) {
        try {
            int duration = Integer.parseInt(test);
            return duration <= MAX_LESSON_DURATION && duration > 0;
        } catch (NumberFormatException e) {
            return false;
        }

    }


}