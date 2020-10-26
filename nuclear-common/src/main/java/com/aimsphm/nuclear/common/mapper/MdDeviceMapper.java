package com.aimsphm.nuclear.common.mapper;

import com.aimsphm.nuclear.common.entity.MdDevice;
import com.aimsphm.nuclear.common.entity.vo.MdDeviceVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lu.yi
 * @since 2020-03-26
 */

@Repository
public interface MdDeviceMapper extends BaseMapper<MdDevice> {

    List<Long> selectDeviceIdsBySubSystemId(Long subSystemId);

    List<MdDeviceVO> selectDeviceBySubSystemId(Long subSystemId);
}