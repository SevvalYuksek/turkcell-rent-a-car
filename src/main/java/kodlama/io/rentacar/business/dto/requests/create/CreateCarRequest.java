package kodlama.io.rentacar.business.dto.requests.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarRequest {
    private  int modelId;
    private int modelYear;
    private String plate;
    private int state; // 1 - Available, 2- Rented, 3- Maintance
    private double dailyPrice;
}
