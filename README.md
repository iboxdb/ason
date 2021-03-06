# Ason

Ason is a dynamic object notation by using Array. the definition is simple, if the first element of the Array ends with ':', this Array is a representation of Key-Value pairs.

#### Getting Started

```java
import static iboxdb.localserver.Ason.*;

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

```java
class java.util.HashMap
{Value=100, Name=Andy}
```

##### Hiberarchy

```java
var obj = ason("Name:", "Andy", "Value:", ason( "SubName:", "SubAndy" ) );
```

**Output**

```java
{Value={SubName=SubAndy}, Name=Andy}
```

##### Use class Ason can call a function

```java
import iboxdb.localserver.*;
import static iboxdb.localserver.Ason.*;
import static iboxdb.localserver.IFunction.*;

public class AsonExample {

    public static void main(String[] args) {
        var obj = new Ason("Name:", "Andy", "Add:", func((inputs) -> {
            return (Integer) inputs[0] + (Integer) inputs[1];
        }));
 
        System.out.println(obj.invoke("Add", 100, 200));
    }
}
```
the Array passed through "new Ason(array)" always dynamic object array, it doesn't check the ':' flag.




if the first element not ends with ':' , it will back to Array , as "new Object[]"

```java
var obj = new Ason("Name:", "Andy", "Addresses:", ason("Addr_01", "Addr_02"));

Object[] os = (Object[]) obj.get("Addresses");
System.out.println(os[0] + " , " + os[1]);
```

if an Array ends with ':', but it isn't dynamic object, just new an Array, 

```java
var obj = new Object[]{ "ABC:", "DEF:"};
```
