package openminder.emeal.domain.post;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class NutrientSum {
    private Long carbohydrate;

    private Long protein;

    private Long fat;

    private LocalDate date;

    public NutrientSum() {

    }
}
