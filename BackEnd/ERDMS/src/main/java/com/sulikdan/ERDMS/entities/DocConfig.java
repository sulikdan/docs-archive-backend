package com.sulikdan.ERDMS.entities;

import lombok.Getter;
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
public class DocConfig {

  // OCR properties
  Boolean highQuality;
  Boolean multiPage;
  String lang;

  // Doc properties
  Boolean scanImmediately;

  public DocConfig(Boolean highQuality, Boolean multiPage, String lang, Boolean scanImmediately) {
    this.highQuality = highQuality;
    this.multiPage = multiPage;
    this.lang = lang;
    this.scanImmediately = scanImmediately;
  }

//  public LinkedMultiValueMap getOcrPropertiesAsMap(){
//    LinkedMultiValueMap map = new LinkedMultiValueMap();
//    map.add("highQuality",highQuality.toString());
//    map.add("multiPage",multiPage.toString());
//    map.add("lang",lang.toString());
//    return map;
//  }

  public Map<String,String> getOcrPropertiesAsMap_X(){
    Map<String,String> map = new HashMap<>();
    map.put("highQuality",highQuality.toString());
    map.put("multiPageFile",multiPage.toString());
    map.put("lang",lang.toString());
    return map;
  }
}
