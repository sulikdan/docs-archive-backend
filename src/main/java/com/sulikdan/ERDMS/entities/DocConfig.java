package com.sulikdan.ERDMS.entities;

import lombok.*;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.util.LinkedMultiValueMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Class DocConfig is used for storing & contains configuration used in OCR tool/framework. The
 * reason to nest all this properties inside class was to avoid sending uncountable amount of
 * params.
 *
 * @author Daniel Šulik
 * @version 1.0
 * @since 25-Jul-20
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocConfig {

  // OCR properties
  @Builder.Default
  Boolean highQuality = false;

  @Builder.Default
  Boolean multiPage = false;

//  @TextIndexed
  @Builder.Default
  String lang  = "eng";

  // Doc properties
  @Builder.Default
  Boolean scanImmediately = false;


  public Map<String,String> getOcrPropertiesAsMap_X(){
    Map<String,String> map = new HashMap<>();
    map.put("highQuality",highQuality.toString());
    map.put("multiPageFile",multiPage.toString());
    map.put("lang",lang.toString());
    return map;
  }

  @Override
  public String toString() {
    return "DocConfig{" + "highQuality=" + highQuality + ", multiPage=" + multiPage + ", lang='" + lang + '\'' + ", scanImmediately=" + scanImmediately + '}';
  }
}
