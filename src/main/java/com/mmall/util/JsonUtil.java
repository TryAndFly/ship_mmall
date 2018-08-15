package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.text.SimpleDateFormat;

@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(Inclusion.ALWAYS);

        //取消默认转换timestamps形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);

        //忽略空bean转json的错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        //所有的日期格式统一
        objectMapper.setDateFormat(new SimpleDateFormat(Util.STANDARD_FORMAT));

        //忽略在json字符串中存在但在java中不存在对应属性的情况
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    public static <T> String objToString(T obj) {
        if (obj == null)
            return null;

        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
//            e.printStackTrace();
            log.warn("Parse object to String error ", e);
            return null;

        }
    }

    public static <T> String objToStringPretty(T obj) {
        if (obj == null)
            return null;

        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
//            e.printStackTrace();
            log.warn("Parse object to String error ", e);
            return null;

        }
    }

    //如果要对List<User>这类的集合进行反序列化时会出现问题
    public static <T> T string2Obj(String str, Class<T> clazz) {
        if (StringUtils.isEmpty(str) || clazz == null) {
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (Exception e) {
//            e.printStackTrace();
            log.warn("Parse string to obj error ", e);
            return null;
        }
    }

    /**
     *     使用此方法对集合类进行反序列化
     *     eg:JsonUtil.strToObj(userListStr,new TypeReference<List<User>>)
     */
    public static <T> T strToObj(String str, TypeReference<T> tTypeReference){
        if (StringUtils.isEmpty(str) || tTypeReference == null) {
            return null;
        }
        try {
            return (T)(tTypeReference.getType().equals(String.class) ?  str : objectMapper.readValue(str, tTypeReference));
        } catch (Exception e) {
//            e.printStackTrace();
            log.warn("Parse string to obj error ", e);
            return null;
        }
    }

    /**
     *     使用此方法对集合类进行反序列化
     *     eg:JsonUtil.strToObj(userListStr,List.Class,User.class)
     */
    public static <T> T strToObj(String str, Class<?> collectionClass,Class<?>... elementClasses){

        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClasses);

        try {
            return objectMapper.readValue(str,javaType);
        } catch (Exception e) {
//            e.printStackTrace();
            log.warn("Parse string to obj error ", e);
            return null;
        }
    }
}
