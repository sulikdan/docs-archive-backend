package eu.sulikdan.erdmsbackendv2.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Created by Daniel Šulik on 09-Aug-20
 * <p>
 * Class TessApiEDoc is used for wrapping result data from OCR API
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TessApiEDoc {

    private String name;

    private String origName;

    private String url;

    private List<String> pages;


    @Override
    public String toString() {
        return "TessApiDoc{" + "name='" + name + '\'' + ", origName='" + origName + '\'' + ", url='" + url + '\'' + ", pages=" + pages + '}';
    }
}
