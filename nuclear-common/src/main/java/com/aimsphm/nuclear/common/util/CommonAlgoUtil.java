package com.aimsphm.nuclear.common.util;

import com.aimsphm.nuclear.common.entity.dto.Cell;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class CommonAlgoUtil {


    public static List<Cell> getVirtureSensorData(Map<String, List<Cell>> sensorMap, String formula) {
        List<List<Cell>> cellList = Lists.newArrayList();
        Iterator<Map.Entry<String, List<Cell>>> it = sensorMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<Cell>> entry = it.next();
            cellList.add(entry.getValue());
        }
        List<Cell> virtualSensorData = Lists.newArrayList();
        List<Long> sortedTsList = alignTime(false, cellList);
        for (int i = 0; i < sortedTsList.size(); i++) {
            Long timestamp = sortedTsList.get(i);
            String newformula = formula;
            for (String key : sensorMap.keySet()) {
                List<Cell> cells = sensorMap.get(key);
                String value = cells.get(i).getValue().toString();
                newformula = replaceStr(newformula, key, value);
            }

            Double newvalue = calcVP(newformula);
            Cell cell = new Cell(timestamp, newvalue);
            virtualSensorData.add(cell);
        }
        return virtualSensorData;
    }

    /**
     * 精确替换字符 防止出现 匹配A 时将AA匹配的情况
     */

    /**
     * 精确替换字符 防止出现 匹配A 时将AA匹配的情况
     */

    public static String replaceStr(String sourceStr, String replaceKey, String replaceValue) {
        String replaceStrReg = "";
        for (char str_char : replaceKey.toCharArray()) {
            replaceStrReg += "[";
            replaceStrReg += str_char;
            replaceStrReg += "]";
        }
        String startReg = "^" + replaceStrReg + "([\\+\\-\\*/,)])";
        String endReg = "([\\+\\-\\*/,(])" + replaceStrReg + "$";
        String reg = "([^a-zA-Z])(" + replaceStrReg + ")" + "([^a-zA-Z])";
        String endStr = sourceStr;
        while (matcheStr(endStr, replaceKey)) {
            endStr = endStr.replaceAll(startReg, replaceValue + "$1");
            endStr = endStr.replaceAll(reg, "$1" + replaceValue + "$3");
            endStr = endStr.replaceAll(endReg, "$1" + replaceValue);
        }
        return endStr;
    }

    /**
     * 精确匹配字符 防止出现 匹配A 时将AA匹配的情况
     */
    public static Boolean matcheStr(String sourceStr, String matchStr) {
        String replaceStrReg = "";
        for (char str_char : matchStr.toCharArray()) {
            replaceStrReg += "[";
            replaceStrReg += str_char;
            replaceStrReg += "]";
        }
        String startReg = "^" + replaceStrReg + "([\\+\\-\\*/,)])[\\s\\S]*";
        String endReg = "[\\s\\S]*([\\+\\-\\*/,(])" + replaceStrReg + "$";
        String reg = "[\\s\\S]*([^A-Za-z])(" + replaceStrReg + ")" + "([^A-Za-z])[\\s\\S]*";
        if (sourceStr.matches(startReg) || sourceStr.matches(reg) || sourceStr.matches(endReg)) {
            return true;
        } else {
            return false;
        }
    }

    public static List<Long> alignTime(boolean byBean, List<List<Cell>> cells) {
        Collection<Long> timestampSet = Sets.newHashSet();
        Map<Integer, Double> meanMap = Maps.newHashMap();
        for (int i = 0; i < cells.size(); i++) {
            List<Cell> listone = cells.get(i);
            double meantotal = 0;
            // listone.stream().filter(e->!ObjectUtils.isEmpty(e.getValue())).map(a -> Long.parseLong(a.getValue().toString())).mapToDouble(s->s).average().getAsDouble();
            int count = 0;
            for (Cell cell : listone) {
                timestampSet.add(cell.getTimestamp());
                if (!ObjectUtils.isEmpty(cell.getValue())) {
                    meantotal += (Double) cell.getValue();
                    count++;
                }
            }
            double mean = count != 0 ? meantotal / count : 0;
            meanMap.put(i, mean);
        }
        List<Long> sortedTsList = new ArrayList<>(timestampSet).stream().sorted().collect(Collectors.toList());
        for (int i = 0; i < cells.size(); i++) {
            List<Cell> listone = cells.get(i);
            double previousV = 0;
            boolean hasSetPreviousV=false;
            Map<Long, Object> map = listone.stream().collect(Collectors.toMap(Cell::getTimestamp, Cell::getValue));
            Double mean = meanMap.get(i);
            for (int j = 0; j < sortedTsList.size(); j++) {
                Long timestamp = sortedTsList.get(j);
                if (ObjectUtils.isEmpty(map.get(timestamp))) {
                    if (byBean) {
                        map.put(timestamp, mean);
                    } else {
                        if (hasSetPreviousV) {
                            map.put(timestamp, previousV);
                        } else {
                            map.put(timestamp, mean);
                        }
                    }
                } else {
                    previousV = (Double) map.get(timestamp);
                    hasSetPreviousV = true;
                }
            }
            List<Cell> listone_new = map.entrySet().stream().map(en -> new Cell(en.getKey(), en.getValue())).collect(Collectors.toList());
            listone_new = listone_new.stream().sorted(Comparator.comparing(Cell::getTimestamp)).collect(Collectors.toList());
            listone.clear();
            listone.addAll(listone_new);
        }
        return sortedTsList;
    }

    public static double calcVP(String formula) {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("JavaScript");
        Object calcResult = null;
        try {
            calcResult = engine.eval(formula);
        } catch (ScriptException e) {
// TODO: handle exception
            log.error("call formula error!", e);
            e.printStackTrace();
        }
        if (calcResult == null) {
            calcResult = "0";
        }
        double expVal = Double.parseDouble(calcResult.toString());
        return expVal;
    }

    public static void main(String[] args) {
        System.out.println(calcVP("(54.591957+7808.5)*3"));
    }
}
