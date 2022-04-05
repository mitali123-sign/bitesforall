package openminder.emeal.domain.post;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class Nutrient {

    private Long nutrientId;

    private NutrientType nutrientType;

    private Long calorie;

    private Long carbohydrate;

    private Long protein;

    private Long fat;

    private Long sugars;

    private Long sodium;

    private Long cholesterol;

    private Long fattyAcid;

    private Long transFat;

    /** Nutrient : Account = 1 : 1 */
    private String username;

    /** Nutrient : Menu = 1 : 1 */
    private Long menuId;

    private LocalDateTime insertTime;

    public Nutrient() {

    }

    public Nutrient(NutrientType nutrientType, Long calorie, Long carbohydrate, Long protein,
                    Long fat, Long sugars, Long sodium, Long cholesterol, Long fattyAcid,
                    Long transFat, Long menuId, String username) {

        this.nutrientType = nutrientType;
        this.calorie = calorie;
        this.carbohydrate = carbohydrate;
        this.protein = protein;
        this.fat = fat;
        this.sugars = sugars;
        this.sodium = sodium;
        this.cholesterol = cholesterol;
        this.fattyAcid = fattyAcid;
        this.transFat = transFat;
        this.menuId = menuId;
        this.username = username;
    }
}
