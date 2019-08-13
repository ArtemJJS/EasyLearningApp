package by.anelkin.easylearning.tag;

import by.anelkin.easylearning.entity.Course;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static by.anelkin.easylearning.entity.Account.*;
import static by.anelkin.easylearning.util.GlobalConstant.*;

@Log4j
@Setter
public class CourseOptionTag extends TagSupport {
    private static final String AUTHOR_LEARNING_PAGE_LINK = "/course/learn?course-id=";
    private static final String AUTHOR_EDIT_COURSE_LINK = "/author/edit-course?course-id=";
    private static final String AUTHOR_EDIT_IMAGE_LINK = "/account/change-picture?course-id=";
    private Course course;

    @Override
    public int doStartTag() throws JspException {
        Locale locale;
        Object localeAttr = pageContext.getSession().getAttribute(ATTR_LOCALE);
        //to prevent error when logging out on page with tag:
        if (localeAttr == null) {
            locale = Locale.US;
        } else {
            String[] localeParts = localeAttr.toString().split(LOCALE_SPLITTER);
            locale = new Locale(localeParts[0], localeParts[1]);
        }
        String contextPath = pageContext.getServletContext().getContextPath();
        AccountType role = (AccountType) pageContext.getSession().getAttribute("role");
        List<Course> coursesAvailable = (List<Course>) pageContext.getSession().getAttribute("coursesAvailable");
        if (role == null) {
            return SKIP_BODY;
        }
        try {
            switch (role) {
                case AUTHOR:
                    writeAuthorLinks(coursesAvailable, contextPath, locale);
                    break;
                case USER:
                    writeUserLinks(coursesAvailable, contextPath, locale);
                    break;
                case ADMIN:
                    writeAdminLinks(coursesAvailable, contextPath, locale);
                    break;
            }

        } catch (IOException e) {
            log.error(e);
            throw new JspTagException(e);
        }
        return SKIP_BODY;
    }


    private void writeAuthorLinks(List<Course> coursesAvailable, String contextPath, Locale locale) throws IOException {
        ResourceBundle rb = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale);
        String learningPage = rb.getString(BUNDLE_LEARNING_PAGE);
        String editCourse = rb.getString(BUNDLE_EDIT_COURSE);
        String editImage = rb.getString(BUNDLE_EDIT_IMAGE);

        JspWriter writer = pageContext.getOut();
        if (coursesAvailable.contains(course)) {
            writer.write("<div class='links'>");
            writer.write("<a href=" + contextPath + AUTHOR_LEARNING_PAGE_LINK + course.getId() + ">" + learningPage + "</a>");
            writer.write("<a href=" + contextPath + AUTHOR_EDIT_COURSE_LINK + course.getId() + ">" + editCourse + "</a>");
            writer.write("<a href=" + contextPath + AUTHOR_EDIT_IMAGE_LINK + course.getId() + ">" + editImage + "</a>");
            writer.write("</div>");
        }
    }

    private void writeUserLinks(List<Course> coursesAvailable, String contextPath, Locale locale) throws IOException {
        ResourceBundle rb = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale);
        List<Integer> markedCourses = (List<Integer>) pageContext.getSession().getAttribute(ATTR_MARKED_COURSES_IDS);
        String learningPage = rb.getString(BUNDLE_LEARNING_PAGE);
        String buyCourse = rb.getString(BUNDLE_BUY_COURSE);
        String markCourse = rb.getString(BUNDLE_MARK_COURSE);
        JspWriter writer = pageContext.getOut();
        if (coursesAvailable.contains(course)) {
            writer.write("<div class='links'>");
            writer.write("<a href=" + contextPath + USER_LEARNING_PAGE_LINK + course.getId() + ">" + learningPage + "</a>");
            writer.write(markLinkIfNotMarked(markedCourses, contextPath, markCourse));
            writer.write("</div>");
        } else {
            writer.write("<div class='links'>");
            writer.write("<a href=" + contextPath + USER_BUY_COURSE_LINK + course.getId() + ">" + buyCourse + "</a>");
            writer.write("</div>");
        }
    }

    private void writeAdminLinks(List<Course> coursesAvailable, String contextPath, Locale locale) throws IOException {
        ResourceBundle rb = ResourceBundle.getBundle(RESOURCE_BUNDLE_BASE, locale);
        String learningPage = rb.getString(BUNDLE_LEARNING_PAGE);
        String editCourse = rb.getString(BUNDLE_EDIT_COURSE);
        String editImage = rb.getString(BUNDLE_EDIT_IMAGE);

        JspWriter writer = pageContext.getOut();
        writer.write("<div class='links'>");
        writer.write("<a href=" + contextPath + AUTHOR_LEARNING_PAGE_LINK + course.getId() + ">" + learningPage + "</a>");
        writer.write("</div>");
    }


    private String markLinkIfNotMarked(List<Integer> markedCoursesIds, String contextPath, String markCourse) {
        return markedCoursesIds.contains(course.getId()) ? "" :
                "<a href=" + contextPath + USER_MARK_COURSE_LINK + course.getId() + ">" + markCourse + "</a>";
    }

}

