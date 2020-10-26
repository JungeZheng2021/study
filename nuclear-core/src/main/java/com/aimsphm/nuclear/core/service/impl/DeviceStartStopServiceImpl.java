package com.aimsphm.nuclear.core.service.impl;

import com.aimsphm.nuclear.common.entity.TxDeviceStopStartRecord;
import com.aimsphm.nuclear.common.exception.InvalidStartStopRecordException;
import com.aimsphm.nuclear.common.service.TxDeviceStopStartRecordService;
import com.aimsphm.nuclear.core.service.DeviceStartStopService;
import com.aimsphm.nuclear.core.util.DeviceStartAndStopUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeviceStartStopServiceImpl implements DeviceStartStopService {



    @Autowired
    private TxDeviceStopStartRecordService txRecordService;

    @Autowired
    private DeviceStartAndStopUtil util;



  /*  @Override
    public TxDeviceStopStartRecord getPreviousRecord(Date baseDate) {
        //可能会return null，因为自己是第一条记录
        return util.findPreviousRecord(baseDate);
    }

    @Override
    public TxDeviceStopStartRecord getNextRecord(Date baseDate) {
        //可能会return null，因为自己是最后一条记录
        return util.findNextRecord(baseDate);
    }
*/
    @Override
    @Transactional
    public Boolean createRecord(TxDeviceStopStartRecord record) throws Exception {

        Boolean isSaved = false;

        if(util.isValidateRecord(record)){
            if(!(record.getStartBeginTime()==null&&record.getStartEndTime()==null&&record.getStopBeginTime()==null&&record.getStopEndTime()==null)){
                List<TxDeviceStopStartRecord> suceedRecords = txRecordService.getSuceedRecords(util.getMaxTimeOfRecord(record),record.getDeviceId(),record.getDeviceType(),record.getId());
                if(suceedRecords.isEmpty()){//证明是最后一条记录
                    Integer maxT = txRecordService.getMaxActiontims(record.getDeviceId(),record.getDeviceType());
                    if(maxT==null){
                        record.setActionTimes(1);
                    }else{
                        record.setActionTimes(maxT+1);
                    }
                    record.setDurationOfStart(util.calculateDuration(record).get("start"));
                    record.setDurationOfStop(util.calculateDuration(record).get("end"));
                    isSaved = txRecordService.save(record);//只存一条
                }else{//后面还有记录，所有后面得记录加1
                    List<TxDeviceStopStartRecord> sortedSuceedRecords = suceedRecords.stream().filter(x->x.getActionTimes()!=null).sorted(Comparator.comparing(TxDeviceStopStartRecord::getActionTimes)).collect(Collectors.toList());
                    Integer minimalActionTime = sortedSuceedRecords.get(0).getActionTimes();
                    record.setActionTimes(minimalActionTime);
                    List<TxDeviceStopStartRecord> toUpdatedRecord = suceedRecords.stream().filter(x->x.getActionTimes()!=null).map(x->{
                        Integer actionTimes= x.getActionTimes();
                        x.setActionTimes(actionTimes+1);
                        return x;
                    }).collect(Collectors.toList());
                    record.setDurationOfStart(util.calculateDuration(record).get("start"));
                    record.setDurationOfStop(util.calculateDuration(record).get("end"));
                    toUpdatedRecord.add(record);
                    isSaved = txRecordService.saveOrUpdateBatch(toUpdatedRecord);
                }

            }else{//全为空
                record.setDurationOfStart(util.calculateDuration(record).get("start"));
                record.setDurationOfStop(util.calculateDuration(record).get("end"));
                isSaved = txRecordService.save(record);
            }
        }else{
            throw new InvalidStartStopRecordException("记录开始和结束时间与其他记录冲突");
        }
        return isSaved;
    }

    @Override
    @Transactional
    public Boolean updateRecord(TxDeviceStopStartRecord record) throws Exception {
        Boolean isSaved = false;
        TxDeviceStopStartRecord originalRecord = txRecordService.selectForUpdateById(record.getId());

        if(util.isValidateRecord(record)){
            if(!(record.getStartBeginTime()==null&&record.getStartEndTime()==null&&record.getStopBeginTime()==null&&record.getStopEndTime()==null)){//不是一条空记录
                List<TxDeviceStopStartRecord> suceedRecords = txRecordService.getSuceedRecords(util.getMaxTimeOfRecord(record),record.getDeviceId(),record.getDeviceType(),record.getId());
                List<TxDeviceStopStartRecord> originalSuceedRecords = txRecordService.getSuceedRecords(util.getMaxTimeOfRecord(originalRecord),originalRecord.getDeviceId(),originalRecord.getDeviceType(),originalRecord.getId());
                List<TxDeviceStopStartRecord> sortedSuceedRecords = suceedRecords.stream().filter(x -> x.getActionTimes() != null).sorted(Comparator.comparing(TxDeviceStopStartRecord::getActionTimes)).collect(Collectors.toList());
                List<TxDeviceStopStartRecord> origuianlSortedSuceedRecords = originalSuceedRecords.stream().filter(x -> x.getActionTimes() != null).sorted(Comparator.comparing(TxDeviceStopStartRecord::getActionTimes)).collect(Collectors.toList());
                if(sortedSuceedRecords.isEmpty()&&origuianlSortedSuceedRecords.isEmpty()){//本来和改后都是最后一条记录
                    Integer maxT = txRecordService.getMaxActiontims(record.getDeviceId(),record.getDeviceType());
                    if(maxT==null){
                        record.setActionTimes(1);
                    }else{
                        record.setActionTimes(maxT);
                    }
                    record.setDurationOfStart(util.calculateDuration(record).get("start"));
                    record.setDurationOfStop(util.calculateDuration(record).get("end"));

                    isSaved = txRecordService.saveOrUpdate(record);//只存一条
                }else {
                    if (sortedSuceedRecords.isEmpty() && !origuianlSortedSuceedRecords.isEmpty()) {//修改后变为最后一条记录，则所欲哦前面的记录-1

                        Integer maxT = txRecordService.getMaxActiontims(record.getDeviceId(), record.getDeviceType());
                        if (maxT == null) {
                            record.setActionTimes(1);
                        } else {
                            record.setActionTimes(maxT);
                        }
                        record.setDurationOfStart(util.calculateDuration(record).get("start"));
                        record.setDurationOfStop(util.calculateDuration(record).get("end"));
                        List<TxDeviceStopStartRecord> toUpdateList = origuianlSortedSuceedRecords.stream().filter(x -> x.getActionTimes() != null).map(x -> {
                            x.setActionTimes(x.getActionTimes() - 1);
                            return x;
                        }).collect(Collectors.toList());

                        toUpdateList.add(record);
                        isSaved = txRecordService.saveOrUpdateBatch(toUpdateList);
                    } else {
                        if (!sortedSuceedRecords.isEmpty() && origuianlSortedSuceedRecords.isEmpty()) {//往前插了

                            Integer actionTimes = sortedSuceedRecords.get(0).getActionTimes();
                            record.setActionTimes(actionTimes);
                            record.setDurationOfStart(util.calculateDuration(record).get("start"));
                            record.setDurationOfStop(util.calculateDuration(record).get("end"));
                            List<TxDeviceStopStartRecord> toUpdateList = sortedSuceedRecords.stream().filter(x -> x.getActionTimes() != null).map(x -> {
                                x.setActionTimes(x.getActionTimes() + 1);
                                return x;
                            }).collect(Collectors.toList());

                            toUpdateList.add(record);
                            isSaved = txRecordService.saveOrUpdateBatch(toUpdateList);
                        } else {//都不为空，则需要考虑往当中哪个位置插了
                            int originalSize = origuianlSortedSuceedRecords.size();
                            int size = sortedSuceedRecords.size();
                            if (originalSize > size) {//往后插
                                origuianlSortedSuceedRecords.removeAll(sortedSuceedRecords);
                                List<TxDeviceStopStartRecord> toUpdateList = origuianlSortedSuceedRecords.stream().filter(x -> x.getActionTimes() != null).map(x -> {
                                    x.setActionTimes(x.getActionTimes() - 1);
                                    return x;
                                }).collect(Collectors.toList());
                                Integer actionTimes = sortedSuceedRecords.get(0).getActionTimes();
                                record.setActionTimes(actionTimes - 1);
                                record.setDurationOfStart(util.calculateDuration(record).get("start"));
                                record.setDurationOfStop(util.calculateDuration(record).get("end"));
                                toUpdateList.add(record);
                                isSaved = txRecordService.saveOrUpdateBatch(toUpdateList);
                            } else {
                                if (originalSize < size) {//往前插
                                    sortedSuceedRecords.removeAll(origuianlSortedSuceedRecords);
                                    List<TxDeviceStopStartRecord> toUpdateList = sortedSuceedRecords.stream().filter(x -> x.getActionTimes() != null).map(x -> {
                                        x.setActionTimes(x.getActionTimes() + 1);
                                        return x;
                                    }).collect(Collectors.toList());
                                    Integer actionTimes = sortedSuceedRecords.get(0).getActionTimes();
                                    record.setActionTimes(actionTimes-1);
                                    record.setDurationOfStart(util.calculateDuration(record).get("start"));
                                    record.setDurationOfStop(util.calculateDuration(record).get("end"));
                                    toUpdateList.add(record);
                                    isSaved = txRecordService.saveOrUpdateBatch(toUpdateList);
                                } else {//还在原地
                                    record.setDurationOfStart(util.calculateDuration(record).get("start"));
                                    record.setDurationOfStop(util.calculateDuration(record).get("end"));

                                    isSaved = txRecordService.saveOrUpdate(record);//只存一条
                                }
                            }
                        }
                    }

                }
               /* if(suceedRecords.isEmpty()){//证明是最后一条记录
                    if(originalSuceedRecords.isEmpty()){//而且本来也是最后一条，只存一条，action time+1
                        Integer maxT = txRecordService.getMaxActiontims(record.getDeviceId(),record.getDeviceType());
                        if(maxT==null){
                            record.setActionTimes(1);
                        }else{
                            record.setActionTimes(maxT+1);
                        }
                        record.setDurationOfStart(util.calculateDuration(record).get("start"));
                        record.setDurationOfStop(util.calculateDuration(record).get("end"));

                        isSaved = txRecordService.saveOrUpdate(record);//只存一条
                    }else{//本来后面有记录

                        List<TxDeviceStopStartRecord> toUpdateList = originalSuceedRecords.stream().filter(x->x.getActionTimes()!=null).map(x->{
                            x.setActionTimes(x.getActionTimes()-1);
                            return x;
                        }).collect(Collectors.toList());
                        Integer maxT = txRecordService.getMaxActiontims(record.getDeviceId(),record.getDeviceType());
                        if(maxT==null){
                            record.setActionTimes(1);
                        }else{
                            record.setActionTimes(maxT+1);
                        }
                        record.setDurationOfStart(util.calculateDuration(record).get("start"));
                        record.setDurationOfStop(util.calculateDuration(record).get("end"));
                        toUpdateList.add(record);
                        isSaved = txRecordService.saveOrUpdateBatch(toUpdateList);

                    }

                }else {//不是最后一条记录
                    if (util.isEmptyRecord(originalRecord)) {//修改前的记录为空
                        List<TxDeviceStopStartRecord> sortedSuceedRecords = suceedRecords.stream().filter(x -> x.getActionTimes() != null).sorted(Comparator.comparing(TxDeviceStopStartRecord::getActionTimes)).collect(Collectors.toList());
                        if(!sortedSuceedRecords.isEmpty()) {
                            Integer minimalActionTime = sortedSuceedRecords.get(0).getActionTimes();
                            record.setActionTimes(minimalActionTime);
                            List<TxDeviceStopStartRecord> toUpdatedRecord = suceedRecords.stream().filter(x -> x.getActionTimes() != null).map(x -> {
                                Integer actionTimes = x.getActionTimes();
                                x.setActionTimes(actionTimes + 1);
                                return x;
                            }).collect(Collectors.toList());
                            record.setDurationOfStart(util.calculateDuration(record).get("start"));
                            record.setDurationOfStop(util.calculateDuration(record).get("end"));
                            toUpdatedRecord.add(record);
                            isSaved = txRecordService.saveOrUpdateBatch(toUpdatedRecord);
                        }else{//修改完是最后一条记录
                            record.setDurationOfStart(util.calculateDuration(record).get("start"));
                            record.setDurationOfStop(util.calculateDuration(record).get("end"));

                            isSaved = txRecordService.saveOrUpdate(record);//只存一条
                        }
                   }else{//修改前不为空
                        record.setDurationOfStart(util.calculateDuration(record).get("start"));
                        record.setDurationOfStop(util.calculateDuration(record).get("end"));

                        isSaved = txRecordService.saveOrUpdate(record);//只存一条
                    }

                }*/

            }else{//全为空
                record.setActionTimes(null);
                if(util.isEmptyRecord(originalRecord)) {//本来的记录也为空
                    record.setDurationOfStart(null);
                    record.setDurationOfStop(null);
                    isSaved = txRecordService.save(record);
                }else{//本来的记录不空，人为设成空
                    Integer baseActionTimes = originalRecord.getActionTimes();
                    LambdaQueryWrapper<TxDeviceStopStartRecord> qr = new LambdaQueryWrapper<>();
                    qr.gt(TxDeviceStopStartRecord::getActionTimes,baseActionTimes);
                    List<TxDeviceStopStartRecord> matchList = txRecordService.list(qr);
                    List<TxDeviceStopStartRecord> toUpdateList = matchList.stream().map(x->{
                        x.setActionTimes(x.getActionTimes()-1);
                        return x;
                    }).collect(Collectors.toList());
                    record.setDurationOfStart(null);
                    record.setDurationOfStop(null);
                    toUpdateList.add(record);
                    isSaved = txRecordService.saveOrUpdateBatch(toUpdateList);
                }

            }
        }else{
            throw new InvalidStartStopRecordException("记录开始和结束时间与其他记录冲突");
        }
        return isSaved;
    }


    @Override
    public Boolean deleteRecord(Long id) throws Exception{
        Boolean isSaved = false;
        TxDeviceStopStartRecord record = txRecordService.selectForUpdateById(id);
       if(util.isEmptyRecord(record)){
           txRecordService.removeById(record.getId());
           isSaved=true;
       }else{
           List<TxDeviceStopStartRecord> suceedRecords = txRecordService.getSuceedRecords(util.getMaxTimeOfRecord(record),record.getDeviceId(),record.getDeviceType(),record.getId());
           List<TxDeviceStopStartRecord> toUpdatedRecord = suceedRecords.stream().filter(x->x.getActionTimes()!=null).map(x->{
               Integer actionTimes= x.getActionTimes();
               x.setActionTimes(actionTimes-1);
               return x;
           }).collect(Collectors.toList());

           isSaved = txRecordService.saveOrUpdateBatch(toUpdatedRecord);
           isSaved = txRecordService.removeById(record.getId());
       }
       return isSaved;
    }

    @Override
    public List<TxDeviceStopStartRecord> getRecordsByDeviceIdAndType(Long deviceId,Integer deviceType) {
        LambdaQueryWrapper<TxDeviceStopStartRecord> qr = new LambdaQueryWrapper<>();
        qr.eq(TxDeviceStopStartRecord::getDeviceId,deviceId).eq(TxDeviceStopStartRecord::getDeviceType,deviceType);
        List<TxDeviceStopStartRecord> originList = txRecordService.list(qr);
        List<TxDeviceStopStartRecord> noNullList = originList.stream().filter(x->x.getActionTimes()!=null).sorted(Comparator.comparing(TxDeviceStopStartRecord::getActionTimes).reversed()).collect(Collectors.toList());
        List<TxDeviceStopStartRecord> nullList = originList.stream().filter(x->x.getActionTimes()==null).collect(Collectors.toList());
        List<TxDeviceStopStartRecord> resultList = new LinkedList<>();
        resultList.addAll(noNullList);
        resultList.addAll(nullList);
        return resultList;
    }

    @Override
    public TxDeviceStopStartRecord getRecordById(Long deviceId) {
        return txRecordService.getById(deviceId);
    }
}
