package codeOrchestra.groovyfx;

import org.codehaus.groovy.transform.GroovyASTTransformationClass;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Eugene Potapenko
 */
@Documented
@Retention(java.lang.annotation.RetentionPolicy.SOURCE)
@Target({ElementType.FIELD, ElementType.TYPE})
@GroovyASTTransformationClass({"codeOrchestra.groovyfx.ast.FXBindableASTTransformation"})
public @interface FXBindable {
}
