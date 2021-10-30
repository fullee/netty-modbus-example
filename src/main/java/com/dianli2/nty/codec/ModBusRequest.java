package com.dianli2.nty.codec;

import lombok.Data;

@Data
public class ModBusRequest {

    /**
     * 报文标识
     */
    short sequenceId;
    /**
     * 协议标识符[固定]
     */
    short identifier;
    /**
     * 数据长度
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
     * 起始寄存器
     */
    short startAddress;
    /**
     * 寄存器个数（1个寄存器占两个字节）
     */
    int count;

    public ModBusRequest() {

    }

    public ModBusRequest(short sequenceId,short startAddress,int count) {
        if (count > 126 || count % 2 != 0) {
            throw new RuntimeException("addressCount can not >126, 126 * 2 = 252 byte");
        }
        this.sequenceId = sequenceId;
        this.startAddress = startAddress;
        this.count = count;
    }

}
