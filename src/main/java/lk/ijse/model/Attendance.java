package lk.ijse.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Attendance {
    private String attendanceId;
    private String employeeId;
    private Date date;
    private String inTime;
    private String offTime;
}
