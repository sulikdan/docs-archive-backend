package com.sulikdan.ERDMS.bootstrap;

import com.sulikdan.ERDMS.entities.*;
import com.sulikdan.ERDMS.entities.users.User;
import com.sulikdan.ERDMS.repositories.DocRepository;
import com.sulikdan.ERDMS.repositories.UserRepository;
import com.sulikdan.ERDMS.services.DocService;
import com.sulikdan.ERDMS.services.FileStorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class DocBootstrap is used for .....
 *
 * <p>* @author Daniel Šulik * @version 1.0 * @since 22-Jul-20
 */
@Slf4j
@Component
@AllArgsConstructor
public class DocBootstrap implements ApplicationListener<ContextRefreshedEvent> {

  private final DocService docService;
  private final FileStorageService fileStorageService;

  private final DocRepository documentRepository;
  private final UserRepository userRepository;

  private final PasswordEncoder bcryptEncoder;

  @Override
  //    @Transactional
  public void onApplicationEvent(ContextRefreshedEvent event) {

    log.info("Loading documents.");

//    User user = saveTestUser();
//
//    List<Doc> docs = loadDocuments(user);
//
//    docs.forEach(docService::saveDoc);
    //    documentRepository.saveAll(documents);

    log.info("Documents should be loaded and stored");
  }

  private User saveTestUser() {
    final String passwordEncoded = bcryptEncoder.encode("tester");

    User user =
        User.builder()
            .id("ObjectId(\"5f931f603abd91209c846e34\")")
            .email("yolouser@IdontKnow.com")
            .username("tester")
            .password(passwordEncoded)
            .enabled(true)
            .build();

    userRepository.save(user);
    return user;
  }

  private List<Doc> loadDocuments(User user) {
    List<DocPage> pages = new ArrayList<>();
    pages.add(
        new DocPage(
            "\n"
                + "What is Lorem Ipsum?\n"
                + "\n"
                + "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n"
                + "Why do we use it?\n"
                + "\n"
                + "It is a long established fact that a reader will be distracted by the readable "
                + "content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n"));
    pages.add(
        new DocPage(
            "\n"
                + "\n"
                + "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus egestas tellus ac ex volutpat, vel tempus eros volutpat. Phasellus a nibh sed erat commodo tincidunt. Suspendisse non feugiat metus. Ut convallis tristique nibh, sit amet malesuada odio condimentum vitae. Mauris tristique elementum ante vitae tincidunt. Donec interdum sodales nisi, a blandit sem. Mauris tincidunt enim est, at interdum augue laoreet vitae. Etiam vehicula magna ut eros dictum, et elementum purus mollis. Maecenas convallis lectus eu ante volutpat, id hendrerit ligula fermentum.\n"
                + "\n"
                + "In tempus dapibus metus, vel placerat magna sollicitudin aliquet. Aliquam nunc magna, dignissim sit amet dapibus at, semper nec libero. Donec scelerisque fringilla vestibulum. Phasellus fringilla neque nunc, ac efficitur augue condimentum tincidunt. Maecenas rhoncus sed mauris non mollis. Vivamus mollis neque in placerat vestibulum. Vestibulum consectetur diam eu scelerisque commodo.\n"
                + "\n"
                + "Curabitur luctus, ligula id viverra suscipit, odio risus facilisis justo, id aliquam nunc mi eget nibh. Aliquam neque dui, pulvinar sit amet orci a, malesuada pellentesque tortor. Donec tellus odio, tempus et ante id, rutrum vehicula nisl. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Duis dignissim venenatis posuere. Nulla ornare commodo faucibus. Sed enim felis, euismod eu lectus eget, convallis euismod nisi.\n"
                + "\n"
                + "Nulla risus eros, bibendum eu bibendum ac, lacinia eu mauris. Nam ipsum sem, luctus non dictum et, pellentesque in est. Aliquam aliquam ac ex at imperdiet. Curabitur consequat felis vitae aliquam pharetra. Ut non accumsan turpis, ac viverra risus. Nulla facilisi. Aenean eu tellus nec nunc pharetra bibendum. Ut est ipsum, pretium sit amet dolor quis, egestas suscipit sem. Praesent at ultricies enim, ut pulvinar mi. Morbi ac laoreet lectus. Sed feugiat ut tortor at euismod. Sed in mi magna. Aenean blandit urna at mi molestie, ac aliquam augue tempor. Sed viverra rutrum cursus. Etiam vitae pellentesque ante, porta luctus dolor.\n"
                + "\n"
                + "Nam a velit nec nisi aliquam tempus. Sed id risus in enim ultricies porttitor. Curabitur mattis libero aliquam porttitor ultricies. Vestibulum nunc sem, congue vel dui vel, sodales fringilla lectus. Pellentesque porttitor nisl a laoreet placerat. Curabitur facilisis lorem dui, quis tincidunt lectus semper vel. Quisque vel ante in leo vehicula fringilla. Nunc nisl enim, vehicula vitae leo vel, tempus fermentum elit. Morbi at ligula a nulla egestas ullamcorper. Vestibulum a pretium libero. Suspendisse rutrum feugiat nisl vel molestie. Pellentesque convallis, lectus vitae commodo gravida, velit sapien ultrices felis, sed dignissim risus dolor varius mi. Sed aliquet dui arcu, et pharetra velit dignissim in. Donec arcu tellus, dignissim eget interdum ut, condimentum ac elit. Morbi tempus ut lorem sed facilisis. Vestibulum sit amet dignissim nibh."));

    Doc doc1 =
        Doc.builder()
            .id(new ObjectId("11abc11011abc11011abc110").toString())
            .nameOfFile("JustRandomFile1.jpg")
            .docConfig(new DocConfig(false, false, "eng", false))
            .docPageList(null)
            .asyncApiInfo(new AsyncApiInfo(AsyncApiState.COMPLETED, "", ""))
            .docPageList(pages)
            .owner(user)
            .build();

    Doc doc2 =
        Doc.builder()
            .id(new ObjectId("22abc22022abc22022abc220").toString())
            .nameOfFile("JustRandomFile2.jpg")
            .docConfig(new DocConfig(true, false, "svk", false))
            .docPageList(null)
            .asyncApiInfo(new AsyncApiInfo(AsyncApiState.COMPLETED, "", ""))
            .owner(user)
            .build();
    Doc doc3 =
        Doc.builder()
            .id(new ObjectId("33abc33033abc33033abc330").toString())
            .nameOfFile("JustRandomFile3.jpg")
            .docConfig(new DocConfig(false, false, "eng", true))
            .docPageList(null)
            .asyncApiInfo(new AsyncApiInfo(AsyncApiState.COMPLETED, "", ""))
            .owner(user)
            .build();

    Doc doc4 =
        Doc.builder()
            .id(new ObjectId("44abc44044abc44044abc440").toString())
            .nameOfFile("JustRandomFile4.jpg")
            .docConfig(new DocConfig(false, false, "eng", false))
            .docPageList(null)
            .asyncApiInfo(new AsyncApiInfo(AsyncApiState.COMPLETED, "", ""))
            .owner(user)
            .build();

    Doc doc5 =
        Doc.builder()
            .id(new ObjectId("55abc55055abc55055abc550").toString())
            .nameOfFile("JustRandomFile5.jpg")
            .docConfig(new DocConfig(false, false, "svk", false))
            .docPageList(null)
            .asyncApiInfo(new AsyncApiInfo(AsyncApiState.COMPLETED, "", ""))
            .owner(user)
            .build();

    return Arrays.asList(doc1, doc2, doc3, doc4, doc5);
  }
}
