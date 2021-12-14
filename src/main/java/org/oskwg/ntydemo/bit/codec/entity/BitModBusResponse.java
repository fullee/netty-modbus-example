package org.oskwg.ntydemo.bit.codec.entity;

import lombok.Data;

import java.util.List;

@Data
public class BitModBusResponse {


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

    List<Byte> values;

    public BitModBusResponse() {

    }

    public BitModBusResponse(short sequenceId, List<Byte> values) {
        this.sequenceId = sequenceId;
        this.values = values;
    }
}
