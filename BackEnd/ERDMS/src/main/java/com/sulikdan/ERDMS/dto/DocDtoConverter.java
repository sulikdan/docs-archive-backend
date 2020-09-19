package com.sulikdan.ERDMS.dto;

import com.sulikdan.ERDMS.entities.AsyncApiInfo;
import com.sulikdan.ERDMS.entities.Doc;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Created by Daniel Šulik on 19-Sep-20
 *
 * <p>Class DocDtoConverter is used for .....
 */
@Component
public class DocDtoConverter {

  ModelMapper modelMapper;

  public DocDtoConverter(ModelMapper modelMapper) {
    this.modelMapper = new ModelMapper();
  }

  public DocDto convertToDto(Doc toConvert) {
    DocDto docDto = modelMapper.map(toConvert, DocDto.class);
    docDto.setDocState(toConvert.getAsyncApiInfo().getAsyncApiState());

    return docDto;
  }

  public Doc convertToEntity(DocDto toConvert) {
    Doc doc = modelMapper.map(toConvert, Doc.class);
    AsyncApiInfo apiInfo = new AsyncApiInfo();
    apiInfo.setAsyncApiState(toConvert.getDocState());
    doc.setAsyncApiInfo(apiInfo);

    return doc;
  }
}
