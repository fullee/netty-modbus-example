package org.oskwg.ntydemo.bit.codec.entity;

import lombok.Data;

@Data
public class BitModBusRequest {

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
     * bit数
     */
    int count;

    public BitModBusRequest() {

    }

    public BitModBusRequest(short sequenceId, short startAddress, int count) {
        // 报文长度标识是一字节，最大能表示256个字节 256*8个bit位
        if (count > 2048) {
            throw new RuntimeException("bitCount can not >2048, 256byte * 8 = 2048 bit");
        }
        this.sequenceId = sequenceId;
        this.startAddress = startAddress;
        this.count = count;
    }

}
