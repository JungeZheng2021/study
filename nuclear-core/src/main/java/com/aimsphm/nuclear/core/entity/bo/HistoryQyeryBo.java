package com.aimsphm.nuclear.core.entity.bo;

import lombok.Data;

import java.util.List;

@Data
public class HistoryQyeryBo {
  private List<String> tagList;
  private long start;
  private long end;

  public HistoryQyeryBo(List<String> tagList, long start, long end) {
	  this.start=start;
	  this.end=end;
	  this.tagList=tagList;
  }
  
}
