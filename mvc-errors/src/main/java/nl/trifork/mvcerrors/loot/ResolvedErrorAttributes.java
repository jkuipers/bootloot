package nl.trifork.mvcerrors.loot;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.MessageSource;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Overrides {@link DefaultErrorAttributes#getErrorAttributes(WebRequest, boolean)} by mapping its list of
 * {@code ObjectErrors} to just a list of error messages, to avoid exposing too much detail about binding and
 * validation errors to clients.
 */
//@Component
public class ResolvedErrorAttributes extends DefaultErrorAttributes {

    private MessageSource messageSource;

    public ResolvedErrorAttributes(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
        resolveBindingErrors(errorAttributes);
        return errorAttributes;
    }

    private void resolveBindingErrors(Map<String, Object> errorAttributes) {
        List<ObjectError> errors = (List<ObjectError>) errorAttributes.get("errors");
        if (errors == null) return;

        List<String> errorMessages = new ArrayList<>();
        for (ObjectError error : errors) {
            String resolved = messageSource.getMessage(error,  Locale.US);
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                errorMessages.add(fieldError.getField() + " " + resolved + " but value was " + fieldError.getRejectedValue());
            } else {
                errorMessages.add(resolved);
            }
        }
        errorAttributes.put("errors", errorMessages);
    }
}
