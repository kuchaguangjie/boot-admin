package ${package.Controller};

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
<#if superControllerClass??>
import ${superControllerClassPackage};
</#if>

/**
 * ${table.comment} 控制器
 *
 * @author ${author}
 * @date ${date}
 */
@RestController
@RequestMapping
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else >
public class ${table.controllerName} {
</#if>
}