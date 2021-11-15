package com.aimsphm.nuclear.opc.model;

import lombok.*;
import org.openscada.opc.dcom.list.ClassDetails;

import java.io.Serializable;

/**
 * @author Administrator
 */
@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class ServerInfo extends ClassDetails implements Serializable {
    private String progId;
    private String clsId;
    private String description;
}
