package com.aimsphm.nuclear.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aimsphm.nuclear.common.entity.MdDevice;
import com.aimsphm.nuclear.common.entity.MdSensor;
import com.aimsphm.nuclear.common.entity.MdSubSystem;
import com.aimsphm.nuclear.common.service.MdDeviceService;
import com.aimsphm.nuclear.common.service.MdSensorService;
import com.aimsphm.nuclear.core.entity.MdSet;
import com.aimsphm.nuclear.core.entity.MdSite;
import com.aimsphm.nuclear.core.entity.MdSystem;
import com.aimsphm.nuclear.core.mapper.MdSiteMapper;
import com.aimsphm.nuclear.core.service.MdSetService;
import com.aimsphm.nuclear.core.service.MdSiteService;
import com.aimsphm.nuclear.core.service.MdSubSystemService;
import com.aimsphm.nuclear.core.service.MdSystemService;
import com.aimsphm.nuclear.core.vo.Tree;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-03-18
 */
@Slf4j
@Service
public class MdSiteServiceImpl extends ServiceImpl<MdSiteMapper, MdSite> implements MdSiteService {

	@Autowired
	private MdSiteMapper mdSiteMapper;

	@Autowired
	private MdSetService mdSetService;

	@Autowired
	private MdSystemService mdSystemService;

	@Autowired
	private MdSubSystemService mdSubSystemService;

	@Autowired
	private MdDeviceService mdDeviceService;

	@Autowired
	private MdSensorService mdSensorService;

	@Override
	public Tree getSiteTree(Long siteId) {
		MdSite mdSite = mdSiteMapper.selectById(siteId);
		if (mdSite == null) {
			return null;
		}
		Tree siteTree = new Tree(mdSite.getId(), mdSite.getSiteName(), mdSite.getSiteDesc(), null, 0, null,null);

		List<Tree> setTreeList = querySetTreeFromSiteTree(siteTree);
		for (Tree setTree : setTreeList) {
			List<Tree> systemTreeList = querySystemTreeFromSetTree(setTree);
			for (Tree systemTree : systemTreeList) {
				List<Tree> subSystemTreeList = querySubSystemTreeFromSystem(systemTree);
				for (Tree subSystemTree : subSystemTreeList) {
					List<Tree> deviceTreeList = queryDeviceTreeFromSubSystemTree(subSystemTree);
					subSystemTree.setChildren(deviceTreeList);
				}
			}
		}
		return siteTree;
	}

	@Override
	public List<Tree> getSetSubTree(Long setId) {
		MdSet mdSet = mdSetService.getById(setId);
		if (mdSet == null) {
			return null;
		}
		Tree setTree = new Tree(mdSet.getId(), mdSet.getSetName(), mdSet.getSetDesc(), null, 1, null,null);

		return querySystemTreeFromSetTree(setTree);
	}

	@Override
	public List<Tree> getSubSystemSubTree(Long subSystemId) {
		MdSubSystem mdSubSystem = mdSubSystemService.getById(subSystemId);
		if (mdSubSystem == null) {
			return null;
		}
		
		Tree subSystemTree = new Tree(mdSubSystem.getId(), mdSubSystem.getSubSystemName(), mdSubSystem.getSubSystemDesc(), null, 3, null,mdSubSystem.getAdditionalType());

		List<Tree> deviceTreeList = queryDeviceTreeFromSubSystemTree(subSystemTree);
		return deviceTreeList;
	}
	
	@Override
	public List<Tree> getSystemSubTree(Long systemId) {
		MdSystem mdSystem = mdSystemService.getById(systemId);
		if (mdSystem == null) {
			return null;
		}
		Tree systemTree = new Tree(mdSystem.getId(), mdSystem.getSystemName(), mdSystem.getSystemDesc(), null, 2, null,null);

		List<Tree> subSystemTreeList = querySubSystemTreeFromSystem(systemTree);
		
		return subSystemTreeList;
	}
	@Override
	public List<Tree> getSensorsBySubSystemId(Long subSystemId) {

//		Tree deviceTree = new Tree(mdDevice.getId(), mdDevice.getDeviceCode(), mdDevice.getDeviceDesc(), null, 4, null);
		QueryWrapper<MdSensor> mdSensorQueryWrapper = new QueryWrapper<>();
		mdSensorQueryWrapper.lambda().eq(MdSensor::getSubSystemId, subSystemId).eq(MdSensor::getPiSensor,true);
		List<MdSensor> childMdDeivces = mdSensorService.list(mdSensorQueryWrapper);
//		List<MdSensor> childMdDeivces = mdSensorService.getMdSensorBySubSystemId(subSystemId);
		return transformSensorListToTreeList(childMdDeivces);
	}
	@Override
	public List<Tree> getDeviceSubTree(Long deviceId) {
		MdDevice mdDevice = mdDeviceService.getById(deviceId);
		if (mdDevice == null) {
			return null;
		}
//		Tree deviceTree = new Tree(mdDevice.getId(), mdDevice.getDeviceCode(), mdDevice.getDeviceDesc(), null, 4, null);
//		QueryWrapper<MdSensor> mdSensorQueryWrapper = new QueryWrapper<>();
//		mdSensorQueryWrapper.lambda().and(Wrapper->Wrapper.eq(MdSensor::getSubSystemId, mdDevice.getSubSystemId()).eq(MdSensor::getSystemSensor, true)).or().eq(MdSensor::getDeviceId, deviceTree.getValue());
//		List<MdSensor> childMdDeivces = mdSensorService.list(mdSensorQueryWrapper);
//		List<MdSensor> childMdDeivces = mdSensorService.getMdSensorsByDeviceId(deviceId);
		QueryWrapper<MdSensor> mdSensorQueryWrapper = new QueryWrapper<>();
		mdSensorQueryWrapper.lambda().eq(MdSensor::getDeviceId, deviceId).eq(MdSensor::getPiSensor,true);
		List<MdSensor> childMdDeivces = mdSensorService.list(mdSensorQueryWrapper);
		return transformSensorListToTreeList(childMdDeivces);
	}
	private List<Tree> transformSensorListToTreeList(List<MdSensor> childMdsensors) {
		List<Tree> treeList = new ArrayList<>();
		for (MdSensor mdSensor : childMdsensors) {
			Tree tree = new Tree(mdSensor.getId(), mdSensor.getTagId(), mdSensor.getSensorName(), null, 5, null,null);
			treeList.add(tree);
		}
		return treeList;
	}
	private List<Tree> querySetTreeFromSiteTree(Tree siteTree) {
		QueryWrapper<MdSet> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(MdSet::getSiteId, siteTree.getValue());
		List<MdSet> childMdsets = mdSetService.list(queryWrapper);
		List<Tree> setTreeList = transformSetListToTreeList(childMdsets);
		siteTree.setChildren(setTreeList);
		return setTreeList;
	}

	private List<Tree> queryDeviceTreeFromSubSystemTree(Tree subSystemTree) {
		QueryWrapper<MdDevice> mdDeviceQueryWrapper = new QueryWrapper<>();
		mdDeviceQueryWrapper.lambda().eq(MdDevice::getSubSystemId, subSystemTree.getValue());
		List<MdDevice> childMdDeivces = mdDeviceService.list(mdDeviceQueryWrapper);
		List<Tree> deviceTreeList = transformDeviceListToTreeList(childMdDeivces);
		return deviceTreeList;
	}

	private List<Tree> querySubSystemTreeFromSystem(Tree systemTree) {
		QueryWrapper<MdSubSystem> subSystemQueryWrapper = new QueryWrapper<>();
		subSystemQueryWrapper.lambda().eq(MdSubSystem::getSystemId, systemTree.getValue());
		List<MdSubSystem> childMdSubSystems = mdSubSystemService.list(subSystemQueryWrapper);
		List<Tree> subSystemTreeList = transformSubSystemListToTreeList(childMdSubSystems);
		systemTree.setChildren(subSystemTreeList);
		return subSystemTreeList;
	}

	private List<Tree> querySystemTreeFromSetTree(Tree setTree) {
		QueryWrapper<MdSystem> systemQueryWrapper = new QueryWrapper<>();
		systemQueryWrapper.lambda().eq(MdSystem::getSetId, setTree.getValue());
		List<MdSystem> childMdSystems = mdSystemService.list(systemQueryWrapper);
		List<Tree> systemTreeList = transformSystemListToTreeList(childMdSystems);
		setTree.setChildren(systemTreeList);
		return systemTreeList;
	}

	private List<Tree> transformSetListToTreeList(List<MdSet> childMdsets) {
		List<Tree> treeList = new ArrayList<>();
		for (MdSet mdSet : childMdsets) {
			Tree tree = new Tree(mdSet.getId(), mdSet.getSetName(), mdSet.getSetDesc(), null, 1, null,null);
			treeList.add(tree);
		}
		return treeList;
	}

	private List<Tree> transformSystemListToTreeList(List<MdSystem> childMdSystems) {
		List<Tree> treeList = new ArrayList<>();
		for (MdSystem mdSystem : childMdSystems) {
			Tree tree = new Tree(mdSystem.getId(), mdSystem.getSystemName(), mdSystem.getSystemDesc(), null, 2, null,null);
			treeList.add(tree);
		}
		return treeList;
	}

	private List<Tree> transformSubSystemListToTreeList(List<MdSubSystem> childMdSubSystems) {
		List<Tree> treeList = new ArrayList<>();
		for (MdSubSystem mdSubSystem : childMdSubSystems) {
			Tree tree = new Tree(mdSubSystem.getId(), mdSubSystem.getSubSystemName(), mdSubSystem.getSubSystemDesc(),
					null, 3, mdSubSystem.getSubSystemType(),mdSubSystem.getAdditionalType());
			treeList.add(tree);
		}
		return treeList;
	}

	private List<Tree> transformDeviceListToTreeList(List<MdDevice> childMdDevices) {
		List<Tree> treeList = new ArrayList<>();
		for (MdDevice mdDevice : childMdDevices) {
			Tree tree = new Tree(mdDevice.getId(), mdDevice.getDeviceName(), mdDevice.getDeviceDesc(), null, 4, null,mdDevice.getAdditionalType());
			treeList.add(tree);
		}
		return treeList;
	}
}