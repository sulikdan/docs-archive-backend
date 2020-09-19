package com.sulikdan.ERDMS.dto;

import com.sulikdan.ERDMS.entities.AsyncApiState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Daniel Šulik on 19-Sep-20
 * <p>
 * Class SearchDocParamsDTO is used for .....
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchDocParamsDto {


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

}
