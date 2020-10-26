package com.aimsphm.nuclear.core.service;

import java.util.List;

import com.aimsphm.nuclear.core.entity.MdSite;
import com.aimsphm.nuclear.core.vo.Tree;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-03-18
 */
public interface MdSiteService extends IService<MdSite> {

	Tree getSiteTree(Long siteId);

	List<Tree> getSetSubTree(Long setId);

	List<Tree> getSystemSubTree(Long systemId);

	List<Tree> getSensorsBySubSystemId(Long subSystemId);

	List<Tree> getDeviceSubTree(Long deviceId);

	List<Tree> getSubSystemSubTree(Long subSystemId);

}