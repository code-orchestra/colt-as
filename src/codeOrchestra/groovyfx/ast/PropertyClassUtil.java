package codeOrchestra.groovyfx.ast;

import javafx.beans.property.*;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;

import java.util.HashMap;
import java.util.Map;

import static org.codehaus.groovy.ast.ClassHelper.make;

/**
 * @author Eugene Potapenko
 */
public class PropertyClassUtil {

    private static final Map<ClassNode, ClassNode> propertyTypeMap = new HashMap<>();
    private static final Map<ClassNode, ClassNode> propertyImplMap = new HashMap<>();

    public static final ClassNode BOOLEAN_PROPERTY = make(BooleanProperty.class);
    public static final ClassNode DOUBLE_PROPERTY = make(DoubleProperty.class);
    public static final ClassNode FLOAT_PROPERTY = make(FloatProperty.class);
    public static final ClassNode INT_PROPERTY = make(IntegerProperty.class);
    public static final ClassNode LONG_PROPERTY = make(LongProperty.class);
    public static final ClassNode STRING_PROPERTY = make(StringProperty.class);
    public static final ClassNode LIST_PROPERTY = make(ListProperty.class);
    public static final ClassNode MAP_PROPERTY = make(MapProperty.class);
    public static final ClassNode SET_PROPERTY = make(SetProperty.class);

    public static final ClassNode SIMPLE_BOOLEAN_PROPERTY = make(SimpleBooleanProperty.class);
    public static final ClassNode SIMPLE_DOUBLE_PROPERTY = make(SimpleDoubleProperty.class);
    public static final ClassNode SIMPLE_FLOAT_PROPERTY = make(SimpleFloatProperty.class);
    public static final ClassNode SIMPLE_INT_PROPERTY = make(SimpleIntegerProperty.class);
    public static final ClassNode SIMPLE_LONG_PROPERTY = make(SimpleLongProperty.class);
    public static final ClassNode SIMPLE_STRING_PROPERTY = make(SimpleStringProperty.class);

    public static final ClassNode SIMPLE_LIST_PROPERTY = make(SimpleListProperty.class);
    public static final ClassNode SIMPLE_MAP_PROPERTY = make(SimpleMapProperty.class);
    public static final ClassNode SIMPLE_SET_PROPERTY = make(SimpleSetProperty.class);

    static {
        propertyTypeMap.put(ClassHelper.STRING_TYPE, STRING_PROPERTY);
        propertyTypeMap.put(ClassHelper.boolean_TYPE, BOOLEAN_PROPERTY);
        propertyTypeMap.put(ClassHelper.Boolean_TYPE, BOOLEAN_PROPERTY);
        propertyTypeMap.put(ClassHelper.double_TYPE, DOUBLE_PROPERTY);
        propertyTypeMap.put(ClassHelper.Double_TYPE, DOUBLE_PROPERTY);
        propertyTypeMap.put(ClassHelper.float_TYPE, FLOAT_PROPERTY);
        propertyTypeMap.put(ClassHelper.Float_TYPE, FLOAT_PROPERTY);
        propertyTypeMap.put(ClassHelper.int_TYPE, INT_PROPERTY);
        propertyTypeMap.put(ClassHelper.Integer_TYPE, INT_PROPERTY);
        propertyTypeMap.put(ClassHelper.long_TYPE, LONG_PROPERTY);
        propertyTypeMap.put(ClassHelper.Long_TYPE, LONG_PROPERTY);
        propertyTypeMap.put(ClassHelper.short_TYPE, INT_PROPERTY);
        propertyTypeMap.put(ClassHelper.Short_TYPE, INT_PROPERTY);
        propertyTypeMap.put(ClassHelper.byte_TYPE, INT_PROPERTY);
        propertyTypeMap.put(ClassHelper.Byte_TYPE, INT_PROPERTY);
        propertyTypeMap.put(ClassHelper.char_TYPE, INT_PROPERTY);
        propertyTypeMap.put(ClassHelper.Character_TYPE, INT_PROPERTY);

        propertyImplMap.put(BOOLEAN_PROPERTY, SIMPLE_BOOLEAN_PROPERTY);
        propertyImplMap.put(DOUBLE_PROPERTY, SIMPLE_DOUBLE_PROPERTY);
        propertyImplMap.put(FLOAT_PROPERTY, SIMPLE_FLOAT_PROPERTY);
        propertyImplMap.put(INT_PROPERTY, SIMPLE_INT_PROPERTY);
        propertyImplMap.put(LONG_PROPERTY, SIMPLE_LONG_PROPERTY);
        propertyImplMap.put(STRING_PROPERTY, SIMPLE_STRING_PROPERTY);
        propertyImplMap.put(LIST_PROPERTY, SIMPLE_LIST_PROPERTY);
        propertyImplMap.put(MAP_PROPERTY, SIMPLE_MAP_PROPERTY);
        propertyImplMap.put(SET_PROPERTY, SIMPLE_SET_PROPERTY);
    }

    public static ClassNode getPropertyForType(ClassNode node){
        return propertyTypeMap.get(node);
    }

    public static ClassNode getPropertyImplementation(ClassNode node){
        return propertyImplMap.get(node);
    }
}
