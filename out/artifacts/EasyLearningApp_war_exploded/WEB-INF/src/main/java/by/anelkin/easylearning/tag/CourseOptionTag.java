package by.anelkin.easylearning.tag;

import by.anelkin.easylearning.entity.Account;
import by.anelkin.easylearning.entity.Course;
import lombok.Setter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import java.io.IOException;
import java.util.List;

import static by.anelkin.easylearning.entity.Account.*;

@Setter
public class CourseOptionTag extends TagSupport {
    private static final String AUTHOR_LEARNING_PAGE = "Learning page";
    private static final String AUTHOR_LEARNING_PAGE_LINK = "/course/learn?course-id=";
    private static final String AUTHOR_EDIT_COURSE = "Edit course";
    private static final String AUTHOR_EDIT_COURSE_LINK = "/author/edit-course?course-id=";
    private static final String AUTHOR_EDIT_IMAGE = "Edit image";
    private static final String AUTHOR_EDIT_IMAGE_LINK = "/account/change-picture?course-id=";

    private static final String USER_LEARNING_PAGE = "Learning page";
    private static final String USER_LEARNING_PAGE_LINK = "/course/learn?course-id=";
    private static final String USER_BUY_COURSE = "Buy course";
    private static final String USER_BUY_COURSE_LINK = "/user/buy-course?course-id=";


    private AccountType role;
    private Course course;
    private String contextPath;
    private List<Course> coursesAvailable;

    @Override
    public int doStartTag() throws JspException {
        JspWriter writer = pageContext.getOut();
        try {
            switch (role) {
                case AUTHOR:
                    if (coursesAvailable.contains(course)) {
                        writer.write("<div class='links'>");
                        writer.write("<a href=" + contextPath + AUTHOR_LEARNING_PAGE_LINK + course.getId() + ">" + AUTHOR_LEARNING_PAGE + "</a>");
                        writer.write("<a href=" + contextPath + AUTHOR_EDIT_COURSE_LINK + course.getId() + ">" + AUTHOR_EDIT_COURSE + "</a>");
                        writer.write("<a href=" + contextPath + AUTHOR_EDIT_IMAGE_LINK + course.getId() + ">" + AUTHOR_EDIT_IMAGE + "</a>");
                        writer.write("</div>");
                    }
                    break;
                case USER:
                    if (coursesAvailable.contains(course)) {
                        writer.write("<div class='links'>");
                        writer.write("<a href=" + contextPath + USER_LEARNING_PAGE_LINK + course.getId() + ">" + USER_LEARNING_PAGE + "</a>");
                        writer.write("</div>");
                    } else {
                        writer.write("<div class='links'>");
                        writer.write("<a href=" + contextPath + USER_BUY_COURSE_LINK + course.getId() + ">" + USER_BUY_COURSE + "</a>");
                        writer.write("</div>");
                    }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }
}

