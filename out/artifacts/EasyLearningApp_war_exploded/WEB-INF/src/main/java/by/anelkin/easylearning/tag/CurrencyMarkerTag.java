package by.anelkin.easylearning.tag;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import java.io.IOException;

import static by.anelkin.easylearning.entity.Payment.*;

@Log4j
@Setter
public class CurrencyMarkerTag extends TagSupport {
    private int currencyId;


    @Override
    public int doStartTag() throws JspException {
        String currencyType = CurrencyType.valueList[currencyId - 1].toString();

        JspWriter writer = pageContext.getOut();
        try {
            writer.write(currencyType);
        } catch (IOException e) {
            log.error(e);
            throw new JspTagException(e);
        }
        return SKIP_BODY;
    }
}
