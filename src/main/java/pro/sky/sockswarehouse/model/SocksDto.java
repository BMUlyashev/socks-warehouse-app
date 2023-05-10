package pro.sky.sockswarehouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocksDto {
    @NotEmpty
    private String color;
    @Range(min = 0, max = 100)
    private int cottonPart;
    @Min(value = 1, message = "Quantity should be > 0")
    private int quantity;
}
