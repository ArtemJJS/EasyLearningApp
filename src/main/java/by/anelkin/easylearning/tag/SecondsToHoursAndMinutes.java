package by.anelkin.easylearning.tag;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

@Log4j
@Setter
public class SecondsToHoursAndMinutes extends TagSupport {
    private static final String HOURS_MARKER = "h ";
    private static final String MINUTES_MARKER = "m ";
    private static final String SECONDS_MARKER = "sec";
    private long secondsAmount;

    @Override
    public int doStartTag() throws JspException {
        long hours = secondsAmount / (60 * 60);
        long minutes = (secondsAmount / 60) % 60;
        long seconds = secondsAmount % 60;

        JspWriter writer = pageContext.getOut();
        try {
            writer.write(hours + HOURS_MARKER + minutes + MINUTES_MARKER + seconds + SECONDS_MARKER);
        } catch (IOException e) {
            log.error(e);
            throw new JspTagException(e);
        }
        return SKIP_BODY;
    }
}
