

package com.bytecub.common.domain;

import com.bytecub.common.enums.BCErrorEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.function.Function;

/**
 * <pre>
 * 名称: 统一返回API数据格式
 * 描述: DataResult响应实体
 * </pre>
 *
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataResult<T> {

    private static final long serialVersionUID = -7391197880029846491L;

    /**
     * 状态
     */
    private int code = 200;
    /**
     * 消息
     */
    private String msg;
    /**
     * 数据
     */
    private T    data;
    /**
     * 时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8" )
    private Date time = new Date();


    /**
     * 构造方法
     * @param data 数据
     */
    public DataResult(final T data) {
        this(200, null, data, new Date());
    }

    public DataResult(final int code, final String msg, final T data) {
        this(code, msg, data, new Date());
    }



    public static <T> DataResult<T> genResult(BCErrorEnum bcErrorEnum, T data) {
        DataResult<T> result = new DataResult<T>();
        result.setCode(bcErrorEnum.getCode());
        result.setMsg(bcErrorEnum.getMsg());
        result.setData(data);
        return result;
    }
    /**
     * 失败实体
     * @param code  状态码
     * @param msg 消息
     * @param result  结果
     * @param <V> 继承自 BaseResponse
     * @return dataResult
     */
    public static <V> DataResult<V> fail(final int code, final String msg, final V result) {
        return new DataResult<V>(code, msg, result, new Date());
    }

    /**
     * 失败实体
     * @param message 消息
     * @param <V> 继承自 BaseResponse
     * @return dataResult
     */
    public static <V> DataResult<V> fail(final String message) {
        DataResult<V> result = new DataResult<V>();
        result.setMsg(message);
        result.setCode(BCErrorEnum.FAIL.getCode());
        return result;
    }

    /**
     * 失败实体
     * @param code  状态码
     * @param message 消息
     * @param <V> 继承自 BaseResponse
     * @return dataResult
     */
    public static <V> DataResult<V> fail(final int code, final String message) {
        DataResult<V> result = new DataResult<V>();
        result.setCode(code);
        result.setMsg(message);
        return result;
    }

    /**
     * 失败实体
     *
      * @author: gaojing [gaojing1996@vip.qq.com]
      */
    public static <V> DataResult<V> fail(final BCErrorEnum errorEnum) {
        DataResult<V> result = new DataResult<V>();
        result.setCode(errorEnum.getCode());
        result.setMsg(errorEnum.getMsg());
        return result;
    }

    /**
     * 成功实体
     * @param <V> 继承自 BaseResponse
     * @return dataResult
     */
    public static <V> DataResult<V> ok() {
        DataResult<V> result = new DataResult<V>();
        result.setCode(BCErrorEnum.SUCCESS.getCode());
        result.setMsg(BCErrorEnum.SUCCESS.getMsg());
        return result;
    }

    /**
     * 成功实体
     * @param data  数据
     * @param <V> 继承自 BaseResponse
     * @return dataResult
     */
    public static <V> DataResult<V> ok(final V data) {
        DataResult<V> result = new DataResult<V>();
        result.setCode(BCErrorEnum.SUCCESS.getCode());
        result.setMsg(BCErrorEnum.SUCCESS.getMsg());
        result.setData(data);
        return result;
    }

    /**
     * 成功实体
     * @param data  数据
     * @param message 消息
     * @param <V> 继承自 BaseResponse
     * @return dataResult
     */
    public static <V> DataResult<V> ok(final String message, final V data) {
        DataResult<V> result = new DataResult<V>();
        result.setCode(BCErrorEnum.SUCCESS.getCode());
        result.setMsg(message);
        result.setData(data);
        return result;
    }

    /**
     * 定制化输出
     * @param fun the fun
     * @return string
     */
    public String toString(final Function<DataResult<T>, String> fun) {
        if (null == fun) {
            return toString();
        }
        return fun.apply(this);
    }
}
