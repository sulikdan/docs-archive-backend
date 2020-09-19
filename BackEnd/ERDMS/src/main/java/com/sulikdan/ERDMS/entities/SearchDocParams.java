package com.sulikdan.ERDMS.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Šulik on 12-Sep-20
 *
 * <p>Class SearchDocParams is used for .....
 */
@Getter
@Setter
public class SearchDocParams {

  private List<String> ids;

  private List<String> searchedText;

  private List<AsyncApiState> states;

  private List<String> languages;

  private String textRegex;

  private LocalDateTime createdFrom;
  private LocalDateTime createdTo;

  private LocalDateTime updatedFrom;
  private LocalDateTime updatedTo;

  private List<String> columnSortList;
  private Boolean sortAscending;


//  private Pageable pageable;



  public SearchDocParams() {
    ids = new ArrayList<>();
    searchedText = new ArrayList<>();
    states = new ArrayList<>();
    languages = new ArrayList<>();
    textRegex = null;
    createdFrom = null;
    createdTo = null;
    updatedFrom = null;
    updatedTo = null;

    columnSortList = new ArrayList<>();
    sortAscending = true;
  }


  public SearchDocParams(
          List<String> ids, List<String> searchedText, List<AsyncApiState> states, List<String> languages,
          String textRegex, LocalDateTime createdFrom, LocalDateTime createdTo, LocalDateTime updatedFrom,
          LocalDateTime updatedTo, List<String> columnSortList, Boolean sortAscending) {
    this.ids            = ids;
    this.searchedText   = searchedText;
    this.states         = states;
    this.languages      = languages;
    this.textRegex      = textRegex;
    this.createdFrom    = createdFrom;
    this.createdTo      = createdTo;
    this.updatedFrom    = updatedFrom;
    this.updatedTo      = updatedTo;
    this.columnSortList = columnSortList;
    this.sortAscending  = sortAscending;
  }
}
