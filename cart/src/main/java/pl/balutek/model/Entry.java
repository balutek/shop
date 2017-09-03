package pl.balutek.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Entry {
    private String productId;
    private String productName;
    private BigDecimal quantity;
    private BigDecimal totalPrice;
}
