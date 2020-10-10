package com.sulikdan.ERDMS.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel Šulik on 12-Sep-20
 *
 * <p>Class SearchDocParams is used for boxing search document params to increase the code
 * readability.
 */
@Getter
@Setter
@ToString
public class SearchDocParams {

  private Integer pageIndex;

  private Integer pageSize;

  private List<String> ids;

  private List<String> searchedText;

  private List<AsyncApiState> states;

  private List<String> languages;

  private List<Tag> tags;

  private String textRegex;

  private LocalDateTime createdFrom;
  private LocalDateTime createdTo;

  private LocalDateTime updatedFrom;
  private LocalDateTime updatedTo;

  private Boolean isShared;

  private List<String> columnSortList;
  private Boolean sortAscending;

  public SearchDocParams() {
    pageIndex = 0;
    pageSize = 20;
    ids = new ArrayList<>();
    searchedText = new ArrayList<>();
    states = new ArrayList<>();
    languages = new ArrayList<>();
    textRegex = null;
    createdFrom = null;
    createdTo = null;
    updatedFrom = null;
    updatedTo = null;
    isShared = null;

    columnSortList = new ArrayList<>();
    sortAscending = true;
  }

  public SearchDocParams(
      Integer pageIndex,
      Integer pageSize,
      List<String> ids,
      List<String> searchedText,
      List<AsyncApiState> states,
      List<String> languages,
      List<Tag> tags,
      String textRegex,
      LocalDateTime createdFrom,
      LocalDateTime createdTo,
      LocalDateTime updatedFrom,
      LocalDateTime updatedTo,
      Boolean isShared,
      List<String> columnSortList,
      Boolean sortAscending) {
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
    this.ids = ids;
    this.searchedText = searchedText;
    this.states = states;
    this.languages = languages;
    this.tags = tags;
    this.textRegex = textRegex;
    this.createdFrom = createdFrom;
    this.createdTo = createdTo;
    this.updatedFrom = updatedFrom;
    this.updatedTo = updatedTo;
    this.isShared = isShared;
    this.columnSortList = columnSortList;
    this.sortAscending = sortAscending;
  }
}
