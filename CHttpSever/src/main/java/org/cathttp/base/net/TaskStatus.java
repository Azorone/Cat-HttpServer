package org.cathttp.base.net;

public enum TaskStatus {
    //注册阶段
    REGISTER_WAIT(1),
    REGISTER_END(2),

    //读阶段
    READ_WAIT(3),
    READ_RUN(4),
    READ_NOT_COMPLETE(5),
    READ_END(6),

    //解码阶段
    DECODE_WAIT(7),
    DECODE_RUN(8),
    DECODE_END(9),


    //处理阶段
    HANDLE_WAIT(10),
    HANDLE_RUN(11),
    HANDLE_END(12),

    //编码阶段
    ENCODE_WAIT(13),
    ENCODE_RUN(14),
    ENCODE_END(25),
    //写阶段
    WRITE_WAIT(15),
    WRITE_RUN(16),
    WRITE_NOT_COMPLETE(17),
    WRITE_END(18),

    //阶段错误
    REGISTRY_ERROR(19),
    READ_ERROR(20),
    DECODE_ERROR(21),
    HANDLE_ERROR(22),
    ENCODE_ERROR(23),
    WRITE_ERROR(24),

    TASK_START(0),
    TASK_END(-1)
    ;


    final int value;
   TaskStatus(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
