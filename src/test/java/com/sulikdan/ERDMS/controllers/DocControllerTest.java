package com.sulikdan.ERDMS.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sulikdan.ERDMS.configurations.configs.JwtTokenUtil;
import com.sulikdan.ERDMS.controllers.ErrorHandlers.DocUploadExceptionAdvice;
import com.sulikdan.ERDMS.dto.DocDto;
import com.sulikdan.ERDMS.dto.DocDtoConverter;
import com.sulikdan.ERDMS.entities.AsyncApiInfo;
import com.sulikdan.ERDMS.entities.Doc;
import com.sulikdan.ERDMS.entities.SearchDocParams;
import com.sulikdan.ERDMS.entities.users.User;
import com.sulikdan.ERDMS.services.DocService;
import com.sulikdan.ERDMS.services.users.UserService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.hateoas.Link;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Daniel Šulik on 25-Sep-20
 *
 * <p>Class DocControllerTest is used for .....
 */
@ExtendWith(MockitoExtension.class)
class DocControllerTest {

  @Mock DocService docService;
  @Mock UserService userService;
  @Mock JwtTokenUtil jwtTokenUtil;
  @Mock SecurityContext securityContext;
  @Mock Authentication authentication;

  @Mock DocDtoConverter docDtoConverter;

  MockMvc mockMvc;

  @InjectMocks DocController docController;

  ModelMapper modelMappper;
  ObjectMapper mapper;

  User user;

  private static String BASE_PATH = "http://localhost";
  private static String DOCS_PATH = "api/documents";

  @BeforeEach
  void setUp() {
    //    docDtoConverter = new DocDtoConverter();

    mockMvc =
        MockMvcBuilders.standaloneSetup(docController)
            .setControllerAdvice(new DocUploadExceptionAdvice())
            .build();

    modelMappper = new ModelMapper();

    user = User.builder().id("1234").username("tester").build();

    mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }

  Doc setUpDocument() {
    Doc doc = new Doc();
    doc.setId("999xyz999");
    doc.setNameOfFile("tmp.png");
    doc.setAsyncApiInfo(new AsyncApiInfo());
    doc.setIsShared(false);
    doc.setOwner(user);
    return doc;
  }

  @Test
  public void getDocById() throws Exception {
    // given
    Doc doc = setUpDocument();
    DocDtoConverter tmp = new DocDtoConverter();
    DocDto docDto = tmp.convertToDto(doc);

    Link selfLink = linkTo(DocController.class).slash(docDto.getId()).withSelfRel();
    docDto.add(new Link(BASE_PATH + selfLink.getHref(), selfLink.getRel()));
    String responseBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(docDto);

    //  loadConnectedUser()
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName())
        .thenReturn(user.getUsername());
    when(userService.loadUserByUserName(user.getUsername()))
        .thenReturn(java.util.Optional.ofNullable(user));

    when(docService.findDocById(docDto.getId(), user)).thenReturn(doc);
    when(docDtoConverter.convertToDto(any(Doc.class))).thenReturn(docDto);

    //    when
    MvcResult mvcResult =
        this.mockMvc
            .perform(get("/documents/{docId}", doc.getId()))
            .andExpect(status().isOk())
            .andReturn();

    //    verifyDocJson(resultActions);
    // then
    Assert.assertEquals(
        mapper.writeValueAsString(docDto), mvcResult.getResponse().getContentAsString());
    verify(docService).findDocById(doc.getId(), user);
  }

  @Test
  public void getDocNotFound() throws Exception {
    // Given

    //  loadConnectedUser()
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName())
        .thenReturn(user.getUsername());
    when(userService.loadUserByUserName(user.getUsername()))
        .thenReturn(java.util.Optional.ofNullable(user));

    when(docService.findDocById("99xaa", user)).thenReturn(null);

    // when
    this.mockMvc.perform(get("/documents/" + "99xaa")).andExpect(status().isNotFound());

    // then
    verify(docService, times(1)).findDocById("99xaa", user);
  }


  @Test
  public void getDocsWihtoutParams() throws Exception {
    // Given
    DocDtoConverter tmpDocDtoConverter = new DocDtoConverter();

    List<Doc> docList = new ArrayList<>();
    Doc doc = setUpDocument();
    docList.add(doc);
    docList.add(doc);

    DocDto docDto = tmpDocDtoConverter.convertToDto(doc);
    List<DocDto> docDtoList = new ArrayList<>();
    docDtoList.add(docDto);
    docDtoList.add(docDto);

    Page<Doc> pagedDocs = new PageImpl<>(docList);
    Page<DocDto> pagedDocsDto = new PageImpl<>(docDtoList);

    //  loadConnectedUser()
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName())
            .thenReturn(user.getUsername());
    when(userService.loadUserByUserName(user.getUsername()))
            .thenReturn(java.util.Optional.ofNullable(user));

    when(docService.findDocsUsingSearchParams(any(),anyInt(),anyInt(),any())).thenReturn(pagedDocs);

    when(docDtoConverter.convertToDto(any())).thenReturn(docDto);

    // when
    MvcResult mvcResult =
            this.mockMvc.perform(get("/documents/")).andExpect(status().isOk()).andReturn();


    // then
    Assert.assertEquals(mapper.writeValueAsString(pagedDocsDto),mvcResult.getResponse().getContentAsString());
    verify(userService).loadUserByUserName(anyString());
    verify(docService).findDocsUsingSearchParams(any(),anyInt(),anyInt(),any());
    verify(docDtoConverter,atLeastOnce()).convertToDto(any());
  }

  @Disabled
  @Test
  public void getDocsByPageIndexAndPageSize() throws Exception {
    // given
    SearchDocParams docParams = new SearchDocParams();
    docParams.setPageIndex(1);

    Doc doc = setUpDocument();
    DocDtoConverter tmp = new DocDtoConverter();
    DocDto docDto = tmp.convertToDto(doc);

    Link selfLink = linkTo(DocController.class).slash(docDto.getId()).withSelfRel();
    docDto.add(new Link(BASE_PATH + selfLink.getHref(), selfLink.getRel()));
    String responseBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(docDto);

    //  loadConnectedUser()
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName())
        .thenReturn(user.getUsername());
    when(userService.loadUserByUserName(user.getUsername()))
        .thenReturn(java.util.Optional.ofNullable(user));

    when(docService.findDocById(docDto.getId(), user)).thenReturn(doc);
    when(docDtoConverter.convertToDto(any(Doc.class))).thenReturn(docDto);

    //    when
    MvcResult mvcResult =
        this.mockMvc
            .perform(get("/documents?{params}", mapper.writeValueAsString(docParams)))
            .andExpect(status().isOk())
            .andReturn();

    //    verifyDocJson(resultActions);
    // then
    Assert.assertEquals(
        mapper.writeValueAsString(docDto), mvcResult.getResponse().getContentAsString());
    verify(docService).findDocById(doc.getId(), user);
  }

  @Test
  void getDocFile() throws Exception {
    // Given
    final String docId = "99xaa";
    final Doc doc = setUpDocument();

    doc.setDocumentAsBytes(new byte[] { 1, 2, 3, 127});

    // Load user ...
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName())
        .thenReturn(user.getUsername());
    when(userService.loadUserByUserName(anyString())).thenReturn(Optional.of(user));

    when(docService.findDocById(docId, user)).thenReturn(doc);

    // when
    MvcResult mvcResult =
        this.mockMvc
            .perform(get("/documents/{docId}/file", docId))
            .andExpect(status().isOk())
            .andReturn();

//    Expected :[B@bfc14b9
//    Actual   :[B@fb6097b

    // then
    Assert.assertNotNull(mvcResult);
    Assert.assertEquals(doc.getDocumentAsBytes()[0], mvcResult.getResponse().getContentAsByteArray()[0]);
    Assert.assertEquals(doc.getDocumentAsBytes()[1], mvcResult.getResponse().getContentAsByteArray()[1]);
    Assert.assertEquals(doc.getDocumentAsBytes()[2], mvcResult.getResponse().getContentAsByteArray()[2]);
    Assert.assertEquals(doc.getDocumentAsBytes()[3], mvcResult.getResponse().getContentAsByteArray()[3]);
    verify(userService).loadUserByUserName(anyString());
    verify(docService).findDocById(any(), any());
  }

  @Test
  void patchDoc() {
    //    // When
    //    final String docId = "99xaa";
    //
    //    when(securityContext.getAuthentication()).thenReturn(authentication);
    //    SecurityContextHolder.setContext(securityContext);
    //    when(SecurityContextHolder.getContext().getAuthentication().getName())
    //            .thenReturn(user.getUsername());
    //    when(userService.loadUserByUserName(anyString())).thenReturn(Optional.of(user));
    //    doNothing().when(docService).deleteDocById(docId, user);
    //
    //    // then
    //    this.mockMvc.perform(delete("/documents/{id}", docId)).andExpect(status().isOk());
    //
    //    verify(userService).loadUserByUserName(anyString());
    //    verify(docService).deleteDocById(any(), any());
  }

  @Test
  void deleteDoc() throws Exception {
    // When
    final String docId = "99xaa";

    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName())
        .thenReturn(user.getUsername());
    when(userService.loadUserByUserName(anyString())).thenReturn(Optional.of(user));
    doNothing().when(docService).deleteDocById(docId, user);

    // then
    this.mockMvc.perform(delete("/documents/{id}", docId)).andExpect(status().isOk());

    verify(userService).loadUserByUserName(anyString());
    verify(docService).deleteDocById(any(), any());
  }

  private void verifyDocJson(final ResultActions resultActions) throws Exception {
    resultActions
        .andExpect(jsonPath("id", is(setUpDocument().getId())))
        .andExpect(jsonPath("docType", is(setUpDocument().getDocType())))
        .andExpect(jsonPath("links[0].rel", is("self")))
        .andExpect(
            jsonPath(
                "links[0].href",
                is(
                    BASE_PATH
                        + linkTo(DocController.class).slash(setUpDocument().getId()).toString())));
  }
}
