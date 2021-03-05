package telran.logs.bugs.dto;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.*;

public class BugResponseDto extends BugAssignDto {
	public long bugId;
	public LocalDate dateClose;
	public BugStatus status;
	public OpeningMethod openingMethod;

	public BugResponseDto(long bugId, @NotNull Seriousness seriousness, @NotEmpty String description,
			LocalDate dateOpen, @Min(1) long programmerId, LocalDate dateClose, BugStatus status,
			OpeningMethod openingMethod) {
		super(seriousness, description, dateOpen, programmerId);
		this.dateClose = dateClose;
		this.status = status;
		this.openingMethod = openingMethod;
		this.bugId = bugId;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(bugId, dateClose, openingMethod, status);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BugResponseDto other = (BugResponseDto) obj;
		return bugId == other.bugId && Objects.equals(dateClose, other.dateClose)
				&& openingMethod == other.openingMethod && status == other.status;
	}



}
