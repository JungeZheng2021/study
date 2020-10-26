package com.aimsphm.nuclear.core.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tree {
	private Long value;
	private String label;
	private String desc;
	private List<Tree> children;
	private Integer level;
	private Integer subSystemType;
	private Integer additionalType;
}
