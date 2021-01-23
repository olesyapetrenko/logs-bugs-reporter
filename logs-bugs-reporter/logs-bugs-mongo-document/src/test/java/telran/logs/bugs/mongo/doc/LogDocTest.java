package telran.logs.bugs.mongo.doc;

import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import telran.logs.bugs.dto.LogDto;
import telran.logs.bugs.dto.LogType;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes=LogsRepo.class)
@EnableAutoConfiguration
public class LogDocTest {
	@Autowired
	LogsRepo logs;
	@Test
	void docStoreTest() {
		LogDto logDto = new LogDto(new Date(), LogType.NO_EXCEPTION, "artifact", 20, "result");
		LogDoc doc = logs.save(new LogDoc(logDto));
		LogDoc actualDoc = logs.findAll().get(0);
		assertEquals(logDto, actualDoc.getLogDto());
		
	}

}
