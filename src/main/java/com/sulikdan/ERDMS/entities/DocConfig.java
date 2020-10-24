package com.sulikdan.ERDMS.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class DocConfig {

  // OCR properties
  Boolean highQuality;
  Boolean multiPage;
  String lang;

  // Doc properties
  Boolean scanImmediately;


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
