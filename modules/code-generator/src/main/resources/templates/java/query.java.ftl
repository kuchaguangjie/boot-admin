package ${package.Query};

<#if superQueryClass??>
import ${superQueryClassPackage};
</#if>
<#list table.queryImportPackages as queryImportPackage>
import ${queryImportPackage};
</#list>
import lombok.Getter;
import lombok.Setter;
<#if queryChainModel>
import lombok.experimental.Accessors;
</#if>

/**
 * ${table.comment} Query
 *
 * @author ${author}
 * @date ${date}
 */
@Getter
@Setter
<#if queryChainModel>
@Accessors(chain = true)
</#if>
<#if superQueryClass??>
public class ${table.queryName} extends ${superQueryClass} {
<#elseif querySerialVersionUID>
public class ${table.queryName} implements Serializable {
<#else>
public class ${table.queryName} {
</#if>
<#if querySerialVersionUID>
    private static final long serialVersionUID = 1L;
</#if>
<#list table.fields as field>
  <#if field.query>
    <#if field.comment??>
    /**
     * ${field.comment}
     */
    </#if>
    <#if field.queryType == 'eq'>
     @Equals
    <#elseif field.queryType == 'like'>
     @Like
    </#if>
    <#if springdoc>
    @Schema(description = "${field.comment}")
    </#if>
    private ${field.propertyType} ${field.propertyName};
  </#if>
</#list>

}