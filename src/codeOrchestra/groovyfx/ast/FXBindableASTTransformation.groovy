package codeOrchestra.groovyfx.ast

import codeOrchestra.groovyfx.FXBindable
import groovy.transform.TypeChecked
import javafx.application.Application
import javafx.beans.property.*
import javafx.collections.FXCollections
import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.messages.SyntaxErrorMessage
import org.codehaus.groovy.runtime.MetaClassHelper
import org.codehaus.groovy.syntax.SyntaxException
import org.codehaus.groovy.syntax.Token
import org.codehaus.groovy.syntax.Types
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.objectweb.asm.Opcodes

import static codeOrchestra.groovyfx.ast.PropertyClassUtil.*
import static org.codehaus.groovy.ast.ClassHelper.getWrapper
import static org.codehaus.groovy.ast.ClassHelper.make

/**
 * @author jimclarke (inspired by Danno Ferrin (shemnon) and Chris Reeved)
 * @author Dean Iverson
 * @author Eugene Potapenko
 */

@SuppressWarnings("GroovyUnusedDeclaration")
@TypeChecked
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class FXBindableASTTransformation implements ASTTransformation, Opcodes {

    private static final ClassNode BOUND_CLASS_NODE = make(FXBindable);

    private static final ClassNode OBSERVABLE_LIST = make(javafx.collections.ObservableList, true);
    private static final ClassNode OBSERVABLE_MAP = make(javafx.collections.ObservableMap, true);
    private static final ClassNode OBSERVABLE_SET = make(javafx.collections.ObservableSet, true);
    private static final ClassNode FX_COLLECTIONS_TYPE = make(FXCollections, true);
    private static final ClassNode LIST_TYPE = make(List, true);
    private static final ClassNode MAP_TYPE = make(Map, true);
    private static final ClassNode SET_TYPE = make(Set, true);

    @Override
    public void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
        if (!(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof AnnotatedNode)) {
            throw new RuntimeException("Internal error: wrong types: ${nodes[0].class} / ${nodes[1].class}");
        }

        AnnotationNode node = (AnnotationNode) nodes[0];
        AnnotatedNode parent = (AnnotatedNode) nodes[1];
        ClassNode declaringClass = parent.declaringClass;

        if (parent instanceof FieldNode) {
            int modifiers = ((FieldNode) parent).modifiers;
            if ((modifiers & ACC_FINAL) != 0) {
                String msg = "@codeOrchestra.groovyfx.FXBindable cannot annotate a final property.";
                generateSyntaxErrorMessage(sourceUnit, node, msg);
            }
            addJavaFXProperty(sourceUnit, node, declaringClass, (FieldNode) parent);
        } else {
            addJavaFXPropertyToClass((ClassNode) parent);
        }
    }


    private static void addJavaFXProperty(SourceUnit source, AnnotationNode node, ClassNode declaringClass, FieldNode field) {
        String fieldName = field.name;
        for (PropertyNode propertyNode : declaringClass.properties) {
            if (propertyNode.name == fieldName) {
                if (field.static) {
                    generateSyntaxErrorMessage(source, node, "@groovy.beans.Bindable cannot annotate a static property.");
                } else {
                    createPropertyGetterSetter(declaringClass, propertyNode);
                }
                return;
            }
        }

        generateSyntaxErrorMessage(source, node,
                "@codeOrchestra.groovyfx.FXBindable must be on a property, not a field. Try removing the private, protected, or public modifier.");
    }

    private static void addJavaFXPropertyToClass(ClassNode classNode) {
        for (PropertyNode propertyNode : classNode.properties) {
            FieldNode field = propertyNode.field;
            // look to see if per-field handlers will catch this one...
            if (field.annotations.any {AnnotationNode it ->  it.classNode == BOUND_CLASS_NODE } || ((field.modifiers & ACC_FINAL) != 0) || field.static) {
                // explicitly labeled properties are already handled,
                // don't transform final properties, don't transform static properties
                // VetoableASTTransformation will handle both @Bindable and @Vetoable
                continue;
            }
            createPropertyGetterSetter(classNode, propertyNode);
        }
    }

    /**
     * Creates the JavaFX property and three methods for accessing the property and a pair of
     * getter/setter methods for accessing the original (now synthetic) Groovy property.  For
     * example, if the original property was "String firstName" then these three methods would
     * be generated:
     *
     *     public String getFirstName()
     *     public void setFirstName(String value)
     *     public StringProperty firstNameProperty()
     *
     * @param classNode The declaring class in which the property will appear
     * @param originalProp The original Groovy property
     */
    private static void createPropertyGetterSetter(ClassNode classNode, PropertyNode originalProp) {
        Expression initExp = originalProp.initialExpression;
        originalProp.field.initialValueExpression = null;

        PropertyNode fxProperty = createFXProperty(originalProp);

        String setterName = "set" + MetaClassHelper.capitalize(originalProp.name);
        if (classNode.getMethods(setterName).empty) {
            Statement setterBlock = createSetterStatement(createFXProperty(originalProp));
            createSetterMethod(classNode, originalProp, setterName, setterBlock);
        } else {
            wrapSetterMethod(classNode, originalProp.name);
        }

        String getterName = "get" + MetaClassHelper.capitalize(originalProp.name);
        if (classNode.getMethods(getterName).empty) {
            Statement getterBlock = createGetterStatement(createFXProperty(originalProp));
            createGetterMethod(classNode, originalProp, getterName, getterBlock);
        } else {
            wrapGetterMethod(classNode, originalProp.name)
        }

        // We want the actual name of the field to be different from the getter (Prop vs Property) so
        // that the getter takes precedence when we say this.somethingProperty.
        FieldNode fxFieldShortName = createFieldNodeCopy(originalProp.name + "Prop", null, fxProperty.field)
        createPropertyAccessor(classNode, createFXProperty(originalProp), fxFieldShortName, initExp)

        //classNode.removeField(originalProp.name)
        classNode.addField(fxFieldShortName)
    }

    /**
     * Creates a new PropertyNode for the JavaFX property based on the original property.  The new property
     * will have "Property" appended to its name and its type will be one of the *Property types in JavaFX.
     *
     * @param orig The original property
     * @return A new PropertyNode for the JavaFX property
     */
    private static PropertyNode createFXProperty(PropertyNode orig) {
        ClassNode origType = orig.type
        ClassNode newType = getPropertyForType(origType)

        // For the ObjectProperty, we need to add the generic type to it.
        if (!newType) {
            if (origType == OBSERVABLE_LIST || origType.declaresInterface(OBSERVABLE_LIST)) {
                newType = make(SimpleListProperty, true)
                newType.genericsTypes = origType.genericsTypes
            } else if (origType == OBSERVABLE_MAP || origType.declaresInterface(OBSERVABLE_MAP)) {
                newType = make(SimpleMapProperty, true)
                newType.genericsTypes = origType.genericsTypes
            } else if (origType == OBSERVABLE_SET || origType.declaresInterface(OBSERVABLE_SET)) {
                newType = make(SimpleSetProperty, true)
                newType.genericsTypes = origType.genericsTypes
            } else { // Object Type
                newType = ClassHelper.makeWithoutCaching(ObjectProperty, true)
                ClassNode genericType = origType
                if (genericType.primaryClassNode) {
                    genericType = getWrapper(genericType)
                }
                newType.genericsTypes = [new GenericsType(genericType)]
            }
        }

        FieldNode fieldNode = createFieldNodeCopy(orig.name + "Property", newType, orig.field)
        return new PropertyNode(fieldNode, orig.modifiers, orig.getterBlock, orig.setterBlock)
    }

    /**
     * Creates a setter method and adds it to the declaring class.  The setter has the form:
     *
     *     void <setter>(<type> fieldName)
     *
     * @param declaringClass The class to which the method is added
     * @param propertyNode The property node being accessed by this setter
     * @param setterName The name of the setter method
     * @param setterBlock The code body of the method
     */
    private static void createSetterMethod(ClassNode declaringClass, PropertyNode propertyNode, String setterName,
                                           Statement setterBlock) {
        Parameter[] setterParameterTypes = [new Parameter(propertyNode.type, "value")]
        int mod = propertyNode.modifiers | ACC_FINAL

        MethodNode setter = new MethodNode(setterName, mod, ClassHelper.VOID_TYPE, setterParameterTypes, ClassNode.EMPTY_ARRAY, setterBlock)
        setter.synthetic = true
        declaringClass.addMethod(setter)
    }

    /**
     * If the setter already exists, this method should wrap it with our code and then a call to the original
     * setter.
     *
     * TODO: Not implemented yet
     *
     * @param classNode The declaring class to which the method will be added
     * @param propertyName The name of the original Groovy property
     */
    private static void wrapSetterMethod(ClassNode classNode, String propertyName) {
        println("wrapSetterMethod for '${classNode.name}', property '${propertyName}' not yet implemented")
    }

    /**
     * Creates a getter method and adds it to the declaring class.
     *
     * @param declaringClass The class to which the method is added
     * @param propertyNode The property node being accessed by this getter
     * @param getterName The name of the getter method
     * @param getterBlock The code body of the method
     */
    private static void createGetterMethod(ClassNode declaringClass, PropertyNode propertyNode, String getterName,
                                           Statement getterBlock) {
        int mod = propertyNode.modifiers | ACC_FINAL
        MethodNode getter = new MethodNode(getterName, mod, propertyNode.type, Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, getterBlock)
        getter.synthetic = true
        declaringClass.addMethod(getter)
    }

    /**
     * If the getter already exists, this method should wrap it with our code.
     *
     * TODO: Not implemented yet -- what to do with the returned value from the original getter?
     *
     * @param classNode The declaring class to which the method will be added
     * @param propertyName The name of the original Groovy property
     */
    private static void wrapGetterMethod(ClassNode classNode, String propertyName) {
        println("wrapGetterMethod for '${classNode.name}', property '${propertyName}' not yet implemented")
    }

    /**
     * Creates the body of a property access method that returns the JavaFX *Property instance.  If
     * the original property was "String firstName" then the generated code would be:
     *
     *     if (firstNameProperty == null) {*         firstNameProperty = new javafx.beans.property.StringProperty()
     *}*     return firstNameProperty
     *
     * @param classNode The declaring class to which the JavaFX property will be added
     * @param fxProperty The new JavaFX property
     * @param fxFieldShortName
     * @param initExp The initializer expression from the original Groovy property declaration
     */
    private static void createPropertyAccessor(ClassNode classNode, PropertyNode fxProperty, FieldNode fxFieldShortName, Expression initExp) {
        FieldExpression fieldExpression = new FieldExpression(fxFieldShortName)

        ArgumentListExpression ctorArgs = !initExp ?
            ArgumentListExpression.EMPTY_ARGUMENTS :
            new ArgumentListExpression(initExp)

        BlockStatement block = new BlockStatement()
        ClassNode fxType = fxProperty.type
        ClassNode implNode = getPropertyImplementation(fxType)

        if (!implNode) {
            if (fxType.typeClass == SIMPLE_LIST_PROPERTY.typeClass) {
                if (initExp) {
                    if (initExp instanceof ListExpression || isCollectionType(initExp, LIST_TYPE)) {
                        ctorArgs = new ArgumentListExpression(
                                new MethodCallExpression(
                                        new ClassExpression(FX_COLLECTIONS_TYPE), "observableList", ctorArgs
                                )
                        )
                    }
                }
                implNode = fxType

            } else if (fxType.typeClass == SIMPLE_MAP_PROPERTY.typeClass) {
                if (initExp) {
                    if (initExp instanceof MapExpression || isCollectionType(initExp, MAP_TYPE)) {
                        ctorArgs = new ArgumentListExpression(
                                new MethodCallExpression(
                                        new ClassExpression(FX_COLLECTIONS_TYPE), "observableMap", ctorArgs
                                )
                        )
                    }
                }
                implNode = fxType
            } else if (fxType.typeClass == SIMPLE_SET_PROPERTY.typeClass) {
                if (initExp) {
                    if (isCollectionType(initExp, SET_TYPE)) {
                        ctorArgs = new ArgumentListExpression(
                                new MethodCallExpression(
                                        new ClassExpression(FX_COLLECTIONS_TYPE),
                                        "observableSet",
                                        ctorArgs
                                )
                        )
                    }
                }
                implNode = fxType
            } else {
                implNode = make(SimpleObjectProperty, true)
                GenericsType[] origGenerics = fxProperty.type.genericsTypes
                implNode.genericsTypes = origGenerics
            }
        }
        Expression initExpression = new ConstructorCallExpression(implNode, ctorArgs)

        IfStatement ifStmt = new IfStatement(
                new BooleanExpression(
                        new BinaryExpression(
                                fieldExpression,
                                Token.newSymbol(Types.COMPARE_EQUAL, 0, 0),
                                ConstantExpression.NULL
                        )
                ),
                new ExpressionStatement(
                        new BinaryExpression(
                                fieldExpression,
                                Token.newSymbol(Types.EQUAL, 0, 0),
                                initExpression
                        )
                ),
                EmptyStatement.INSTANCE
        )
        block.addStatement(ifStmt)
        block.addStatement(new ReturnStatement(fieldExpression))

        String getterName = getFXPropertyGetterName(fxProperty)
        MethodNode accessor = new MethodNode(getterName, fxProperty.modifiers, fxProperty.type,
                Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, block)
        accessor.synthetic = true
        classNode.addMethod(accessor)

        // Create the xxxxProperty() method that merely calls getXxxxProperty()
        block = new BlockStatement()

        VariableExpression thisExpression = VariableExpression.THIS_EXPRESSION
        ArgumentListExpression emptyArguments = ArgumentListExpression.EMPTY_ARGUMENTS

        MethodCallExpression getProperty = new MethodCallExpression(thisExpression, getterName, emptyArguments)
        block.addStatement(new ReturnStatement(getProperty))

        String javaFXPropertyFunction = fxProperty.name

        accessor = new MethodNode(javaFXPropertyFunction, fxProperty.modifiers, fxProperty.type,
                Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, block)
        accessor.synthetic = true
        classNode.addMethod(accessor)

        // Create the xxxx() method that merely calls getXxxxProperty()
        block = new BlockStatement()

        thisExpression = VariableExpression.THIS_EXPRESSION
        emptyArguments = ArgumentListExpression.EMPTY_ARGUMENTS

        getProperty = new MethodCallExpression(thisExpression, getterName, emptyArguments)
        block.addStatement(new ReturnStatement(getProperty))
        javaFXPropertyFunction = fxProperty.name.replace("Property", "")

        accessor = new MethodNode(javaFXPropertyFunction, fxProperty.modifiers, fxProperty.type,
                Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, block)
        accessor.synthetic = true
        classNode.addMethod(accessor)
    }

    private static boolean isCollectionType(Expression initExp, ClassNode collectionType) {
        if (initExp instanceof CastExpression) {
            CastExpression castExp = ((CastExpression) initExp)
            return castExp.type == collectionType || castExp.type.declaresInterface(collectionType)
        }
        if (initExp instanceof ConstructorCallExpression) {
            ConstructorCallExpression constExp = ((ConstructorCallExpression) initExp)
            return constExp.type == collectionType || constExp.type.declaresInterface(collectionType)
        }
        return false
    }

    /**
     * Creates the body of a setter method for the original property that is actually backed by a
     * JavaFX *Property instance:
     *
     *     Object $property = this.someProperty()
     *     $property.setValue(value)
     *
     * @param fxProperty The original Groovy property that we're creating a setter for.
     * @return A Statement that is the body of the new setter.
     */
    private static Statement createSetterStatement(PropertyNode fxProperty) {
        String fxPropertyGetter = getFXPropertyGetterName(fxProperty)
        VariableExpression thisExpression = VariableExpression.THIS_EXPRESSION
        ArgumentListExpression emptyArgs = ArgumentListExpression.EMPTY_ARGUMENTS

        MethodCallExpression getProperty = new MethodCallExpression(thisExpression, fxPropertyGetter, emptyArgs)

        ArgumentListExpression valueArg = new ArgumentListExpression([new VariableExpression("value")] as Expression[])
        MethodCallExpression setValue = new MethodCallExpression(getProperty, "setValue", valueArg)

        return new ExpressionStatement(setValue)
    }

    /**
     * Creates the body of a getter method for the original property that is actually backed by a
     * JavaFX *Property instance:
     *
     *     Object $property = this.someProperty()
     *     return $property.getValue()
     *
     * @param fxProperty The new JavaFX property.
     * @return A Statement that is the body of the new getter.
     */
    private static Statement createGetterStatement(PropertyNode fxProperty) {
        String fxPropertyGetter = getFXPropertyGetterName(fxProperty)
        VariableExpression thisExpression = VariableExpression.THIS_EXPRESSION
        ArgumentListExpression emptyArguments = ArgumentListExpression.EMPTY_ARGUMENTS

        // We're relying on the *Property() method to provide the return value - is this still needed??
        //        Expression defaultReturn = defaultReturnMap.get(originalProperty.getType())
        //        if (defaultReturn == null)
        //            defaultReturn = ConstantExpression.NULL

        MethodCallExpression getProperty = new MethodCallExpression(thisExpression, fxPropertyGetter, emptyArguments)
        MethodCallExpression getValue = new MethodCallExpression(getProperty, "getValue", emptyArguments)

        return new ReturnStatement(new ExpressionStatement(getValue))
    }

    /**
     * Generates a SyntaxErrorMessage based on the current SourceUnit, AnnotationNode, and a specified
     * error message.
     *
     * @param sourceUnit The SourceUnit
     * @param node The node that was annotated
     * @param msg The error message to display
     */
    private static void generateSyntaxErrorMessage(SourceUnit sourceUnit, AnnotationNode node, String msg) {
        SyntaxException error = new SyntaxException(msg, node.lineNumber, node.columnNumber)
        sourceUnit.errorCollector.addErrorAndContinue(new SyntaxErrorMessage(error, sourceUnit))
    }

    /**
     * Creates a copy of a FieldNode with a new name and, optionally, a new type.
     *
     * @param newName The name for the new field node.
     * @param newType The new type of the field.  If null, the old FieldNode's type will be used.
     * @param f The FieldNode to copy.
     * @return The new FieldNode.
     */
    private static FieldNode createFieldNodeCopy(String newName, ClassNode newType, FieldNode f) {
        if (!newType) newType = f.type
        newType = newType.plainNodeReference
        return new FieldNode(newName, f.modifiers, newType, f.owner, f.initialValueExpression)
    }

    /**
     * Generates the correct getter method name for a JavaFX property.
     *
     * @param fxProperty The property for which the getter should be generated.
     * @return The getter name as a String.
     */
    private static String getFXPropertyGetterName(PropertyNode fxProperty) {
        return "get" + fxProperty.name?.capitalize()
    }
}