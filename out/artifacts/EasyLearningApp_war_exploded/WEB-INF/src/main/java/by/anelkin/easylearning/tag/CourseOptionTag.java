package by.anelkin.easylearning.tag;

import by.anelkin.easylearning.entity.Course;
import lombok.Setter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import java.io.IOException;
import java.util.List;

import static by.anelkin.easylearning.entity.Account.*;

@Setter
public class CourseOptionTag extends TagSupport {
    private static final String ATTR_MARKED_COURSES_IDS = "marked_courses_ids";

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
    private static final String USER_MARK_COURSE = "Mark course";
    private static final String USER_MARK_COURSE_LINK = "/user/mark-course?course-id=";


    private Course course;

    @Override
    public int doStartTag() throws JspException {
        String contextPath = pageContext.getServletContext().getContextPath();
        AccountType role = (AccountType) pageContext.getSession().getAttribute("role");
        List<Integer> markedCourses = (List<Integer>) pageContext.getSession().getAttribute(ATTR_MARKED_COURSES_IDS);
        List<Course> coursesAvailable = (List<Course>) pageContext.getSession().getAttribute("coursesAvailable");
        JspWriter writer = pageContext.getOut();
        if (role == null) {
            return SKIP_BODY;
        }
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
                        writer.write(markLinkIfNotMarked(markedCourses, contextPath));
                        writer.write("</div>");
                    } else {
                        writer.write("<div class='links'>");
                        writer.write("<a href=" + contextPath + USER_BUY_COURSE_LINK + course.getId() + ">" + USER_BUY_COURSE + "</a>");
                        writer.write("</div>");
                    }
            }

        } catch (IOException e) {
            throw new JspTagException(e);
        }
        return SKIP_BODY;
    }

    private String markLinkIfNotMarked(List<Integer> markedCoursesIds, String contextPath) {
        return markedCoursesIds.contains(course.getId()) ? "" :
                "<a href=" + contextPath + USER_MARK_COURSE_LINK + course.getId() + ">" + USER_MARK_COURSE + "</a>";
    }
}

