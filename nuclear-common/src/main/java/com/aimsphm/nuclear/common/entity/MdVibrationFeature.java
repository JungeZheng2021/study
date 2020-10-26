package com.aimsphm.nuclear.common.entity;

import com.aimsphm.nuclear.common.entity.ModelBase;
import lombok.Data;

/**
 * 
 *
 * @author lu.yi
 * @since 2020-08-14
 */
@Data
public class MdVibrationFeature extends ModelBase {
    private static final long serialVersionUID = 1105149893600864383L;
    private String vifPostfix;
    private String vifName;
    private Integer ccs;
    private Integer cwsW;
    private Integer cwsO;
    private Integer sws;
    private Integer fws;
    private Integer fwsM;
    private Integer fwsG;
    private String desCodes;

    public String getGroupName() {
        if (vifPostfix.startsWith("acc")) {
            return "acc";
        } else if (vifPostfix.startsWith("vec")) {
            return "vec";
        } else if (vifPostfix.startsWith("temp")) {
            return "temp";
        }
        return "other";
    }
    public String getOrderString(){
        if(vifPostfix.equals("vec-Rms"))
        {
            return "0";
        }else return vifPostfix;
    }

}