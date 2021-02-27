package telran.logs.bugs.dto;

import java.time.LocalDate;

import javax.validation.constraints.*;

public class CloseBugData {
	@Min(1)
public long bugId;
public LocalDate dateClose;
public String description;

}
