package org.oskwg.ntydemo.nty.codec;

import lombok.Data;

import java.util.List;

@Data
public class ModBusResponse {


    /**
     * 报文标识
     */
    short sequenceId;
    /**
     * 协议标识符[固定]
     */
    short identifier;
    /**
     * 协议数据长度
     */
    long protocolLength;
    /**
     * Modbus TCP单元标识符[固定]
     */
    byte unitIdentifier;
    /**
     * 功能码
     */
    int funCode;
    /**
     * 返回寄存器字节数
     */
    int valueByteLength;

    List<Float> values;

    public ModBusResponse() {

    }

    public ModBusResponse(short sequenceId,List<Float> values) {
        this.sequenceId = sequenceId;
        this.values = values;
    }
}
