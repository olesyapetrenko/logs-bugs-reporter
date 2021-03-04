package telran.logs.bugs;

import java.time.LocalDate;
import java.util.*;

import javax.validation.constraints.*;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import telran.logs.bugs.dto.*;
import telran.logs.bugs.interfaces.BugsReporter;
import telran.logs.bugs.jpa.entities.Programmer;

import static telran.logs.bugs.api.BugsReporterApi.*;


@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureDataJpa
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BugsReporterTests {
	static class EmailBugCountTest implements EmailBugsCount {
		public EmailBugCountTest(String email, long count) {
					super();
					this.email = email;
					this.count = count;
				}

		@Override
				public int hashCode() {
					return Objects.hash(count, email);
				}

				@Override
				public boolean equals(Object obj) {
					if (this == obj)
						return true;
					if (obj == null)
						return false;
					if (getClass() != obj.getClass())
						return false;
					EmailBugCountTest other = (EmailBugCountTest) obj;
					return count == other.count && Objects.equals(email, other.email);
				}

		String email;
		long count;
		public EmailBugCountTest() {
		}

				@Override
				public String getEmail() {
					return email;
				}

				@Override
				public long getCount() {
					
					return count;
				}
				
			}
	private static final @NotEmpty String DESCRIPTION = "Not working";
	private static final LocalDate DATE_OPEN = LocalDate.of(2020,12,1);
	private static final @Min(1) long PROGRAMMER_ID_VALUE = 123;
	private static final @Email String EMAIL ="moshe@gmail.com";
	private static final @Email String VASYA_EMAIL = "vasya@gmail.com";
	BugDto bugUnAssigned = new BugDto(Seriousness.BLOCKING, DESCRIPTION,
			DATE_OPEN);
	BugAssignDto bugAssigned2 = new BugAssignDto(Seriousness.BLOCKING, DESCRIPTION, DATE_OPEN, PROGRAMMER_ID_VALUE);
	BugAssignDto bugAssigned3 = new BugAssignDto(Seriousness.BLOCKING, DESCRIPTION, DATE_OPEN, PROGRAMMER_ID_VALUE);
	BugResponseDto expectedUnAssigned = new BugResponseDto(1, Seriousness.BLOCKING, DESCRIPTION,
			DATE_OPEN, 0, null, BugStatus.OPENNED, OpeningMethod.MANUAL);
	BugResponseDto expectedAssigned2 = new BugResponseDto(2, Seriousness.BLOCKING, DESCRIPTION,
			DATE_OPEN, PROGRAMMER_ID_VALUE, null, BugStatus.ASSIGNED, OpeningMethod.MANUAL);
	BugResponseDto expectedAssigned3 = new BugResponseDto(3, Seriousness.BLOCKING, DESCRIPTION,
			DATE_OPEN, PROGRAMMER_ID_VALUE, null, BugStatus.ASSIGNED, OpeningMethod.MANUAL);
	BugResponseDto expectedAssigned1 = new BugResponseDto(1, Seriousness.BLOCKING, DESCRIPTION
			+ BugsReporter.ASSIGNMENT_DESCRIPTION_TITLE,
			DATE_OPEN, PROGRAMMER_ID_VALUE, null, BugStatus.ASSIGNED, OpeningMethod.MANUAL);
	List<BugResponseDto> expectedBugs123 = Arrays.asList(expectedAssigned1,
			expectedAssigned2, expectedAssigned3);
	List<EmailBugCountTest> expectedEmailCounts = Arrays.asList(new EmailBugCountTest(EMAIL, 3),
			new EmailBugCountTest(VASYA_EMAIL, 0));
	@Autowired
WebTestClient testClient;
	@Test
	@Order(1)
	void addProgrammers() {
		ProgrammerDto programmer = new ProgrammerDto(PROGRAMMER_ID_VALUE,"Moshe", EMAIL);
		addProgrammerRequest(programmer);
		programmer = new ProgrammerDto(PROGRAMMER_ID_VALUE + 1, "Vasya", VASYA_EMAIL);
		addProgrammerRequest(programmer);
	}

	private void addProgrammerRequest(ProgrammerDto programmer) {
		testClient.post().uri(BUGS_PROGRAMMERS)
		.contentType(MediaType.APPLICATION_JSON).bodyValue(programmer)
		.exchange().expectStatus().isOk().expectBody(ProgrammerDto.class);
	}
	
	@Test
	@Order(2)
	void openBug() {
		testClient.post().uri(BUGS_OPEN)
		.contentType(MediaType.APPLICATION_JSON).bodyValue(bugUnAssigned).exchange().expectStatus().isOk()
		.expectBody(BugResponseDto.class).isEqualTo(expectedUnAssigned);
	}
	@Test
	@Order(3) 
	void openAndAssign() {
		
		openAssignRequest(bugAssigned2, expectedAssigned2);
		openAssignRequest(bugAssigned3, expectedAssigned3);
	}

	private void openAssignRequest(BugAssignDto bugAssignDto, BugResponseDto bugResponseDto) {
		testClient.post().uri(BUGS_OPEN_ASSIGN).bodyValue(bugAssignDto).exchange().expectStatus()
		.isOk().expectBody(BugResponseDto.class).isEqualTo(bugResponseDto);
	}
	@Test
	@Order(4)
	void assign() {
		testClient.put().uri(BUGS_ASSIGN).bodyValue(new AssignBugData(1, PROGRAMMER_ID_VALUE, ""))
		.exchange().expectStatus().isOk();
		
	}
	@Test
	@Order(5)
	void bugsProgrammers() {
		testClient.get().uri(BUGS_PROGRAMMERS + "?" + PROGRAMMER_ID + "=" + PROGRAMMER_ID_VALUE).exchange().expectStatus().isOk()
		.expectBodyList(BugResponseDto.class).isEqualTo(expectedBugs123);
	}
	@Test
	void bugsProgrammersNoProgrammerID() {
		testClient.get().uri(BUGS_PROGRAMMERS + "?" + PROGRAMMER_ID + "=" + 10000).exchange().expectStatus().isOk()
		.expectBodyList(BugResponseDto.class).isEqualTo(new LinkedList<>());
	}
	@Test
	void invalidOpenBug() {
		invalidPostRequest(BUGS_OPEN, new BugDto(Seriousness.BLOCKING, null, LocalDate.now()));
	}
	@Test
	void invalidAddProgrammer() {
		invalidPostRequest(BUGS_PROGRAMMERS, new Programmer(1, "Moshe", "kuku"));
	}
	@Test
	void invalidOpenAssignBug() {
		invalidPostRequest(BUGS_OPEN_ASSIGN, new BugAssignDto(Seriousness.BLOCKING,
				DESCRIPTION, DATE_OPEN, -20));
	}
	@Test
	void invalidAssignBug() {
		invalidPutRequest(BUGS_ASSIGN, new AssignBugData(0, PROGRAMMER_ID_VALUE, DESCRIPTION));
	}
	

	private void invalidPostRequest(String uriStr, Object invalidObject) {
		testClient.post().uri(uriStr).contentType(MediaType.APPLICATION_JSON).bodyValue(invalidObject)
		.exchange().expectStatus().isBadRequest();
	}
	private void invalidPutRequest(String uriStr, Object invalidObject) {
		testClient.put().uri(uriStr).contentType(MediaType.APPLICATION_JSON).bodyValue(invalidObject)
		.exchange().expectStatus().isBadRequest();
	}
	
	@Test
	void emailCounts() {
		testClient.get().uri(BUGS_PROGRAMMERS_COUNT).exchange().expectStatus().isOk()
		.expectBodyList(EmailBugCountTest.class).isEqualTo(expectedEmailCounts);
	}

}
