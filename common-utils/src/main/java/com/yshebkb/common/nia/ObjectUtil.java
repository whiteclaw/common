package com.yshebkb.common.nia;


import java.io.*;

/**
 * @author whiteclaw
 * created on 2018-12-07
 */
public class ObjectUtil {

    /**
     * 深度克隆java对象
     *
     * @param obj 原对象
     * @return 克隆对象
     * @throws IOException            读取原对象和写入克隆对象的时候可能会发生IO异常
     * @throws ClassNotFoundException 从流中读取克隆对象的时候, 可能发生对象的class类型找不到的异常
     */

    public static <T extends Serializable> T deepClone(T obj) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object cloneObj;
        try {
            cloneObj = ois.readObject();
        } finally {
            oos.close();
            ois.close();
        }
        // 因为输入类型是泛型T, 所以这样做是可以的
        @SuppressWarnings("unchecked") final T checked = (T) cloneObj;
        return checked;
    }
}
