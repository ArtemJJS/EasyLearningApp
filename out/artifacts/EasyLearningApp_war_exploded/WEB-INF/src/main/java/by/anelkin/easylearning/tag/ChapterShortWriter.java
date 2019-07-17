package by.anelkin.easylearning.tag;

import by.anelkin.easylearning.entity.CourseChapter;
import lombok.Setter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

@Setter
public class ChapterShortWriter extends TagSupport {
    private CourseChapter chapter;

    @Override
    public int doStartTag() throws JspException {

        try {
            // TODO: 7/12/2019 нормально ли столько тегов из jsp убирать? неудобно стили писать потом
            JspWriter writer = pageContext.getOut();
            writer.write("<div class=\"chapter\">");
            writer.write("<div class=\"materials_title\">" + chapter.getName() + "</div>");
            writer.write("<div class=\"lessons_minutes\"><div class='lessons'>" + chapter.getLessonAmount() + " lessons</div><div class='seconds'>" +
                    "<ctg:time-prettier secondsAmount='" + chapter.getDuration() + "'/></div>");
            writer.write("</div></div>");
        } catch (IOException e) {
            throw new JspTagException(e);
        }
        return SKIP_BODY;
    }
}

