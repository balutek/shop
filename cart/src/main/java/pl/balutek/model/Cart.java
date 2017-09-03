package pl.balutek.model;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Builder
@Document
public class Cart {

    @Id
    private String _id;

    private List<Entry> entries;
    private BigDecimal totalPrice;
}
