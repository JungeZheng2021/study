package com.aimsphm.nuclear.core.util;

import com.aimsphm.nuclear.common.entity.TxDeviceStopStartRecord;
import com.aimsphm.nuclear.common.service.TxDeviceStopStartRecordService;
import com.aimsphm.nuclear.core.entity.bo.TimeComparisionBO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.*;
import java.util.stream.Collectors;

@Component
public class DeviceStartAndStopUtil {


    @Autowired
    private TxDeviceStopStartRecordService recordService;


    public TxDeviceStopStartRecord findPreviousRecord(Date baseDate,Long deviceId,Integer deviceType,Long exceptionalId) {

        //按照4个 时间分别取到前一条记录
        TxDeviceStopStartRecord s1 = recordService.getPreviousRecordByColumn("start_begin_time", baseDate,deviceId,deviceType,exceptionalId);
        TxDeviceStopStartRecord s2 = recordService.getPreviousRecordByColumn("start_end_time", baseDate,deviceId,deviceType,exceptionalId);
        TxDeviceStopStartRecord e1 = recordService.getPreviousRecordByColumn("stop_begin_time", baseDate,deviceId,deviceType,exceptionalId);
        TxDeviceStopStartRecord e2 = recordService.getPreviousRecordByColumn("stop_end_time", baseDate,deviceId,deviceType,exceptionalId);

        Set<TxDeviceStopStartRecord> ressultSet = new HashSet<>();
        ressultSet.add(s1);
        ressultSet.add(s2);
        ressultSet.add(e1);
        ressultSet.add(e2);

        List<TxDeviceStopStartRecord> resultList = ressultSet.stream().filter(x->x!=null).collect(Collectors.toList());

        List<TimeComparisionBO> comparisionBOList = new ArrayList<>();
        for (TxDeviceStopStartRecord rec : resultList) {
            TimeComparisionBO cs1 = new TimeComparisionBO();
            cs1.setId(rec.getId());
            cs1.setTime(rec.getStartBeginTime());

            TimeComparisionBO cs2 = new TimeComparisionBO();
            cs2.setId(rec.getId());
            cs2.setTime(rec.getStartEndTime());

            TimeComparisionBO ce1 = new TimeComparisionBO();
            ce1.setId(rec.getId());
            ce1.setTime(rec.getStopBeginTime());

            TimeComparisionBO ce2 = new TimeComparisionBO();
            ce2.setId(rec.getId());
            ce2.setTime(rec.getStopEndTime());

            comparisionBOList.add(cs1);
            comparisionBOList.add(cs2);
            comparisionBOList.add(ce1);
            comparisionBOList.add(ce2);

        }

        Optional<TimeComparisionBO> resultOptional = comparisionBOList.stream().filter(x -> x.getTime() != null).sorted(Comparator.comparing(TimeComparisionBO::getTime).reversed()).findFirst();

        TxDeviceStopStartRecord resultRecord = null;
        if (resultOptional.isPresent()) {
            for (TxDeviceStopStartRecord rec : resultList) {
                if (rec.getId() == resultOptional.get().getId()) {
                    resultRecord = rec;
                    break;
                }
            }
        }

        return resultRecord;
    }

    public TxDeviceStopStartRecord findNextRecord(Date baseDate,Long deviceId,Integer deviceType,Long exceptionalId) {
        TxDeviceStopStartRecord s1 = recordService.getNextRecordByColumn("start_begin_time", baseDate,deviceId,deviceType,exceptionalId);
        TxDeviceStopStartRecord s2 = recordService.getNextRecordByColumn("start_end_time", baseDate,deviceId,deviceType,exceptionalId);
        TxDeviceStopStartRecord e1 = recordService.getNextRecordByColumn("stop_begin_time", baseDate,deviceId,deviceType,exceptionalId);
        TxDeviceStopStartRecord e2 = recordService.getNextRecordByColumn("stop_end_time", baseDate,deviceId,deviceType,exceptionalId);

        Set<TxDeviceStopStartRecord> ressultSet = new HashSet<>();
        ressultSet.add(s1);
        ressultSet.add(s2);
        ressultSet.add(e1);
        ressultSet.add(e2);
        List<TxDeviceStopStartRecord> resultList = ressultSet.stream().filter(x->x!=null).collect(Collectors.toList());

        List<TimeComparisionBO> comparisionBOList = new ArrayList<>();
        for (TxDeviceStopStartRecord rec : resultList) {
            TimeComparisionBO cs1 = new TimeComparisionBO();
            cs1.setId(rec.getId());
            cs1.setTime(rec.getStartBeginTime());

            TimeComparisionBO cs2 = new TimeComparisionBO();
            cs2.setId(rec.getId());
            cs2.setTime(rec.getStartEndTime());

            TimeComparisionBO ce1 = new TimeComparisionBO();
            ce1.setId(rec.getId());
            ce1.setTime(rec.getStopBeginTime());

            TimeComparisionBO ce2 = new TimeComparisionBO();
            ce2.setId(rec.getId());
            ce2.setTime(rec.getStopEndTime());

            comparisionBOList.add(cs1);
            comparisionBOList.add(cs2);
            comparisionBOList.add(ce1);
            comparisionBOList.add(ce2);

        }

        Optional<TimeComparisionBO> resultOptional = comparisionBOList.stream().filter(x -> x.getTime() != null).sorted(Comparator.comparing(TimeComparisionBO::getTime)).findFirst();

        TxDeviceStopStartRecord resultRecord = null;
        if (resultOptional.isPresent()) {
            for (TxDeviceStopStartRecord rec : resultList) {
                if (rec.getId() == resultOptional.get().getId()) {
                    resultRecord = rec;
                    break;
                }
            }
        }

        return resultRecord;
    }

    public Boolean isValidateRecord(TxDeviceStopStartRecord record) {
        Boolean isValid = false;


        Date baseStartTime = getMinimalTimeOfRecord(record);
        Date baseEndTime = getMaxTimeOfRecord(record);
        if (baseStartTime == null) {//是一条空记录
            isValid = true;
        } else {
            TxDeviceStopStartRecord previous = findPreviousRecord(baseEndTime,record.getDeviceId(),record.getDeviceType(),record.getId());//万一有重合
            TxDeviceStopStartRecord next = findNextRecord(baseStartTime,record.getDeviceId(),record.getDeviceType(),record.getId());//万一有重合
            if(previous!=null&&next!=null){
                Date previousBaseEndTime = getMaxTimeOfRecord(previous);
                Date nextBaseStartTime = getMinimalTimeOfRecord(next);
                if (baseStartTime.after(previousBaseEndTime) && baseEndTime.before(nextBaseStartTime)) {
                    isValid = true;
                }
            }else{
                if(previous!=null&&next==null){
                    Date previousBaseEndTime = getMaxTimeOfRecord(previous);
                    if (baseStartTime.after(previousBaseEndTime)) {
                        isValid = true;
                    }
                }else{
                    if(previous==null&&next!=null){
                        Date nextBaseStartTime = getMinimalTimeOfRecord(next);
                        if (baseEndTime.before(nextBaseStartTime)) {
                            isValid = true;
                        }
                    }else{
                        isValid=true;
                    }
                }
            }


        }

        return isValid;
    }

  /*  public Integer countValidRecord() {

        LambdaQueryWrapper<TxDeviceStopStartRecord> wrapper = new LambdaQueryWrapper<>();


        return recordService.count(wrapper.isNotNull(TxDeviceStopStartRecord::getStartBeginTime).or().isNotNull(TxDeviceStopStartRecord::getStartEndTime)
                .or().isNotNull(TxDeviceStopStartRecord::getStopBeginTime).or().isNotNull(TxDeviceStopStartRecord::getStopEndTime));
    }*/

    public List<Date> genTimeSequence(TxDeviceStopStartRecord record) {
        List<Date> timeList = new ArrayList<>();
        timeList.add(record.getStartBeginTime());
        timeList.add(record.getStartEndTime());
        timeList.add(record.getStopBeginTime());
        timeList.add(record.getStopEndTime());
        List<Date> filteredList = timeList.stream().filter(x -> x != null).sorted().collect(Collectors.toList());
        return filteredList;
    }

    public Date getMinimalTimeOfRecord(TxDeviceStopStartRecord record) {
        List<Date> timeSeqList = genTimeSequence(record);
        Date result = null;
        if (!timeSeqList.isEmpty()) {
            result = timeSeqList.get(0);
        }
        return result;
    }

    public Date getMaxTimeOfRecord(TxDeviceStopStartRecord record) {
        List<Date> timeSeqList = genTimeSequence(record);
        Date result = null;
        if (!timeSeqList.isEmpty()) {
            result = timeSeqList.get(timeSeqList.size() - 1);
        }
        return result;
    }


    public Boolean isEmptyRecord(TxDeviceStopStartRecord record){
        return record.getStartBeginTime()==null&&record.getStartEndTime()==null&&record.getStopBeginTime()==null&&record.getStopEndTime()==null?true:false;
    }

    public Map<String,Long> calculateDuration(TxDeviceStopStartRecord record){
        Map<String,Long> result = new HashMap<>();
        if(record.getStartBeginTime()!=null && record.getStartEndTime()!=null){
            Long gap = record.getStartEndTime().getTime()-record.getStartBeginTime().getTime();
            result.put("start",gap);
        }else{
            result.put("start",null);
        }

        if(record.getStopBeginTime()!=null && record.getStopEndTime()!=null){
            Long gap = record.getStopEndTime().getTime()-record.getStopBeginTime().getTime();
            result.put("end",gap);
        }else{
            result.put("end",null);
        }
        return result;
    }
}
