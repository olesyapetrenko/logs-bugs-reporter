 package telran.logs.bugs.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import javax.validation.Valid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WebMvcTest(LogDtoTest.TestController.class) // what classes are tested
@ContextConfiguration(classes = LogDtoTest.TestController.class) // what classes will be in ApplContext
public class LogDtoTest {
	public static @RestController class TestController {
	static LogDto logDtoExp = new LogDto(new Date(), LogType.NO_EXCEPTION, "artifact", 0, "");

		@PostMapping("/")
		void testPost(@RequestBody @Valid LogDto logDto) {
			assertEquals(logDtoExp, logDto);
		}
	}

	ObjectMapper mapper = new ObjectMapper();
	@Autowired
	MockMvc mock;
	
	@BeforeEach
	public void setup() {
		TestController.logDtoExp.dateTime = new Date();
		TestController.logDtoExp.logType = LogType.NO_EXCEPTION;
		TestController.logDtoExp.artifact = "artifact";
		TestController.logDtoExp.responseTime = 0;
		TestController.logDtoExp.result = "";
	}

	@Test
	void testPostRun() throws JsonProcessingException, Exception {
//	assertEquals(200, mock.perform(post("/")
//			.contentType(MediaType.APPLICATION_JSON)
//			.content(mapper.writeValueAsString(TestController.logDtoExp)))
//			.andReturn()
//			.getResponse().getStatus());
		
		testPostError(200);	
	}

	@Test
	void wrongDateTimeParam() throws JsonProcessingException, Exception {
		TestController.logDtoExp.dateTime = null;
		testPostError(400);
	}
	
	@Test
	void wrongLogTypeParam() throws JsonProcessingException, Exception {
		TestController.logDtoExp.logType =null;
		testPostError(400);
	}
	@Test
	void wrongArtifactParam() throws JsonProcessingException, Exception {
		TestController.logDtoExp.artifact = "";
		testPostError(400);
		
	}
	
	void testPostError(int errorCode) throws JsonProcessingException, Exception{
		mock.perform(post("/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(TestController.logDtoExp))
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is(errorCode));
	}

}
