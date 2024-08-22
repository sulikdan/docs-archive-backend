package eu.sulikdan.erdmsbackendv2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


/**
 * Class Page is used to save pages as string. Later may be extended with more properties per page.
 *
 */
@Getter
@Setter
@Entity
@Table(name = "e_doc_page")
@NoArgsConstructor
@AllArgsConstructor
public class EDocPage {

    @Id
    private UUID id = UUID.randomUUID();

    private String content;
}



