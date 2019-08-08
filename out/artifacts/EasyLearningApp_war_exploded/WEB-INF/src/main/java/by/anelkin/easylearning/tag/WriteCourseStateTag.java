package by.anelkin.easylearning.tag;


import lombok.Setter;
import lombok.extern.log4j.Log4j;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import java.io.IOException;

import static by.anelkin.easylearning.entity.Course.*;

@Log4j
@Setter
public class WriteCourseStateTag extends TagSupport {
    private static final String FROZEN_COLOR = "blue";
    private static final String WAIT_APPROVAL_COLOR = "#B1009B";
    private static final String APPROVED_COLOR = "darkgreen";

    private CourseState state;

    @Override
    public int doStartTag() throws JspException {
        String currColor;
        switch (state) {
            case FREEZING:
                currColor = FROZEN_COLOR;
                break;
            case NOT_APPROVED:
                currColor = WAIT_APPROVAL_COLOR;
                break;
            case APPROVED:
                currColor = APPROVED_COLOR;
                break;
            default:
                log.warn("Custom tag couldn't choose color according to course state: " + state);
                return SKIP_BODY;
        }

        JspWriter writer = pageContext.getOut();

        try {
            writer.write("<span style='color : " + currColor + "'>" + state.toString() + "</span>");
        } catch (IOException e) {
            log.error(e);
            throw new JspTagException(e);
        }
        return SKIP_BODY;
    }
}
