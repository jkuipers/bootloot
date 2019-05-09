package nl.trifork.mvcerrors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ValidatedRequest {
    @NotNull
    private String foo;

    @Pattern(regexp = "\\w+")
    private String bar;

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }
}
