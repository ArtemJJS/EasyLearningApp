package by.anelkin.easylearning.tag;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Setter
public class MillisecToDateTag extends TagSupport {
    private long millisecAmount;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    @Override
    public int doStartTag() throws JspException {
        String prettyTime = DATE_FORMAT.format(new Date(millisecAmount));

        JspWriter writer = pageContext.getOut();
        try {
            writer.write(prettyTime);
        } catch (IOException e) {
            throw new JspTagException(e);
        }
        return SKIP_BODY;
    }
}
