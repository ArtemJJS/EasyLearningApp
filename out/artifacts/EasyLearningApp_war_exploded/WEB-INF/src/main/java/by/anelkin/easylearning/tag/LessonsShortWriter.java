package by.anelkin.easylearning.tag;

import by.anelkin.easylearning.entity.CourseLesson;
import lombok.Setter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

@Setter
public class LessonsShortWriter extends TagSupport {
    private List<CourseLesson> lessons;

    @Override
    public int doStartTag() throws JspException {
        JspWriter writer = pageContext.getOut();
        try {
            for (CourseLesson lesson : lessons) {
                writer.write(" <div class='lesson'>" + lesson.getName() + "</div>");
            }
        } catch (IOException e) {
           throw new JspTagException(e);
        }
        return SKIP_BODY;
    }
}
