package com.sulikdan.ERDMS.dto;

import com.sulikdan.ERDMS.entities.AsyncApiInfo;
import com.sulikdan.ERDMS.entities.Doc;
import com.sulikdan.ERDMS.entities.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Created by Daniel Šulik on 19-Sep-20
 *
 * <p>Class DocDtoConverter is used to converter between Doc and DocDto.
 */
@Component
public class DocDtoConverter {

  ModelMapper modelMapper;

  public DocDtoConverter() {
    this.modelMapper = new ModelMapper();
  }

  public DocDto convertToDto(Doc toConvert) {
    DocDto docDto = modelMapper.map(toConvert, DocDto.class);
    if( toConvert.getAsyncApiInfo() != null )
      docDto.setDocState(toConvert.getAsyncApiInfo().getAsyncApiState());
    if( toConvert.getDocumentPreview() != null )
      docDto.setDocumentPreview(Base64.getEncoder().encodeToString(toConvert.getDocumentPreview()));

    if(  toConvert.getTags() != null ){
      List<Tag> tags = new ArrayList<>();
      toConvert.getTags().forEach(tag -> tags.add(new Tag(tag.getTagType())));
      docDto.setTags(tags);
    }

    return docDto;
  }

  public Doc convertToEntity(DocDto toConvert) {
    Doc doc = modelMapper.map(toConvert, Doc.class);
    AsyncApiInfo apiInfo = new AsyncApiInfo();
    apiInfo.setAsyncApiState(toConvert.getDocState());
    doc.setAsyncApiInfo(apiInfo);

    if( toConvert.getTags() != null ){
      List<Tag> tags = new ArrayList<>();
      toConvert.getTags().forEach(tag -> tags.add(new Tag(tag.getTagType())));
      doc.setTags(tags);
    }

    return doc;
  }
}
