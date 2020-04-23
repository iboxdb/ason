# Ason

Ason is a dynamic object notation by using Array. the definition is sample, if the first element of the Array ends with ':', this Array is a representation of Key-Value pairs.

#### Getting Started

```java
import static iBoxDB.LocalServer.Ason.*;

public class AsonExample {
     
    public static void main(String[] args)
    {
        var obj = ason("Name:", "Andy", "Value:", 100);
                
        System.out.println(obj.getClass());
        System.out.println(obj);
    }
}
```

**Output**

```
class java.util.HashMap
{Value=100, Name=Andy}
```
