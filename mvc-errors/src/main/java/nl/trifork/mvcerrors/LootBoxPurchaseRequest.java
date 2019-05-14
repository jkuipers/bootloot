package nl.trifork.mvcerrors;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class LootBoxPurchaseRequest {

    @NotNull @Pattern(regexp = "\\d{8}")
    private String productEan;

    @NotNull @Positive @DecimalMax("25.00")  // pesky Dutch gambling rules...
    private BigDecimal eurosToSpend;

    public String getProductEan() {
        return productEan;
    }

    public void setProductEan(String productEan) {
        this.productEan = productEan;
    }

    public BigDecimal getEurosToSpend() {
        return eurosToSpend;
    }

    public void setEurosToSpend(BigDecimal eurosToSpend) {
        this.eurosToSpend = eurosToSpend;
    }
}
