package openminder.emeal.domain.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Attendance {

    private Long attendanceId;

    private String username;

    private String yearAndMonth;

    private int day;

}
