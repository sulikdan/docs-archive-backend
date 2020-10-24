package com.sulikdan.ERDMS.dto;

import com.sulikdan.ERDMS.entities.*;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Created by Daniel Šulik on 19-Sep-20
 *
 * <p>Class DocDtoConverterTest is used for .....
 */
@ExtendWith(MockitoExtension.class)
class DocDtoConverterTest {

  DocDtoConverter docDtoConverter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);

    docDtoConverter = new DocDtoConverter();
  }

  @Test
  public void docDtoConvertedToDoc() {
    DocDto toBeConverted = new DocDto();
    toBeConverted.setId("aaa111aaa");
    toBeConverted.setDocConfig(new DocConfig(false, false, "eng", false));
    toBeConverted.setDocType(DocType.IMG);
    toBeConverted.setTags(Arrays.asList(new Tag("Forest"), new Tag("Sun")));
    toBeConverted.setCreateDateTime(LocalDateTime.now());
    toBeConverted.setIsShared(false);
    toBeConverted.setDocState(AsyncApiState.COMPLETED);

    // tested method
    Doc converted = docDtoConverter.convertToEntity(toBeConverted);

    Assert.assertNotNull(converted);
    Assert.assertEquals(toBeConverted.getId(),converted.getId());
    Assert.assertEquals(toBeConverted.getDocType(),converted.getDocType());
    Assert.assertEquals(toBeConverted.getTags(),converted.getTags());
    Assert.assertEquals(toBeConverted.getIsShared(),converted.getIsShared());
    Assert.assertEquals(toBeConverted.getDocConfig(),converted.getDocConfig());
    Assert.assertEquals(toBeConverted.getDocState(),converted.getAsyncApiInfo().getAsyncApiState());
  }

  @Test
  public void docConvertedToDocDto() {
    AsyncApiInfo apiInfo = new AsyncApiInfo();
    apiInfo.setAsyncApiState(AsyncApiState.PROCESSING);
    apiInfo.setOcrApiDocStatus("xxxx");
    apiInfo.setOcrApiDocResult("yyyy");

    Doc toBeConverted = new Doc();
    toBeConverted.setId("aaa111aaa");
    toBeConverted.setDocConfig(new DocConfig(false, false, "eng", false));
    toBeConverted.setDocType(DocType.IMG);
    toBeConverted.setTags(Arrays.asList(new Tag("Forest"), new Tag("Sun")));
    toBeConverted.setCreateDateTime(LocalDateTime.now());
    toBeConverted.setIsShared(false);
    toBeConverted.setAsyncApiInfo(apiInfo);

    // tested method
    DocDto converted = docDtoConverter.convertToDto(toBeConverted);

    Assert.assertNotNull(converted);
    Assert.assertEquals(toBeConverted.getId(),converted.getId());
    Assert.assertEquals(toBeConverted.getDocType(),converted.getDocType());
    Assert.assertEquals(toBeConverted.getTags(),converted.getTags());
    Assert.assertEquals(toBeConverted.getIsShared(),converted.getIsShared());
    Assert.assertEquals(toBeConverted.getDocConfig(),converted.getDocConfig());
    Assert.assertEquals(toBeConverted.getAsyncApiInfo().getAsyncApiState(),converted.getDocState());
  }
}
