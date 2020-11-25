package com.sulikdan.ERDMS.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Daniel Šulik on 09-Aug-20
 * <p>
 * Class TessApiDoc is used for wrapping result data from OCR API
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TessApiDoc {
    @JsonProperty("name")
    private String name;
    @JsonProperty("origName")
    private String origName;
    @JsonProperty("url")
    private String url;
    @JsonProperty("pages")
    private List<String> pages;


    @Override
    public String toString() {
        return "TessApiDoc{" + "name='" + name + '\'' + ", origName='" + origName + '\'' + ", url='" + url + '\'' + ", pages=" + pages + '}';
    }
}
