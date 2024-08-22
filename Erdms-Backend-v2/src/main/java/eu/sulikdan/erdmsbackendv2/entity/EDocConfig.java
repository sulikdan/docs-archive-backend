package eu.sulikdan.erdmsbackendv2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Class EDocConfig is used for storing & contains configuration used in OCR tool/framework. The
 * reason to nest all these properties inside class was to avoid sending uncountable amount of
 * params.
 *
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "E_DOC_CONFIG")
public class EDocConfig {

    @Id
    private UUID id = UUID.randomUUID();

    private Boolean highQuality = false;

    private Boolean multiPage = false;

    private String lang = "eng";

    private Boolean scanImmediately = false;



    @Override
    public String toString() {
        return "DocConfig{" + "highQuality=" + highQuality + ", multiPage=" + multiPage + ", lang='" + lang + '\'' + ", scanImmediately=" + scanImmediately + '}';
    }

}
