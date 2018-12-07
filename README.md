# Java工具类

## 1. add dependency
```xml
<dependency>
    <groupId>com.whiteclaw.common</groupId>
    <artifactId>common-utils</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 2. Usage

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
class Sample {
    Integer id;
    String name;
}

public class Test{
    public static void main(String[] args){
      Sample sample = new Sample(1, "jackson");
      String result = JacksonUtil.obj2Json(sample);
      System.out.println(result);
    }
}
```