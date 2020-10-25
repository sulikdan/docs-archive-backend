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
import com.sulikdan.ERDMS.entities.users.User;
import com.sulikdan.ERDMS.services.DocService;
import com.sulikdan.ERDMS.services.users.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by Daniel Šulik on 25-Sep-20
 *
 * <p>Class DocControllerTest is used for .....
 */
// @ExtendWith(SpringExtension.class)
// @WebMvcTest(DocController.class)
class DocControllerTest {

  @Mock DocService docService;
  @Mock UserService userService;
  @Mock JwtTokenUtil jwtTokenUtil;
  SecurityContext securityContext;
  Authentication authentication;

  DocDtoConverter docDtoConverter;

  MockMvc mockMvc;

  DocController docController;

  ModelMapper modelMappper;
  ObjectMapper mapper;

  User user;

  private static String BASE_PATH = "http://localhost";
  private static String DOCS_PATH = "api/documents";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);

    authentication = Mockito.mock(Authentication.class);
    securityContext = Mockito.mock(SecurityContext.class);

    docDtoConverter = new DocDtoConverter();

    docController = new DocController(docService, docDtoConverter, userService, jwtTokenUtil);

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
    doc.setAsyncApiInfo(new AsyncApiInfo());
    doc.setIsShared(false);
    doc.setOwner(user);
    return doc;
  }

  @Test
  public void getDocById() throws Exception {
    Doc doc = setUpDocument();

    DocDto docDto = docDtoConverter.convertToDto(doc);
    Link selfLink = linkTo(DocController.class).slash(docDto.getId()).withSelfRel();
    docDto.add(new Link(BASE_PATH + selfLink.getHref(), selfLink.getRel()));
    String responseBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(docDto);

    // When
    //  loadConnectedUser()
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(user.getUsername());
    when(userService.loadUserByUserName(user.getUsername())).thenReturn(java.util.Optional.ofNullable(user));

    when(docService.findDocById(docDto.getId(), user)).thenReturn(doc);

    //    then
    this.mockMvc
        .perform(get("/api/documents/" + doc.getId()))
        .andExpect(status().isOk())
        .andExpect(content().string(responseBody));

    //    verifyDocJson(resultActions);

    verify(docService, times(1)).findDocById(doc.getId(), user);
  }

  @Test
  public void getDocNotFound() throws Exception {

    // When

    //  loadConnectedUser()
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(user.getUsername());
    when(userService.loadUserByUserName(user.getUsername())).thenReturn(java.util.Optional.ofNullable(user));

    when(docService.findDocById("99xaa", user)).thenReturn(null);

    // then
    this.mockMvc.perform(get("/api/documents/" + "99xaa")).andExpect(status().isNotFound());

    verify(docService, times(1)).findDocById("99xaa", user);
  }

  @Test
  public void getDocsWihtoutParams() throws Exception {

    // TODO

  }

  @Test
  public void getDocsByPageIndexAndPageSize() throws Exception {

    // TODO

  }

  @Test
  public void uploadFiles() throws Exception {
    //    TODO
  }

  @Test
  public void postFilterOptions() throws Exception {
    //    TODO
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
